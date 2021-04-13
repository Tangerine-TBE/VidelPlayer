package com.example.module_video.domain

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/5 14:27:50
 * @class describe
 */
data class ValueMediaType(val videoList:MutableList<MediaInformation>,val audioList:MutableList<MediaInformation>,val finish:Boolean)
