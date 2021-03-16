package com.example.module_video.ui.widget.popup.play

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.example.module_video.R
import razerdp.basepopup.BasePopupWindow

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/16 16:37:05
 * @class describe
 */
class PlayListPopup(context: Context) : BasePopupWindow(context) {

    private lateinit var mIvArrow:ImageView
    override fun onCreateContentView(): View = createPopupById(R.layout.popup_play_list_window)
    override fun onViewCreated(contentView: View) {
        mIvArrow=contentView.findViewById(R.id.iv_arrow)
    }



    override fun onPopupLayout(popupRect: Rect, anchorRect: Rect) {
        val gravity = computeGravity(popupRect, anchorRect)
        var verticalCenter = false
        when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth() shr 1).toFloat())
                mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight()).toFloat())
                mIvArrow.setRotation(0f)
            }
            Gravity.BOTTOM -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth() shr 1).toFloat())
                mIvArrow.setTranslationY(0f)
                mIvArrow.setRotation(180f)
            }
            Gravity.CENTER_VERTICAL -> verticalCenter = true
        }
        when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.LEFT -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth()).toFloat())
                mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight() shr 1).toFloat())
                mIvArrow.setRotation(270f)
            }
            Gravity.RIGHT -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX(0f)
                mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight() shr 1).toFloat())
                mIvArrow.setRotation(90f)
            }
            Gravity.CENTER_HORIZONTAL -> mIvArrow.setVisibility(if (verticalCenter) View.INVISIBLE else View.VISIBLE)
        }
    }
}