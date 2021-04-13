package com.example.module_video.ui.widget.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.module_video.R
import com.example.module_video.databinding.PopupBuyVipWindowBinding
import com.example.module_video.databinding.PopupPayWindowBinding

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/4/1 10:19:07
 * @class describe
 */
class PayPopup (activity: FragmentActivity?): BasePopup<PopupPayWindowBinding>(
    activity,
    R.layout.popup_pay_window,
    ViewGroup.LayoutParams.MATCH_PARENT
) {

    override fun initEvent() {
        mView.apply {
            payWX.setOnClickListener {
                mListener?.cancel()
                dismiss()
            }

            payZfb.setOnClickListener {
                mListener?.sure()
                dismiss()
            }

        }

    }

}