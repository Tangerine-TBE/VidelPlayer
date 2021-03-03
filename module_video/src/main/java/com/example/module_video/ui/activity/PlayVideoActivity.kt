package com.example.module_video.ui.activity

import android.annotation.TargetApi
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import androidx.core.view.ViewCompat
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.gsonHelper
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayVideoBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.SwitchVideoModel
import com.example.module_video.viewmode.PlayVideoViewModel
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.GSYStateUiListener
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

class PlayVideoActivity : BaseVmViewActivity<ActivityPlayVideoBinding, PlayVideoViewModel>() {
    private lateinit var orientationUtils: OrientationUtils
    private var isTransition = false

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"

        const val VIDEO_MSG="VIDEO_MSG"
    }


    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        MyStatusBarUtil.setFullScreen(this,hasFocus)
    }


    override fun getLayoutView(): Int = R.layout.activity_play_video
    override fun getViewModelClass(): Class<PlayVideoViewModel> {
        return PlayVideoViewModel::class.java
    }

    override fun initView() {
        binding.apply {
            //设置旋转
            orientationUtils = OrientationUtils(this@PlayVideoActivity, videoPlayer)
            val videoMsg = intent.getStringExtra(VIDEO_MSG)
            isTransition = intent.getBooleanExtra(TRANSITION, false)
            gsonHelper<MediaInformation>(videoMsg)?.apply {
                val list = arrayListOf(
                        SwitchVideoModel(
                                "$uri"
                        )
                )
                videoPlayer?.apply {
                    setUp(list, true, "$name")
                    //增加title
                    titleTextView.visibility = View.VISIBLE
                    //设置返回键
                    backButton.visibility = View.VISIBLE

                    //增加封面
                 /*   val imageView = ImageView(this@PlayVideoActivity)
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    imageView.setImageBitmap(bitmap)
                    thumbImageView = imageView*/
                    //是否可以滑动调整
                    setIsTouchWiget(true)
                    //设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
                    fullscreenButton.setOnClickListener { orientationUtils.resolveByClick() }
                    dismissControlTime = 5500
                }
            }


        }
        //过渡动画
        initTransition()
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
        super.onPause()
        binding.videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        binding.videoPlayer.onVideoResume()

    }

    override fun release() {
        orientationUtils.releaseListener()
    }

    override fun onBackPressed() {
        //先返回正常状态
        if (orientationUtils.screenType == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            binding.videoPlayer.fullscreenButton.performClick()
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