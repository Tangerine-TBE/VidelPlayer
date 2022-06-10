package com.example.module_video.ui.activity

import android.view.KeyEvent
import com.example.module_ad.ad.ad_help.AdController
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.SplashHelper
import com.example.module_ad.advertisement.TTAdManagerHolder
import com.example.module_ad.utils.AdMsgUtil
import com.example.module_ad.utils.Contents
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.module_video.R
import com.example.module_video.databinding.ActivityBeginBinding
import com.example.module_video.livedata.MediaLiveData
import com.example.module_video.ui.widget.popup.AgreementPopup
import com.example.module_video.viewmodel.BeginViewModel
import com.umeng.commonsdk.UMConfigure


class BeginActivity : BaseVmViewActivity<ActivityBeginBinding, BeginViewModel>() {

    private var showCount = 0



    private val mAdController by lazy {
        AdController.Builder(this)
            .setPage(AdType.START_PAGE)
            .setContainer(hashMapOf(AdController.ContainerType.TYPE_SPLASH to  binding.mAdContainer))
            .create()
            .setSplashAction {
                if (sp.getBoolean(com.example.module_video.utils.Constants.SP_SET_PWD_STATE)) {
                    toOtherActivity<LockActivity>(this,true) { putExtra(Contents.KEY_ACTION,1) }
                } else {
                    toOtherActivity<HomeActivity>(this,true){}
                }
            }
    }



    private val mAgreementPopup by lazy {
        AgreementPopup(this)
    }

    override fun getViewModelClass(): Class<BeginViewModel> {
        return BeginViewModel::class.java
    }

    override fun getLayoutView(): Int = R.layout.activity_begin
    override fun initView() {
        sp.putBoolean(Contents.NO_BACK, true)
        viewModel.loadAdMsg()

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        MyStatusBarUtil.fullScreenWindow(hasFocus, this)
        if (showCount < 1) {
            if (hasFocus) {
                sp.apply {
                    if (getBoolean(Constants.IS_FIRST, true)) {
                        mAgreementPopup?.showPopupView(binding.mAdContainer)
                        showCount++
                    } else {
                        initSdk()
                        mAdController.show()
                    }
                }
            }
        }
    }


    override fun initEvent() {
        mAgreementPopup.setOnActionClickListener(object : BasePopup.OnActionClickListener {
            override fun sure() {
                initSdk()
//                checkAppPermission(
//                    askAllPermissionLis, {
                        if (AdMsgUtil.getADKey() != null) {
                           // mSplashHelper.showAd()
                            mAdController.show()
                        } else {
                            goHome()
                        }
//                    },
//                    {
//                        goHome()
//                    }, this@BeginActivity
//                )
            }

            override fun cancel() {
                finish()
            }
        })

    }

    fun goHome() {
        if (sp.getBoolean(com.example.module_video.utils.Constants.SP_SET_PWD_STATE)) {
            toOtherActivity<LockActivity>(this@BeginActivity, true) {
                putExtra(Contents.KEY_ACTION,1)
            }
        } else {
            toOtherActivity<HomeActivity>(this@BeginActivity, true) {}
        }
    }

    //禁用返回键
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean =
        if (keyCode == KeyEvent.KEYCODE_BACK) true
        else super.onKeyDown(keyCode, event)


    override fun release() {
        sp.putBoolean(Contents.NO_BACK, false)
        mAgreementPopup.dismiss()
    }

    private fun initSdk(){
        //友盟 605b0b9cb8c8d45c13ae24a4
        UMConfigure.init(
            BaseApplication.application,
            UMConfigure.DEVICE_TYPE_PHONE,
            "605b0b9cb8c8d45c13ae24a4"
        )
        UMConfigure.setLogEnabled(true)
        TTAdManagerHolder.init(BaseApplication.application)
    }

}