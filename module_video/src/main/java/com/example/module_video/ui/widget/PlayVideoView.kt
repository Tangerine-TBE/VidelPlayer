package com.example.module_video.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.graphics.BlendMode
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.LogUtils
import com.example.module_video.R
import com.example.module_video.domain.SwitchVideoModel
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.AppFloatDefaultAnimator
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.enums.SidePattern
import com.lzf.easyfloat.interfaces.OnFloatCallbacks
import com.lzf.easyfloat.interfaces.OnInvokeView
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
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

    override fun getLayoutId(): Int{
      return  if (mIfCurrentIsFullscreen) {
            R.layout.widget_play_video_land
        }else{
            R.layout.widget_play_video_normal
        }
    }

    private var mUrlList: List<SwitchVideoModel> = ArrayList()

    //数据源
    private var mSourcePosition = 0
    //记住切换切换尺寸
    private var mType = 0
    //记住切换切换速度
    private var mSpeed = 0


    private val mMoreScale: TextView = findViewById(R.id.switch_Size)
    private val mChangeSpeed: TextView = findViewById(R.id.switch_speed)
    val mShowSmallWindow: TextView = findViewById(R.id.switch_window)

    init {
        //切换尺寸
        initSize()
        mMoreScale.setOnClickListener { v: View? ->
            if (!mHadPlay) {
                return@setOnClickListener
            }
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
            resolveTypeUI()
        }

        //切换速度
        initSpeed()
        mChangeSpeed.setOnClickListener {
            if (!mHadPlay) {
                return@setOnClickListener
            }
            selectSpeed()
            resolveSpeedUI()
        }

    }

    private fun initSize() {
        mType = GSYVideoType.getShowType()
        resolveTypeUI()
    }

    private fun initSpeed(){
        mSpeed=speed
        resolveSpeedUI()
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
        val playVideoView = super.startWindowFullscreen(context, actionBar, statusBar) as PlayVideoView
        playVideoView.mSourcePosition = mSourcePosition
        playVideoView.mUrlList = mUrlList

        playVideoView.mType = mType
        playVideoView.resolveTypeUI()

        playVideoView.mSpeed=mSpeed
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
    override fun resolveNormalVideoShow(oldF: View?, vp: ViewGroup?, gsyVideoPlayer: GSYVideoPlayer?) {
        super.resolveNormalVideoShow(oldF, vp, gsyVideoPlayer)
        if (gsyVideoPlayer != null) {
            val playVideoView = gsyVideoPlayer as PlayVideoView
            mSourcePosition = playVideoView.mSourcePosition
            setUp(mUrlList, mCache, mCachePath, mTitle)
            mType = playVideoView.mType
            resolveTypeUI()

            mSpeed=playVideoView.mSpeed
            resolveSpeedUI()

        }
    }




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

        LogUtils.i("*-mMoreScale--mIfCurrentIsFullscreen:${mIfCurrentIsFullscreen}--------${mMoreScale.hashCode()}-----------$mSpeed----------")
        changeTextureViewShowType()
        if (mTextureView != null) mTextureView.requestLayout()
    }

    private fun selectSpeed(){
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


    private fun resolveSpeedUI() {
        LogUtils.i("*---mIfCurrentIsFullscreen:${mIfCurrentIsFullscreen}--------${mChangeSpeed.hashCode()}-----------$mSpeed----------")
        mChangeSpeed.text = "播放速度：$mSpeed"
        setSpeedPlaying(mSpeed, true)
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
       // super.lockTouchLogic()
        if (mLockCurScreen) {
            mLockScreen.setImageResource(R.mipmap.icon_video_unlock)
            mLockCurScreen = false
        } else {
            mLockScreen.setImageResource(R.mipmap.icon_video_lock)
            mLockCurScreen = true
            hideAllWidget()
        }

    }




}