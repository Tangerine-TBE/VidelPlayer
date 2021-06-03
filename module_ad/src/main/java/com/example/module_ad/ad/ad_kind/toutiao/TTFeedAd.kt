package com.example.module_ad.ad.ad_kind.toutiao

import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdDislike.DislikeInteractionCallback
import com.bytedance.sdk.openadsdk.TTAdNative.NativeExpressAdListener
import com.bytedance.sdk.openadsdk.TTAppDownloadListener
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.bytedance.sdk.openadsdk.TTNativeExpressAd.ExpressAdInteractionListener
import com.example.module_ad.ad.Ad
import com.example.module_ad.utils.DeviceUtils.getScreenHeight
import com.example.module_ad.utils.DeviceUtils.getScreenWidth
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.SizeUtils


/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_kind.toutiao
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 10:09:00
 * @class describe
 */
class TTFeedAd(activity: Activity, container: FrameLayout): Ad(activity, container) {
    private var mTTAd: TTNativeExpressAd? = null
    override fun showAd() {
        container.removeAllViews()
        val mExpressViewWidth = getScreenWidth(activity).toFloat()
        val screenHeight = getScreenHeight(activity)
        //val expressViewHeight = SizeUtils.fitFeedHeight(screenHeight)
        val expressViewHeight =SizeUtils.expressHeight(screenHeight)
        LogUtils.i("高-------------->$screenHeight")
        LogUtils.i("宽-------------->$mExpressViewWidth")
        //step4:创建广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(mKTouTiaoSeniorKey) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(mExpressViewWidth, expressViewHeight) //期望模板广告view的size,单位dp
                .build()
        //step5:请求广告，对请求回调的广告作渲染处理
        mTTAdNative.loadNativeExpressAd(adSlot, object : NativeExpressAdListener {
            override fun onError(code: Int, message: String) {
                LogUtils.i(code.toString() + "onError------------------------>" + message)
                container.removeAllViews()
                mAdShowStateListener?.showError()
            }

            override fun onNativeExpressAdLoad(ads: List<TTNativeExpressAd>) {
                if (ads == null || ads.isEmpty()) {
                    return
                }
                mTTAd = ads[0]?.apply {
                    bindAdListener(this)
                    render()
                }
            }
        })
    }

    private var mHasShowDownloadActive = false
    private fun bindAdListener(ad: TTNativeExpressAd) {
        ad.setExpressInteractionListener(object : ExpressAdInteractionListener {
            override fun onAdClicked(view: View, type: Int) {
                LogUtils.i("广告被点击------------------------>")
            }

            override fun onAdShow(view: View, type: Int) {
                mAdShowStateListener?.showSuccess()
                LogUtils.i("广告展示------------------------>")
            }

            override fun onRenderFail(view: View, msg: String, code: Int) {
                LogUtils.i("$msg------------------------>$code")
               mAdShowStateListener?.showError()
            }

            override fun onRenderSuccess(view: View, width: Float, height: Float) {
                //返回view的宽高 单位 dp
                LogUtils.i("宽：--$width-----------onRenderSuccess------------->高：--$height-----------${view.height}---${view.width}------")
              //  container.layoutParams.height=height.toInt()
                container.removeAllViews()
                container.addView(view)
            }
        })
        //dislike设置
        bindDislike(ad, false)
        if (ad.interactionType != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return
        }
        ad.setDownloadListener(object : TTAppDownloadListener {
            override fun onIdle() {
                LogUtils.i("点击开始下载------------------------>")
            }

            override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                if (!mHasShowDownloadActive) {
                    mHasShowDownloadActive = true
                    LogUtils.i("下载中，点击暂停------------------------>")
                }
            }

            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                LogUtils.i("下载暂停，点击继续------------------------>")
            }

            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                LogUtils.i("下载失败，点击重新下载------------------------>")
            }

            override fun onInstalled(fileName: String?, appName: String) {
                LogUtils.i("安装完成，点击图片打开------------------------>")
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String, appName: String) {
                LogUtils.i("点击安装------------------------>")
            }
        })
    }


    /**
     * 设置广告的不喜欢，注意：强烈建议设置该逻辑，如果不设置dislike处理逻辑，则模板广告中的 dislike区域不响应dislike事件。
     * @param ad
     * @param customStyle 是否自定义样式，true:样式自定义
     */
    private fun bindDislike(ad: TTNativeExpressAd, customStyle: Boolean) {
        //使用默认模板中默认dislike弹出样式
        ad.setDislikeCallback(activity, object : DislikeInteractionCallback {
            override fun onShow() {

            }

            override fun onSelected(position: Int, value: String) {
                //用户选择不喜欢原因后，移除广告展示
                container.removeAllViews()
            }

            override fun onCancel() {}
            override fun onRefuse() {}
        })
    }

    override fun releaseAd() {
        mTTAd?.destroy()
    }

}