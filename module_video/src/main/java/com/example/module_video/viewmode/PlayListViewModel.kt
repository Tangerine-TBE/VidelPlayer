package com.example.module_video.viewmode

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseViewModel
import com.example.module_video.domain.FileBean
import com.example.module_video.utils.FileUtil

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/10 17:55:35
 * @class describe
 */
class PlayListViewModel:BaseViewModel() {

    val fileList by lazy {
        MutableLiveData<MutableList<FileBean>>()
    }

    fun getFolderList(){
        fileList.value= FileUtil.getFileList()
    }

}