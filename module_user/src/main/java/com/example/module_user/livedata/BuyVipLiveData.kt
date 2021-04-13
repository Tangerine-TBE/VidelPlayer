package com.example.module_user.livedata

import com.example.module_base.base.BaseLiveData

/**
 * @name VidelPlayer
 * @class name：com.example.module_user.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/4/1 14:33:25
 * @class describe
 */
object BuyVipLiveData:BaseLiveData<Boolean>(){

    fun setBuyState(state:Boolean){
        value=state
    }
}