package com.example.module_video.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_ad.ad.ad_help.AdController
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.utils.Contents
import com.example.module_base.activity.AboutActivity
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.base.BaseVmFragment
import com.example.module_base.utils.*
import com.example.module_base.utils.Constants.SET_DEAL1
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.livedata.BuyVipLiveData
import com.example.module_user.livedata.RemoveAdLiveData
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.ui.activity.LoginActivity
import com.example.module_user.utils.Constants.ALI_PAY
import com.example.module_user.utils.Constants.BODY
import com.example.module_user.utils.Constants.KEY_BUY_VIP
import com.example.module_user.utils.Constants.PAY_ALI_URL
import com.example.module_user.utils.Constants.PAY_WX_URL
import com.example.module_user.utils.Constants.PRICE
import com.example.module_user.utils.Constants.SUBJECT
import com.example.module_user.utils.Constants.TRADE
import com.example.module_user.utils.Constants.VIP13
import com.example.module_user.utils.Constants.VIP_PRICE
import com.example.module_user.utils.Constants.VIP_TITLE_13
import com.example.module_user.utils.Constants.WX_PAY
import com.example.module_user.utils.NetState
import com.example.module_video.R
import com.example.module_video.databinding.FragmentSetBinding
import com.example.module_video.domain.ItemBean
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.activity.LockActivity
import com.example.module_video.ui.adapter.recycleview.SetAdapter
import com.example.module_video.ui.widget.popup.BuyVipPopup
import com.example.module_video.ui.widget.popup.FunctionSelectPopup
import com.example.module_video.ui.widget.popup.PayPopup
import com.example.module_video.ui.widget.popup.UserRemindPopup
import com.example.module_video.utils.Constants
import com.example.module_video.viewmodel.SetViewModel
import com.just.agentweb.AgentWeb
import java.util.*
import kotlin.collections.ArrayList

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.fragment
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:55:03
 * @class describe
 */
