package com.example.module_video.ui.activity

import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.setStatusBar
import com.example.module_base.utils.toolbarEvent
import com.example.module_base.widget.MyToolbar
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayListBinding
import com.example.module_video.domain.FileBean
import com.example.module_video.ui.adapter.recycleview.FileListAdapter
import com.example.module_video.ui.widget.popup.InputPopup
import com.example.module_video.viewmode.MediaViewModel
import com.example.module_video.viewmode.PlayListViewModel
import com.example.module_video.viewmode.PlayVideoViewModel
import com.tamsiree.rxkit.view.RxToast

class PlayListActivity : BaseVmViewActivity<ActivityPlayListBinding, MediaViewModel>() {

    private val mFileListAdapter by lazy {
        FileListAdapter()
    }

    private val mCreateListPopup by lazy {
        InputPopup(this).apply {
            setHint("播放列表名")
            setTitle("新建播放列表")
        }
    }

    override fun getLayoutView(): Int = R.layout.activity_play_list

    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }

    override fun initView() {
        viewModel.getFolderList()
        binding.apply {
            setStatusBar(this@PlayListActivity, playListBar, LayoutType.CONSTRAINTLAYOUT)

            listContainer.apply {
                layoutManager= LinearLayoutManager(this@PlayListActivity)
                adapter=mFileListAdapter
            }
        }

    }


    override fun observerData() {
        binding.apply {
            viewModel.apply {
                fileList.observe(this@PlayListActivity,{
                    mFileListAdapter.setList(it)
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

            //新建弹窗
            mCreateListPopup.apply {
                doSure {
                    val name = getContent()
                    if (!TextUtils.isEmpty(name)) {
                        viewModel.createFileFolder(name)
                    } else {
                        RxToast.normal("文件名不能为空！")
                    }
                }
            }

            mFileListAdapter.setOnItemClickListener(object :FileListAdapter.OnItemClickListener{
                override fun onItemClick(item: FileBean, position: Int) {

                }

                override fun onItemSubClick(item: FileBean, position: Int) {

                }
            })

        }
    }

    override fun release() {
        mCreateListPopup.dismiss()
    }

}