package com.example.module_video.ui.activity

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.module_base.base.BaseVmViewActivity
import com.example.module_video.R
import com.example.module_video.databinding.ActivityPlayVideoBinding
import com.example.module_video.ui.widget.IjkVideoView
import com.example.module_video.viewmode.PlayVideoViewModel
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

class PlayVideoActivity : BaseVmViewActivity<ActivityPlayVideoBinding,PlayVideoViewModel>() {

    override fun getLayoutView(): Int =R.layout.activity_play_video

    override fun getViewModelClass(): Class<PlayVideoViewModel> {
        return PlayVideoViewModel::class.java
    }

    override fun initView() {
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");


        binding.apply {
            mVideoView.setListener(object : IjkVideoView.VideoPlayerListener(){
                override fun onPrepared(mp: IMediaPlayer?) {
                    //播放成功处理
                    mp?.start();
                }

                override fun onCompletion(mp: IMediaPlayer?) {

                }

                override fun onError(mp: IMediaPlayer?, p1: Int, p2: Int): Boolean {
                    return true
                }

            })
            //路径
            mVideoView.setPath("http://ips.ifeng.com/video19.ifeng.com/video09/2014/06/16/1989823-102-086-0009.mp4");
            mVideoView.start();

        }



    }
}