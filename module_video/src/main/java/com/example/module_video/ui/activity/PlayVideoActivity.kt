package com.example.module_video.ui.activity

import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.transition.Transition
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentActivity
import com.example.module_ad.utils.Contents
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_base.utils.*
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayVideoBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListBean
import com.example.module_video.ui.widget.FloatPlayerView
import com.example.module_video.ui.widget.FloatingVideo
import com.example.module_video.ui.widget.ScaleImage
import com.example.module_video.ui.widget.popup.PlayErrorPopup
import com.example.module_video.utils.Constants
import com.example.module_video.utils.WindowUtil
import com.example.module_video.viewmodel.PlayVideoViewModel
import com.google.gson.Gson
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.anim.AppFloatDefaultAnimator
import com.lzf.easyfloat.anim.DefaultAnimator
import com.lzf.easyfloat.enums.ShowPattern
import com.lzf.easyfloat.interfaces.OnPermissionResult
import com.lzf.easyfloat.permission.PermissionUtils
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoView

import tv.danmaku.ijk.media.exo2.Exo2PlayerManager


class PlayVideoActivity : BaseVmViewActivity<ActivityPlayVideoBinding, PlayVideoViewModel>() {
    private lateinit var orientationUtils: OrientationUtils
    private var isTransition = false
    private var isPlay = false
    private var isPause = false
    private var playPosition=0
    private var mChannel=0

    private val mPlayErrorPopup by lazy {
        PlayErrorPopup(this)
    }


    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
        const val VIDEO_MSG = "VIDEO_MSG"
        const val PLAY_POSITION = "PLAY_POSITION"
        const val FROM_CHANNEL = "FROM_CHANNEL"
        const val PROGRESS = "PROGRESS"

        fun toPlayVideo(activity: FragmentActivity?, position: Int, channel: Int = 0) {

            toOtherActivity<PlayVideoActivity>(activity){
                putExtra(TRANSITION, true)
                putExtra(PLAY_POSITION, position)
                putExtra(FROM_CHANNEL, channel)
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.P)
    override fun setFullScreenWindow() {
            if (WindowUtil.hasNotch(this) || WindowUtil.hasNotchAtHuawei(this) || WindowUtil.hasNotchInOppo(this) || WindowUtil.hasNotchInScreenAtVoio(this)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val lp = window.attributes
                    lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
                    window.attributes = lp
                }
            } else {
                MyStatusBarUtil.setColor(this,Color.TRANSPARENT)
                MyStatusBarUtil.fullStateWindow(true, this)
        }
    }




    override fun getLayoutView(): Int = R.layout.activity_play_video
    override fun getViewModelClass(): Class<PlayVideoViewModel> {
        return PlayVideoViewModel::class.java
    }

    override fun initView() {
        sp.putBoolean(Contents.NO_BACK, true)
        if (EasyFloat.appFloatIsShow(FloatingVideo.SMALL_TAG)) {
            EasyFloat.dismissAppFloat(FloatingVideo.SMALL_TAG)
        }
        binding.apply {
            initVideoPlayer()
            //第三方打开
            byOtherApp()

            videoPlayer.startPlayLogic()
        }
    }

    private fun ActivityPlayVideoBinding.byOtherApp() {
        try {
            if (intent.action == Intent.ACTION_VIEW) {
                intent.data?.apply {
                    path?.let {
                        val index = it.lastIndexOf("/")
                        val name = it.substring(index + 1)
                        videoPlayer.setUp(arrayListOf(MediaInformation(uri = this.toString(), name = name)), 0, true, name)
                    }
                }
            }
        }catch (e:Exception){
            showToast("打开视频异常")
            finish()
        }
    }


    private fun ActivityPlayVideoBinding.initVideoPlayer() {
        //设置旋转
        orientationUtils = OrientationUtils(this@PlayVideoActivity, videoPlayer)
        orientationUtils.isEnable=false
        playPosition=intent.getIntExtra(PLAY_POSITION, 0)
        isTransition = intent.getBooleanExtra(TRANSITION, false)
        mChannel = intent.getIntExtra(FROM_CHANNEL, 0)
        GSYVideoOptionBuilder()
                .setIsTouchWiget(true)
                .setNeedLockFull(true)
                .setRotateWithSystem(true)
                .setAutoFullWithSize(false)
                .setRotateViewAuto(false)
                .setShowFullAnimation(false)
                .setVideoAllCallBack(object : GSYSampleCallBack() {
                    override fun onPrepared(url: String?, vararg objects: Any?) {
                        super.onPrepared(url, *objects)
                        //开始播放了才能旋转和全屏
                        orientationUtils.isRotateWithSystem = false
                        isPlay = true
                        orientationUtils.isEnable = !mLock
                    }

                    override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                        super.onQuitFullscreen(url, *objects)
                        orientationUtils.backToProtVideo()
                    }
                })
                .setLockClickListener { view, lock ->
                    //配合下方的onConfigurationChanged
                    mLock=lock
                    orientationUtils.isEnable = !lock
                }
                .build(videoPlayer)

