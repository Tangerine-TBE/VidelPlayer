package com.example.module_video.ui.widget.popup

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BasePopup
import com.example.module_video.R
import com.example.module_video.databinding.PopupInputWindowBinding
import com.example.module_video.databinding.PopupRemindWindowBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.ui.adapter.recycleview.PopupSelectItemAdapter


/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/9 18:07:08
 * @class describe
 */
class RemindPopup(activity: FragmentActivity?): BasePopup<PopupRemindWindowBinding>(activity, R.layout.popup_remind_window, ViewGroup.LayoutParams.MATCH_PARENT) {
    private  val mSelectItemAdapter=PopupSelectItemAdapter()
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

    fun setContent(list:MutableList<ItemBean>){
       mView.remindContent.apply {
            layoutManager=LinearLayoutManager(activity)
            adapter=mSelectItemAdapter
            mSelectItemAdapter.setList(list)
            mSelectItemAdapter.notifyDataSetChanged()
        }
    }

    fun setTitle(title:String){
        mView.remindTitle.text="$title"
    }




}