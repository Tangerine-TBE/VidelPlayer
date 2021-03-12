package com.example.module_video.ui.fragment

import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.module_base.utils.toOtherActivity
import com.example.module_video.R
import com.example.module_video.databinding.FragmentMediaBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.domain.MediaDataBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.ValueMediaType
import com.example.module_video.livedata.MediaLiveData
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.activity.PlayListActivity
import com.example.module_video.ui.activity.PlayVideoActivity
import com.example.module_video.ui.adapter.IndicatorAdapter
import com.example.module_video.ui.adapter.recycleview.MediaFileAdapter
import com.example.module_video.ui.adapter.viewpager.HomePagerAdapter
import com.example.module_video.ui.widget.popup.InputPopup
import com.example.module_video.ui.widget.popup.ItemSelectPopup
import com.example.module_video.ui.widget.popup.RemindPopup
import com.example.module_video.utils.FileUtil
import com.example.module_video.utils.GeneralState
import com.example.module_video.utils.MediaState
import com.example.module_video.viewmode.MediaViewModel
import com.google.gson.Gson
import com.tamsiree.rxkit.RxKeyboardTool
import com.tamsiree.rxkit.view.RxToast
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import java.io.File

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

    private val mRenamePopup by lazy {
        InputPopup(activity).apply {
            setTitle("重命名")
        }
    }
    private val mDeletePopup by lazy {
        RemindPopup(activity)
    }


    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_media


    override fun initView() {

        binding.apply {
            data = viewModel
            //设置滑动栏
            initIndicator()
            //设置不同类型列表
            initRecycleViewType()
        }

    }


    private var mMediaResource:ValueMediaType?=null

    private val mAllMediaList = ArrayList<MediaInformation>()
    private var mItemValue: MediaInformation? = null
    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that = this@MediaFragment
                MediaLiveData.observe(that, {
                    mMediaResource=it
                    mMediaAllAdapter.setList(setPositionData(it))
                })

                editAction.observe(that, {
                    mMediaAllAdapter.setEditAction(it)
                    viewModel.setSelectItemList(mMediaAllAdapter.getSelectList())
                })

                currentPosition.observe(that,{
                    setMediaListData()
                })

                deleteFileState.observe(that,{
                    when(it){
                        GeneralState.LOADING->mLoadingDialog.show()
                        GeneralState.SUCCESS->mLoadingDialog.dismiss()
                    }
                })

                searchMediaList.observe(that,{
                    mMediaAllAdapter.setList(it)
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
                }
            })

            //搜索中
            searchInput.doOnTextChanged { text, start, before, count ->
                if (text?.length ?: 0 > 0) {
                    showView(searchDelete)
                    mMediaResource?.let {
                        viewModel.getSearchList(text.toString().trim(), setPositionData(it))
                    }
                } else {
                    goneView(searchDelete)
                    setMediaListData()
                }
            }

            //删除
            searchDelete.setOnClickListener {
                searchInput.setText("")
                RxKeyboardTool.hideSoftInput(searchInput)
                viewModel.setSearchAction(false)
                setMediaListData()
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
                override fun onItemClick(item: MediaInformation, position: Int,itemView: View) {
                    if (viewModel.getEditAction_()) {
                        viewModel.setSelectItemList(mMediaAllAdapter.getSelectList())
                    } else {
                        PlayVideoActivity.toPlayVideo(activity,itemView,Gson().toJson(item))
                    }
                }

                override fun onItemSubClick(item: MediaInformation, position: Int) {
                    mItemSelectPopup?.apply {
                        mItemValue = item
                        setTitleNormal(item.name, DataProvider.homePopupList)
                        showPopupView(mediaAll)
                    }
                }
            })

            //选择弹窗动作
            mItemSelectPopup?.apply {
                setItemAction({
                    //重命名
                    mRenamePopup.apply {
                        doItemAction {
                            setHint(it.name)
                            showPopupView(mediaAll)
                            mItemSelectPopup.dismiss()
                        }
                    }
                }, {
                    //添加到列表
                    doItemAction {
                        toOtherActivity<PlayListActivity>(activity) {
                            putExtra(PlayListActivity.KEY_MEDIA_LIST,Gson().toJson(MediaDataBean(arrayListOf(it.id))))
                        }
                    }
                },
                        {
                            //打开方式
                            doItemAction {
                                FileUtil.toAppOpenFile(activity, File(it.path))
                            }
                        },
                        {
                            doItemAction {
                                //删除
                                mDeletePopup.apply {
                                    setContent(arrayListOf(ItemBean(title = it.name ?: "")))
                                    showPopupView(mediaAll)
                                    mItemSelectPopup.dismiss()
                                }
                            }

                        })
            }
            //重命名
            mRenamePopup.apply {
                doSure {
                    mItemValue?.let {
                        val name = getContent()
                        if (!TextUtils.isEmpty(name)) {
                            viewModel.reNameToMediaFile(name,it,it.type)
                        } else {
                            RxToast.normal("文件名不能为空！")
                        }
                    }
                }
            }

            //删除
                mDeletePopup.doSure {
                    mItemValue?.let {
                        viewModel.deleteMediaFile(Uri.parse("${it.uri}"))
                        viewModel.deleteFile(arrayListOf(it.path?:""))
                    }
            }

        }

    }

    private fun setMediaListData() {
        mMediaResource?.let {
            mMediaAllAdapter.setList(setPositionData(it))
        }
    }

    private fun doItemAction(block:(MediaInformation)->Unit){
        mItemValue?.let {
            block(it)
        }
    }

    private fun setPositionData(it:ValueMediaType)=
           when (viewModel.getCurrentPosition_()) {
                0 -> {
                    //所有
                    mAllMediaList.apply {
                        clear()
                        addAll(it.videoList)
                        addAll(it.audioList)
                    }
                }
                1 -> {
                  it.videoList
                }
                2 -> {
                  it.audioList
                }
               else->mAllMediaList
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
        mRenamePopup?.dismiss()
        mDeletePopup?.dismiss()
        mLoadingDialog.dismiss()
    }

}