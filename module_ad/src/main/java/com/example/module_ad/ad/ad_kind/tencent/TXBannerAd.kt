package com.example.module_ad.ad.ad_kind.tencent

import android.app.Activity
import android.graphics.Point
import android.view.View
import android.widget.FrameLayout
import com.example.module_ad.ad.Ad
import com.example.module_base.base.BaseApplication.Companion.application
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.RxNetTool.isNetworkAvailable
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError

import kotlin.math.roundToInt

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_kind.tencent
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 10:04:03
 * @class describe
 */
@Suppress("DEPRECATION")
class TXBannerAd(activity: Activity, container: FrameLayout): Ad(activity, container) {
    private var bv: UnifiedBannerView? = null



    override fun showAd() {
        getBanner()?.loadAD()
    }

    private fun getBanner(): UnifiedBannerView? {
        if (isNetworkAvailable(application)) {
            if (bv != null) {
                container.removeView(bv)
                bv?.destroy()
            }
            bv = UnifiedBannerView(activity, mKgdtMobSDKBannerKey, object : UnifiedBannerADListener {
                override fun onNoAD(adError: AdError) {
                    LogUtils.i("广告加载失败----------------->")
                    mAdShowStateListener?.showError()
                }

                override fun onADReceive() {
                    mAdShowStateListener?.showSuccess()
                    LogUtils.i("广告加载成功回调----------------->")
                }

                override fun onADExposure() {
                    LogUtils.i("onADExposure----------------->")
                }

                override fun onADClosed() {
                    LogUtils.i("当广告关闭 ----------------->")
                    container.visibility = View.GONE
                }

                override fun onADClicked() {
                    LogUtils.i("当广告点击----------------->")
                }

                override fun onADLeftApplication() {
                    LogUtils.i("由于广告点击离开 APP 时调用----------------->")
                }

                override fun onADOpenOverlay() {
                    LogUtils.i("onADOpenOverlay----------------->")
                }

                override fun onADCloseOverlay() {
                    LogUtils.i("onADCloseOverlay----------------->")
                }
            })

            // 不需要传递tags使用下面构造函数
            // this.bv = new UnifiedBannerView(this, Constants.APPID, posId, this);
            bv?.setDownloadConfirmListener(GDTDownloadConfirmListener.DOWNLOAD_CONFIRM_LISTENER)
            container.addView(bv, getUnifiedBannerLayoutParams())
            container.visibility = View.VISIBLE
            return this.bv
        }
        return null
    }

    private fun getUnifiedBannerLayoutParams(): FrameLayout.LayoutParams? {
        val screenSize = Point()
        activity.windowManager.defaultDisplay.getSize(screenSize)
        return FrameLayout.LayoutParams(screenSize.x, (screenSize.x / 6.4f).roundToInt())
    }

    override fun releaseAd() {
        bv?.destroy()
    }

}