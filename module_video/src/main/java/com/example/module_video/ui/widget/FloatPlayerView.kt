package com.example.module_video.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.module_base.utils.LogUtils
import com.example.module_video.domain.MediaInformation

/**
 * 适配了悬浮窗的view
 * Created by guoshuyu on 2017/12/25.
 */
class FloatPlayerView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
   private var videoPlayer=FloatingVideo(context)

    init {
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
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


    fun setCurrentResource(list:List<MediaInformation>,position:Int,currentPositionWhenPlaying:Int){
        videoPlayer.setUp(list, position,false, list[position].name)
        videoPlayer.seekOnStart=currentPositionWhenPlaying.toLong()
        videoPlayer.startPlayLogic()
        LogUtils.i("--setCurrentResource-------------------------$currentPositionWhenPlaying.toLong()-----------")
    }

    fun onPause() {
        videoPlayer.currentPlayer.onVideoPause()
    }

    fun onResume() {
        videoPlayer.currentPlayer.onVideoResume()
    }
}