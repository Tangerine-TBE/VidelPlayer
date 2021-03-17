package com.example.module_video.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.module_video.R
import com.example.module_video.domain.MediaInformation
import com.example.module_video.ui.widget.popup.play.PlayListPopup
import com.shuyu.gsyvideoplayer.utils.Debuger
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import moe.codeest.enviews.ENDownloadView
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
class PlayVideoView : StandardGSYVideoPlayer {
    constructor(context: Context, fullFlag: Boolean?) : super(context, fullFlag) {}
    constructor(context: Context) : super(context) {}
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    override fun getLayoutId(): Int {
        return if (mIfCurrentIsFullscreen) {
            R.layout.widget_play_video_land
        } else {
            R.layout.widget_play_video_normal
        }
    }
    private val mPlayListPopup by lazy {
        PlayListPopup(context)
    }

    private var mUrlList: List<MediaInformation> = ArrayList()

    //数据源
    private var mSourcePosition = 0

    //记住切换切换尺寸
    private var mType = 0

    //记住切换切换速度
    private var mSpeed = 0

    //播放模式
    private var mPlayModel = PlayState.ORDERLY

    //-------------------控件----------------------
    //尺寸
    private val mMoreScale: TextView = findViewById(R.id.switch_Size)

    //速度
    private val mChangeSpeed: TextView = findViewById(R.id.switch_speed)

    //模式
    private val mChangeModel: TextView = findViewById(R.id.action_recycle)

    //更多
    private val mMore: ImageView = findViewById(R.id.more)

    //列表
    private val mPlayList: TextView = findViewById(R.id.playList)

    //上一个
    private val mPre: ImageView = findViewById(R.id.pre)

    //下一个
    private val mNext: ImageView = findViewById(R.id.next)

    private val mCoverImage:ImageView = findViewById(R.id.thumbImage)

    //小窗
    val mShowSmallWindow: TextView = findViewById(R.id.switch_window)

    init {
        //切换尺寸
        initSize()
        //切换速度
        initSpeed()
        //点击事件
        initEvent()
    }

    //---------------------初始化-------------------------
    private fun initSize() {
        mType = GSYVideoType.getShowType()
        resolveTypeUI()
    }

    private fun initSpeed() {
        mSpeed = speed
        resolveSpeedUI()
    }

    //----------------------事件监听--------------------------
    private fun initEvent() {
        //上一个
        mPre.setOnClickListener {
            playPreAction()
        }
        //下一个
        mNext.setOnClickListener {
            playNextAction()
        }
        //切换播放速度
        mChangeSpeed.setOnClickListener {
            if (!mHadPlay) {
                return@setOnClickListener
            }
            selectSpeed()
            resolveSpeedUI()
        }
        //播放列表
        mPlayList.setOnClickListener {
            mPlayListPopup.apply {
                setListData(mUrlList, mSourcePosition)
                popupGravity = Gravity.TOP
                showPopupWindow(it)
            }

        }
        //切换尺寸
        mMoreScale.setOnClickListener { v: View? ->
            if (!mHadPlay) {
                return@setOnClickListener
            }
            selectType()
            resolveTypeUI()
        }
        //播放模式
        mChangeModel.setOnClickListener {
            if (!mHadPlay) {
                return@setOnClickListener
            }
            selectModel()
            resolvePlayModelUI()
        }

        mPlayListPopup.selectMedia {
            mSourcePosition=it
            startPlayVideo()
        }
    }


    //---------------------播放动作---------------------------
    private fun playPreAction() {
        if (mSourcePosition > 0) {
            mSourcePosition--
            startPlayVideo()
        }
    }

    private fun playNextAction() {
        if (mSourcePosition < mUrlList.size - 1) {
            mSourcePosition++
            startPlayVideo()
        } else {
            mSourcePosition=0
            startPlayVideo()
        }
    }

    private fun playRandomAction() {
        mSourcePosition = Random().nextInt(mUrlList.size)
        startPlayVideo()
    }

    private fun playLoopAction() {
        startPlayVideo()
    }

    private fun startPlayVideo() {
        setUp(mUrlList, mSourcePosition, true, mUrlList[mSourcePosition].name)
        startPlayLogic()
        mPlayListPopup.setListPosition(mSourcePosition)
    }

