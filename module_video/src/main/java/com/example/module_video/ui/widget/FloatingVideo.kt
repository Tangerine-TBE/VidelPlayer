package com.example.module_video.ui.widget

import android.app.AlertDialog
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.util.AttributeSet
import android.view.WindowManager
import com.example.module_video.R
import com.shuyu.gsyvideoplayer.utils.NetworkUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import java.util.*

/**
 * 多窗体下的悬浮窗页面支持Video
 * Created by shuyu on 2017/12/25.
 */
class FloatingVideo : StandardGSYVideoPlayer {
    protected var mDismissControlViewTimer: Timer? = null

    /**
     * 1.5.0开始加入，如果需要不同布局区分功能，需要重载
     */
    constructor(context: Context?, fullFlag: Boolean?) : super(context, fullFlag) {}
    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

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
    }

    override fun getLayoutId(): Int {
        return R.layout.layout_floating_video
    }

    override fun startPrepare() {
        if (gsyVideoManager.listener() != null) {
            gsyVideoManager.listener().onCompletion()
        }
        gsyVideoManager.setListener(this)
        gsyVideoManager.playTag = mPlayTag
        gsyVideoManager.playPosition = mPlayPosition
        mAudioManager.requestAudioFocus(
            onAudioFocusChangeListener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
        )
        //((Activity) getActivityContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mBackUpPlayingBufferState = -1
        gsyVideoManager.prepare(mUrl, mMapHeadData, mLooping, mSpeed, mCache, mCachePath, null)
        setStateAndUi(CURRENT_STATE_PREPAREING)
    }

    override fun onAutoCompletion() {
        setStateAndUi(CURRENT_STATE_AUTO_COMPLETE)
        mSaveChangeViewTIme = 0
        if (mTextureViewContainer.childCount > 0) {
            mTextureViewContainer.removeAllViews()
        }
        if (!mIfCurrentIsFullscreen) gsyVideoManager.setLastListener(null)
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener)
        //((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        releaseNetWorkState()
        if (mVideoAllCallBack != null && isCurrentMediaListener) {
            mVideoAllCallBack.onAutoComplete(mOriginUrl, mTitle, this)
        }
    }

    override fun onCompletion() {
        //make me normal first
        setStateAndUi(CURRENT_STATE_NORMAL)
        mSaveChangeViewTIme = 0
        if (mTextureViewContainer.childCount > 0) {
            mTextureViewContainer.removeAllViews()
        }
        if (!mIfCurrentIsFullscreen) {
            gsyVideoManager.setListener(null)
            gsyVideoManager.setLastListener(null)
        }
        gsyVideoManager.currentVideoHeight = 0
        gsyVideoManager.currentVideoWidth = 0
        mAudioManager.abandonAudioFocus(onAudioFocusChangeListener)
        //((Activity) getActivityContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        releaseNetWorkState()
    }

    override fun getActivityContext(): Context {
        return context
    }

    override fun isShowNetConfirm(): Boolean {
        return false
    }

    override fun showWifiDialog() {
        if (!NetworkUtils.isAvailable(mContext)) {
            //Toast.makeText(mContext, getResources().getString(R.string.no_net), Toast.LENGTH_LONG).show();
            startPlayLogic()
            return
        }
        val builder = AlertDialog.Builder(activityContext)
        builder.setMessage(resources.getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi))
        builder.setPositiveButton(resources.getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_confirm)) { dialog, which ->
            dialog.dismiss()
            startPlayLogic()
        }
        builder.setNegativeButton(resources.getString(com.shuyu.gsyvideoplayer.R.string.tips_not_wifi_cancel)) { dialog, which -> dialog.dismiss() }
        val alertDialog = builder.create()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
        } else {
            alertDialog.window!!.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
        }
        alertDialog.show()
    }
}