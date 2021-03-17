package com.example.module_video.ui.adapter.recycleview

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemSetContainerBinding
import com.example.module_video.domain.ItemBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/12 17:36:02
 * @class describe
 */
class SetAdapter:BaseQuickAdapter<ItemBean,BaseDataBindingHolder<ItemSetContainerBinding>>(R.layout.item_set_container) {

    private var mListener:OnCheckListener?=null
    private var hasContact=false
    private var hasSwitch=false
    private var hasSet=false

    fun setHasSet(){
        hasSet=true
        notifyDataSetChanged()
    }

    fun setHasSwitch(){
        hasSwitch=true
        notifyDataSetChanged()
    }

    fun setHasContact(){
        hasContact=true
        notifyDataSetChanged()
    }

    override fun convert(holder: BaseDataBindingHolder<ItemSetContainerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            setTitle.text = "${item.title}"
            if (hasContact) {
                if (holder.adapterPosition == 1) {
                    hintText.text =item.hint
                }
            }

            if (hasSwitch) {
                if (holder.adapterPosition == 0) {
                    switchBt.isChecked=item.hasPwd
                    imageView.visibility = View.GONE
                    switchBt.visibility = View.VISIBLE

                    switchBt.setOnClickListener {
                        mListener?.onClick()
                    }

                }
            }

            if (hasSet){
                hintText.text =item.hint
            }

        }
    }



    interface OnCheckListener{
        fun onCheck(b:Boolean)
        fun onClick()
    }

    fun setOnCheckListener(listener:OnCheckListener){
        mListener=listener
    }
}