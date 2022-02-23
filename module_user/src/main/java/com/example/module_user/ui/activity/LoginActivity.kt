package com.example.module_user.ui.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.provider.ModuleProvider
import com.example.module_base.utils.*
import com.example.module_user.R
import com.example.module_user.databinding.ActivityLoginBinding
import com.example.module_user.livedata.BuyVipLiveData
import com.example.module_user.utils.Constants
import com.example.module_user.utils.NetState
import com.example.module_user.viewmodel.LoginViewModel
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.Tencent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Route(path = ModuleProvider.ROUTE_LOGIN_ACTIVITY)
class LoginActivity : BaseVmViewActivity<ActivityLoginBinding, LoginViewModel>() {

    companion object {
        const val REGISTER = 1  //注册
        const val FIND_PWD = 2 //找回密码

        const val KEY_LOGIN_STATE="KEY_LOGIN_STATE"
        const val KEY_REQUEST_CODE=200


    }

    private var mWxapi: IWXAPI? = null

    override fun getViewModelClass(): Class<LoginViewModel> {
        return LoginViewModel::class.java
    }


    override fun getLayoutView(): Int = R.layout.activity_login

    private var buyAction=false

    override fun initView() {
        EventBus.getDefault().register(this)
        binding.apply {
            setStatusBar(this@LoginActivity, binding.loginToolbar, LayoutType.CONSTRAINTLAYOUT)
            buyAction= intent.getBooleanExtra(Constants.KEY_BUY_VIP,false)
        }

        //微信
        mWxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false)
        mWxapi?.registerApp(Constants.WX_APP_ID)
    }

    override fun initEvent() {
        binding.apply {

            back.setOnClickListener {
                finish()
            }

            toRegister.setOnClickListener {
                toOtherActivity<RegisterPwdActivity>(this@LoginActivity) {
                    putExtra(Constants.USER_ACTION, REGISTER)
                    putExtra(Constants.KEY_BUY_VIP,buyAction)
                }
            }


            toForgetPwd.setOnClickListener {
                toOtherActivity<RegisterPwdActivity>(this@LoginActivity) {
                    putExtra(Constants.USER_ACTION, FIND_PWD)
                }
            }


            toLogin.setOnClickListener {

                includeLogin.apply {
                    val number = nameInput.text.trim().toString()
                    val pwd = pwdInput.text.trim().toString()
                    viewModel.toLocalLogin(number, pwd)
                }

            }

            ivQq.setOnClickListener {
                viewModel.toQQLogin(this@LoginActivity)
            }

            ivWx.setOnClickListener {
                if (!mWxapi!!.isWXAppInstalled) {
                    Toast.makeText(this@LoginActivity, "您的设备未安装微信客户端", Toast.LENGTH_SHORT).show()
                } else {
                    val req = SendAuth.Req()
                    req.scope = "snsapi_userinfo"
                    req.state = "wechat_sdk_demo_test"
                    mWxapi!!.sendReq(req)
                }
            }

        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun checkRegister(wechatOpenId: String){
        viewModel.doCheckRegister(wechatOpenId,Constants.WECHAT_TYPE)
    }

    override fun observerData() {
        viewModel.apply {
            loginState.observe(this@LoginActivity, {
                mLoadingDialog.apply {
                    when (it.state) {
                        NetState.LOADING -> showDialog(this@LoginActivity)
                        NetState.SUCCESS -> {
                            dismiss()
                            showToast(it.msg)
                            if (buyAction) {
                                BuyVipLiveData.setBuyState(true)
                              //  setResult(KEY_REQUEST_CODE,Intent().putExtra(KEY_LOGIN_STATE,true))
                            }
                            finish()
                        }
                        NetState.ERROR -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }
                }
            })

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModel.onActivityRedultData(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

}