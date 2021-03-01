package com.example.module_video.ui.fragment

import com.example.module_base.base.BaseVmFragment
import com.example.module_video.R
import com.example.module_video.databinding.FragmentListBinding
import com.example.module_video.viewmode.ListViewModel

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:54:55
 * @class describe
 */
class FileListFragment  : BaseVmFragment<FragmentListBinding, ListViewModel>() {



    override fun getViewModelClass(): Class<ListViewModel> {
        return ListViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_list
}