package com.example.module_video.livedata

import android.database.ContentObserver
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.viewModelScope
import com.example.module_base.base.BaseApplication
import com.example.module_base.base.BaseLiveData
import com.example.module_base.utils.LogUtils
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.ValueMediaType
import com.example.module_video.utils.MediaUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @name VidelPlayer
 * @class name：com.example.module_video
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/5 11:28:51
 * @class describe
 */
object MediaLiveData : BaseLiveData<ValueMediaType>(){

    fun getMedia(){
        try {
            mScope.launch(Dispatchers.IO){
                MediaUtil.getVideo{
                    postValue(it)
                }
            }
        }catch (e:Exception){

        }
    }


    override fun onInactive() {
        super.onInactive()
     //  mJob.cancel()
    }




}