package com.example.module_video.viewmode

import android.net.Uri
import androidx.core.content.contentValuesOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.PlayListMsgBean
import com.example.module_video.livedata.PlayListLiveData
import com.example.module_video.repository.db.DbHelper
import com.example.module_video.utils.FileUtil
import com.example.module_video.utils.GeneralState
import com.example.module_video.utils.MediaState
import com.example.module_video.utils.MediaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:56:06
 * @class describe
 */
class MediaViewModel:BaseViewModel() {

    //media
    val currentPosition by lazy {
        MutableLiveData(0)
    }

    val editAction by lazy {
        MutableLiveData(false)
    }

    val searchAction by lazy {
        MutableLiveData(false)
    }


    val selectItemList by lazy {
        MutableLiveData<MutableList<MediaInformation>>()
    }

    val deleteFileState by lazy {
        MutableLiveData<GeneralState>()
    }

    fun getEditAction_():Boolean=editAction.value?:false

    fun getSearchAction_():Boolean=searchAction.value?:false

    fun getCurrentPosition_()=currentPosition.value

    fun setEditAction(action:Boolean){
        editAction.value=action
    }

    fun setSearchAction(action:Boolean){
        searchAction.value=action
    }

    fun setCurrentPosition(position:Int){
        currentPosition.value=position
    }


    fun setSelectItemList(list:MutableList<MediaInformation>){
        selectItemList.value=list
    }



     fun reNameToMediaFile(name:String,msg:MediaInformation,state:MediaState){
        viewModelScope.launch {
            var destPath: String
            val updateState = withContext(Dispatchers.IO) {
                LogUtils.i("-reNameToMediaFile---  ${ msg.name}  ------------ ${ msg.path} --------------")
                val typeIndex = msg.path.lastIndexOf(".")
                val pathIndex = msg.path.lastIndexOf("/")

                val realType = msg.path.substring(typeIndex)
                val realPath = msg.path.substring(0, pathIndex)

                destPath= "${realPath}/${name}${realType}"
                File(msg.path).renameTo(File(destPath))
            }
            withContext(Dispatchers.IO){
                if (updateState) {
                  val reNameToMedia = MediaUtil.reNameToMedia(Uri.parse(msg.uri), name,destPath,state)
                }
            }
        }
    }



    fun deleteMediaFile(uri: Uri){
        viewModelScope.launch(Dispatchers.IO) {
            val deleteMedia = MediaUtil.deleteMedia(uri)
            LogUtils.i("------deleteMedia--------$deleteMedia----------------------")
        }
    }

    fun deleteFile(pathList:MutableList<String>){
        viewModelScope.launch (Dispatchers.IO){
            pathList.forEach {
                val deleteFile = FileUtil.deleteFile(File(it))
            }
        }

    }



    //FileList
    val listEditAction by lazy {
        MutableLiveData(false)
    }

    val selectItems by lazy {
        MutableLiveData<MutableList<PlayListMsgBean>>()
    }

    fun setSelectItems(listBean:MutableList<PlayListMsgBean>){
        selectItems.value=listBean
    }

    fun getListEditAction_()=listEditAction.value?:false

   fun  setListEditAction(action:Boolean){
       listEditAction.value=action
    }


    fun addNewPlayList(msgBean:PlayListMsgBean){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                DbHelper.addListFile<PlayListMsgBean>("name=?",msgBean.name,msgBean){null}
            }
            PlayListLiveData.getPlayList()
        }
    }


    fun deletePlayList(list:MutableList<String>){
        viewModelScope.launch  {
            withContext(Dispatchers.IO){
                list.forEach {
                    DbHelper.deleteListFile<PlayListMsgBean>("name=?",it)
                }
            }
            PlayListLiveData.getPlayList()
        }
    }


    fun reNamePlayList(oldName:String, newName:String){
        viewModelScope.launch {
            val size = withContext(Dispatchers.IO) {
                DbHelper.updateListFile<PlayListMsgBean>(
                    "name=?",
                    oldName
                ) { contentValuesOf("name" to newName) }
            }

          if (size>0){
              PlayListLiveData.getPlayList()
          }
        }
    }

}


