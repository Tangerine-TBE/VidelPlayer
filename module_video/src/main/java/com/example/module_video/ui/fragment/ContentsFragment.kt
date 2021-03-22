package com.example.module_video.ui.fragment

import com.example.module_base.base.BaseVmFragment
import com.example.module_video.R
import com.example.module_video.databinding.FragmentContentsBinding
import com.example.module_video.ui.adapter.recycleview.MediaFileAdapter
import com.example.module_video.viewmodel.ContentsViewModel

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 15:24:23
 * @class describe
 */
class ContentsFragment:BaseVmFragment<FragmentContentsBinding,ContentsViewModel>() {

    private val mMediaFileAdapter by lazy {
        MediaFileAdapter()
    }

    companion object{
        @JvmStatic
        fun getInstance(position:Int):ContentsFragment{
            return ContentsFragment().apply {
             /*   val bundle = Bundle()
                bundle.putInt(Constants.KEY_TYPE_FRAGMENT,position)
                arguments=bundle*/
            }
        }
    }

    override fun getViewModelClass(): Class<ContentsViewModel> {
        return ContentsViewModel::class.java
    }
    override fun getChildLayout(): Int = R.layout.fragment_contents

    private var mType=0

    override fun initView() {
 /*       binding.apply {
            contentList.layoutManager=LinearLayoutManager(activity)
            contentList.adapter=mMediaFileAdapter
        }

        mType=arguments?.getInt(Constants.KEY_TYPE_FRAGMENT)?:0


        viewModel.getMediaFile(mType)*/

    }

    override fun observerData() {
 /*       binding.apply {
            viewModel.apply {
                videoList.observe(this@ContentsFragment,{
                    mMediaFileAdapter.setList(it)
                    LogUtils.i("--observerData-------------${it.size}--------")
                })
            }
        }*/

    }

    override fun initEvent() {
   /*     mMediaFileAdapter.setOnItemClickListener { adapter, view, position ->
            mMediaFileAdapter.data[position]?.let {
                toPlayVideo(view,Gson().toJson(it))
            }
        }*/
    }



}