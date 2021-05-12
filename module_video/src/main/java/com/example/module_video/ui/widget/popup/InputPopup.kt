package com.example.module_video.ui.widget.popup

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.RxKeyboardTool
import com.example.module_video.R
import com.example.module_video.databinding.PopupInputWindowBinding


/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/9 15:13:06
 * @class describe
 */
class InputPopup(activity: FragmentActivity?):BasePopup<PopupInputWindowBinding>(activity,R.layout.popup_input_window,ViewGroup.LayoutParams.MATCH_PARENT) {


    override fun initEvent() {
        mView.apply {
            inputDelete.setOnClickListener {
                inputContent.setText("")
            }


            cancel.setOnClickListener {
                dismiss()
            }

            sure.setOnClickListener {
                mListener?.sure()
                dismiss()
            }


        }

    }

    override fun showPopupView(view: View, gravity: Int, x: Int, y: Int) {
        super.showPopupView(view, gravity, x, y)
        mView.inputContent.setText("")
        RxKeyboardTool.toggleSoftInput(view.context,mView.inputContent)
    }


    fun setHint(hint:String?){
        mView.inputContent.hint="$hint"

    }

    fun setTitle(title:String){
        mView.inputTitle.text="$title"
    }

    fun setContent(content:String){
        mView.inputContent.setText(content)
    }

    fun getContent()=mView.inputContent.text.toString().trim()

}