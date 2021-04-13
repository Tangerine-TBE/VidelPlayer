package com.example.module_video.ui.fragment

import android.net.Uri
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_ad.ad.ad_help.AdController
import com.example.module_ad.advertisement.AdType
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.module_user.livedata.RemoveAdLiveData
import com.example.module_video.R
import com.example.module_video.databinding.FragmentMediaBinding
import com.example.module_video.domain.*
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
import com.example.module_video.utils.Constants
import com.example.module_video.utils.FileUtil
import com.example.module_video.viewmodel.MediaViewModel
import com.google.gson.Gson
import com.scwang.smart.refresh.header.MaterialHeader
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


    private val mMediaAdapter by lazy {
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

    private val mAdController by lazy {
        AdController.Builder(requireActivity())
            .setPage(AdType.HOME_PAGE)
            .setContainer(hashMapOf(AdController.ContainerType.TYPE_FEED to  binding.mAdContainer))
            .create()
    }


    private var mFinishAction=false

    override fun initView() {
        binding.apply {
            data = viewModel
            //设置滑动栏
            initIndicator()
            //设置不同类型列表
            initRecycleViewType()

            mAdController.show()
        }
    }


    private var mItemValue: MediaInformation? = null
    private val mAllMediaList = ArrayList<MediaInformation>()
    private var updatePosition=-1
    private var videoSize=0
    private var audioSize=0

    /**
     * 数据观察
     */
    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that = this@MediaFragment
                //音视频资源
                MediaLiveData.observe(that, {
                    mSmartRefreshLayout.finishRefresh()

                    mFinishAction=it.finish

                    videoSize=it.videoList.size
                    audioSize=it.audioList.size

                    mAllMediaList.apply {
                        clear()
                        addAll(it.videoList)
                        addAll(it.audioList)
                    }

                    mMediaAdapter.setGo(it.finish)
                    mMediaAdapter.setList(setPositionData(mAllMediaList))

                })
                //编辑动作
                editAction.observe(that, {
                    mSmartRefreshLayout.setEnableRefresh(!it)
                    mMediaAdapter.setEditAction(it)
                    viewModel.setSelectItemList(mMediaAdapter.getSelectList())
                })
                //当前位置
                currentPosition.observe(that,{
                    setMediaListData()
                })
                //搜索数据
                searchMediaList.observe(that,{
                    mMediaAdapter.setList(it)
                })
                //删除数据
                deleteMediaList.observe(that,{
                    mAllMediaList.removeAll(it)
                    mMediaAdapter.setList(setPositionData(mAllMediaList))
                })
                //重命名状态
                renameState.observe(that, {
                    if (it.state) {
                        it.msg.apply {
                            name=it.name
                            mAllMediaList[it.position] = this
                            mMediaAdapter.setList(setPositionData(mAllMediaList))
                        }
                        LogUtils.i("---------renameState--------------$it----------------")
                    }
                })

                RemoveAdLiveData.observe(that,{
                    if (it) goneView(mAdContainer) else showView(mAdContainer)
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
                    if (mFinishAction) {
                        viewModel.getSearchList(
                            text.toString().trim(),
                            setPositionData(mAllMediaList)
                        )
                    } else {
                        showToast("未完成资源搜索")
                    }
                } else {
                    goneView(searchDelete)
                    setMediaListData()
                }
            }

            searchInput.setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN) {
                    //先隐藏键盘
                    RxKeyboardTool.hideSoftInput(searchInput)

                }
                //记得返回false
                false
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
                if (mFinishAction) {
                    viewModel.setEditAction(!viewModel.getEditAction_())
                } else {
                    showToast("未完成资源搜索")
                }

            }

            //搜索
            searchAction.setOnClickListener {
                searchInput.requestFocus()
                RxKeyboardTool.showSoftInput(requireContext(), searchInput)
                viewModel.setSearchAction(!viewModel.getSearchAction_())
            }


            mMediaAdapter.setOnItemClickListener(object : MediaFileAdapter.OnItemClickListener {
                override fun onItemClick(item: MediaInformation, position: Int,itemView: View) {
                    if (viewModel.getEditAction_()) {
                        viewModel.setSelectItemList(mMediaAdapter.getSelectList())
                    } else {
                        PlayVideoActivity.toPlayVideo(activity,Gson().toJson(PlayListBean(mMediaAdapter.getData())),position)
                    }
                }

                override fun onItemSubClick(item: MediaInformation, position: Int) {
                    mItemSelectPopup?.apply {
                        updatePosition=position
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
                                    setContent(arrayListOf(ItemBean(title = it.name)))
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
                            viewModel.reNameToMediaFile(name, it,updatePosition)
                        } else {
                            RxToast.normal("文件名不能为空！")
                        }
                    }
                }
            }

            //删除
                mDeletePopup.doSure {
                    mItemValue?.let {
                        mAllMediaList.remove(it)
                        mMediaAdapter.notifyItemRemoved(updatePosition)
                        viewModel.deleteMediaFile(Uri.parse("${it.uri}"),it.path)

                    }
            }

            //下拉刷新
            mSmartRefreshLayout.setOnRefreshListener {
                if (mFinishAction) {
                    MediaLiveData.getMedia()
                } else {
                    it.finishRefresh()
                }
            }
        }

    }




    private fun setMediaListData() {
        mMediaAdapter.setList(setPositionData(mAllMediaList))
    }

    private fun doItemAction(block:(MediaInformation)->Unit){
        mItemValue?.let {
            block(it)
        }
    }

    private fun setPositionData(it:MutableList<MediaInformation>)=
           when (viewModel.getCurrentPosition_()) {
                0 -> {
                    it
                }
                1 -> {
                    if (it.size >=videoSize) it.subList(0,videoSize) else it
                }
               2 -> {
                   if (it.size >=videoSize) it.subList(videoSize, it.size) else it
               }
               else->it
            }

    private fun FragmentMediaBinding.initRecycleViewType() {
        mediaAll.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mMediaAdapter
        }
        //设置下拉刷新的头
        mSmartRefreshLayout.setRefreshHeader(MaterialHeader(activity))
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
        mAdController.release()
    }

}