    override fun onAutoCompletion() {
        when (mPlayModel) {
            PlayState.ORDERLY -> {
                playNextAction()
            }
            PlayState.LOOP -> {
                playLoopAction()
            }
            PlayState.RANDOM -> {
                playRandomAction()
            }
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
    override fun startWindowFullscreen(
        context: Context?,
        actionBar: Boolean,
        statusBar: Boolean
    ): GSYBaseVideoPlayer? {
        val playVideoView =
                super.startWindowFullscreen(context, actionBar, statusBar) as PlayVideoView
        playVideoView.mSourcePosition = mSourcePosition
        playVideoView.mUrlList = mUrlList

        playVideoView.mPlayModel = mPlayModel
        resolvePlayModelUI()
        playVideoView.mType = mType
        playVideoView.resolveTypeUI()
        playVideoView.mSpeed = mSpeed
        playVideoView.resolveSpeedUI()

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
    override fun resolveNormalVideoShow(
        oldF: View?,
        vp: ViewGroup?,
        gsyVideoPlayer: GSYVideoPlayer?
    ) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)
        if (gsyVideoPlayer != null) {
            val playVideoView = gsyVideoPlayer as PlayVideoView
            mSourcePosition = playVideoView.mSourcePosition
            setUp(mUrlList, mCache, mCachePath, mTitle)

            mType = playVideoView.mType
            resolveTypeUI()
            mSpeed = playVideoView.mSpeed
            resolveSpeedUI()
            mPlayModel = playVideoView.mPlayModel
            resolvePlayModelUI()
        }
    }

    private fun selectModel() {
        mPlayModel = when (mPlayModel) {
            PlayState.ORDERLY -> PlayState.LOOP
            PlayState.LOOP -> PlayState.RANDOM
            PlayState.LOOP -> PlayState.ORDERLY
            else->PlayState.ORDERLY
        }
    }

    private fun selectType() {
        when (mType) {
            0 -> {
                mType = 1
            }
            1 -> {
                mType = 2
            }
            2 -> {
                mType = 3
            }
            3 -> {
                mType = 4
            }
            4 -> {
                mType = 0
            }
            -4 -> {
                mType = 0
            }

        }
    }


    private fun selectSpeed() {
        when (mSpeed) {
            1f -> {
                mSpeed = 1.25f
            }
            1.25f -> {
                mSpeed = 1.5f
            }
            1.5f -> {
                mSpeed = 2f
            }
            2f -> {
                mSpeed = 3f
            }
            3f -> {
                mSpeed = 0.5f
            }
            0.5f -> {
                mSpeed = 0.75f
            }
            0.75f -> {
                mSpeed = 1f
            }
        }
    }

    /**
     * 设置播放URL
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param title         title
     * @return
     */
    fun setUp(
        url: List<MediaInformation>,
        position: Int,
        cacheWithPlay: Boolean,
        title: String?
    ): Boolean {
        mSourcePosition = position
        mUrlList = url
        return setUp(url[mSourcePosition].uri, cacheWithPlay, title)
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
    private fun setUp(
        url: List<MediaInformation>,
        cacheWithPlay: Boolean,
        cachePath: File?,
        title: String?
    ): Boolean {
        mUrlList = url
        return setUp(url[mSourcePosition].uri, cacheWithPlay, cachePath, title)
    }

    override fun getVolumeLayoutId(): Int =R.layout.my_video_volume_dialog
    //----------------------------UI控制-----------------------------------
    /**
     * 显示比例
     * 注意，GSYVideoType.setShowType是全局静态生效，除非重启APP。
     */
    private fun resolveTypeUI() {
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
            -4 -> {
                mMoreScale.text = "拉伸全屏"
                GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL)
            }
        }

        changeTextureViewShowType()
        if (mTextureView != null) mTextureView.requestLayout()
    }


