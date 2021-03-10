package com.example.module_video.ui.activity

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.transition.Transition
import android.view.View
import android.view.animation.BounceInterpolator
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.gsonHelper
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayVideoBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.SwitchVideoModel
import com.example.module_video.ui.widget.FloatPlayerView
import com.example.module_video.utils.floatUtil.FloatWindow
import com.example.module_video.utils.floatUtil.MoveType
import com.example.module_video.utils.floatUtil.Screen
import com.example.module_video.viewmode.PlayVideoViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.utils.GSYVideoType
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer

class PlayVideoActivity : BaseVmViewActivity<ActivityPlayVideoBinding, PlayVideoViewModel>() {
    private lateinit var orientationUtils: OrientationUtils
    private var isTransition = false
    private var isPlay = false
    private var isPause = false
    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
        const val VIDEO_MSG = "VIDEO_MSG"

         fun toPlayVideo(activity: FragmentActivity?, view: View, msg:String){
             activity?.let {
                 val intent = Intent(activity, PlayVideoActivity::class.java)
                 intent.putExtra(TRANSITION, true)
                 intent.putExtra(VIDEO_MSG, msg)
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                     val pair: Pair<View, String> = Pair<View, String>(view, IMG_TRANSITION)
                     val activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(
                         it, pair
                     )
                     ActivityCompat.startActivity(it, intent, activityOptions.toBundle())
                 } else {
                    it.startActivity(intent)
                     it.overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
                 }
             }

        }
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        MyStatusBarUtil.fullStateWindow(hasFocus, this)
    }

    override fun getLayoutView(): Int = R.layout.activity_play_video
    override fun getViewModelClass(): Class<PlayVideoViewModel> {
        return PlayVideoViewModel::class.java
    }

    override fun initView() {
        binding.apply {
            val showType = GSYVideoType.getShowType()
            LogUtils.i("----GSYVideoType-------$showType------------")
            initVideoPlayer()
        }
        //过渡动画
        initTransition()
    }

    private fun ActivityPlayVideoBinding.initVideoPlayer() {
        //设置旋转
        orientationUtils = OrientationUtils(this@PlayVideoActivity, videoPlayer)
        val videoMsg = intent.getStringExtra(VIDEO_MSG)
        isTransition = intent.getBooleanExtra(TRANSITION, false)
        gsonHelper<MediaInformation>(videoMsg)?.apply {
            setFloatWindow("$uri")
            val list = arrayListOf(
                SwitchVideoModel(
                    "$uri"
                )
            )
            GSYVideoOptionBuilder()
                    .setIsTouchWiget(true)
                    .setRotateViewAuto(true)
                    .setRotateWithSystem(true)
                    .setLockLand(false)
                    .setAutoFullWithSize(false)
                    .setShowFullAnimation(false)
                    .setNeedLockFull(true)
                    .setVideoAllCallBack(object : GSYSampleCallBack() {
                        override fun onStartPrepared(url: String?, vararg objects: Any?) {
                            super.onStartPrepared(url, *objects)
                            //开始播放了才能旋转和全屏
                            orientationUtils.isRotateWithSystem = false

                            isPlay = true
                        }

                        override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
                            super.onEnterFullscreen(url, *objects)

                        }

                        override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                            super.onQuitFullscreen(url, *objects)
                            orientationUtils.backToProtVideo()
                        }
                    })
                    .setLockClickListener { view, lock ->
                        //配合下方的onConfigurationChanged
                        orientationUtils.isEnable = !lock
                    }
                    .build(videoPlayer)

            videoPlayer.setUp(list, true, "$name")
            videoPlayer.seekTo(100000)
        }
    }



    /**
     * orientationUtils 和  detailPlayer.onConfigurationChanged 方法是用于触发屏幕旋转的
     */
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            binding.videoPlayer.onConfigurationChanged(
                this,
                newConfig,
                orientationUtils,
                true,
                true
            )
            LogUtils.i("---onConfigurationChanged-------------------${orientationUtils.screenType}--------------")
        }
    }


    private fun setFloatWindow(path:String){
       binding.videoPlayer.mShowSmallWindow.setOnClickListener {
            if (FloatWindow.get() == null) {
                val floatPlayerView = FloatPlayerView(applicationContext)
                floatPlayerView.setUir(path)
                FloatWindow.with(applicationContext)
                    .setView(floatPlayerView)
                    .setWidth(Screen.width, 0.4f)
                    .setHeight(Screen.width, 0.4f)
                    .setX(Screen.width, 0.8f)
                    .setY(Screen.height, 0.3f)
                    .setMoveType(MoveType.slide)
                    .setFilter(false)
                    .setMoveStyle(500, BounceInterpolator())
                    .build()
                FloatWindow.get().show()
            }
        }
    }

    override fun initEvent() {
        binding.apply {
            videoPlayer.apply {


                backButton.setOnClickListener {
                    onBackPressed()
                }

                setGSYStateUiListener {
                    when (it) {
                       // GSYVideoView.CURRENT_STATE_PLAYING ->btPlay.setImageResource(R.mipmap.icon_video_play)
                      //  GSYVideoView.CURRENT_STATE_PAUSE ->btPlay.setImageResource(R.mipmap.icon_video_pause)
                    }
                }
            }
        }
    }


    override fun onPause() {
        binding.videoPlayer.onVideoPause()
        super.onPause()
        isPause = true

    }

    override fun onResume() {
        binding.videoPlayer.onVideoResume()
        super.onResume()
        isPause = false


    }

    override fun release() {
        if (isPlay) {
            getCurPlay().release()
        }
        orientationUtils.releaseListener()
    }

    private fun getCurPlay(): GSYVideoPlayer {
        return if (binding.videoPlayer.fullWindowPlayer != null) {
            binding.videoPlayer.fullWindowPlayer
        } else  binding.videoPlayer
    }

    override fun onBackPressed() {
        orientationUtils.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        //释放所有
        binding.videoPlayer.setVideoAllCallBack(null)
        GSYVideoManager.releaseAllVideos()
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.onBackPressed()
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out)
            }, 500)
        }
    }


    private fun initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(
                binding.videoPlayer,
                IMG_TRANSITION
            )
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            binding.videoPlayer.startPlayLogic()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener(): Boolean {
        val transition = window.sharedElementEnterTransition
        if (transition != null) {
            transition.addListener(object : Transition.TransitionListener {
                override fun onTransitionStart(transition: Transition?) {
                }

                override fun onTransitionEnd(transition: Transition?) {
                    binding.videoPlayer.startPlayLogic()
                    transition?.removeListener(this)
                }

                override fun onTransitionCancel(transition: Transition?) {
                }

                override fun onTransitionPause(transition: Transition?) {
                }

                override fun onTransitionResume(transition: Transition?) {
                }
            })
            return true
        }
        return false
    }

}