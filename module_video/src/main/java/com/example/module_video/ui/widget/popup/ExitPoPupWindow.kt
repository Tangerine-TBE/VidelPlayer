package com.example.module_video.ui.widget.popup


import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.example.module_ad.ad.ad_help.AdController
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.advertisement.FeedHelper
import com.example.module_ad.utils.BaseBackstage
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.MyActivityManager
import com.example.module_video.R
import com.example.module_video.databinding.DiyExitPopupWindowBinding


class ExitPoPupWindow(mActivity: FragmentActivity) : BasePopup<DiyExitPopupWindowBinding>(
    mActivity,
    R.layout.diy_exit_popup_window,
    ViewGroup.LayoutParams.MATCH_PARENT,
    ViewGroup.LayoutParams.MATCH_PARENT
) {

    private val mFeedAd by lazy {
            AdController.Builder(activity!!)
                    .setPage(AdType.EXIT_PAGE)
                    .setContainer(hashMapOf(AdController.ContainerType.TYPE_FEED to mView.exitAdContainer))
                    .create()

    }

    override fun initEvent() {
        mView?.apply {
            mCancel.setOnClickListener {
                mOutValueAnimator?.start()
                dismiss()
                exitAdContainer.removeAllViews()
            }
            mSure.setOnClickListener {
                mOutValueAnimator?.start()
                dismiss()
                MyActivityManager.removeAllActivity()
            }

        }
        setOnDismissListener {
            mFeedAd?.release()
        }

    }


    private fun popupShowAd() {
        mFeedAd?.show()
    }

    override fun showPopupView(view: View, gravity: Int, x: Int, y: Int) {
        popupShowAd()
        super.showPopupView(view, gravity, x, y)

    }
}