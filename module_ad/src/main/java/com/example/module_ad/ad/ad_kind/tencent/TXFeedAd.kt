package com.example.module_ad.ad.ad_kind.tencent

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.ad.Ad
import com.example.module_base.utils.LogUtils
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.ADSize
import com.qq.e.ads.nativ.NativeExpressAD
import com.qq.e.ads.nativ.NativeExpressAD.NativeExpressADListener
import com.qq.e.ads.nativ.NativeExpressADView
import com.qq.e.ads.nativ.NativeExpressMediaListener
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_kind.tencent
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 10:13:36
 * @class describe
 */
class TXFeedAd(activity: Activity, container: FrameLayout): Ad(activity, container)  {
    private var nativeExpressADView: NativeExpressADView? = null

    override fun showAd() {
       val nativeExpressAD = NativeExpressAD(activity, ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT), mKgdtMobSDKJSmallNativeKey, object : NativeExpressADListener {
           override fun onADLoaded(adList: List<NativeExpressADView>) {
               // 释放前一个 NativeExpressADView 的资源
               nativeExpressADView?.destroy()
               // 3.返回数据后，SDK 会返回可以用于展示 NativeExpressADView 列表
                adList[0]?.apply {
                   if (boundData.adPatternType == AdPatternType.NATIVE_VIDEO) {
                       setMediaListener(mediaListener)
                   }
                   render()
                   /* if (mFeedContainer.getChildCount() > 0) {
                    }*/LogUtils.i("onADLoaded ----------------->")
                   // 需要保证 View 被绘制的时候是可见的，否则将无法产生曝光和收益。
                    this.setDownloadConfirmListener(GDTDownloadConfirmListener.DOWNLOAD_CONFIRM_LISTENER)
                   container.removeAllViews()
                   container.addView(this)
               }
           }

           override fun onRenderFail(nativeExpressADView: NativeExpressADView) {
               LogUtils.i("NativeExpressADView 渲染广告失败----------------->")
               mAdShowStateListener?.showError()
           }

           override fun onRenderSuccess(nativeExpressADView: NativeExpressADView) {
               mAdShowStateListener?.showSuccess()
               LogUtils.i("渲染广告成功----------------->")
           }

           override fun onADExposure(nativeExpressADView: NativeExpressADView) {
               LogUtils.i("广告曝光----------------->")
           }

           override fun onADClicked(nativeExpressADView: NativeExpressADView) {
               LogUtils.i("当广告点击----------------->")
           }

           override fun onADClosed(nativeExpressADView: NativeExpressADView) {
               LogUtils.i("当广告关闭----------------->")
           }

           override fun onADLeftApplication(nativeExpressADView: NativeExpressADView) {
               LogUtils.i("因为广告点击等原因离开当前 app 时调用----------------->")
           }

           override fun onNoAD(adError: AdError) {
               LogUtils.i("无广告填充    ----------------->")
           }
       }) // 传入Activity
        // 注意：如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        nativeExpressAD.setVideoOption(VideoOption.Builder()
                .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(true) // 自动播放时为静音
                .build()) //
        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */

        /**
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         *
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         *
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
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

        nativeExpressAD.loadAD(1)
    }
    private val mediaListener: NativeExpressMediaListener = object : NativeExpressMediaListener {
        override fun onVideoInit(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoLoading(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoCached(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoReady(nativeExpressADView: NativeExpressADView, l: Long) {}
        override fun onVideoStart(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoPause(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoComplete(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoError(nativeExpressADView: NativeExpressADView, adError: AdError) {}
        override fun onVideoPageOpen(nativeExpressADView: NativeExpressADView) {}
        override fun onVideoPageClose(nativeExpressADView: NativeExpressADView) {}
    }

    override fun releaseAd() {
        nativeExpressADView?.destroy()
    }
}