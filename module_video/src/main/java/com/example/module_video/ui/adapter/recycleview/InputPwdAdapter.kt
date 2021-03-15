package com.example.module_video.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemInputPwdContainerBinding
import com.example.module_video.domain.ItemBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/15 10:42:54
 * @class describe
 */
class InputPwdAdapter:BaseQuickAdapter<ItemBean,BaseDataBindingHolder<ItemInputPwdContainerBinding>>(R.layout.item_input_pwd_container) {
    override fun convert(
        holder: BaseDataBindingHolder<ItemInputPwdContainerBinding>,
        item: ItemBean
    ) {
        holder.dataBinding?.apply {
            pwdNumber.text="${item.title}"
        }
    }
}