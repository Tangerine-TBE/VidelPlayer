package com.example.module_video.ui.widget

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.io.IOException

/**
 * @author wujinming QQ:1245074510
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget
 * @class describe
 * @time 2021/3/1 16:21:20
 * @class describe
 */
class IjkVideoView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var mMediaPlayer: IMediaPlayer? = null //视频控制类
    private var mVideoPlayerListener //自定义监听器
            : VideoPlayerListener? = null
    private var mSurfaceView //播放视图
            : SurfaceView? = null
    private var mPath = "" //视频文件地址

    init {
        initVideoView(context)
    }

    abstract class VideoPlayerListener : IMediaPlayer.OnPreparedListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnErrorListener

    private fun initVideoView(context: Context) {
        isFocusable = true
    }

    fun setPath(path: String) {
        if (TextUtils.equals("", mPath)) {
            mPath = path
            initSurfaceView()
        } else {
            mPath = path
            loadVideo()
        }
    }

    private fun initSurfaceView() {
        mSurfaceView = SurfaceView(context)
            .apply {
                holder.addCallback(LmnSurfaceCallback())
                val layoutParams =
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
                mSurfaceView?.layoutParams = layoutParams
                this@IjkVideoView.addView(this)
            }

    }

    //surfaceView的监听器
    private inner class LmnSurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {}
        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            loadVideo()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {}
    }

    //加载视频
    private fun loadVideo() {
        if (mMediaPlayer != null) {
            mMediaPlayer?.stop()
            mMediaPlayer?.release()
        }

        val ijkMediaPlayer = IjkMediaPlayer()
        mMediaPlayer = ijkMediaPlayer.apply {
            if (mVideoPlayerListener != null) {
                setOnPreparedListener(mVideoPlayerListener)
                setOnErrorListener(mVideoPlayerListener)
            }
            try {
                dataSource = mPath
            } catch (e: IOException) {
                e.printStackTrace()
            }
            setDisplay(mSurfaceView?.holder)
            prepareAsync()
        }

    }

    fun setListener(listener: VideoPlayerListener?) {
        mVideoPlayerListener = listener
        mMediaPlayer?.setOnPreparedListener(listener)
    }

    val isPlaying: Boolean
        get() = mMediaPlayer?.isPlaying?:false

    fun start() {
        mMediaPlayer?.start()
    }

    fun pause() {
            mMediaPlayer?.pause()
    }

    fun stop() {
            mMediaPlayer?.stop()
    }

    fun reset() {
        mMediaPlayer?.reset()

    }

    fun release() {
        mMediaPlayer?.apply {
            reset()
            release()
            mMediaPlayer = null
        }
    }
}