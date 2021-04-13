package com.example.module_ad.ad.ad_help

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.ad.AdShowStateListener
import com.example.module_ad.ad.ad_kind.tencent.TXFeedAd
import com.example.module_ad.ad.ad_kind.toutiao.TTFeedAd
import com.example.module_ad.base.AdTypeBean
import com.example.module_ad.utils.AdProbabilityUtil
import com.example.module_user.utils.UserInfoUtil

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_help
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 14:38:34
 * @class describe
 */
class FeedHelper(activity: Activity, container: FrameLayout):AdHelper(activity, container)  {

    private var mTTFeedAd: TTFeedAd? = null
    private var mTXFeedAd: TXFeedAd? = null
    private var mAddToutiaoAdError = false
    private var mAddTengxunAdError = false

    override fun showAd(type: AdTypeBean?, random: Double) {

        if (UserInfoUtil.isVIP()) {
            return
        }
        type?.let {
        val config = it.baseNative_screen
        if (config.isBaseStatus) {
            val showAdProbability = AdProbabilityUtil.showAdProbability(config.baseAd_percent)
            if (random >= showAdProbability) {
                showTTFeedAd()
            } else {
                showTXFeedAd()
            }
        }
        }
    }

    private fun showTXFeedAd() {
        mTXFeedAd = TXFeedAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {

                }

                override fun showError() {
                    if (!mAddTengxunAdError) {
                        showTTFeedAd()
                    }
                    mAddTengxunAdError = true
                }
            })
        }
    }

    private fun showTTFeedAd() {
        mTTFeedAd = TTFeedAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {

                }

                override fun showError() {
                    if (!mAddToutiaoAdError) {
                        showTXFeedAd()
                    }
                    mAddToutiaoAdError = true
                }
            })
        }
    }

    override fun releaseAd() {
        mTTFeedAd?.releaseAd()
        mTXFeedAd?.releaseAd()
    }
}