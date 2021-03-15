package com.example.module_video.ui.widget.popup

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.module_video.R
import com.example.module_video.databinding.PopupRemindWindowBinding
import com.example.module_video.databinding.PopupUserWindowBinding

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/15 20:26:33
 * @class describe
 */
class UserRemindPopup(activity: FragmentActivity?): BasePopup<PopupUserWindowBinding>(activity,R.layout.popup_user_window, ViewGroup.LayoutParams.MATCH_PARENT) {
    override fun initEvent() {
        mView.apply {
            cancel.setOnClickListener {
                this@UserRemindPopup.dismiss()
            }

            sure.setOnClickListener {
                this@UserRemindPopup.dismiss()
                mListener?.sure()
            }

        }
    }


    fun setRemindContent(content:String){
        mView.hintTip.text=content
    }

}