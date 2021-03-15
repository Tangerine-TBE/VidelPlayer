package com.example.module_video.ui.adapter.recycleview

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemPwdContianerBinding
import com.example.module_video.domain.ItemBean
import com.umeng.commonsdk.debug.I

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/15 10:41:27
 * @class describe
 */
class PwdAdapter:BaseQuickAdapter<ItemBean,BaseDataBindingHolder<ItemPwdContianerBinding>>(R.layout.item_pwd_contianer) {
    override fun convert(holder: BaseDataBindingHolder<ItemPwdContianerBinding>, item: ItemBean) {
        holder.dataBinding?.apply {
            pwdHint.setImageResource(if (item.hasPwd) R.drawable.shape_pwd_select else R.drawable.shape_pwd_normal)
        }
    }
}