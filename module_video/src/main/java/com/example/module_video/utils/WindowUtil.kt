package com.example.module_video.utils

import android.content.Context

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/22 18:24:48
 * @class describe
 */
object WindowUtil {

    /**
     * 华为
     * @param context Context
     * @return Boolean
     */
    fun hasNotchAtHuawei(context: Context): Boolean {
        var ret = false
        try {
            val classLoader: ClassLoader = context.classLoader
            val HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = HwNotchSizeUtil.getMethod("hasNotchInScreen")
             ret = get.invoke(HwNotchSizeUtil) as Boolean
        } catch (e: ClassNotFoundException) {

        } catch (e: NoSuchMethodException) {

        } catch (e: Exception) {

        } finally {
            return ret
        }
    }

    const val NOTCH_IN_SCREEN_VOIO = 0x00000020 //是否有凹槽

    const val ROUNDED_IN_SCREEN_VOIO = 0x00000008 //是否有圆角


    /**
     * oppo
     * @param context Context
     * @return Boolean
     */
    fun hasNotchInOppo(context: Context): Boolean {
        return context.packageManager.hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }


    /**
     * vivo
     * @param context Context
     * @return Boolean
     */
    fun hasNotchInScreenAtVoio(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val FtFeature = cl.loadClass("com.util.FtFeature")
            val get = FtFeature.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            ret = get.invoke(FtFeature, NOTCH_IN_SCREEN_VOIO) as Boolean
        } catch (e: ClassNotFoundException) {

        } catch (e: NoSuchMethodException) {

        } catch (e: java.lang.Exception) {

        } finally {
            return ret
        }
    }



    /**小米
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    fun hasNotch(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val SystemProperties = cl.loadClass("android.os.SystemProperties")
            val get = SystemProperties.getMethod("getInt", String::class.java, Int::class.javaPrimitiveType)
            ret = get.invoke(SystemProperties, "ro.miui.notch", 0) as Int == 1
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            return ret
        }
    }


}