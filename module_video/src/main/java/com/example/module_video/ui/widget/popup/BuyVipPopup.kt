package com.example.module_video.ui.widget.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.module_video.R
import com.example.module_video.databinding.PopupBuyVipWindowBinding
import com.example.module_video.databinding.PopupFunctionWindowBinding

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/4/1 9:55:14
 * @class describe
 */
class BuyVipPopup (activity: FragmentActivity?): BasePopup<PopupBuyVipWindowBinding>(
    activity,
    R.layout.popup_buy_vip__window,
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {



    override fun initEvent() {
        mView.apply {
            cancel.setOnClickListener {
                dismiss()
            }

            sure.setOnClickListener {
                mListener?.sure()
                dismiss()
            }

        }

    }

}