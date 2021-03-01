package com.example.module_video.ui.activity


import androidx.fragment.app.Fragment
import com.example.module_base.base.BaseViewActivity
import com.example.module_video.R
import com.example.module_video.databinding.ActivityHomeBinding
import com.example.module_video.ui.fragment.FileListFragment
import com.example.module_video.ui.fragment.MediaFragment
import com.example.module_video.ui.fragment.SetFragment


class HomeActivity : BaseViewActivity<ActivityHomeBinding>() {

    private val mMediaFragment by lazy {  MediaFragment()}
    private val mListFragment by lazy {  FileListFragment() }
    private val mSetFragment by lazy {  SetFragment()}
    override fun getLayoutView(): Int = R.layout.activity_home
    override fun initView() {
        binding.apply {
            showFragment(mMediaFragment)
        }
    }


    override fun initEvent() {
        binding.apply {
            bottomNavigationView.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.home_media->showFragment(mMediaFragment)
                    R.id.home_list->showFragment(mListFragment)
                    R.id.home_set->showFragment(mSetFragment)
                }
                return@setOnNavigationItemSelectedListener true
            }
        }
    }

    private var oldFragment: Fragment?=null
    private fun showFragment(fragment: Fragment){
        if (oldFragment === fragment) {
            return
        }
        supportFragmentManager.beginTransaction().apply {
            if (fragment.isAdded) show(fragment) else add(R.id.homeFragment,fragment)

            oldFragment?.let {
                hide(it)
            }

            oldFragment=fragment
            commitAllowingStateLoss()
        }
    }
}