        val videoMsg = sp.getString(Constants.SP_PLAY_LIST)
        gsonHelper<PlayListBean>(videoMsg)?.apply {
            if (list.size>playPosition){
                try {
                    if (mChannel == 1) {
                        videoPlayer.seekOnStart=intent.getIntExtra(PROGRESS, 0).toLong()
                    }
                    videoPlayer.setUp(list, playPosition, true, list[playPosition].name)
                }catch (e:Exception){
                    showToast("打开视频异常")
                    finish()
                }
            }
        }
    }

    private var mLock=false


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
        }
    }

    override fun initEvent() {
        binding.apply {
            videoPlayer.apply {
                backButton.setOnClickListener {
                    onBackPressed()
                }


                setGSYStateUiListener {
                    when(it){
                        GSYVideoView.CURRENT_STATE_ERROR -> {
                            errorSelectCore()
                        }
                    }
                }

                openSmallWindow {
                    checkPermission()
                }
            }
            mPlayErrorPopup.doSure {
                finish()
            }

        }
    }

    private var errorCount=0
    private fun errorSelectCore(){
        sp.apply {
           val type = when (getInt(Constants.SP_CORE_TYPE)) {
               0 -> {
                   PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
                   1
               }
               1 -> {
                   PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
                   2
               }
               2 -> {
                   PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
                   0
               }
               else->{
                   PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
                   0
               }
           }
            putInt(Constants.SP_CORE_TYPE,type)
            errorCount++
           // showToast("播放失败，尝试切换内核")
            errorCount = if (errorCount > 2) {
                mPlayErrorPopup.showPopupView(binding.videoPlayer)
                0
            } else {
                binding.videoPlayer.startPlayLogic()
                0
            }
        }
    }


    private  fun checkPermission(){
        if (PermissionUtils.checkPermission(this)) {
            showSmallWindow()
        } else {
            AlertDialog.Builder(this)
                    .setMessage("使用浮窗功能，需要您授权悬浮窗权限。")
                    .setPositiveButton("去开启") { _, _ ->
                        PermissionUtils.requestPermission(this, object : OnPermissionResult {
                            override fun permissionResult(isOpen: Boolean) {
                            }
                        })
                    }
                    .setNegativeButton("取消") { _, _ -> }
                    .show()

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
        sp.putBoolean(Contents.NO_BACK, false)
        if (isPlay) {
            getCurPlay().release()
        }
        orientationUtils.releaseListener()
        mPlayErrorPopup.dismiss()
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
                finish()
        }
    }



   private fun  showSmallWindow(){
        EasyFloat.with(this)
                // 设置浮窗xml布局文件，并可设置详细信息
                .setLayout(R.layout.layout_small_float) {
                    val smallVideoPlayer= FloatPlayerView(this)
                    val playContainer=it.findViewById<FrameLayout>(R.id.container)
                    val content = it.findViewById<RelativeLayout>(R.id.rlContent)
                    playContainer.addView(smallVideoPlayer)
                    binding.videoPlayer.apply {
                        smallVideoPlayer.setCurrentResource(getPlayList(), getCurrentPosition(), getProgress())
                    }
                    val params = content.layoutParams as FrameLayout.LayoutParams
                    val scaleImage = it.findViewById<ScaleImage>(R.id.ivScale)
                    scaleImage.setOnScaledListener(object : ScaleImage.OnScaledListener {
                        override fun onScaled(x: Float, y: Float, event: MotionEvent) {
                            val screenWidth = SizeUtils.getScreenWidth(BaseApplication.application)
                            val screenHeight = SizeUtils.getScreenHeight(BaseApplication.application)
                            var scaleWith = params.width + x.toInt()
                            var scaleHeight = params.height + y.toInt()

                            if (scaleWith < screenWidth / 3) {
                                scaleWith = screenWidth / 3
                            }
                            if (screenHeight < screenHeight / 5) {
                                scaleHeight = screenHeight / 5
                            }
                            params.width = scaleWith.coerceAtMost(screenWidth)
                            params.height = scaleHeight.coerceAtMost(screenHeight)
                            content.layoutParams = params
                            LogUtils.i("---OnScaledListener-----${params.width}--------------${params.height}------------------")
                        }
                    })
                    smallVideoPlayer.videoPlayer.showScaleIcon { state->
                        scaleImage.visibility=if (state) View.VISIBLE else View.GONE
                    }
                }
                // 设置浮窗显示类型，默认只在当前Activity显示，可选一直显示、仅前台显示、仅后台显示
                .setShowPattern(ShowPattern.ALL_TIME)
                // 设置浮窗的标签，用于区分多个浮窗
                .setTag(FloatingVideo.SMALL_TAG)
                // 设置浮窗是否可拖拽，默认可拖拽
                .setDragEnable(true)
                // 设置浮窗固定坐标，ps：设置固定坐标，Gravity属性和offset属性将无效
                .setLocation(100, 200)
                // 设置浮窗的对齐方式和坐标偏移量
                .setGravity(Gravity.END or Gravity.CENTER_VERTICAL, 0, 200)
                // 设置宽高是否充满父布局，直接在xml设置match_parent属性无效
                .setMatchParent(widthMatch = false, heightMatch = false)
                // 设置Activity浮窗的出入动画，可自定义，实现相应接口即可（策略模式），无需动画直接设置为null
                .setAnimator(DefaultAnimator())
                // 设置系统浮窗的出入动画，使用同上
                .setAppFloatAnimator(AppFloatDefaultAnimator())
                // 设置系统浮窗的不需要显示的页面
               // .setFilter(MainActivity::class.java, SecondActivity::class.java)
                // 浮窗的一些状态回调，如：创建结果、显示、隐藏、销毁、touchEvent、拖拽过程、拖拽结束。
                // ps：通过Kotlin DSL实现的回调，可以按需复写方法，用到哪个写哪个
                .registerCallback {
                    createResult { isCreated, msg, view ->  }
                    show {       }
                    hide {  }
                    dismiss {  }
                    touchEvent { view, motionEvent ->  }
                    drag { view, motionEvent ->  }
                    dragEnd {  }
                }
                // 创建浮窗（这是关键哦😂）
                .show()
               finish()
    }

}