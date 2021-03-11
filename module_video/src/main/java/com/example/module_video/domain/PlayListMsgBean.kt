package com.example.module_video.domain

import org.litepal.crud.LitePalSupport

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/11 9:34:02
 * @class describe
 */
data class PlayListMsgBean(val name:String="", val media:String="", val date:Long=0L):LitePalSupport()


data class MediaDataBean(val idList:MutableList<Long>?=null)