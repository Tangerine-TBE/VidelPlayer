package com.example.module_ad.ad.ad_help

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.ad.AdShowStateListener
import com.example.module_ad.ad.ad_kind.tencent.TXBannerAd
import com.example.module_ad.ad.ad_kind.toutiao.TTBannerAd
import com.example.module_ad.base.AdTypeBean
import com.example.module_ad.utils.AdProbabilityUtil

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_help
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 14:38:27
 * @class describe
 */
class BannerHelper(activity: Activity, container: FrameLayout):AdHelper(activity, container)  {
    private var mAddToutiaoAdError_ban = false
    private var mAddTengxunAdError_ban = false
    private var mTTBannerAd: TTBannerAd? = null
    private var mTXBannerAd: TXBannerAd? = null
    override fun showAd(type: AdTypeBean?, random: Double) {
        type?.let {
            val config = type.baseBanner_screen
            if (config.isBaseStatus) {
                val showAdProbability = AdProbabilityUtil.showAdProbability(config.baseAd_percent)
                if (random >= showAdProbability) {
                    showTTBannerAd()
                } else {
                    showTXBannerAd()
                }
            }
        }

    }

    private fun showTTBannerAd() {
        mTTBannerAd = TTBannerAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {

                }

                override fun showError() {
                    if (!mAddToutiaoAdError_ban) {
                        showTXBannerAd()
                    }
                    mAddToutiaoAdError_ban = true
                }
            })
        }
    }

    private fun showTXBannerAd() {
        mTXBannerAd = TXBannerAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {

                }

                override fun showError() {
                    if (!mAddTengxunAdError_ban) {
                        showTTBannerAd()
                    }
                    mAddTengxunAdError_ban = true
                }
            })
        }
    }

    override fun releaseAd() {
        mTXBannerAd?.releaseAd()
        mTTBannerAd?.releaseAd()
    }
}