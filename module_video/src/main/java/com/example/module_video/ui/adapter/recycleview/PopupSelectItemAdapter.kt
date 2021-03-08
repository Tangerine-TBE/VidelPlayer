package com.example.module_video.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemSelectPopupContainerBinding
import com.example.module_video.domain.ItemBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/8 17:04:24
 * @class describe
 */
class PopupSelectItemAdapter:BaseQuickAdapter<ItemBean,BaseDataBindingHolder<ItemSelectPopupContainerBinding>>(R.layout.item_select_popup_container) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemSelectPopupContainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            itemText.text="${item.title}"
        }
    }

}