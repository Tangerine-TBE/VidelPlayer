package com.example.module_video.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemFunctionPopupContainerBinding
import com.example.module_video.databinding.ItemSelectPopupContainerBinding
import com.example.module_video.domain.ItemBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/17 14:42:34
 * @class describe
 */
class PopupFunctionAdapter:
    BaseQuickAdapter<ItemBean, BaseDataBindingHolder<ItemFunctionPopupContainerBinding>>(R.layout.item_function_popup_container) {

    private val mPosition=0

    override fun convert(
        holder: BaseDataBindingHolder<ItemFunctionPopupContainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            functionText.text=item.title

            functionSmoothCheckBox.isChecked = mPosition == holder.adapterPosition

        }
    }

}