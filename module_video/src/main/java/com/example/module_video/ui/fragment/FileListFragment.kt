package com.example.module_video.ui.fragment

import android.text.TextUtils
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.LayoutType
import com.example.module_base.utils.setStatusBar
import com.example.module_video.R
import com.example.module_video.databinding.FragmentListBinding
import com.example.module_video.domain.FileBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.ui.adapter.recycleview.FileListAdapter
import com.example.module_video.ui.adapter.recycleview.MediaFileAdapter
import com.example.module_video.ui.widget.popup.InputPopup
import com.example.module_video.ui.widget.popup.ItemSelectPopup
import com.example.module_video.ui.widget.popup.RemindPopup
import com.example.module_video.utils.FileUtil
import com.example.module_video.viewmode.ListViewModel
import com.example.module_video.viewmode.MediaViewModel
import com.tamsiree.rxkit.view.RxToast

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:54:55
 * @class describe
 */
class FileListFragment  : BaseVmFragment<FragmentListBinding, MediaViewModel>() {

    private val mFileListAdapter by lazy {
        FileListAdapter()
    }
    private val mCreateListPopup by lazy {
        InputPopup(activity).apply {
            setHint("播放列表名")
            setTitle("新建播放列表")
        }
    }

    private val mItemSelectPopup by lazy {
        ItemSelectPopup(activity)
    }


    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_list

    override fun initView() {
        viewModel.getFileList()
        binding.apply {
            data=viewModel
            setStatusBar(context, listBar, LayoutType.LINEARLAYOUT)

            fileListContainer.apply {
                layoutManager=LinearLayoutManager(activity)
                adapter=mFileListAdapter
            }


        }
    }

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that=this@FileListFragment
                fileList.observe(that,{
                    mFileListAdapter.setList(it)
                })


                createFileSate.observe(that,{
                    if (!it){
                        RxToast.normal("文件已存在！")
                    }
                })

                listEditAction.observe(that,{
                    mFileListAdapter.setEditAction(it)
                    viewModel.setSelectItems(mFileListAdapter.getSelectList())
                })

            }

        }
    }

    override fun initEvent() {
        binding.apply {
            //编辑
            editListAction.setOnClickListener {
                viewModel.setListEditAction(!viewModel.getListEditAction_())
            }

            fileAdd.setOnClickListener {
                mCreateListPopup.showPopupView(fileListContainer)
            }

            mCreateListPopup?.apply {
                setOnActionClickListener(object : BasePopup.OnActionClickListener {
                    override fun sure() {
                        val name = getContent()
                        if (!TextUtils.isEmpty(name)) {
                            viewModel.createFile(name)
                        } else {
                            RxToast.normal("文件名不能为空！")
                        }
                    }
                    override fun cancel() {

                    }

                })
            }

            mFileListAdapter.setOnItemClickListener(object : FileListAdapter.OnItemClickListener {
                override fun onItemClick(item: FileBean, position: Int) {
                    if (viewModel.getListEditAction_()) {
                        viewModel.setSelectItems(mFileListAdapter.getSelectList())
                    } else {


                    }
                }

                override fun onItemSubClick(item: FileBean, position: Int) {
                    mItemSelectPopup?.apply {
                        setTitleNormal(item.name)
                        showPopupView(fileListContainer)
                    }
                }
            })

        }




    }




    override fun release() {
        mCreateListPopup.dismiss()
    }
}