package com.example.module_ad.ad.ad_kind.toutiao

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.MainThread
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdNative.SplashAdListener
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.example.module_ad.ad.Ad
import com.example.module_ad.advertisement.TTAdManagerHolder
import com.example.module_ad.utils.DeviceUtils
import com.example.module_base.utils.LogUtils

class TTSplashAd( activity: Activity,container:FrameLayout):Ad(activity,container) {

    override fun showAd() {
        val screenHeight = DeviceUtils.getScreenHeight(activity)
        val screenWidth = DeviceUtils.getScreenWidth(activity)
        LogUtils.i("$screenHeight-------------------------->$screenWidth")
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val  adSlot = AdSlot.Builder()
            .setCodeId(mKTouTiaoKaiPing)
            .setSupportDeepLink(true)
            .setImageAcceptedSize(screenWidth, screenHeight)
            .build()
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative.loadSplashAd(adSlot, object : SplashAdListener {
            @MainThread
            override fun onError(code: Int, message: String) {
                LogUtils.i("onError-------${code}----------$message--------->" )
                mAdShowStateListener?.showError()
            }

            @MainThread
            override fun onTimeout() {
                mAdShowStateListener?.showSuccess()
                LogUtils.i("onTimeout-------------------------->")
            }

            @MainThread
            override fun onSplashAdLoad(ad: TTSplashAd?) {
                //获取SplashView
                val view = ad?.splashView
                if (view != null && !activity.isFinishing) {
                    container.apply {
                        removeAllViews()
                        addView(view)
                    }
                    //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕高
                    LogUtils.i("onSplashAdLoad-------------------------->")
                    //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                    //ad.setNotAllowSdkCountdown();
                } else {
                    mAdShowStateListener?.showError()
                    LogUtils.i("onSplashAdLoad-------------------------->")
                }
                //设置SplashView的交互监听器
                ad?.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View, type: Int) {
                        LogUtils.i("开屏广告点击-------------------------->")
                    }

                    override fun onAdShow(view: View, type: Int) {
                        LogUtils.i("开屏广告展示-------------------------->")
                    }

                    override fun onAdSkip() {
                        LogUtils.i("开屏广告跳过-------------------------->")
                        mAdShowStateListener?.showSuccess()
                    }

                    override fun onAdTimeOver() {
                        LogUtils.i("开屏广告倒计时结束-------------------------->")
                        mAdShowStateListener?.showSuccess()
                    }
                })
                if (ad?.interactionType == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    ad.setDownloadListener(object : TTAppDownloadListener {
                        var hasShow = false
                        override fun onIdle() {}
                        override fun onDownloadActive(
                            totalBytes: Long,
                            currBytes: Long,
                            fileName: String,
                            appName: String
                        ) {
                            if (!hasShow) {
                                LogUtils.i("下载中------------------>")
                                hasShow = true
                            }
                        }

                        override fun onDownloadPaused(
                            totalBytes: Long,
                            currBytes: Long,
                            fileName: String,
                            appName: String
                        ) {
                            LogUtils.i("下载暂停------------------>")
                        }

                        override fun onDownloadFailed(
                            totalBytes: Long,
                            currBytes: Long,
                            fileName: String,
                            appName: String
                        ) {
                            LogUtils.i("下载失败------------------>")
                        }

                        override fun onDownloadFinished(
                            totalBytes: Long,
                            fileName: String,
                            appName: String
                        ) {
                            LogUtils.i("下载完成------------------>")
                        }

                        override fun onInstalled(fileName: String, appName: String) {
                            LogUtils.i("安装完成------------------>")
                        }
                    })
                }
            }
        }, AD_TIME_OUT)

    }

    override fun releaseAd() {

    }
}