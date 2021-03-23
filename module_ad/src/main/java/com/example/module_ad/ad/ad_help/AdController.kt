package com.example.module_ad.ad.ad_help

import android.app.Activity
import android.widget.FrameLayout
import com.example.module_ad.advertisement.AdType
import com.example.module_ad.utils.AdMsgUtil.*
import com.tamsiree.rxkit.RxNetTool

/**
 * @name VidelPlayer
 * @class name：com.example.module_ad.ad
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/23 10:29:44
 * @class describe
 */
class AdController(val activity: Activity) {


    private var page: AdType? = null
    private var container: HashMap<ContainerType, FrameLayout>? = null
    private var mBannerHelper: BannerHelper? = null
    private var mFeedHelper: FeedHelper? = null
    private var mInsertHelper: InsertHelper? = null
    private var mSplashAction: () -> Unit = {}

    private fun setContainer(container: HashMap<ContainerType, FrameLayout>?) {
        this.container = container
    }


    private fun setPage(page: AdType?) {
        this.page = page
    }

    fun setSplashAction(block: () -> Unit): AdController {
        mSplashAction = block
        return this
    }


    fun show() {
        getAdState()?.let {
            val type = switchAdType(page, it)
            val random = Math.random()
            //开屏
            container?.get(ContainerType.TYPE_SPLASH)?.apply {
                if (RxNetTool.isNetworkAvailable(activity)) {
                    SplashHelper(activity, this).apply {
                        setSplashAction(mSplashAction)
                        showAd(null, random)
                    }
                } else {
                    mSplashAction()
                }
            }
            //banner
            container?.get(ContainerType.TYPE_BANNER)?.apply {
                mBannerHelper = BannerHelper(activity, this).apply {
                    showAd(type, random)
                }
            }
            //feed
            container?.get(ContainerType.TYPE_FEED)?.apply {
                mFeedHelper = FeedHelper(activity, this).apply {
                    showAd(type, random)
                }
            }
            //insert
            container?.get(ContainerType.TYPE_INSERT)?.apply {
                mInsertHelper = InsertHelper(activity, this).apply {
                    showAd(type, random)
                }
            }
        }
    }

    fun release() {
        mBannerHelper?.releaseAd()
        mFeedHelper?.releaseAd()
        mInsertHelper?.releaseAd()
    }

    class Builder(val activity: Activity) {
        private var mPage: AdType? = null
        private var mContainer: HashMap<ContainerType, FrameLayout>? = null


        fun setContainer(container: HashMap<ContainerType, FrameLayout>): Builder {
            this.mContainer = container
            return this
        }

        fun setPage(page: AdType?): Builder {
            this.mPage = page
            return this
        }


        fun create(): AdController {
            return AdController(activity).apply {
                setContainer(mContainer)
                setPage(mPage)
            }
        }
    }

    enum class ContainerType {
        TYPE_SPLASH, TYPE_BANNER, TYPE_FEED, TYPE_INSERT
    }
}