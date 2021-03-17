package com.example.module_video.ui.adapter.recycleview

import android.view.View
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemPopupPlayListContainerBinding
import com.example.module_video.domain.MediaInformation

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/17 18:55:13
 * @class describe
 */
class PopupPlayListAdapter:BaseQuickAdapter<MediaInformation,BaseDataBindingHolder<ItemPopupPlayListContainerBinding>>(R.layout.item_popup_play_list_container) {
    private var mPosition=0

    fun setPosition(position:Int){
        mPosition=position
        notifyDataSetChanged()
    }


    override fun convert(
        holder: BaseDataBindingHolder<ItemPopupPlayListContainerBinding>,
        item: MediaInformation
    ) {
        holder.dataBinding?.apply {
            mediaName.text=item.name
            mediaName.setTextColor( ContextCompat.getColor(context,  if (mPosition==holder.adapterPosition)R.color.theme_color else R.color.white))
            selectMedia.visibility=if (mPosition==holder.adapterPosition) View.VISIBLE else View.GONE
        }
    }
}