    private fun resolvePlayModelUI() {
        mChangeModel.apply {
            when (mPlayModel) {
                PlayState.RANDOM -> {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context.getDrawable(R.mipmap.icon_video_random),
                        null,
                        null
                    )
                    text = "随机播放"
                }

                PlayState.ORDERLY -> {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context.getDrawable(R.mipmap.icon_video_orderly),
                        null,
                        null
                    )
                    text = "顺序播放"
                }

                PlayState.LOOP -> {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context.getDrawable(R.mipmap.icon_video_recycle1),
                        null,
                        null
                    )
                    text = "循环播放"
                }
            }
        }

    }


    private fun resolveSpeedUI() {
        mChangeSpeed.text = "播放速度：$mSpeed"
        setSpeedPlaying(mSpeed, true)
    }

    override fun updateStartImage() {
        mStartButton.visibility = View.VISIBLE
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                CURRENT_STATE_PLAYING -> {
                    imageView.setImageResource(R.mipmap.icon_video_pause)
                }
                CURRENT_STATE_ERROR -> {
                    imageView.setImageResource(R.mipmap.icon_video_play)
                }
                else -> {
                    imageView.setImageResource(R.mipmap.icon_video_play)
                }
            }
        }
    }


    private val isLinkScroll = false
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (isLinkScroll && !isIfCurrentIsFullscreen) {
            parent.requestDisallowInterceptTouchEvent(true)
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun lockTouchLogic() {
        if (mLockCurScreen) {
            mLockScreen.setImageResource(R.mipmap.icon_video_unlock)
            mLockCurScreen = false
        } else {
            mLockScreen.setImageResource(R.mipmap.icon_video_lock)
            mLockCurScreen = true
            hideAllWidget()
        }

    }


    override fun changeUiToNormal() {
        if (!mLockCurScreen) {
            Debuger.printfLog("changeUiToNormal")
            setViewShowState(mTopContainer, VISIBLE)
            setViewShowState(mBottomContainer, INVISIBLE)
            setViewShowState(mStartButton, VISIBLE)
            setViewShowState(mLoadingProgressBar, INVISIBLE)
            setViewShowState(mThumbImageViewLayout, VISIBLE)
            setViewShowState(mBottomProgressBar, INVISIBLE)
            setViewShowState(mLockScreen, if (mIfCurrentIsFullscreen&&mNeedLockFull) VISIBLE else GONE)
            updateStartImage()
            if (mLoadingProgressBar is ENDownloadView) {
                (mLoadingProgressBar as ENDownloadView).reset()
            }
        }
    }

    override fun changeUiToPlayingShow() {
        if (!mLockCurScreen) {
            Debuger.printfLog("changeUiToPlayingShow")
            setViewShowState(mTopContainer, VISIBLE)
            setViewShowState(mBottomContainer, VISIBLE)
            setViewShowState(mStartButton, VISIBLE)
            setViewShowState(mLoadingProgressBar, INVISIBLE)
            setViewShowState(mThumbImageViewLayout, INVISIBLE)
            setViewShowState(mBottomProgressBar, INVISIBLE)
            setViewShowState(mLockScreen, if (mIfCurrentIsFullscreen&&mNeedLockFull) VISIBLE else GONE)
            if (mLoadingProgressBar is ENDownloadView) {
                (mLoadingProgressBar as ENDownloadView).reset()
            }
            updateStartImage()
        }
    }


    override fun changeUiToPreparingShow() {
        if (!mLockCurScreen) {
            setViewShowState(mTopContainer, VISIBLE)
            setViewShowState(mBottomContainer, VISIBLE)
            setViewShowState(mStartButton, VISIBLE)
            setViewShowState(mLoadingProgressBar, VISIBLE)
            setViewShowState(mThumbImageViewLayout, INVISIBLE)
            setViewShowState(mBottomProgressBar, INVISIBLE)
            setViewShowState(mLockScreen, if (mIfCurrentIsFullscreen&&mNeedLockFull) VISIBLE else GONE)
            if (mLoadingProgressBar is ENDownloadView) {
                val enDownloadView = mLoadingProgressBar as ENDownloadView
                if (enDownloadView.currentState == ENDownloadView.STATE_PRE) {
                    (mLoadingProgressBar as ENDownloadView).start()
                }
            }
        }

    }

    override fun changeUiToPlayingBufferingShow() {
        if (!mLockCurScreen) {
            setViewShowState(mTopContainer, VISIBLE)
            setViewShowState(mBottomContainer, VISIBLE)
            setViewShowState(mStartButton, VISIBLE)
            setViewShowState(mLoadingProgressBar, VISIBLE)
            setViewShowState(mThumbImageViewLayout, INVISIBLE)
            setViewShowState(mBottomProgressBar, INVISIBLE)
            setViewShowState(mLockScreen, if (mIfCurrentIsFullscreen&&mNeedLockFull) VISIBLE else GONE)
            if (mLoadingProgressBar is ENDownloadView) {
                val enDownloadView = mLoadingProgressBar as ENDownloadView
                if (enDownloadView.currentState == ENDownloadView.STATE_PRE) {
                    (mLoadingProgressBar as ENDownloadView).start()
                }
            }
        }
    }

    enum class PlayState {
        LOOP, RANDOM, ORDERLY
    }
}