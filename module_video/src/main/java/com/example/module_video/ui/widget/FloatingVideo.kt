package com.example.module_video.ui.widget

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.module_base.utils.LogUtils
import com.example.module_video.R
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListBean
import com.example.module_video.ui.activity.PlayVideoActivity
import com.google.gson.Gson
import com.lzf.easyfloat.EasyFloat
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
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
    private lateinit var mInclude: ViewGroup

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
        mStartButton = findViewById(R.id.start)
        mBottomContainer = findViewById(R.id.layout_bottom)

        mStartButton.setOnClickListener { clickStartIcon() }

        mPre = findViewById(R.id.small_pre)
        mNext= findViewById(R.id.small_next)
        mDelete = findViewById(R.id.small_delete)
        mFull= findViewById(R.id.small_full)
        mInclude= findViewById(R.id.smallInclude)

        initEvent()
    }



    private fun initEvent() {
        mInclude.setOnClickListener {
            LogUtils.i("--initEvent---------------mInclude------------------")
            mBottomContainer.visibility=View.VISIBLE
            scaleAction(true)
        }

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
            context.startActivity(Intent(context,PlayVideoActivity::class.java).apply {
                putExtra(PlayVideoActivity.VIDEO_MSG,  Gson().toJson(PlayListBean(mUrlList.toMutableList())))
                putExtra(PlayVideoActivity.PLAY_POSITION,mSourcePosition)
                putExtra(PlayVideoActivity.FROM_CHANNEL, 1)
                putExtra(PlayVideoActivity.PROGRESS, currentPositionWhenPlaying)
            })
        }
    }


    private var mUrlList: List<MediaInformation> = ArrayList()

    //数据源
    private var mSourcePosition = 0



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
    override fun updateStartImage() {
        mStartButton.visibility = View.VISIBLE
        if (mStartButton is ImageView) {
            val imageView = mStartButton as ImageView
            when (mCurrentState) {
                CURRENT_STATE_PLAYING -> {
                    imageView.setImageResource(R.mipmap.icon_small_pause)
                }
                CURRENT_STATE_ERROR -> {
                    imageView.setImageResource(R.mipmap.icon_small_play)
                }
                else -> {
                    imageView.setImageResource(R.mipmap.icon_small_play)
                }
            }
        }
    }

   override fun changeUiToNormal(){
       setViewShowState(mStartButton, VISIBLE)
       updateStartImage()

   }
    override fun changeUiToPlayingShow(){
        setViewShowState(mStartButton, VISIBLE)
        updateStartImage()
    }

    override fun changeUiToPreparingShow(){
        setViewShowState(mStartButton, VISIBLE)
    }

    override fun changeUiToPlayingBufferingShow(){
        setViewShowState(mStartButton, VISIBLE)
    }


    private var scaleAction:(Boolean)->Unit={false}

    fun showScaleIcon(block:(Boolean)->Unit){
        scaleAction=block
    }


    override fun  hideAllWidget(){
        scaleAction(false)
        setViewShowState(mBottomContainer, INVISIBLE)
        setViewShowState(mTopContainer, INVISIBLE)
        setViewShowState(mBottomProgressBar, VISIBLE)
        setViewShowState(mStartButton, VISIBLE)
    }

}