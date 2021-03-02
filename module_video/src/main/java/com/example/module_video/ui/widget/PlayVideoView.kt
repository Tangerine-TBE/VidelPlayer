package com.example.module_video.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.module_video.R
import com.example.module_video.domain.SwitchVideoModel
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import moe.codeest.enviews.ENPlayView
import java.io.File
import java.util.*

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/2 14:08:48
 * @class describe
 */
class PlayVideoView(context: Context?, attrs: AttributeSet?) :
        StandardGSYVideoPlayer(context, attrs) {
    constructor(context: Context) : this(context, null)

    override fun getLayoutId(): Int = R.layout.widget_play_video
    private var mUrlList: List<SwitchVideoModel> = ArrayList()

    //数据源
    private var mSourcePosition = 0

    private val mMoreScale: TextView = findViewById(R.id.switch_Size)

    init {
        // setStatusBar(context,mTopContainer,LayoutType.CONSTRAINTLAYOUT,)

        //切换清晰度
        mMoreScale.setOnClickListener { v: View? ->
            if (!mHadPlay) {
                return@setOnClickListener
            }
            if (mType == 0) {
                mType = 1
            } else if (mType == 1) {
                mType = 2
            } else if (mType == 2) {
                mType = 3
            } else if (mType == 3) {
                mType = 4
            } else if (mType == 4) {
                mType = 0
            }
            resolveTypeUI()
        }
    }


    /**
     * 全屏时将对应处理参数逻辑赋给全屏播放器
     *
     * @param context
     * @param actionBar
     * @param statusBar
     * @return
     */
    override fun startWindowFullscreen(context: Context?, actionBar: Boolean, statusBar: Boolean): GSYBaseVideoPlayer? {
        val playVideoView = super.startWindowFullscreen(context, true, true) as PlayVideoView
        playVideoView.mSourcePosition = mSourcePosition
        playVideoView.mType = mType
        playVideoView.mUrlList = mUrlList
        //sampleVideo.resolveTransform();
        playVideoView.resolveTypeUI()
        //sampleVideo.resolveRotateUI();
        //这个播放器的demo配置切换到全屏播放器
        //这只是单纯的作为全屏播放显示，如果需要做大小屏幕切换，请记得在这里耶设置上视频全屏的需要的自定义配置
        //比如已旋转角度之类的等等
        //可参考super中的实现
        return playVideoView
    }

    /**
     * 推出全屏时将对应处理参数逻辑返回给非播放器
     *
     * @param oldF
     * @param vp
     * @param gsyVideoPlayer
     */
    override fun resolveNormalVideoShow(oldF: View?, vp: ViewGroup?, gsyVideoPlayer: GSYVideoPlayer?) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)
        if (gsyVideoPlayer != null) {
            val playVideoView = gsyVideoPlayer as PlayVideoView
            mSourcePosition = playVideoView.mSourcePosition
            mType = playVideoView.mType
            setUp(mUrlList, mCache, mCachePath, mTitle)
            resolveTypeUI()
        }
    }


    //记住切换数据源类型
    private var mType = 0

    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private fun resolveTypeUI() {
        if (!mHadPlay) {
            return
        }
        when (mType) {
            1 -> {
                mMoreScale.text = "16:9"
                GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_16_9)
            }
            2 -> {
                mMoreScale.text = "4:3"
                GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_4_3)
            }
            3 -> {
                mMoreScale.text = "全屏"
                GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL)
            }
            4 -> {
                mMoreScale.text = "拉伸全屏"
                GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
            }
            0 -> {
                mMoreScale.text = "原始比例"
                GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_DEFAULT)
            }
        }
        changeTextureViewShowType()
        if (mTextureView != null) mTextureView.requestLayout()
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */
    fun setUp(url: List<SwitchVideoModel>, cacheWithPlay: Boolean, title: String?): Boolean {
        mUrlList = url
        return setUp(url[mSourcePosition].url, cacheWithPlay, title)
    }


    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param title         title
     * @return
     */
    fun setUp(url: List<SwitchVideoModel>, cacheWithPlay: Boolean, cachePath: File?, title: String?): Boolean {
        mUrlList = url
        return setUp(url[mSourcePosition].url, cacheWithPlay, cachePath, title)
    }


    override fun updateStartImage() {
        mStartButton.visibility=View.VISIBLE
      if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                CURRENT_STATE_PLAYING -> {
                    imageView.setImageResource(R.mipmap.icon_video_pause)
                }
                CURRENT_STATE_ERROR -> {
                    imageView.setImageResource(R.drawable.video_click_error_selector)
                }
                else -> {
                    imageView.setImageResource(R.mipmap.icon_video_play)
                }
            }
        }
    }

}