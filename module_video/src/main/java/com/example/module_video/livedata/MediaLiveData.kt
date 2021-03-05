package com.example.module_video.livedata

import android.database.ContentObserver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseLiveData
import com.example.module_base.utils.LogUtils
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.ValueMediaType
import com.example.module_video.utils.MediaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @name VidelPlayer
 * @class name：com.example.module_video
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/5 11:28:51
 * @class describe
 */
object MediaLiveData : BaseLiveData<ValueMediaType>(){

    private val mVideoObserver by lazy {
        VideoObserver()
    }

    private val mAudioObserver by lazy {
        AudioObserver()
    }

    fun getMediaResource() {
        mScope.launch(Dispatchers.IO) {
            val video = async {
                MediaUtil.getAllVideo()
            }
            val audio = async {
                MediaUtil.getAllAudio()
            }
            postValue(ValueMediaType(video.await(),audio.await()))
        }
    }


    override fun onActive() {
        super.onActive()
        BaseApplication.application.contentResolver.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,true,mVideoObserver)
        BaseApplication.application.contentResolver.registerContentObserver(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,true,mAudioObserver)
    }

    override fun onInactive() {
        super.onInactive()
      //  BaseApplication.application.contentResolver.unregisterContentObserver(mVideoObserver)
      //  BaseApplication.application.contentResolver.unregisterContentObserver(mAudioObserver)
    }


    class VideoObserver:ContentObserver(BaseApplication.mHandler){
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            getMediaResource()
            LogUtils.i("--VideoObserver--------onChange---------------------")
        }
    }

    class AudioObserver:ContentObserver(BaseApplication.mHandler){
        override fun onChange(selfChange: Boolean, uri: Uri?) {
            getMediaResource()
            LogUtils.i("--AudioObserver--------onChange---------------------")
        }
    }

}