class SetFragment : BaseVmFragment<FragmentSetBinding, SetViewModel>(),
    SharedPreferences.OnSharedPreferenceChangeListener {
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
    private val mCoreFunctionPopup by lazy {
        FunctionSelectPopup(activity)
    }

    private val mBuyVipPopup by lazy {
        BuyVipPopup(activity)
    }

    private val mPayPopup by lazy {
        PayPopup(activity)
    }


    private val mAdController by lazy {
        AdController.Builder(requireActivity())
            .setPage(AdType.SETTING_PAGE)
            .setContainer(hashMapOf(AdController.ContainerType.TYPE_FEED to binding.mAdContainer))
            .create()
    }




    override fun getViewModelClass(): Class<SetViewModel> {
        return SetViewModel::class.java
    }

    override fun getChildLayout(): Int = R.layout.fragment_set

    private val mSetFunctionList = ArrayList<ItemBean>()
    private val mGeneralList = ArrayList<ItemBean>()


    override fun onResume() {
        super.onResume()
        showSet()
        if (buyAction) {
            mValueUserInfo?.loginInfo?.let {
                when (it.loginType) {
                    com.example.module_user.utils.Constants.LOCAL_TYPE -> {
                      if ( it.phone!="" && it.pwd!="" )
                        viewModel.toLocalLogin(it.phone, it.pwd)
                    }
                }
            }
        }

    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            showSet()
        }
    }

    private fun showSet() {
        mSetFunctionList.clear()
        mSetFunctionList.apply {
            add(ItemBean(title = "密码锁定", hasPwd = sp.getBoolean(Constants.SP_SET_PWD_STATE)))
            add(ItemBean(title = "内核设置", hint = getCoreType()))
            mFunctionAdapter.setHasSet()
            mFunctionAdapter.setList(this)
        }
    }

    private fun getCoreType()= when(sp.getInt(Constants.SP_CORE_TYPE)){
        0 -> "IJK内核"
        1 -> "EXO内核"
        2 -> "系统内核"
           else->"系统内核"
       }

    override fun initView() {

        sp.prefs.registerOnSharedPreferenceChangeListener(this)
        binding.apply {
            setStatusBar(context, mNestedScrollView, LayoutType.CONSTRAINTLAYOUT)

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
            mAdController.show()
        }
    }

    private var currentLoginState = false
    private var mValueUserInfo:ValueUserInfo?=null
    private var userId=""
    private var userVip=0
    override fun observerData() {
        binding.apply {
            viewModel.apply {
                val that=this@SetFragment
                UserInfoLiveData.observe(that, {
                    mValueUserInfo=it
                    userId = it.userInfo?.data?.id.toString()
                    userVip=it.userInfo?.data?.vip?:0
                    currentLoginState = it.loginState
                    mGeneralList.clear()
                    mGeneralList.apply {
                        val data = it?.userInfo?.data
                        add(ItemBean(title = if (it.loginState) "ID:${data?.id?.toString()?: ""}"+"${if (data?.vip?:0> 0) "(VIP)" else "" }"   else "登陆/注册"))
                        add(ItemBean(title = "移除底部广告"))
                        if (it.loginState) {
                            add(ItemBean(title = "退出登录"))
                            add(ItemBean(title = "账号注销"))
                        }
                        mGeneralAdapter.setList(this)
                    }

                    RemoveAdLiveData.setRemoveAction(userVip>0)

                })


                RemoveAdLiveData.observe(that,{
                    if (it) goneView(mAdContainer) else showView(mAdContainer)
                })

                BuyVipLiveData.observe(that, {
                    if (it) {
                        if (userVip<=0) {
                            mPayPopup.showPopupView(mSetContainer)
                            BuyVipLiveData.setBuyState(false)
                        }
                    }
                })

                loginState.observe(that, {
                    mLoadingDialog.apply {
                        when (it.state) {
                            NetState.LOADING -> showDialog(activity)
                            NetState.SUCCESS -> {
                                dismiss()
                                if (userVip > 0) {
                                    showToast("您已成为尊贵的VIP")
                                } else {
                                    showToast("购买失败")
                                }
                                buyAction=false
                            }
                            NetState.ERROR -> {
                                dismiss()
                                showToast(it.msg)
                            }
                        }
                    }

                })

                logOutState.observe(that, {
                    mLoadingDialog?.apply {
                        when (it.state) {
                            NetState.LOADING -> showDialog(activity)
                            NetState.SUCCESS, NetState.ERROR -> {
                                dismiss()
                                showToast(it.msg)
                            }
                        }

                    }
                })
            }

        }

    }


    override fun initEvent() {
        mFunctionAdapter.setOnCheckListener(object : SetAdapter.OnCheckListener {
            override fun onCheck(b: Boolean) {}
            override fun onClick() {
                toOtherActivity<LockActivity>(activity) {
                    putExtra(Contents.KEY_ACTION, 0)
                }
            }
        })

        binding.apply {
            mFunctionAdapter.setOnItemClickListener { adapter, view, position ->
                when(position){
                    1 -> {
                        mCoreFunctionPopup.apply {
                            setSelectType()
                            showPopupView(mSetContainer)
                        }
                    }
                }
            }

            mGeneralAdapter.setOnItemClickListener { adapter, view, position ->
                when(position){
                    0 -> if (!currentLoginState) toOtherActivity<LoginActivity>(activity) {
                        putExtra(KEY_BUY_VIP,false)
                    }
                    1 -> {
                        mBuyVipPopup.showPopupView(mSetContainer)
                    }
                    2 -> if (currentLoginState) mExitPopup.showPopupView(mSetContainer)
                    3 -> if (currentLoginState) mLogoutPopup.showPopupView(mSetContainer)

                }
            }

            mUsSetAdapter.setOnItemClickListener { adapter, view, position ->
                when (position) {
                    0 -> {
                        copyContent(context, "2681706890@qq.com")
                    }
                    1 -> toOtherActivity<AboutActivity>(activity) {}
                    2 -> toOtherActivity<DealViewActivity>(activity) {
                        putExtra(SET_DEAL1, 2)
                    }
                    3 -> toOtherActivity<DealViewActivity>(activity) {
                        putExtra(SET_DEAL1, 1)
                    }
                    4 -> PermissionUtil.gotoPermission(activity)
                }
            }
            mBuyVipPopup.doSure {
                mBuyVipPopup.dismiss()
                mPayPopup.showPopupView(mSetContainer)
            }

            mPayPopup.setOnActionClickListener(object :BasePopup.OnActionClickListener{
                override fun sure() {
                    //支付宝
                    playAction(ALI_PAY)

                }

                override fun cancel() {
                    //微信
                    playAction(WX_PAY)
                }
            })

            mLogoutPopup.doSure {
                viewModel.toLogOut(userId)
            }

            mExitPopup.doSure {
                UserInfoLiveData.setUserInfo(ValueUserInfo(false, null,null))
            }

        }
    }

    private var buyAction=false

    /**
     * 生成订单号
     */
    private fun getTrade(payType: String): String? {
        val channel = PackageUtil.getAppMetaData(activity, Contents.PLATFORM_KEY)
        var str = channel.substring(channel.indexOf("_") + 1)
        if (str == "360") {
            str = "SLL"
        }
        str = str.toUpperCase()
        return VIP13 + "_" + userId + "_" + str + "_" + payType + "_" + Random().nextInt(100000)
    }

    private fun playAction(payType: String){
        if (currentLoginState) {
            if (userVip<=0) {
                val appName = PackageUtil.getAppMetaData(
                    activity,
                    com.example.module_user.utils.Constants.APP_NAME
                )
                val url: String =
                    (if (payType == ALI_PAY) PAY_ALI_URL else PAY_WX_URL) + TRADE + "=" + getTrade(
                        payType
                    ).toString() + "&" + SUBJECT + "=" + appName + VIP13 + "&" + PRICE + "=" + VIP_PRICE
                        .toString() + "&" + BODY + "=" + appName + VIP_TITLE_13
                AgentWeb.with(this)
                    .setAgentWebParent(binding.webContainer, LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(url)
                LogUtils.i("----playAction-----------$url----------------------")
                buyAction=true
            } else {
                showToast("您已经是尊贵的VIP了")
            }
        } else {
        /*    Intent(context,LoginActivity::class.java).apply {
                putExtra(KEY_BUY_VIP,true)
                startActivityForResult(this,LoginActivity.KEY_REQUEST_CODE)
            }*/

            toOtherActivity<LoginActivity>(activity){
                putExtra(KEY_BUY_VIP,true)
            }

        }
    }


/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==200) {
            data?.let {
                if (it.getBooleanExtra(LoginActivity.KEY_LOGIN_STATE,false)) {
                    mPayPopup.showPopupView(binding.mSetContainer)
                }
            }
        }

    }*/


    override fun release() {
        mLoadingDialog.dismiss()
        sp.prefs.unregisterOnSharedPreferenceChangeListener(this)
        mAdController.release()
        mBuyVipPopup.dismiss()
        mExitPopup.dismiss()
        mLogoutPopup.dismiss()
        mPayPopup.dismiss()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        mSetFunctionList[1].hint=getCoreType()
        mFunctionAdapter.setList(mSetFunctionList)
    }

}