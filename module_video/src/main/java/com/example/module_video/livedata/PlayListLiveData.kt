package com.example.module_video.livedata

import com.example.module_base.base.BaseLiveData
import com.example.module_video.domain.PlayListMsgBean
import com.example.module_video.repository.db.DbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/11 13:56:07
 * @class describe
 */
object PlayListLiveData:BaseLiveData<MutableList<PlayListMsgBean>>() {



    fun getPlayList(){
        mScope.launch (Dispatchers.IO){
            postValue(DbHelper.queryAllListFile())
        }
    }


}