package com.example.module_video.ui.activity


import android.net.Uri
import android.view.Gravity
import android.view.KeyEvent
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.provider.ModuleProvider
import com.example.module_base.utils.*
import com.example.module_video.R
import com.example.module_video.databinding.ActivityHomeBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.domain.MediaDataBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListMsgBean
import com.example.module_video.livedata.MediaLiveData
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.adapter.recycleview.BottomAdapter
import com.example.module_video.ui.fragment.FileListFragment
import com.example.module_video.ui.fragment.MediaFragment
import com.example.module_video.ui.fragment.SetFragment
import com.example.module_video.ui.widget.popup.ExitPoPupWindow
import com.example.module_video.ui.widget.popup.RemindPopup
import com.example.module_video.viewmodel.MediaViewModel
import com.google.gson.Gson

@Route(path = ModuleProvider.ROUTE_HOME_ACTIVITY)
class HomeActivity : BaseVmViewActivity<ActivityHomeBinding, MediaViewModel>() {

    private val mExitPoPupWindow by lazy { ExitPoPupWindow(this) }

    private val mBottomAnimationShow by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_bottom_show)
    }
    private val mBottomAnimationExit by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_bottom_exit)
    }

    private val mRemindHomePopup by lazy {
        RemindPopup(this)
    }

    private val mRemindListPopup by lazy {
        RemindPopup(this)
    }

    private val mMediaFragment by lazy { MediaFragment() }
    private val mListFragment by lazy { FileListFragment() }
    private val mSetFragment by lazy { SetFragment() }
    private val mBottomAdapter by lazy { BottomAdapter() }

    override fun getLayoutView(): Int = R.layout.activity_home
    override fun initView() {
        sp.putBoolean(Constants.IS_FIRST, false)
        binding.apply {
            data = viewModel
            showFragment(mMediaFragment)
            bottomNavigationView.apply {
                layoutManager = GridLayoutManager(this@HomeActivity, 3)
                mBottomAdapter.setList(DataProvider.homeBottomList)
                adapter = mBottomAdapter
            }
        }
        if (lacksPermissions()) {
            MediaLiveData.getMedia()
        }
    }

    private var hasData = false

    private var mFileListBean: MutableList<PlayListMsgBean> = ArrayList()
    private var mMediaList: MutableList<MediaInformation> = ArrayList()

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that = this@HomeActivity
                //媒体库编辑
                editAction.observe(that, {
                    bottomActionLayout.bottomInclude.startAnimation(if (it) mBottomAnimationShow else mBottomAnimationExit)
                })
                //播放列表编辑
                listEditAction.observe(that, {
                    listActionLayout.listIncludeActionLayout.startAnimation(if (it) mBottomAnimationShow else mBottomAnimationExit)
                })

                //媒体库选择的item
                selectItems.observe(that, {
                    mFileListBean = it
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

                //播放列表选择的item
                selectItemList.observe(that, {
                    hasData = it.size > 0
                    mMediaList.clear()
                    mMediaList.addAll(it)
                    bottomActionLayout.apply {
                        moveActionIcon.setImageResource(if (it.size > 0) R.mipmap.icon_edit_remove_select else R.mipmap.icon_edit_remove_normal)
                        deleteActionIcon.setImageResource(if (it.size > 0) R.mipmap.icon_edit_delete_select else R.mipmap.icon_edit_delete_normal)

                        moveActionTitle.setTextColor(
                            ContextCompat.getColor(
                                that,
                                if (it.size > 0) R.color.white else R.color.item_text
                            )
                        )
                        deleteActionTitle.setTextColor(
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
            //导航切换
            mBottomAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> showFragment(mMediaFragment)
                    1 -> showFragment(mListFragment)
                    2 -> showFragment(mSetFragment)
                }
                mBottomAdapter.setSelectPosition(position)
            }

            //媒体库删除、移动动作
            mRemindHomePopup.apply {
                bottomActionLayout.apply {
                    //移动
                    actionMove.setOnClickListener {
                        if (hasData) {
                            val itemList = ArrayList<Long>()
                            mMediaList.forEach {
                                itemList.add(it.id)
                            }
                            toOtherActivity<PlayListActivity>(activity) {
                                putExtra(
                                    PlayListActivity.KEY_MEDIA_LIST,
                                    Gson().toJson(MediaDataBean(itemList))
                                )
                            }
                            viewModel.setEditAction(false)
                        }
                    }
                    //删除
                    actionDelete.setOnClickListener {
                        if (hasData) {
                            val itemList = ArrayList<ItemBean>()
                            mMediaList.forEach {
                                itemList.add(ItemBean(title = it.name))
                            }
                            setContent(itemList)
                            showPopupView(homeFragment)
                        }
                    }
                }
                //确定
                doSure {
                    mMediaList?.let { it ->
                        it.forEach {
                            viewModel.deleteMediaFile(Uri.parse("${it.uri}"),it.path)
                        }
                        viewModel.deleteItemList(it)
                        viewModel.setEditAction(false)
                    }
                }

            }

            //播放列表删除动作
            mRemindListPopup?.apply {
                listActionLayout.listIncludeActionLayout.setOnClickListener {
                    if (hasData) {
                        val itemList = ArrayList<ItemBean>()
                        mFileListBean.forEach {
                            itemList.add(ItemBean(title = it.name))
                        }
                        setContent(itemList)
                        showPopupView(homeFragment)
                    }
                }
                doSure {
                    mFileListBean?.let { it ->
                        val pathList = ArrayList<String>()
                        it.forEach {
                            pathList.add(it.name)
                        }
                        viewModel.deletePlayList(pathList)
                        viewModel.setListEditAction(false)
                    }

                }
            }
        }
    }


    private var oldFragment: Fragment? = null
    private fun showFragment(fragment: Fragment) {
        if (oldFragment === fragment) {
            return
        }
        supportFragmentManager.beginTransaction().apply {
            if (fragment.isAdded) show(fragment) else add(R.id.homeFragment, fragment)

            oldFragment?.let {
                hide(it)
            }

            oldFragment = fragment
            commitAllowingStateLoss()
        }
    }

    override fun getViewModelClass(): Class<MediaViewModel> {
        return MediaViewModel::class.java
    }


    override fun release() {
        mRemindListPopup.dismiss()
        mRemindHomePopup.dismiss()
        mLoadingDialog.dismiss()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            viewModel.apply {
                if (getEditAction_() || getListEditAction_()) {
                    setEditAction(false)
                    setListEditAction(false)
                    return true
                } else {
                    mExitPoPupWindow.showPopupView(binding.homeFragment, Gravity.BOTTOM)
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onResume() {
        super.onResume()
        when (intent.getIntExtra(ModuleProvider.FRAGMENT_ID, 5)) {
            3 -> showFragment(mSetFragment)
        }
    }
}