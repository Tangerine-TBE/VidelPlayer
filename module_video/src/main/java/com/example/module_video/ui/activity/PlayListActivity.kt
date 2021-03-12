package com.example.module_video.ui.activity

import android.text.TextUtils
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.gsonHelper
import com.example.module_base.utils.setStatusBar
import com.example.module_base.widget.MyToolbar
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayListBinding
import com.example.module_video.domain.MediaDataBean
import com.example.module_video.domain.PlayListMsgBean
import com.example.module_video.livedata.PlayListLiveData
import com.example.module_video.ui.adapter.recycleview.FileListAdapter
import com.example.module_video.ui.widget.popup.InputPopup
import com.example.module_video.viewmode.MediaViewModel
import com.example.module_video.viewmode.SelectFileViewModel
import com.google.gson.Gson
import com.tamsiree.rxkit.RxKeyboardTool
import com.tamsiree.rxkit.view.RxToast

class PlayListActivity : BaseVmViewActivity<ActivityPlayListBinding, SelectFileViewModel>() {

    private val mFileListAdapter by lazy {
        FileListAdapter()
    }

    private val mCreateListPopup by lazy {
        InputPopup(this).apply {
            setHint("播放列表名")
            setTitle("新建播放列表")
        }
    }

    companion object{
        const val KEY_MEDIA_LIST="KEY_MEDIA_LIST"
    }

    override fun getLayoutView(): Int = R.layout.activity_play_list

    override fun getViewModelClass(): Class<SelectFileViewModel> {
        return SelectFileViewModel::class.java
    }

    override fun initView() {
        PlayListLiveData.getPlayList()
        binding.apply {
            setStatusBar(this@PlayListActivity, playListBar, LayoutType.CONSTRAINTLAYOUT)

            listContainer.apply {
                layoutManager= LinearLayoutManager(this@PlayListActivity)
                adapter=mFileListAdapter
            }
        }

    }

    private var mPlayList:MutableList<PlayListMsgBean> = ArrayList()

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                PlayListLiveData.observe(this@PlayListActivity,{
                    mPlayList=it
                    mFileListAdapter.setList(it)
                })

                searchPlayList.observe(this@PlayListActivity,{
                    mFileListAdapter.setList(it)
                })

                addListState.observe(this@PlayListActivity,{
                   finish()
                })
            }

        }
    }

    override fun initEvent() {
        binding.apply {
            playListBar.setOnBackClickListener(object : MyToolbar.OnBackClickListener {
                override fun onBack() {
                    finish()
                }

                override fun onRightTo() {

                }

                override fun onRightTwoTo() {
                    mCreateListPopup.showPopupView(listContainer)
                }

            })

            searchInput.doOnTextChanged { text, start, before, count ->
                if (text?.length ?: 0 > 0) {
                    visibleView(searchDelete)
                    viewModel.queryPlayList(text.toString().trim())
                } else {
                    goneView(searchDelete)
                    mFileListAdapter.setList(mPlayList)
                }
            }

            //删除
            searchDelete.setOnClickListener {
                searchInput.setText("")
                RxKeyboardTool.hideSoftInput(searchInput)
                mFileListAdapter.setList(mPlayList)
            }



            //新建弹窗
            mCreateListPopup.apply {
                doSure {
                    val name = getContent()
                    if (!TextUtils.isEmpty(name)) {
                        viewModel.addNewPlayList(PlayListMsgBean(name, Gson().toJson(MediaDataBean(null)),System.currentTimeMillis()))
                    } else {
                        RxToast.normal("文件名不能为空！")
                    }
                }
            }

            mFileListAdapter.setOnItemClickListener(object :FileListAdapter.OnItemClickListener{
                override fun onItemClick(item: PlayListMsgBean, position: Int) {
                    val mediaData = intent.getStringExtra(KEY_MEDIA_LIST)
                    gsonHelper<MediaDataBean>(mediaData)?.let {
                        it.idList?.apply {
                            viewModel.addVideoList(item.name,this)
                            RxKeyboardTool.hideSoftInput(binding.searchInput)
                        }
                    }
                }

                override fun onItemSubClick(item: PlayListMsgBean, position: Int) {

                }
            })

        }
    }

    override fun release() {
        mCreateListPopup.dismiss()
    }

}