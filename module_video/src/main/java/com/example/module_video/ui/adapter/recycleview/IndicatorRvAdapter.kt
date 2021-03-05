package com.example.module_video.ui.adapter.recycleview

import android.graphics.Color
import android.util.TypedValue
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemIndicatorContainerBinding
import java.text.FieldPosition

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/5 11:38:19
 * @class describe
 */
class IndicatorRvAdapter:BaseQuickAdapter<String,BaseDataBindingHolder<ItemIndicatorContainerBinding>>(R.layout.item_indicator_container) {

    private var mSelectPosition=0

     fun setPosition(position: Int){
        mSelectPosition=position
        notifyDataSetChanged()
    }

    override fun convert(holder: BaseDataBindingHolder<ItemIndicatorContainerBinding>, item: String) {
        holder.dataBinding?.apply {
            indicatorText.apply {
                text= "$item"
                setTextColor( if (mSelectPosition == holder.adapterPosition) ContextCompat.getColor(context,R.color.theme_color) else Color.WHITE )
               if (mSelectPosition == holder.adapterPosition) {
                  startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_scale_text))
              }
            }
        }
    }
}