package com.example.module_ad.ad.ad_help

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.base.AdTypeBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad.ad_help
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 14:40:25
 * @class describe
 */
abstract class AdHelper(val activity: Activity,val container: FrameLayout) {


    abstract fun showAd(type: AdTypeBean?=null,random:Double)

    abstract fun releaseAd()

}