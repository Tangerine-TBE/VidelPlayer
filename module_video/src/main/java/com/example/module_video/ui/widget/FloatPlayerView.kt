package com.example.module_video.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * 适配了悬浮窗的view
 * Created by guoshuyu on 2017/12/25.
 */
class FloatPlayerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
   lateinit var videoPlayer: FloatingVideo
   fun init() {
        videoPlayer = FloatingVideo(context)
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layoutParams.gravity = Gravity.CENTER
        addView(videoPlayer, layoutParams)


        //增加封面
        /*ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(R.mipmap.xxx1);
        videoPlayer.setThumbImageView(imageView);*/

        //是否可以滑动调整
        videoPlayer.setIsTouchWiget(true)
    }

    fun setUir(url:String){
        videoPlayer.setUp(url, true, "测试视频")
    }

    fun onPause() {
        videoPlayer.currentPlayer.onVideoPause()
    }

    fun onResume() {
        videoPlayer.currentPlayer.onVideoResume()
    }
}