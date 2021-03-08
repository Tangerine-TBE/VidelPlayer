package com.example.module_video.ui.fragment

import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.setStatusBar
import com.example.module_video.R
import com.example.module_video.databinding.FragmentMediaBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.ValueMediaType
import com.example.module_video.livedata.MediaLiveData
import com.example.module_video.ui.adapter.IndicatorAdapter
import com.example.module_video.ui.adapter.recycleview.MediaFileAdapter
import com.example.module_video.ui.adapter.viewpager.HomePagerAdapter
import com.example.module_video.ui.widget.popup.ItemSelectPopup
import com.example.module_video.viewmode.MediaViewModel
import com.tamsiree.rxkit.RxKeyboardTool
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
class MediaFragment : BaseVmFragment<FragmentMediaBinding, MediaViewModel>() {
    private val mHomePagerAdapter by lazy {
        HomePagerAdapter(childFragmentManager)
    }
    private val mIndicatorAdapter by lazy {
        IndicatorAdapter()
    }


    private val mMediaAllAdapter by lazy {
        MediaFileAdapter()
    }

    private val mItemSelectPopup by lazy {
        ItemSelectPopup(activity)
    }

    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_media


    override fun initView() {
        //查询资源
        MediaLiveData.getMediaResource()
        binding.apply {
            data = viewModel
            //设置滑动栏
            initIndicator()
            //设置不同类型列表
            initRecycleViewType()
        }

    }

    private val mAllMediaList = ArrayList<MediaInformation>()
    private var mAllResource: ValueMediaType? = null
    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that = this@MediaFragment
                MediaLiveData.observe(that, { it ->
                    mAllResource = it
                    //所有
                    mAllMediaList.clear()
                    mAllMediaList.addAll(it.videoList)
                    mAllMediaList.addAll(it.audioList)
                    mMediaAllAdapter.setList(mAllMediaList)
                }
                )

                editAction.observe(that, {
                    mMediaAllAdapter.setEditAction(it)
                    viewModel.setSelectItemList(mMediaAllAdapter.getSelectList())
                })
            }
        }


    }

    override fun initEvent() {
        binding.apply {
            mIndicatorAdapter.setOnIndicatorClickListener(object :
                IndicatorAdapter.OnIndicatorClickListener {
                override fun onIndicatorClick(position: Int) {
                    homePager.currentItem = position
                    viewModel.setCurrentPosition(position)
                    setPositionData(position)
                }
            })

            //搜索中
            searchInput.doOnTextChanged { text, start, before, count ->
                if (text?.length ?: 0 > 0) {
                    showView(searchDelete)
                } else {
                    goneView(searchDelete)
                }
            }

            //删除
            searchDelete.setOnClickListener {
                searchInput.setText("")
                RxKeyboardTool.hideSoftInput(searchInput)
                viewModel.setSearchAction(false)
            }

            //编辑
            editAction.setOnClickListener {
                viewModel.setEditAction(!viewModel.getEditAction_())

            }

            //搜索
            searchAction.setOnClickListener {
                searchInput.requestFocus()
                RxKeyboardTool.showSoftInput(requireContext(), searchInput)
                viewModel.setSearchAction(!viewModel.getSearchAction_())
            }


            mMediaAllAdapter.setOnItemClickListener(object : MediaFileAdapter.OnItemClickListener {
                override fun onItemClick(item: MediaInformation, position: Int) {
                    if (viewModel.getEditAction_()) {
                        viewModel.setSelectItemList(mMediaAllAdapter.getSelectList())
                    } else {
                        mItemSelectPopup?.apply {
                            setTitleText(item)
                            showPopupView(mediaAll)
                        }
                    }
                }
            })

        }
    }

    private fun setPositionData(position: Int) {
        mAllResource?.let {

            mMediaAllAdapter.getSelectList().forEach {
                LogUtils.i("*------------mMediaAllAdapter------------------${it.hashCode()}-")
            }
            when (position) {
                0 -> {
                    mMediaAllAdapter.setList(mAllMediaList)

                }
                1 -> {
                    mMediaAllAdapter.setList(it.videoList)
                }
                2 -> {
                    mMediaAllAdapter.setList(it.audioList)
                }
            }
        }
    }

    private fun FragmentMediaBinding.initRecycleViewType() {
        mediaAll.layoutManager = LinearLayoutManager(activity)
        mediaAll.adapter = mMediaAllAdapter
    }

    private fun FragmentMediaBinding.initIndicator() {
        setStatusBar(context, homeIndicator, LayoutType.CONSTRAINTLAYOUT)
        val commonNavigator = CommonNavigator(context)
        commonNavigator.adapter = mIndicatorAdapter
        homeIndicator.navigator = commonNavigator
        homePager.adapter = mHomePagerAdapter
        ViewPagerHelper.bind(homeIndicator, homePager)
    }

    override fun release() {
        mItemSelectPopup?.dismiss()
    }

}