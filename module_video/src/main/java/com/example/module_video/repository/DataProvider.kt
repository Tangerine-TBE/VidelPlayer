package com.example.module_video.repository

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
}