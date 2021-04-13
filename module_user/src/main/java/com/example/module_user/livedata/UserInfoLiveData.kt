package com.example.module_user.livedata

import com.example.module_base.base.BaseLiveData
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.utils.Constants
import com.example.module_user.utils.GsonUtil
import com.example.module_user.utils.UserInfoUtil

/**
 * @name Wifi_Manager
 * @class name：com.example.module_user.livedata
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/29 15:16:58
 * @class describe
 */
object UserInfoLiveData : BaseLiveData<ValueUserInfo>() {

    override fun onActive() {
        super.onActive()
        sp?.apply {
            val userInfo = getString(Constants.USER_INFO, "")
            value= GsonUtil.strResolve<ValueUserInfo>(userInfo) ?: ValueUserInfo(false)
        }
    }

    fun setUserInfo(info:ValueUserInfo){
        if (!info.loginState) {
            UserInfoUtil.deleteUserMsg()
        }else{
            UserInfoUtil.saveUserMsg(info)
        }
       postValue(info)
    }



}