package com.example.module_video.ui.fragment

import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.module_base.utils.toOtherActivity
import com.example.module_video.R
import com.example.module_video.databinding.FragmentMediaBinding
import com.example.module_video.ui.activity.PlayVideoActivity
import com.example.module_video.ui.adapter.IndicatorAdapter
import com.example.module_video.ui.adapter.viewpager.HomePagerAdapter
import com.example.module_video.viewmode.ListViewModel
import com.example.module_video.viewmode.MediaViewModel
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:54:16
 * @class describe
 */
class MediaFragment :BaseVmFragment<FragmentMediaBinding,MediaViewModel>(){
    private val mHomePagerAdapter by lazy {
        HomePagerAdapter(childFragmentManager)
    }
    private val mIndicatorAdapter by lazy {
        IndicatorAdapter()
    }

    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }
    override fun getChildLayout(): Int= R.layout.fragment_media


    override fun initView() {
        binding.apply {
            setStatusBar(activity,homeIndicator, LayoutType.CONSTRAINTLAYOUT)

            val commonNavigator = CommonNavigator(context)
            commonNavigator.adapter=mIndicatorAdapter
            homeIndicator.navigator=commonNavigator

            homePager.adapter=mHomePagerAdapter
            ViewPagerHelper.bind(homeIndicator,homePager)


        }

    }


    override fun initEvent() {
    binding.apply {
        mIndicatorAdapter.setOnIndicatorClickListener(object :IndicatorAdapter.OnIndicatorClickListener{
            override fun onIndicatorClick(position: Int) {
                homePager.currentItem=position
            }
        })

        imageView.setOnClickListener {
            toOtherActivity<PlayVideoActivity>(activity){}
        }

    }
    }
}