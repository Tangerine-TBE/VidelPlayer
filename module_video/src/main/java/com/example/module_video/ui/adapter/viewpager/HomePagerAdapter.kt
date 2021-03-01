package com.example.module_video.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.fragment.ContentsFragment

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.viewpager
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 14:22:44
 * @class describe
 */
class HomePagerAdapter( fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager,
    BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int=DataProvider.homeItemList.size
    override fun getItem(position: Int): Fragment {
        return ContentsFragment.getInstance()
        }
}