package com.example.module_video.viewmode

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseViewModel
import com.example.module_base.base.BaseVmFragment
import com.example.module_user.domain.ValueResult
import com.example.module_user.domain.ValueUserInfo
import com.example.module_user.livedata.UserInfoLiveData
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.Constants
import com.example.module_user.utils.NetState
import com.example.module_user.utils.UserInfoHelper
import com.example.module_video.R
import com.example.module_video.databinding.FragmentMediaBinding
import com.example.module_video.databinding.FragmentSetBinding

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 11:56:17
 * @class describe
 */
class SetViewModel:BaseViewModel(){

    val logOutState by lazy {
        MutableLiveData<ValueResult>()
    }

    fun toLogOut(id:String){
        doRequest({
            logOutState.postValue(ValueResult(NetState.LOADING, ""))
            UserRepository.userLogOut(UserInfoHelper.userLogOut(Constants.DELETE_USER,id))?.let {
                if (it.ret == NET_SUCCESS) {
                    logOutState.postValue(ValueResult(NetState.SUCCESS, "账号注销成功！"))
                    UserInfoLiveData.setUserInfo(ValueUserInfo(false,null))
                } else {
                    logOutState.postValue(ValueResult(NetState.ERROR, it.msg))
                }
            }
        }, {
            logOutState.postValue(ValueResult(NetState.ERROR, "网络错误！"))
        })
    }


}