package com.example.module_video.viewmode

import android.net.Uri
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
import com.example.module_video.utils.FileUtil
import com.example.module_video.utils.MediaUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/11 9:58:21
 * @class describe
 */
class PlayListMsgViewModel : BaseViewModel() {

    val playListMsg by lazy {
        MutableLiveData<PlayListMsgBean>()
    }


    val editAction by lazy {
        MutableLiveData(false)
    }

    val selectAllAction by lazy {
        MutableLiveData(false)
    }

    fun getEditAction_(): Boolean = editAction.value ?: false

    fun getSelectAllAction_(): Boolean = selectAllAction.value ?: false

    fun setEditAction(action: Boolean) {
        editAction.value = action
    }

    fun setSelectAllAction(action: Boolean){
        selectAllAction.value= action
    }


    val selectItems by lazy {
        MutableLiveData<MutableList<MediaInformation>>()
    }



    fun setSelectItems(listBean: MutableList<MediaInformation>) {
        selectItems.value = listBean
    }


    fun getPlayListMsg(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playListMsg.postValue(DbHelper.queryListFile("name=?", name))
        }
    }


    fun deleteListMsg(name: String, list: MutableList<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            DbHelper.queryListFile<PlayListMsgBean>("name=?", name)?.let {
                gsonHelper<MediaDataBean>(it.media)?.apply {
                    idList?.let {
                        idList?.removeAll(list)
                          withContext(Dispatchers.IO) {
                            doDelete(name, idList)
                        }
                       // PlayListLiveData.getPlayList()
                        getPlayListMsg(name)
                    }
                }
            }
        }
    }

    private fun doDelete(name: String, list: MutableList<Long>) =
        DbHelper.addListFile<PlayListMsgBean>("name=?", name, null) {
            contentValuesOf("media" to Gson().toJson(MediaDataBean(list)))
        }

}