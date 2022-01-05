package com.example.module_ad.ad.ad_kind.toutiao

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdNative.NativeExpressAdListener
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.example.module_ad.ad.Ad
import com.example.module_base.utils.LogUtils

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_kind.toutiao
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 10:24:06
 * @class describe
 */
class TTInsertAd(activity: Activity, container: FrameLayout): Ad(activity, container) {
    private var mHasShowDownloadActive = false
    private var mTTAd: TTNativeExpressAd? = null
    override fun showAd() {
        //设置广告参数
        val adSlot = AdSlot.Builder()
                .setCodeId(mKTouTiaoChaPingKey) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setDownloadType(TTAdConstant.DOWNLOAD_TYPE_POPUP)
                .setExpressViewAcceptedSize(640f, 320f) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640, 320) //这个参数设置即可，不影响个性化模板广告的size
                .build()
        //加载广告
        mTTAdNative.loadInteractionExpressAd(adSlot, object : NativeExpressAdListener {
            override fun onError(code: Int, message: String) {
                mAdShowStateListener?.showError()
            }

            override fun onNativeExpressAdLoad(ads: List<TTNativeExpressAd>) {
                if (ads == null || ads.isEmpty()) {
                    return
                }
                mTTAd = ads[0]?.apply {
                    bindAdListener(this)
                    render() //调用render开始渲染广告

                }

            }
        })
        //绑定广告行为
    }

    private fun bindAdListener(ad: TTNativeExpressAd) {
        ad.setExpressInteractionListener(object : TTNativeExpressAd.AdInteractionListener {
            override fun onAdDismiss() {}
            override fun onAdClicked(view: View, type: Int) {
                LogUtils.i("广告被点击")
            }

            override fun onAdShow(view: View, type: Int) {
                LogUtils.i("广告展示")
            }

            override fun onRenderFail(view: View, msg: String, code: Int) {
                LogUtils.i("$msg code:$code")
            }

            override fun onRenderSuccess(view: View, width: Float, height: Float) {
                //返回view的宽高 单位 dp
                LogUtils.i("渲染成功")
                //在渲染成功回调时展示广告，提升体验
                mTTAd?.showInteractionExpressAd(activity)
                mAdShowStateListener?.showSuccess()
            }
        })
        if (ad.interactionType != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return
        }
        //可选，下载监听设置
        ad.setDownloadListener(object : TTAppDownloadListener {
            override fun onIdle() {
                LogUtils.i("点击开始下载")
            }

            override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true
                    LogUtils.i("下载中，点击暂停")
                }
            }

            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                LogUtils.i("下载暂停，点击继续")
            }

            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                LogUtils.i("下载失败，点击重新下载")
            }

            override fun onInstalled(fileName: String, appName: String) {
                LogUtils.i("安装完成，点击图片打开")
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String, appName: String) {
                LogUtils.i("点击安装")
            }
        })
    }


    override fun releaseAd() {
        mTTAd?.destroy()
    }
}