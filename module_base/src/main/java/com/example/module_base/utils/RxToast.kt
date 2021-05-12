package com.example.module_base.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.NinePatchDrawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.example.module_base.R
import com.example.module_base.base.BaseApplication


/**
 * @name VidelPlayer
 * @class name：com.example.module_base.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/4/15 17:17:16
 * @class describe
 */
object RxToast {
    @ColorInt
    private val DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF")

    @ColorInt
    private val ERROR_COLOR = Color.parseColor("#FD4C5B")

    @ColorInt
    private val INFO_COLOR = Color.parseColor("#2196F3")

    @ColorInt
    private val SUCCESS_COLOR = Color.parseColor("#52BA97")

    @ColorInt
    private val WARNING_COLOR = Color.parseColor("#FFA900")
    private const val TOAST_TYPEFACE = "sans-serif-condensed"
    private var currentToast: Toast? = null
    //*******************************************普通 使用ApplicationContext 方法*********************
    /**
     * Toast 替代方法 ：立即显示无需等待
     */
    private var mToast: Toast? = null
    private var mExitTime: Long = 0



    //******************************************系统 Toast 替代方法***************************************
    @JvmStatic
    fun setBackground(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    @JvmStatic
    fun getDrawable(context: Context, @DrawableRes id: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     *
     * @param context Context
     * @param str     要显示的字符串
     * @param isLong  Toast.LENGTH_LONG / Toast.LENGTH_SHORT
     */
    @JvmStatic
    fun showToast(context: Context?, str: String?, isLong: Boolean) {
        if (isLong) {
            Toast.makeText(context, str, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastShort(str: String?) {
        Toast.makeText( BaseApplication.application, str, Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastShort(resId: Int) {
        Toast.makeText( BaseApplication.application,  BaseApplication.application.getString(resId), Toast.LENGTH_SHORT).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastLong(str: String?) {
        Toast.makeText( BaseApplication.application, str, Toast.LENGTH_LONG).show()
    }

    /**
     * 封装了Toast的方法 :需要等待
     */
    @JvmStatic
    fun showToastLong(resId: Int) {
        Toast.makeText( BaseApplication.application,  BaseApplication.application.getString(resId), Toast.LENGTH_LONG).show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param msg 显示内容
     */
    @JvmStatic
    fun showToast(msg: String?) {
        if (mToast == null) {
            mToast = Toast.makeText( BaseApplication.application, msg, Toast.LENGTH_LONG)
        } else {
            mToast?.setText(msg)
        }
        mToast?.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param resId String资源ID
     */
    @SuppressLint("ShowToast")
    @JvmStatic
    fun showToast(resId: Int) {
        if (mToast == null) {

            mToast = Toast.makeText( BaseApplication.application,  BaseApplication.application.getString(resId), Toast.LENGTH_LONG)
        } else {
            mToast?.setText( BaseApplication.application.getString(resId))
        }
        mToast?.show()
    }

    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param resId    String资源ID
     * @param duration 显示时长
     */
    @JvmStatic
    fun showToast(context: Context, resId: Int, duration: Int) {
        showToast(context, context.getString(resId), duration)
    }
    //===========================================Toast 替代方法======================================
    /**
     * Toast 替代方法 ：立即显示无需等待
     *
     * @param context  实体
     * @param msg      要显示的字符串
     * @param duration 显示时长
     */
    @SuppressLint("ShowToast")
    @JvmStatic
    fun showToast(context: Context?, msg: String?, duration: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration)
        } else {
            mToast?.setText(msg)
        }
        mToast?.show()
    }

}