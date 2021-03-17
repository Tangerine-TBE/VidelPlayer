package com.example.module_video.ui.activity

import android.app.Activity
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayListMsgBinding
import com.example.module_video.domain.*
import com.example.module_video.livedata.MediaLiveData
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.adapter.recycleview.MediaFileAdapter
import com.example.module_video.ui.widget.popup.ItemSelectPopup
import com.example.module_video.ui.widget.popup.RemindPopup
import com.example.module_video.utils.Constants
import com.example.module_video.utils.FileUtil
import com.example.module_video.viewmode.PlayListMsgViewModel
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class PlayListMsgActivity : BaseVmViewActivity<ActivityPlayListMsgBinding, PlayListMsgViewModel>() {


    private val mBottomAnimationShow by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_bottom_show)
    }
    private val mBottomAnimationExit by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_bottom_exit)
    }

    private val mMediaFileAdapter by lazy {
        MediaFileAdapter()
    }

    private val mRemindListPopup by lazy {
        RemindPopup(this)
    }
    private val mItemSelectPopup by lazy {
        ItemSelectPopup(this)
    }

    private val mDeletePopup by lazy {
        RemindPopup(this)
    }
    private var model = 0
    private var name = ""
    private var isAdd=false
    private var hasData = false
    private var mValueMediaType: ValueMediaType? = null
    private var mMediaInformation: MediaInformation? = null
    private var mMediaList: MutableList<MediaInformation> = ArrayList()

    companion object {
        fun toAddPlayActivity(activity: Activity, name: String) {
            toOtherActivity<SelectFileActivity>(activity) {
                putExtra(Constants.KEY_NAME, name)
            }
        }
    }

    override fun getViewModelClass(): Class<PlayListMsgViewModel> {
        return PlayListMsgViewModel::class.java
    }

    override fun getLayoutView(): Int = R.layout.activity_play_list_msg

    override fun initView() {
        binding.apply {
            data = viewModel
            setStatusBar(this@PlayListMsgActivity, playListBar, LayoutType.CONSTRAINTLAYOUT)
            model = intent.getIntExtra(Constants.KEY_MODE, 0)
            name = intent.getStringExtra(Constants.KEY_NAME) ?: ""

            if (!TextUtils.isEmpty(name)) {
                binding.playListTitle.text = "$name"
                viewModel.getPlayListMsg(name)
            }


            if (model == 1) {
                isAdd=true
                toAddPlayActivity(this@PlayListMsgActivity, name) }

            playListContainer.apply {
                layoutManager = LinearLayoutManager(this@PlayListMsgActivity)
                adapter = mMediaFileAdapter
            }
        }

    }


    override fun onResume() {
        super.onResume()
        if (isAdd) {
            viewModel.getPlayListMsg(name)
        }
    }



    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that = this@PlayListMsgActivity
                playListMsg.observe(that, { it ->
                    val list = ArrayList<MediaInformation>()
                    gsonHelper<MediaDataBean>(it.media)?.let { data ->
                        mValueMediaType?.let {
                            data.idList?.forEach { id ->
                                it.videoList.forEach {
                                    if (it.id == id) list.add(it)
                                }
                                it.audioList.forEach {
                                    if (it.id == id) list.add(it)
                                }
                            }
                        }
                        list.reverse()
                        mMediaFileAdapter.setList(list)

                    }
                })
                //全部媒体数据
                MediaLiveData.observe(that, {
                    mValueMediaType = it
                })

                //编辑状态
                editAction.observe(that, {
                    isAdd=!it
                    mMediaFileAdapter.setEditAction(it)
                    listActionLayout.listIncludeActionLayout.startAnimation(if (it) mBottomAnimationShow else mBottomAnimationExit)
                    listIcon.setImageResource(if (it) R.mipmap.icon_normal else R.mipmap.icon_list_play)
                })

                //全选状态
                selectAllState.observe(that, {
                    if (getEditAction_()) {
                        listIcon.setImageResource(if (it) R.mipmap.icon_select1 else R.mipmap.icon_normal)
                        viewModel.setSelectItems(mMediaFileAdapter.getSelectList())
                    }


                })


                //选择的item
                selectItems.observe(that, { it ->
                    mMediaList.clear()
                    mMediaList.addAll(it)
                    hasData = it.size > 0
                    listActionLayout.apply {
                        deleteListActionIcon.setImageResource(if (it.size > 0) R.mipmap.icon_edit_delete_select else R.mipmap.icon_edit_delete_normal)
                        deleteListActionTitle.setTextColor(
                                ContextCompat.getColor(
                                        that,
                                        if (it.size > 0) R.color.white else R.color.item_text
                                )
                        )
                    }
                })
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            //全选 播放动作
            listIcon.setOnClickListener {
                if (viewModel.getEditAction_()) {
                    mMediaFileAdapter.apply {
                                if (getSelectState()) {
                                    clearAllItems()
                                } else {
                                    selectAllItems()
                                }
                            viewModel.setSelectAllState(getSelectState())
                    }
                } else {
                    PlayVideoActivity.toPlayVideo(
                        this@PlayListMsgActivity,
                        it,
                        Gson().toJson(PlayListBean(mMediaFileAdapter.getData())),0
                    )
                }
            }
            //返回
            playListBack.setOnClickListener {
                if (viewModel.getEditAction_()) {
                    viewModel.setEditAction(false)
                }else{
                    finish()
                }

            }
           //添加
            playListAdd.setOnClickListener {
                isAdd=true
                toAddPlayActivity(this@PlayListMsgActivity, name)
            }
           //编辑
            playListEdit.setOnClickListener {
                viewModel.setEditAction(!viewModel.getEditAction_())
                viewModel.setSelectItems(mMediaFileAdapter.getSelectList())
            }
            //适配器点击
            mMediaFileAdapter.apply {
                setOnItemClickListener(object : MediaFileAdapter.OnItemClickListener {
                    override fun onItemClick(item: MediaInformation, position: Int, view: View) {
                        if (viewModel.getEditAction_()) {
                            viewModel.setSelectAllState(getSelectState())

                        } else {
                            PlayVideoActivity.toPlayVideo(
                                    this@PlayListMsgActivity,
                                    view,
                                    Gson().toJson(PlayListBean(mMediaFileAdapter.getData())),position
                            )
                        }
                    }

                    override fun onItemSubClick(item: MediaInformation, position: Int) {
                        mItemSelectPopup?.apply {
                            mMediaInformation = item
                            setTitleNormal(item.name, DataProvider.listMsgPopup)
                            showPopupView(playListContainer)
                        }
                    }
                })
            }
            //选择弹窗
            mItemSelectPopup.setItemAction({
                doPopupAction {
                    FileUtil.toAppOpenFile(this@PlayListMsgActivity, File(it.path))
                }
            }, {
                doPopupAction {
                    mDeletePopup.apply {
                        setContent(arrayListOf(ItemBean(title = it.name)))
                        showPopupView(playListContainer)
                        mItemSelectPopup.dismiss()
                    }

                }
            })
            //弹窗删除
            mDeletePopup.doSure {
                doPopupAction {
                    viewModel.deleteListMsg(name, arrayListOf(it.id))
                }

            }
            //多选删除
            mRemindListPopup?.apply {
                listActionLayout.listIncludeActionLayout.setOnClickListener {
                    if (hasData) {
                        val itemList = ArrayList<ItemBean>()
                        mMediaList.forEach {
                            itemList.add(ItemBean(title = it.name))
                        }
                        setContent(itemList)
                        showPopupView(playListContainer)
                    }
                }

                doSure {
                    mMediaList?.let { it ->
                        val pathList = ArrayList<Long>()
                        it.forEach {
                            pathList.add(it.id)
                        }
                        viewModel.deleteListMsg(name, pathList)
                        viewModel.setEditAction(false)
                    }
                }
            }

        }
    }


    private fun doPopupAction(block: (MediaInformation) -> Unit) {
        mMediaInformation?.let {
            block(it)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (viewModel.getEditAction_()) {
                viewModel.setEditAction(false)
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun release() {
        mRemindListPopup?.dismiss()
        mItemSelectPopup?.dismiss()
        mDeletePopup?.dismiss()
    }
}