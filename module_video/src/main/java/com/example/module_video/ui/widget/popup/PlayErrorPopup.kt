package com.example.module_video.ui.widget.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.module_video.R
import com.example.module_video.databinding.PopupPlayErrorWindowBinding

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/19 13:53:40
 * @class describe
 */
class PlayErrorPopup(activity: FragmentActivity?):BasePopup<PopupPlayErrorWindowBinding>(activity, R.layout.popup_play_error_window, ViewGroup.LayoutParams.MATCH_PARENT) {

    override fun initEvent() {
        mView.sure.setOnClickListener {
            mListener?.sure()
            dismiss()
        }
    }
}