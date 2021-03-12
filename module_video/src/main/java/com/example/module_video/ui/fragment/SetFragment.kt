package com.example.module_video.ui.fragment

import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.module_base.utils.toOtherActivity
import com.example.module_video.R
import com.example.module_video.databinding.FragmentSetBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.activity.PlayListActivity
import com.example.module_video.ui.adapter.recycleview.SetAdapter
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

    private val mGeneralAdapter by lazy {
        SetAdapter()
    }


    private val mUsSetAdapter by lazy {
        SetAdapter()
    }

    private val mFunctionAdapter by lazy {
        SetAdapter()
    }


    override fun getViewModelClass(): Class<SetViewModel> {
        return SetViewModel::class.java
    }
    override fun getChildLayout(): Int = R.layout.fragment_set

    override fun initView() {
        val generalList= arrayListOf(
            ItemBean(title = "登陆/注册",isLogin = false),
            ItemBean(title = "移除底部广告"),
        )



        binding.apply {
            setStatusBar(context, mNestedScrollView, LayoutType.LINEARLAYOUT)

            mGeneralContainer.apply {
                layoutManager=LinearLayoutManager(activity)
                mGeneralAdapter.setList(generalList)
                adapter=mGeneralAdapter
            }


            mWeContainer.apply {
                layoutManager=LinearLayoutManager(activity)
                mUsSetAdapter.setList(DataProvider.setConnectUsList)
                adapter=mUsSetAdapter
            }

            mSetContainer.apply {
                layoutManager=LinearLayoutManager(activity)
                mFunctionAdapter.setList(DataProvider.setFunctionList)
                adapter=mFunctionAdapter
            }


        }
    }

    override fun initEvent() {
        binding.apply {


        }
    }

}