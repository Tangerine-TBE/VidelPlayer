package com.example.module_video.repository

import android.Manifest
import com.example.module_video.R
import com.example.module_video.domain.ItemBean

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.repository
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/1 14:12:30
 * @class describe
 */
object DataProvider {
    val permissionList = arrayListOf(
        ItemBean(icon = R.mipmap.icon_ps_store, title = "储存", hint = "为您扫描手机上的音视频文件"),
    )


    val homeItemList= arrayListOf(
        "所有",
        "视频",
        "音频",
    )


    val homeBottomList= arrayListOf(
        ItemBean(R.mipmap.icon_home_media_normal,R.mipmap.icon_home_media,"媒体库"),
        ItemBean(R.mipmap.icon_home_list,R.mipmap.icon_home_list_select,"播放列表"),
        ItemBean(R.mipmap.icon_home_set,R.mipmap.icon_home_set_select,"设置")
    )

    val homePopupList= arrayListOf(
        ItemBean(title = "重命名"),
        ItemBean(title = "添加到播放列表"),
        ItemBean(title = "打开方式"),
        ItemBean(title = "删除"),
    )

    val listPopup= arrayListOf(
        ItemBean(title = "重命名"),
        ItemBean(title = "删除"),
    )

    val listMsgPopup= arrayListOf(
        ItemBean(title = "打开方式"),
        ItemBean(title = "删除"),
    )


    val setConnectUsList= arrayListOf(
            ItemBean(title = "意见反馈"),
            ItemBean(title = "联系客服",hint = "2681706890@qq.com"),
            ItemBean(title = "关于我们"),
            ItemBean(title = "隐私政策"),
            ItemBean(title = "用户协议"),
            ItemBean(title = "系统隐私权限"),
    )

}