package com.example.module_video.ui.activity

import android.text.TextUtils
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.module_video.R
import com.example.module_video.databinding.ActivitySelectMediaBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.livedata.MediaLiveData
import com.example.module_video.ui.adapter.recycleview.MediaFileAdapter
import com.example.module_video.utils.Constants
import com.example.module_video.viewmodel.SelectFileViewModel
import com.tamsiree.rxkit.RxKeyboardTool
import com.tamsiree.rxkit.view.RxToast

class SelectFileActivity : BaseVmViewActivity<ActivitySelectMediaBinding, SelectFileViewModel>() {

    private val mMediaFileAdapter by lazy {
        MediaFileAdapter()
    }

    override fun getViewModelClass(): Class<SelectFileViewModel> {
        return SelectFileViewModel::class.java
    }

    override fun getLayoutView(): Int=R.layout.activity_select_media

    private var playListName=""

    private val mAllMediaList = ArrayList<MediaInformation>()

    override fun initView() {
        binding.apply {
            data=viewModel
            setStatusBar(this@SelectFileActivity, selectBar, LayoutType.CONSTRAINTLAYOUT)
            playListName=intent.getStringExtra(Constants.KEY_NAME)?:""
            ItemContainer.apply {
                layoutManager = LinearLayoutManager(this@SelectFileActivity)
                adapter = mMediaFileAdapter
            }
        }
    }


    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that=this@SelectFileActivity
                MediaLiveData.observe(that,{
                    mAllMediaList.clear()
                    mAllMediaList.addAll(it.videoList)
                    mAllMediaList.addAll(it.audioList)
                    mMediaFileAdapter.setList(mAllMediaList)
                    mMediaFileAdapter.setEditAction(true)
                })

                searchMediaList.observe(that,{
                    mMediaFileAdapter.setList(it)
                })

                addListState.observe(that, {
                    if (it) finish() else RxToast.normal("添加失败！")
                })
            }

        }
    }

    override fun initEvent() {
        binding.apply {
            searchInput.doOnTextChanged { text, start, before, count ->
                if (text?.length ?: 0 > 0) {
                    visibleView(searchDelete)
                    mAllMediaList.let {
                        viewModel.getSearchList(text.toString().trim(),it)
                    }
                } else {
                    goneView(searchDelete)
                    mMediaFileAdapter.setList(mAllMediaList)
                }
            }

            //删除
            searchDelete.setOnClickListener {
                searchInput.setText("")
                RxKeyboardTool.hideSoftInput(searchInput)
                mMediaFileAdapter.setList(mAllMediaList)
            }

            selectBack.setOnClickListener {
                finish()
            }

            mMediaFileAdapter.setOnItemClickListener(object :MediaFileAdapter.OnItemClickListener{
                override fun onItemClick(item: MediaInformation, position: Int, view: View) {
                    viewModel.setSelectVideoList(mMediaFileAdapter.getSelectList())
                }

                override fun onItemSubClick(item: MediaInformation, position: Int) {

                }

            })

            selectRightTitle.setOnClickListener {
                val selectList = mMediaFileAdapter.getSelectList()
                if (selectList.size > 0) {
                    if (!TextUtils.isEmpty(playListName)) {
                        val idList = ArrayList<Long>()
                        selectList.forEach {
                            idList.add(it.id)
                        }
                        viewModel.addVideoList(playListName, idList)
                    }
                } else {
                    finish()
                }
            }
        }
    }
}