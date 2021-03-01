package com.example.module_video.ui.fragment

import androidx.navigation.fragment.findNavController
import com.example.module_base.base.BaseVmFragment
import com.example.module_video.R
import com.example.module_video.databinding.FragmentSetBinding
import com.example.module_video.viewmode.SetViewModel

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:55:03
 * @class describe
 */
class SetFragment : BaseVmFragment<FragmentSetBinding, SetViewModel>(){
    override fun getViewModelClass(): Class<SetViewModel> {
        return SetViewModel::class.java
    }
    override fun getChildLayout(): Int = R.layout.fragment_set

    override fun initView() {

    }

}