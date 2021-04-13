package com.example.module_ad.ad

import android.app.Activity
import android.widget.FrameLayout
import com.bytedance.sdk.openadsdk.TTAdNative
import com.example.module_ad.advertisement.TTAdManagerHolder
import com.example.module_ad.utils.AdMsgUtil
import com.example.module_ad.utils.Contents
import com.qq.e.comm.managers.GDTADManager

abstract class Ad(val activity: Activity,val container: FrameLayout) {

    protected val mTTAdNative: TTAdNative by lazy {
        TTAdManagerHolder.get().createAdNative(activity)
    }

    protected val AD_TIME_OUT = 2000
    protected var mKTouTiaoAppKey: String? = null
    protected var mKTouTiaoKaiPing: String? = null
    protected  var mKTouTiaoBannerKey: String? = null
    protected var mKTouTiaoChaPingKey: String? = null
    protected var mKTouTiaoJiLiKey: String? = null
    protected var mKTouTiaoSeniorKey: String? = null
    protected var mKgdtMobSDKAppKey: String? = null
    protected var mKgdtMobSDKChaPingKey: String? = null
    protected  var mKgdtMobSDKKaiPingKey: String? = null
    protected var mKgdtMobSDKBannerKey: String? = null
    protected  var mKgdtMobSDKNativeKey: String? = null
    protected var mKgdtMobSDKJiLiKey: String? = null
    protected var mKgdtMobSDKJSmallNativeKey: String? = null
    protected  var mAdShowStateListener:AdShowStateListener?=null

    init {
        val adKey = AdMsgUtil.getADKey()
        if (adKey != null) {
            mKTouTiaoAppKey = adKey[Contents.KT_OUTIAO_APPKEY]
            mKTouTiaoKaiPing = adKey[Contents.KT_OUTIAO_KAIPING]
            mKTouTiaoBannerKey = adKey[Contents.KT_OUTIAO_BANNERKEY]
            mKTouTiaoChaPingKey = adKey[Contents.KT_OUTIAO_CHAPINGKEY]
            mKTouTiaoJiLiKey = adKey[Contents.KT_OUTIAO_JILIKEY]
            mKTouTiaoSeniorKey = adKey[Contents.KT_OUTIAO_SENIORKEY]
            mKgdtMobSDKAppKey = adKey[Contents.KGDT_MOBSDK_APPKEY]
            mKgdtMobSDKChaPingKey = adKey[Contents.KGDT_MOBSDK_CHAPINGKEY]
            mKgdtMobSDKKaiPingKey = adKey[Contents.KGDT_MOBSDK_KAIPINGKEY]
            mKgdtMobSDKBannerKey = adKey[Contents.KGDT_MOBSDK_BANNERKEY]
            mKgdtMobSDKNativeKey = adKey[Contents.KGDT_MOBSDK_NATIVEKEY]
            mKgdtMobSDKJiLiKey = adKey[Contents.KGDT_MOBSDK_JILIKEY]
            mKgdtMobSDKJSmallNativeKey = adKey[Contents.KGDT_MOBSDK_SMALLNATIVEKEY]
            GDTADManager.getInstance().initWith(activity, mKgdtMobSDKAppKey)
        }

    }

    abstract fun showAd()

    abstract fun  releaseAd()

    fun setAdShowStateListener(listener: AdShowStateListener){
        mAdShowStateListener=listener
    }

}


interface AdShowStateListener{

    fun showSuccess()

    fun showError()
}