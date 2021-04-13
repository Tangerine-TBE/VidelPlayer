package com.example.module_ad.ad.ad_help

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.ad.AdShowStateListener
import com.example.module_ad.ad.ad_kind.tencent.TxSplashAd
import com.example.module_ad.ad.ad_kind.toutiao.TTSplashAd
import com.example.module_ad.base.AdTypeBean
import com.example.module_ad.utils.AdMsgUtil
import com.example.module_ad.utils.AdProbabilityUtil
import com.example.module_base.utils.showToast
import com.example.module_user.utils.UserInfoUtil

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_help
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 14:37:40
 * @class describe
 */
class SplashHelper(activity: Activity, container: FrameLayout):AdHelper(activity, container) {

    private var mTTSplashAd: TTSplashAd? = null
    private var mTxSplashAd: TxSplashAd? = null
    private var mSplashAction:()->Unit={}

    private var mAddToutiaoAdError = false
    private var mAddTengxunAdError = false


    fun setSplashAction(block: () -> Unit) {
        mSplashAction=block
    }

    override fun showAd(type: AdTypeBean?, random: Double) {
        if (UserInfoUtil.isVIP()) {
            mSplashAction()
            return
        }
        AdMsgUtil.getAdState()?.let {
            val spreadScreen = it.start_page.spread_screen
            if (spreadScreen.isStatus) {
                if (random >= AdProbabilityUtil.showAdProbability(spreadScreen.ad_percent)) {
                    showTTAd()
                } else {
                    showTxAd()
                }
            } else {
                mSplashAction()
            }
        }
    }

    private fun showTxAd() {
        mTxSplashAd = TxSplashAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {
                    mSplashAction()

                }

                override fun showError() {
                    if (!mAddTengxunAdError) {
                        showTTAd()
                    }
                    mAddTengxunAdError = true
                    showADError()
                }
            })
        }
    }

    private fun showTTAd() {
        mTTSplashAd = TTSplashAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {
                    mSplashAction()
                }

                override fun showError() {
                    if (!mAddToutiaoAdError) {
                        showTxAd()
                    }
                    mAddToutiaoAdError = true
                    showADError()
                }
            })
        }
    }


    //都加载失败
    private fun showADError() {
        if (mAddTengxunAdError and mAddToutiaoAdError) {
            mSplashAction()
        }
    }


    override fun releaseAd() {

    }


}


