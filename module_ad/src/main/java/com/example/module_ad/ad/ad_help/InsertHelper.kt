package com.example.module_ad.ad.ad_help

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.ad.AdShowStateListener
import com.example.module_ad.ad.ad_kind.tencent.TXInsertAd
import com.example.module_ad.ad.ad_kind.toutiao.TTInsertAd
import com.example.module_ad.base.AdTypeBean
import com.example.module_ad.utils.AdProbabilityUtil

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_help
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 14:38:41
 * @class describe
 */
class InsertHelper(activity: Activity, container: FrameLayout):AdHelper(activity, container) {

    private var mTTInsertAd: TTInsertAd? = null
    private var mTXInsertAd: TXInsertAd? = null
    private var mAddToutiaoAdError = false
    private var mAddTengxunAdError = false

    override fun showAd(type: AdTypeBean?, random: Double) {
        type?.let {
        val config = it.baseIncentiveVideo_screen
        if (config.isBaseStatus) {
            val showAdProbability = AdProbabilityUtil.showAdProbability(config.baseAd_percent)
            if (random >= showAdProbability) {
                showTTInsertAd()
            } else {
                showTXInsertAd()
            }
        }
        }
    }

    private fun showTXInsertAd() {
        mTXInsertAd = TXInsertAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {

                }

                override fun showError() {
                    if (!mAddTengxunAdError) {
                        showTTInsertAd()
                    }
                    mAddTengxunAdError = true
                }
            })
        }
    }

    private fun showTTInsertAd() {
        mTTInsertAd = TTInsertAd(activity, container).apply {
            showAd()
            setAdShowStateListener(object : AdShowStateListener {
                override fun showSuccess() {

                }

                override fun showError() {
                    if (!mAddToutiaoAdError) {
                        showTXInsertAd()
                    }
                    mAddToutiaoAdError = true
                }
            })
        }
    }

    override fun releaseAd() {
        mTTInsertAd?.releaseAd()
        mTXInsertAd?.releaseAd()
    }

}