package com.example.module_video.domain

import android.graphics.Bitmap
import android.net.Uri

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/3 14:31:42
 * @class describe
 */
data class MediaInformation(var id:Long=0L,var name:String?="",var duration:String?="",
                            var size:String?="",var date:String?="",
                            var resolution:String?="",var path:String?="",
                            var uri :String?="",val bitmap: Bitmap?=null
)
