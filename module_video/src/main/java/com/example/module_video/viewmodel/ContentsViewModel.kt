package com.example.module_video.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_video.domain.MediaInformation
import com.example.module_video.utils.MediaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 15:24:42
 * @class describe
 */
class ContentsViewModel:BaseViewModel() {

    val videoList by lazy {
        MutableLiveData<MutableList<MediaInformation>>()
    }

    fun getMediaFile(type:Int){
        viewModelScope.launch {
            val video = async(Dispatchers.IO) {
                MediaUtil.getAllVideo()
            }

            val audio = async(Dispatchers.IO) {
                MediaUtil.getAllAudio()
            }

            when (type) {
                0 -> {
                    val list=ArrayList<MediaInformation>()
                    list.addAll(video.await())
                    list.addAll(audio.await())
                    videoList.value=list
                }
                1-> videoList.value =video.await()
                2 -> videoList.value = audio.await()
            }
        }
    }
}