package com.example.module_user.viewmodel

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.getCurrentThreadName
import com.example.module_user.domain.LoginInfo
import com.example.module_user.domain.ValueResult
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.domain.login.LoginBean
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.*
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject
import java.lang.Exception

/**
 * @author: 铭少
 * @date: 2021/1/24 0024
 * @description：
 */
class LoginViewModel : BaseViewModel() {

    var listener: IUiListener? = null

    val loginState by lazy {
        MutableLiveData<ValueResult>()
    }


    //登录
    fun toLocalLogin(number: String, pwd: String) {
        loginState.postValue(ValueResult(NetState.LOADING,""))
        val md5Pwd = ApiMapUtil.md5(pwd)
        doRequest({
            UserRepository.userLogin(UserInfoHelper.userEvent(Constants.LOGIN,
                    mapOf(Constants.MOBILE to number, Constants.PASSWORD to md5Pwd)))?.string()?.let { it ->
                GsonUtil.setUserResult<LoginBean>(it, {
                    UserInfoLiveData.setUserInfo(ValueUserInfo(true,it, LoginInfo(Constants.LOCAL_TYPE,number,pwd)))
                    loginState.postValue(ValueResult(NetState.SUCCESS,"登录成功！"))
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                }, {
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                    loginState.postValue(ValueResult(NetState.ERROR,it.msg))
                })
            }
        }) {
            loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        }
    }

    fun toQQLogin(activity: Activity){
        val mTencent =Tencent.createInstance(Constants.QQ_ID,BaseApplication.application)
        listener = object : IUiListener{
            override fun onComplete(p0: Any?) {

                //  LogUtils.i(   LoginActivity.this,"登录成功: " + object.toString() );
                val jsonObject = p0 as JSONObject
                try {
                    //得到token、expires、openId等参数
                    val token =
                        jsonObject.getString(com.tencent.connect.common.Constants.PARAM_ACCESS_TOKEN)
                    val expires =
                        jsonObject.getString(com.tencent.connect.common.Constants.PARAM_EXPIRES_IN)
                    val mOpenId =
                        jsonObject.getString(com.tencent.connect.common.Constants.PARAM_OPEN_ID)
                    mTencent.setAccessToken(token, expires)
                    mTencent.openId = mOpenId

                    //检查是否注册
                    doCheckRegister(mOpenId,Constants.QQ_TYPE)
//                    mSPUtil.putBoolean(Contents.NOT_BACK, true)
//                    //获取个人信息
//                    getQQInfo()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onError(p0: UiError?) {
                LogUtils.e("")
            }

            override fun onCancel() {
                LogUtils.e("")
            }

            override fun onWarning(p0: Int) {
                LogUtils.e("")
            }

        }
        mTencent.login(activity,"all",listener)

    }

    fun doCheckRegister(mOpenId: String,type: String){
        doRequest(success = {
            UserRepository.doCheckRegister(UserInfoHelper.userEvent(Constants.CHECK_THIRD,
                mapOf(Constants.OPENID to mOpenId,Constants.TYPE to type)))?.string()?.let {
                val obj = JSONObject(it)
                val data = obj.getString("data")
                if (data == "0"){
                    doRegister(mOpenId,type)
                }else if (data == "1"){
                    doThirdLogin(mOpenId,type)
                }else{
                    loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
                }
            }
        }, error = {
            loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        })
    }

    //QQ注册
    private fun doRegister(mOpenId: String,type: String){
        doRequest(success = {
            UserRepository.doThirdRegister(UserInfoHelper.userEvent(Constants.REGISTER_BY_THIRD, mapOf(Constants.OPENID to mOpenId,Constants.TYPE to type,Constants.PACKAGE to BaseApplication.mPackName,
                    Constants.PLATFORM to PackageUtil.getAppMetaData(
                        BaseApplication.application,
                        Constants.PLATFORM_KEY
                    ) )))?.string()?.let {
                val obj = JSONObject(it)
                    if (obj.getInt("ret") == NET_SUCCESS){
                        doThirdLogin(mOpenId,type)
                    }else
                        loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
            }
        }, error = {
            loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        })
    }

    //登录
    private fun doThirdLogin(mOpenId: String,type: String){
        doRequest(success = {
            UserRepository.doThirdLogin(UserInfoHelper.userEvent(Constants.LOGIN_THIRD,
            mapOf(Constants.OPENID to mOpenId,Constants.TYPE to type)))?.string().let {
                GsonUtil.setUserResult<LoginBean>(it, {
                    UserInfoLiveData.setUserInfo(ValueUserInfo(true,it, LoginInfo(type,"","",mOpenId)))
                    loginState.postValue(ValueResult(NetState.SUCCESS,"登录成功！"))
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                }, {
                    LogUtils.i("-----toLocalLogin-111- ${getCurrentThreadName()}-----${it.msg}------------------")
                    loginState.postValue(ValueResult(NetState.ERROR,it.msg))
                })
            }
        }, error = {
            loginState.postValue(ValueResult(NetState.ERROR, NET_ERROR))
        })
    }

    //Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener)
    fun onActivityRedultData(requestCode: Int, resultCode: Int, data: Intent?){
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, listener)
        }
    }

}