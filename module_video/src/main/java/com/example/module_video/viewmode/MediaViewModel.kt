package com.example.module_video.viewmode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_video.domain.FileBean
import com.example.module_video.domain.MediaInformation
import com.example.module_video.utils.FileUtil
import com.example.module_video.utils.MediaUtil
import kotlinx.coroutines.launch
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
        MutableLiveData<Int>()
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


    fun getEditAction_():Boolean=editAction.value?:false

    fun getSearchAction_():Boolean=searchAction.value?:false

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

    //FileList
    val listEditAction by lazy {
        MutableLiveData(false)
    }
    val fileList by lazy {
        MutableLiveData<MutableList<FileBean>>()
    }
    val createFileSate by lazy {
        MutableLiveData<Boolean>()
    }

    val selectItems by lazy {
        MutableLiveData<MutableList<FileBean>>()
    }

    fun setSelectItems(list:MutableList<FileBean>){
        selectItems.value=list
    }


    fun getListEditAction_()=listEditAction.value?:false

   fun  setListEditAction(action:Boolean){
       listEditAction.value=action
    }



    fun getFileList(){
        fileList.value=FileUtil.getFileList()
    }

    fun createFile(name:String){
        val createFile = FileUtil.createFile(name)
        createFileSate.value=createFile
        if (createFile) {
            getFileList()
        }
    }

    fun deleteFile(list:MutableList<FileBean>){
        var isDelete=false
        list.forEach {
            isDelete= FileUtil.deleteFile(File(it.path))
        }
        if (isDelete) {
            getFileList()
        }
    }
}


