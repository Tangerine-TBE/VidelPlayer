package com.example.module_video.ui.fragment

import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_ad.ad.ad_help.AdController
import com.example.module_ad.advertisement.AdType
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.module_user.livedata.RemoveAdLiveData
import com.example.module_video.R
import com.example.module_video.databinding.FragmentListBinding
import com.example.module_video.domain.*
import com.example.module_video.livedata.PlayListLiveData
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.activity.PlayListMsgActivity
import com.example.module_video.ui.adapter.recycleview.FileListAdapter
import com.example.module_video.ui.widget.popup.InputPopup
import com.example.module_video.ui.widget.popup.ItemSelectPopup
import com.example.module_video.ui.widget.popup.RemindPopup
import com.example.module_video.utils.Constants
import com.example.module_video.viewmodel.MediaViewModel
import com.google.gson.Gson
import com.tamsiree.rxkit.view.RxToast
import java.util.*

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
    private val mRenamePopup by lazy {
        InputPopup(activity).apply {
            setTitle("重命名")
        }
    }

    private val mDeletePopup by lazy {
        RemindPopup(activity)
    }

    private val mItemSelectPopup by lazy {
        ItemSelectPopup(activity)
    }

    private val mAdController by lazy {
        AdController.Builder(requireActivity())
                .setPage(AdType.HOME_PAGE)
                .setContainer(hashMapOf(AdController.ContainerType.TYPE_FEED to  binding.mAdContainer))
                .create()
    }



    companion object{
        const val MODEL_CHECK=0
        const val MODEL_NEW=1
    }

    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_list

    override fun initView() {
        mAdController.show()
        binding.apply {
            data=viewModel
            setStatusBar(context, listBar, LayoutType.CONSTRAINTLAYOUT)

            fileListContainer.apply {
                layoutManager=LinearLayoutManager(activity)
                adapter=mFileListAdapter
            }


        }
    }

    override fun onResume() {
        super.onResume()
        PlayListLiveData.getPlayList()
    }

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that=this@FileListFragment
                PlayListLiveData.observe(that, {
                    mFileListAdapter.setList(it)
                    noListHint.visibility=if (it.size>0) View.GONE else View.VISIBLE
                })


                listEditAction.observe(that,{
                    mFileListAdapter.setEditAction(it)
                    viewModel.setSelectItems(mFileListAdapter.getSelectList())
                })


                RemoveAdLiveData.observe(that,{
                    if (it) goneView(mAdContainer) else showView(mAdContainer)
                })

            }

        }
    }

    private var mFileBean:PlayListMsgBean?=null
    override fun initEvent() {
        binding.apply {
            //编辑
            editListAction.setOnClickListener {
                viewModel.setListEditAction(!viewModel.getListEditAction_())
            }

            //新建
            fileAdd.setOnClickListener {
                mCreateListPopup.showPopupView(fileListContainer)
            }

            //新建列表弹窗
            mCreateListPopup.apply {
                doSure {
                    val name = getContent()
                    if (!TextUtils.isEmpty(name)) {
                        toOtherActivity<PlayListMsgActivity>(activity){
                            putExtra(Constants.KEY_MODE, MODEL_NEW)
                            putExtra(Constants.KEY_NAME, name)
                        }
                        viewModel.addNewPlayList(PlayListMsgBean(name,Gson().toJson(MediaDataBean(null)),System.currentTimeMillis()))
                    } else {
                        RxToast.normal("文件名不能为空！")
                    }
                }
            }
            //列表监听
            mFileListAdapter.setOnItemClickListener(object : FileListAdapter.OnItemClickListener {
                override fun onItemClick(item: PlayListMsgBean, position: Int) {
                    if (viewModel.getListEditAction_()) {
                        viewModel.setSelectItems(mFileListAdapter.getSelectList())
                    } else {
                        toOtherActivity<PlayListMsgActivity>(activity){
                            putExtra(Constants.KEY_NAME,item.name )
                            putExtra(Constants.KEY_MODE, MODEL_CHECK)
                        }
                    }
                }

                override fun onItemSubClick(item: PlayListMsgBean, position: Int) {
                    mItemSelectPopup?.apply {
                        mFileBean=item
                        setTitleNormal(item.name,DataProvider.listPopup)
                        showPopupView(fileListContainer)
                    }
                }
            })

            //动作选择弹窗
            mItemSelectPopup.setItemAction({
                mFileBean?.let {
                    //重命名
                    mRenamePopup.apply {
                        setHint(it.name)
                        showPopupView(fileListContainer)
                        mItemSelectPopup.dismiss()
                    }
                }
            },{
                mFileBean?.let {
                    //删除
                    mDeletePopup.apply {
                        setContent(arrayListOf(ItemBean(title = it.name)))
                        showPopupView(fileListContainer)
                        mItemSelectPopup.dismiss()
                    }
                }
            })

            //重命名动作
            mRenamePopup?.apply {
                doSure {
                    mFileBean?.let {
                        val name = getContent()
                        if (!TextUtils.isEmpty(name)) {
                           viewModel.reNamePlayList(it.name,name)
                        } else {
                            RxToast.normal("文件名不能为空！")
                        }
                    }
                }
            }
            //删除动作
            mDeletePopup.doSure {
                mFileBean?.let {
                    viewModel.deletePlayList(arrayListOf(it.name))
                }
            }
        }
    }




    override fun release() {
        mItemSelectPopup.dismiss()
        mCreateListPopup.dismiss()
        mRenamePopup.dismiss()
        mDeletePopup.dismiss()
        mAdController.release()
    }
}