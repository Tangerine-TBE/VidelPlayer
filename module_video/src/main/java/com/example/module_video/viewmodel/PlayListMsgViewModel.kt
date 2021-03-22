package com.example.module_video.viewmodel

import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.gsonHelper
import com.example.module_video.domain.MediaDataBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListMsgBean
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


    val selectAllState by lazy {
        MutableLiveData(false)
    }

    fun setSelectAllState(state:Boolean){
        selectAllState.value=state
    }

    fun getEditAction_(): Boolean = editAction.value ?: false

    fun setEditAction(action: Boolean) {
        editAction.value = action
    }


    val selectItems by lazy {
        MutableLiveData<HashSet<MediaInformation>>()
    }



    fun setSelectItems(listBean: HashSet<MediaInformation>) {
        selectItems.value = listBean
    }


    fun getPlayListMsg(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playListMsg.postValue( DbHelper.queryListFile<PlayListMsgBean>("name=?", name)[0])
        }
    }


    fun deleteListMsg(name: String, list: MutableList<Long>) {
        viewModelScope.launch(Dispatchers.IO) {
            DbHelper.queryListFile<PlayListMsgBean>("name=?", name)[0]?.let {
                gsonHelper<MediaDataBean>(it.media)?.apply {
                    idList?.let {
                        idList?.removeAll(list)
                          withContext(Dispatchers.IO) {
                            doDelete(name, idList)
                        }
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