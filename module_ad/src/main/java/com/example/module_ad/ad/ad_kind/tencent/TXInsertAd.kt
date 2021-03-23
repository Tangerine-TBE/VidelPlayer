package com.example.module_ad.ad.ad_kind.tencent

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.ad.Ad
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_kind.tencent
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 10:18:13
 * @class describe
 */
class TXInsertAd(activity: Activity, container: FrameLayout): Ad(activity, container), UnifiedInterstitialADListener, UnifiedInterstitialMediaListener {

    private var mAd: UnifiedInterstitialAD? = null

    override fun showAd() {
        mAd = UnifiedInterstitialAD(activity, mKgdtMobSDKChaPingKey, this)
        mAd?.loadAD()
    }

    private fun setVideoOption() {
        mAd?.setVideoOption(VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()) //
        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br></br>
         * 如果广告位仅支持图文广告，则无需调用
         */
        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略
         *
         *
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br></br>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        mAd?.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO) // 本次拉回的视频广告，从用户的角度看是自动播放的
    }


    override fun releaseAd() {
        mAd?.apply {
            close()
            destroy()
        }
    }

    override fun onADReceive() {
        mAd?.apply {
            show()
            mAdShowStateListener?.showSuccess()
            if (adPatternType == AdPatternType.NATIVE_VIDEO) {
              setMediaListener(this@TXInsertAd)
            }
        }

    }

    override fun onVideoCached() {

    }

    override fun onNoAD(p0: AdError?) {
        mAdShowStateListener?.showError()
    }

    override fun onADOpened() {

    }

    override fun onADExposure() {

    }

    override fun onADClicked() {

    }

    override fun onADLeftApplication() {

    }

    override fun onADClosed() {

    }

    override fun onVideoInit() {

    }

    override fun onVideoLoading() {

    }

    override fun onVideoReady(p0: Long) {

    }

    override fun onVideoStart() {
    }

    override fun onVideoPause() {

    }

    override fun onVideoComplete() {

    }

    override fun onVideoError(p0: AdError?) {

    }

    override fun onVideoPageOpen() {

    }

    override fun onVideoPageClose() {

    }
}