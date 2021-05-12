package com.example.module_video.ui.widget.popup

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.activity.DealViewActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.Constants
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.showToast
import com.example.module_base.utils.toOtherActivity
import com.example.module_video.R
import com.example.module_video.databinding.PopupAgreementWindowBinding
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.adapter.recycleview.PermissionAdapter


/**
 * @name Wifi_Manager
 * @class name：com.example.wifi_manager.ui.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/28 13:42:27
 * @class describe
 */
class AgreementPopup(activity: FragmentActivity):BasePopup<PopupAgreementWindowBinding>(activity, R.layout.popup_agreement_window,ViewGroup.LayoutParams.MATCH_PARENT) {


    private val mAppName=PackageUtil.getAppMetaData(activity,Constants.APP_NAME)

    private val mPermissionAdapter by lazy {
        PermissionAdapter()
    }

    init {
        isFocusable=false
        isOutsideTouchable =false

        mView.apply {
            welcomeTitle.text="欢迎使用${mAppName}"
            permissionContainer.layoutManager = LinearLayoutManager(activity)
            permissionContainer.adapter = mPermissionAdapter
            mPermissionAdapter.setList(DataProvider.permissionList)


            val str = activity.resources.getString(R.string.user_agreement)
            val stringBuilder = SpannableStringBuilder(str)
            val span1 = TextViewSpan1()
            stringBuilder.setSpan(span1, 10, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            val span2 = TextViewSpan2()
            stringBuilder.setSpan(span2, 19, 25, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            descriptions.text=stringBuilder
            descriptions.movementMethod= LinkMovementMethod.getInstance()
        }




    }

    override fun initEvent() {
        mView.apply {
            btCancel?.setOnClickListener {
                showToast("您需要同意后才能继续使${mAppName}提供的服务")
            }

            btSure?.setOnClickListener {
                    dismiss()
                    mListener?.sure()
            }

        }

    }

    inner class TextViewSpan1 : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color =ContextCompat.getColor(mView.root.context,R.color.theme_bg_color)
        }

        override fun onClick(widget: View) {
            //点击事件
            toOtherActivity<DealViewActivity>(activity,false){
                putExtra(Constants.SET_DEAL1,1)
            }
        }
    }

    inner  class TextViewSpan2 : ClickableSpan() {
        override fun updateDrawState(ds: TextPaint) {
            ds.color =ContextCompat.getColor(mView.root.context,R.color.theme_bg_color)
        }

        override fun onClick(widget: View) {
            //点击事件
            toOtherActivity<DealViewActivity>(activity,false){
             putExtra(Constants.SET_DEAL1, 2)
            }
        }
    }

}