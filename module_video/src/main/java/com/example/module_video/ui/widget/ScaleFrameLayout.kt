package com.example.module_video.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.FrameLayout
import android.widget.Toast
import com.example.module_base.utils.LogUtils
import com.example.module_video.R
import com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext
import kotlin.math.abs
import kotlin.math.sqrt


/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/18 17:58:46
 * @class describe
 */
class ScaleFrameLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var touchDownX = 0f
    private var touchDownY = 0f

    var onScaledListener: OnScaledListener? = null

    interface OnScaledListener {
        fun onScaled(x: Float, y: Float, event: MotionEvent)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event == null) return super.onTouchEvent(event)
        // 屏蔽掉浮窗的事件拦截，仅由自身消费
        parent?.requestDisallowInterceptTouchEvent(true)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownX = event.x
                touchDownY = event.y
            }

            MotionEvent.ACTION_MOVE ->
                onScaledListener?.onScaled(event.x - touchDownX, event.y - touchDownY, event)

        }
        return true
    }

}