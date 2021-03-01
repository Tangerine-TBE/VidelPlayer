package com.example.module_video.ui.adapter

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.module_video.R
import com.example.module_video.repository.DataProvider.homeItemList
import com.example.module_video.ui.widget.ScaleTransitionPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * @author: Administrator
 * @date: 2020/8/9 0009
 */
class IndicatorAdapter : CommonNavigatorAdapter() {
    private var mOnIndicatorClickListener: OnIndicatorClickListener? = null
    override fun getCount()=homeItemList.size

    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        val simplePagerTitleView: ColorTransitionPagerTitleView =
            ScaleTransitionPagerTitleView(context)
        simplePagerTitleView.text = homeItemList[index]
        simplePagerTitleView.textSize = 13f
        simplePagerTitleView.normalColor = Color.WHITE
        simplePagerTitleView.selectedColor = ContextCompat.getColor(context, R.color.theme_color)
        simplePagerTitleView.setOnClickListener {
                mOnIndicatorClickListener?.onIndicatorClick(index)
        }
        return simplePagerTitleView
    }

    fun setOnIndicatorClickListener(listener: OnIndicatorClickListener?) {
        mOnIndicatorClickListener = listener
    }

    interface OnIndicatorClickListener {
        fun onIndicatorClick(position: Int)
    }

    override fun getIndicator(context: Context): IPagerIndicator {
        val indicator = LinePagerIndicator(context)
        indicator.lineHeight = 0f
        return indicator
    }
}