package com.example.module_video.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.getCurrentThreadName
import com.example.module_user.domain.LoginInfo
import com.example.module_user.domain.ValueResult
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.domain.login.LoginBean
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.*

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:56:17
 * @class describe
 */
class SetViewModel:BaseViewModel(){

    val logOutState by lazy {
        MutableLiveData<ValueResult>()
    }

    val loginState by lazy {
        MutableLiveData<ValueResult>()
    }

    fun toLogOut(id:String){
        doRequest({
            logOutState.postValue(ValueResult(NetState.LOADING, ""))
            UserRepository.userLogOut(UserInfoHelper.userLogOut(Constants.DELETE_USER,id))?.let {
                if (it.ret == NET_SUCCESS) {
                    logOutState.postValue(ValueResult(NetState.SUCCESS, "账号注销成功！"))
                    UserInfoLiveData.setUserInfo(ValueUserInfo(false,null))
                } else {
                    logOutState.postValue(ValueResult(NetState.ERROR, it.msg))
                }
            }
        }, {
            logOutState.postValue(ValueResult(NetState.ERROR, "网络错误！"))
        })
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

    fun doThirdLogin(mOpenId: String,type: String){
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

}