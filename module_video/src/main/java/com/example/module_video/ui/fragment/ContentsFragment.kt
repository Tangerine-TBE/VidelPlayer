package com.example.module_video.ui.fragment

import com.example.module_base.base.BaseVmFragment
import com.example.module_video.R
import com.example.module_video.databinding.FragmentContentsBinding
import com.example.module_video.viewmode.ContentsViewModel

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 15:24:23
 * @class describe
 */
class ContentsFragment:BaseVmFragment<FragmentContentsBinding,ContentsViewModel>() {

    companion object{
        @JvmStatic
        fun getInstance():ContentsFragment{
            return ContentsFragment()
        }

    }

    override fun getViewModelClass(): Class<ContentsViewModel> {
        return ContentsViewModel::class.java
    }
    override fun getChildLayout(): Int = R.layout.fragment_contents
}