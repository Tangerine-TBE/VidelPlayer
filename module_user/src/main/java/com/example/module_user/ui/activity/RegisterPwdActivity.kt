package com.example.module_user.ui.activity

import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.provider.ModuleProvider
import com.example.module_base.utils.*
import com.example.module_user.R
import com.example.module_user.databinding.ActivityRegisterAvtivityBinding
import com.example.module_user.utils.Constants
import com.example.module_user.utils.NetState
import com.example.module_user.viewmodel.RegisterPwdViewModel
import com.example.module_user.widget.LoginView
import com.tamsiree.rxkit.view.RxToast.warning

class RegisterPwdActivity :
        BaseVmViewActivity<ActivityRegisterAvtivityBinding, RegisterPwdViewModel>() {
    private var action = LoginActivity.REGISTER
    override fun getViewModelClass(): Class<RegisterPwdViewModel> {
        return RegisterPwdViewModel::class.java
    }

    override fun getLayoutView(): Int = R.layout.activity_register_avtivity

    override fun initView() {
        binding.apply {
            setStatusBar(this@RegisterPwdActivity, binding.registerToolbar, LayoutType.CONSTRAINTLAYOUT)
            action = intent.getIntExtra(Constants.USER_ACTION, LoginActivity.REGISTER)
            titleHint.text=("${if (action == LoginActivity.REGISTER) getString(R.string.login_register_hint) else getString(
                            R.string.find_pwd)}")
            registerView.setLoginBtText("${if (action == LoginActivity.REGISTER) getString(R.string.register_hint) else getString(
                R.string.find_pwd)}")
        }
    }


    override fun observerData() {
        viewModel.apply {
            mLoadingDialog.apply {
                codeState.observe(this@RegisterPwdActivity, {
                    when (it.state) {
                        NetState.LOADING -> showDialog(this@RegisterPwdActivity)
                        NetState.ERROR, NetState.SUCCESS -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }
                })


                registerState.observe(this@RegisterPwdActivity, {
                    when (it.state) {
                        NetState.LOADING -> showDialog(this@RegisterPwdActivity)
                        NetState.ERROR -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }
                })

                loginState.observe(this@RegisterPwdActivity, {
                    when (it.state) {
                        NetState.LOADING -> showDialog(this@RegisterPwdActivity)
                        NetState.SUCCESS -> {
                            dismiss()
                            showToast(it.msg)
                            ARouter.getInstance().build(ModuleProvider.ROUTE_HOME_ACTIVITY).withInt(ModuleProvider.FRAGMENT_ID, 3).navigation()
                        }
                        NetState.ERROR -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }
                })

                findPwdState.observe(this@RegisterPwdActivity, {
                    when (it.state) {
                        NetState.LOADING -> showDialog(this@RegisterPwdActivity)
                        NetState.SUCCESS -> {
                            dismiss()
                            showToast(it.msg)
                            finish()
                        }
                        NetState.ERROR -> {
                            dismiss()
                            showToast(it.msg)
                        }
                    }
                })
            }


        }
    }

    override fun initEvent() {
        binding.apply {
            back.setOnClickListener {
                finish()
            }

            registerView.setonStateClickListener(object : LoginView.onStateClickListener {
                override fun getVerificationCodeClick(number: String) {
                    if (!TextUtils.isEmpty(number)) {
                        viewModel.getVerCode(number)
                    } else {
                        warning(getString(R.string.login_phone_hint))
                    }
                }

                override fun onLoginClick(phone: String, code: String, password: String) {
                    if (action == LoginActivity.REGISTER) {
                        viewModel.toRegister(phone, code, password)
                    } else {
                        viewModel.toFindPwd(phone, code, password)
                    }
                }
            })

        }

    }

}