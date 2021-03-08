package com.example.module_video.viewmode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseViewModel
import com.example.module_video.domain.MediaInformation
import com.example.module_video.utils.MediaUtil
import kotlinx.coroutines.launch

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:56:06
 * @class describe
 */
class MediaViewModel:BaseViewModel() {

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



}


