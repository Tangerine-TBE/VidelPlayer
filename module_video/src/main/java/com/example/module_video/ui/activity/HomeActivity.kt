package com.example.module_video.ui.activity


import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LogUtils
import com.example.module_video.R
import com.example.module_video.databinding.ActivityHomeBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.adapter.recycleview.BottomAdapter
import com.example.module_video.ui.fragment.FileListFragment
import com.example.module_video.ui.fragment.MediaFragment
import com.example.module_video.ui.fragment.SetFragment
import com.example.module_video.viewmode.MediaViewModel


class HomeActivity : BaseVmViewActivity<ActivityHomeBinding, MediaViewModel>() {

    private val mBottomAnimationShow by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_bottom_show)
    }
    private val mBottomAnimationExit by lazy {
        AnimationUtils.loadAnimation(this, R.anim.anim_bottom_exit)
    }


    private val mMediaFragment by lazy { MediaFragment() }
    private val mListFragment by lazy { FileListFragment() }
    private val mSetFragment by lazy { SetFragment() }
    private val mBottomAdapter by lazy { BottomAdapter() }

    override fun getLayoutView(): Int = R.layout.activity_home
    override fun initView() {
        binding.apply {
            data = viewModel
            showFragment(mMediaFragment)

            bottomNavigationView.apply {
                layoutManager = GridLayoutManager(this@HomeActivity, 3)
                mBottomAdapter.setList(DataProvider.homeBottomList)
                adapter = mBottomAdapter
            }
        }
    }

    private var hasData=false

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that = this@HomeActivity
                editAction.observe(that, {

                    bottomActionLayout.bottomInclude.startAnimation(if (it) mBottomAnimationShow else mBottomAnimationExit)
                })


                selectItemList.observe(that, {
                    hasData=it.size>0
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
            mBottomAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> showFragment(mMediaFragment)
                    1 -> showFragment(mListFragment)
                    2 -> showFragment(mSetFragment)
                }
                mBottomAdapter.setSelectPosition(position)
            }

            bottomActionLayout.apply {

                    actionMove.setOnClickListener {
                        if (hasData) {
                            LogUtils.i("------bottomActionLayout----------------move")
                        }
                    }


                    actionDelete.setOnClickListener {
                        if (hasData) {
                            LogUtils.i("--------bottomActionLayout--------------delete")
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


}