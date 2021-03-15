package com.example.module_video.ui.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.sdk.android.feedback.impl.FeedbackAPI
import com.example.module_base.activity.AboutActivity
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.module_base.utils.Constants.SET_DEAL1
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.ui.activity.LoginActivity
import com.example.module_video.R
import com.example.module_video.databinding.FragmentSetBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.activity.LockActivity
import com.example.module_video.ui.adapter.recycleview.SetAdapter
import com.example.module_video.ui.widget.popup.UserRemindPopup
import com.example.module_video.utils.Constants
import com.example.module_video.viewmode.SetViewModel

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:55:03
 * @class describe
 */
class SetFragment : BaseVmFragment<FragmentSetBinding, SetViewModel>() {

    private val mGeneralAdapter by lazy {
        SetAdapter()
    }


    private val mUsSetAdapter by lazy {
        SetAdapter()
    }

    private val mFunctionAdapter by lazy {
        SetAdapter()
    }

    private val mExitPopup by lazy {
        UserRemindPopup(activity).apply {
       setRemindContent("您确定要退出登录吗？")
        }
    }

    private val mLogoutPopup by lazy {
        UserRemindPopup(activity).apply {
            setRemindContent("您确定要注销账号吗？")
        }
    }



    override fun getViewModelClass(): Class<SetViewModel> {
        return SetViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_set


    private val mSetFunctionList = ArrayList<ItemBean>()
    private val mGeneralList = ArrayList<ItemBean>()

    override fun onResume() {
        super.onResume()
        mSetFunctionList.clear()
        mSetFunctionList.apply {
            add(ItemBean(title = "密码锁定", hasPwd = sp.getBoolean(Constants.SP_SET_PWD_STATE)))
            add(ItemBean(title = "硬件加速"))
            add(ItemBean(title = "字幕大小"))
            add(ItemBean(title = "字幕文本编码"))
            mFunctionAdapter.setList(this)
        }



    }

    override fun initView() {

        binding.apply {
            setStatusBar(context, mNestedScrollView, LayoutType.LINEARLAYOUT)

            mGeneralContainer.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = mGeneralAdapter
            }


            mWeContainer.apply {
                layoutManager = LinearLayoutManager(activity)
                mUsSetAdapter.setList(DataProvider.setConnectUsList)
                adapter = mUsSetAdapter
                mUsSetAdapter.setHasContact()
            }

            mSetContainer.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = mFunctionAdapter
                mFunctionAdapter.setHasSwitch()
            }

        }
    }

    private var currentLoginState = false

    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that=this@SetFragment
                UserInfoLiveData.observe(that, {
                    currentLoginState = it.loginState
                    mGeneralList.clear()
                    mGeneralList.apply {
                        add(ItemBean(title = if (it.loginState) "ID:${it?.userInfo?.data?.id?.toString()?:""}" else "登陆/注册"))
                        add( ItemBean(title = "移除底部广告"))
                        add(ItemBean(title = "账号注销"))
                        add(ItemBean(title = "退出登录"))
                        mGeneralAdapter.setList(this)
                    }
                })
            }


        }

    }

    override fun initEvent() {
        binding.apply {
            mFunctionAdapter.setOnCheckListener(object : SetAdapter.OnCheckListener {
                override fun onCheck(b: Boolean) {

                }

                override fun onClick() {
                    toOtherActivity<LockActivity>(activity) {}
                }
            })

            mGeneralAdapter.setOnItemClickListener { adapter, view, position ->
                when(position){
                    0 ->if (!currentLoginState)  toOtherActivity<LoginActivity>(activity){}
                    1->{

                    }
                    2-> if (currentLoginState) mLogoutPopup.showPopupView(mSetContainer)
                    3-> if (currentLoginState) mExitPopup.showPopupView(mSetContainer)
                }
            }

            mLogoutPopup.doSure {

            }

            mExitPopup.doSure {
                UserInfoLiveData.setUserInfo(ValueUserInfo(false, null))
            }



            mFunctionAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {

                }
            }

            mUsSetAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> FeedbackAPI.openFeedbackActivity()
                    1 -> {
                        copyContent(context, "2681706890@qq.com")
                    }
                    2 -> toOtherActivity<AboutActivity>(activity) {}
                    3 -> toOtherActivity<DealViewActivity>(activity) {
                        putExtra(SET_DEAL1, 2)
                    }
                    4 -> toOtherActivity<DealViewActivity>(activity) {
                        putExtra(SET_DEAL1, 1)
                    }
                    5 -> PermissionUtil.gotoPermission(activity)
                }
            }
        }
    }

}