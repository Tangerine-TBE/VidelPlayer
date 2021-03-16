package com.example.module_video.utils

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/10 11:59:51
 * @class describe
 */


enum class MediaState{
    VIDEO,AUDIO
}


enum class GeneralState{
    LOADING,SUCCESS
}

enum class InputPwdState{
    BEGIN,AGAIN,ERROR,NONE
}

enum class BackState{
    CANCEL,DELETE
}

enum class CheckState{
    EXIT,UNLOCK,ERROR
}

//网络请求进度
enum class RequestNetState{
    SUCCESS,ERROR,LOADING,EMPTY
}
