package com.example.module_video.viewmodel

import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.gsonHelper
import com.example.module_video.domain.MediaDataBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListMsgBean
import com.example.module_video.livedata.PlayListLiveData
import com.example.module_video.repository.db.DbHelper
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        MutableLiveData<HashSet<MediaInformation>>()
    }

    val addListState by lazy {
        MutableLiveData<Boolean>()
    }


    val searchMediaList by lazy {
        MutableLiveData<MutableList<MediaInformation>>()
    }


    fun setSelectVideoList(list:HashSet<MediaInformation>){
        selectVideoList.value=list
    }


    fun getSearchList(name: String, list: MutableList<MediaInformation>) {
        val searchList = ArrayList<MediaInformation>()
        list.forEach {
            if (it.name.toLowerCase().contains(name.toLowerCase())) {
                searchList.add(it)
            }
            searchMediaList.value=searchList
        }
    }


    fun addVideoList(name:String,list:MutableList<Long>){
        viewModelScope.launch(Dispatchers.IO) {
            DbHelper.queryListFile<PlayListMsgBean>("name=?", name)[0]?.let {
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


    val searchPlayList by lazy {
        MutableLiveData<MutableList<PlayListMsgBean>>()
    }

    fun queryPlayList(name:String){
        viewModelScope.launch (Dispatchers.IO){
            searchPlayList.postValue(DbHelper.queryListFile("name=?",name))
        }
    }




    fun addNewPlayList(msgBean:PlayListMsgBean){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                DbHelper.addListFile<PlayListMsgBean>("name=?",msgBean.name,msgBean){null}
            }
            PlayListLiveData.getPlayList()
        }
    }

    private fun doAdd(name: String,list: MutableList<Long>) =
        DbHelper.addListFile<PlayListMsgBean>("name=?", name, null) {
            contentValuesOf("media" to Gson().toJson(MediaDataBean(list)))
        }



}