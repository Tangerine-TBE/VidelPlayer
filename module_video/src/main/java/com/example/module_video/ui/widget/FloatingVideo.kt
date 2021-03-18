package com.example.module_video.ui.widget

import android.app.AlertDialog
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import com.example.module_video.R
import com.example.module_video.domain.MediaInformation
import com.lzf.easyfloat.EasyFloat
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import java.io.File
import java.util.*

/**
 * 多窗体下的悬浮窗页面支持Video
 * Created by shuyu on 2017/12/25.
 */
class FloatingVideo : StandardGSYVideoPlayer {

    companion object{
        const val SMALL_TAG="SMALL_TAG"
    }

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag) {}
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    //上一个
    private lateinit var mPre: ImageView
    //下一个
    private lateinit var mNext: ImageView
    //退出
    private lateinit var mDelete: ImageView
    //全屏
    private lateinit var mFull: ImageView

    override fun getLayoutId(): Int {
        return R.layout.layout_floating_video
    }

    override fun init(context: Context) {
        mContext = if (activityContext != null) {
            activityContext
        } else {
            context
        }
        initInflate(mContext)
        mTextureViewContainer = findViewById(R.id.surface_container)


        mStartButton = findViewById(R.id.start)
        if (isInEditMode) return
        mScreenWidth = activityContext.resources.displayMetrics.widthPixels
        mScreenHeight = activityContext.resources.displayMetrics.heightPixels
        mAudioManager =
            activityContext.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mStartButton = findViewById(com.shuyu.gsyvideoplayer.R.id.start)
        mStartButton.setOnClickListener { clickStartIcon() }

        mPre = findViewById(R.id.small_pre)
        mNext= findViewById(R.id.small_next)
        mDelete = findViewById(R.id.small_delete)
        mFull= findViewById(R.id.small_full)
        initEvent()
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
            parent.requestDisallowInterceptTouchEvent(true)
        return super.onInterceptTouchEvent(ev)
    }

    private fun initEvent() {
        //上一个
        mPre.setOnClickListener {
            playPreAction()
        }
        //下一个
        mNext.setOnClickListener {
            playNextAction()
        }
        //退出
        mDelete.setOnClickListener {
            EasyFloat.dismissAppFloat(SMALL_TAG)
            GSYVideoManager.releaseAllVideos()
        }
        //全屏
        mFull.setOnClickListener {

        }

        
    }


    private var mUrlList: List<MediaInformation> = ArrayList()

    //数据源
    private var mSourcePosition = 0


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

    private fun startPlayVideo() {
        setUp(mUrlList, mSourcePosition, false, mUrlList[mSourcePosition].name)
        startPlayLogic()
    }


    override fun onAutoCompletion() {
        playNextAction()
    }


    override fun getActivityContext(): Context {
        return context
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


}