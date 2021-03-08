package com.example.module_video.ui.adapter.recycleview

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemBottomContainerBinding
import com.example.module_video.domain.ItemBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/8 15:04:52
 * @class describe
 */
class BottomAdapter:BaseQuickAdapter<ItemBean,BaseDataBindingHolder<ItemBottomContainerBinding>>(R.layout.item_bottom_container){

    private var mPosition=0
    override fun convert(
        holder: BaseDataBindingHolder<ItemBottomContainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            bottomTitle.text="${item.title}"
            bottomIcon.setImageResource( if (mPosition == holder.adapterPosition) item.selectIcon else item.normalIcon)
            bottomTitle.setTextColor(  ContextCompat.getColor(context,if (mPosition == holder.adapterPosition) R.color.theme_color else R.color.item_text) )
        }
    }

    fun setSelectPosition(position: Int) {
        mPosition=position
        notifyDataSetChanged()
    }
}