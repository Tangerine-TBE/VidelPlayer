package com.example.module_video.ui.activity

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import android.view.KeyEvent
import androidx.recyclerview.widget.GridLayoutManager
import com.example.module_ad.utils.Contents
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.MyActivityManager
import com.example.module_base.utils.toOtherActivity
import com.example.module_base.utils.toolbarEvent
import com.example.module_video.R
import com.example.module_video.databinding.ActivityLockBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.ui.adapter.recycleview.InputPwdAdapter
import com.example.module_video.ui.adapter.recycleview.PwdAdapter
import com.example.module_video.utils.*
import com.example.module_video.viewmode.LockViewModel
import com.glance.guolindev.Glance.context
import kotlin.system.exitProcess


class LockActivity : BaseVmViewActivity<ActivityLockBinding, LockViewModel>() {

    private val mPwdAdapter by lazy {
        PwdAdapter()
    }

    private val mInputPwdAdapter by lazy {
        InputPwdAdapter()
    }

    override fun setFullScreenWindow() {

    }


    override fun getLayoutView(): Int = R.layout.activity_lock
    override fun getViewModelClass(): Class<LockViewModel> {
        return LockViewModel::class.java
    }

    private val inputPwdList = ArrayList<ItemBean>().apply {
        for (i in 1 until 13) {
            when (i) {
                in 1..9 -> add(ItemBean(title = "$i"))
                10 -> add(ItemBean(title = ""))
                11 -> add(ItemBean(title = "0"))
                12 -> add(ItemBean(title = "取消"))
            }
        }
    }
    

    private var mOpenAction=0

    override fun initView() {
        binding.apply {
            mOpenAction= intent.getIntExtra(Contents.KEY_ACTION, 0)

            passwordContainer.apply {
                layoutManager = GridLayoutManager(this@LockActivity, 4)
                adapter = mPwdAdapter
            }

            inputPassWordContainer.apply {
                val divider: GridItemDecoration = GridItemDecoration.Builder(this@LockActivity)
                    .setColorResource(R.color.transparent)
                    .setShowLastLine(false)
                    .build()
                addItemDecoration(divider)
                layoutManager = GridLayoutManager(this@LockActivity, 3)
                mInputPwdAdapter.setList(inputPwdList)
                adapter = mInputPwdAdapter
            }

        }
    }

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that=this@LockActivity
                pwdStateValue.observe(that, {
                    hintInput.text = when (it.state) {
                        InputPwdState.BEGIN -> {
                            "请输入您的密码"
                        }
                        InputPwdState.AGAIN -> {
                            "请再次输入密码"
                        }
                        InputPwdState.ERROR -> {
                            "密码错误，请再次输入"
                        }
                        else->"应用已经被锁住，请重启应用"
                    }
                    mPwdAdapter.setList(it.list)
                })

                checkState.observe(that, {
                    when (it) {
                        CheckState.EXIT -> {
                            hintInput.text = "应用已经被锁住，请重启应用"
                            inputPwdList[11].title = "退出应用"
                            mInputPwdAdapter.setList(inputPwdList)
                            mPwdAdapter.setList(hintList)
                        }
                        CheckState.UNLOCK -> {
                            if (mOpenAction == 0) finish() else toOtherActivity<HomeActivity>(this@LockActivity,true){}
                        }
                        CheckState.ERROR -> {
                            hintInput.text = "密码错误，请再次输入"
                            mPwdAdapter.setList(hintList)
                        }
                    }
                })

                backState.observe(that,{
                    inputPwdList[11].title=when(it){
                       BackState.CANCEL->"取消"
                       BackState.DELETE->"删除"
                        else->"取消"
                    }
                    mInputPwdAdapter.setList(inputPwdList)
                })

                checkPwdState.observe(that,{
                    if (it){
                        finish()
                    }else{
                        hintInput.text="两次密码输入不一致"
                    }
                })
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            mInputPwdAdapter.setOnItemClickListener { adapter, view, position ->
                if (viewModel.getCheckState() == CheckState.EXIT) {
                    if (position == 11) {
                        MyActivityManager.removeAllActivity()
                    } else {
                        return@setOnItemClickListener
                    }
                } else {
                    if (viewModel.getBackState()==BackState.CANCEL) {
                        if (position == 11) {
                            finish()
                        }
                    }
                }
                val realPosition = position + 1
                viewModel.setInputState(realPosition,mOpenAction)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_BACK) {
            if (viewModel.getCheckState() == CheckState.EXIT) {
                return true
            } else {
                finish()
            }
        }
        return super.onKeyDown(keyCode, event)
    }

}