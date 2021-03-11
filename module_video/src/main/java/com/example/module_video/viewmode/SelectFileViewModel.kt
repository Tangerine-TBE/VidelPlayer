package com.example.module_video.viewmode

import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.gsonHelper
import com.example.module_video.domain.MediaDataBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListMsgBean
import com.example.module_video.livedata.PlayListLiveData
import com.example.module_video.repository.db.DbHelper
import com.example.module_video.utils.MediaUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/11 10:19:26
 * @class describe
 */
class SelectFileViewModel:BaseViewModel() {


    val selectVideoList by lazy {
        MutableLiveData<MutableList<MediaInformation>>()
    }

    val addListState by lazy {
        MutableLiveData<Boolean>()
    }



    fun setSelectVideoList(list:MutableList<MediaInformation>){
        selectVideoList.value=list
    }


    fun addVideoList(name:String,list:MutableList<Long>){
        viewModelScope.launch(Dispatchers.IO) {
            DbHelper.queryListFile<PlayListMsgBean>("name=?", name)?.let {
                gsonHelper<MediaDataBean>(it.media)?.apply {
                   val addList = if (idList != null) {
                        list.removeAll(idList)
                        idList.addAll(list)
                        idList
                    } else {
                        list
                    }
                    addListState.postValue(doAdd(name,addList))
                    PlayListLiveData.getPlayList()
                }

             }

        }
    }

    private fun doAdd(name: String,list: MutableList<Long>) =
        DbHelper.addListFile<PlayListMsgBean>("name=?", name, null) {
            contentValuesOf("media" to Gson().toJson(MediaDataBean(list)))
        }
}