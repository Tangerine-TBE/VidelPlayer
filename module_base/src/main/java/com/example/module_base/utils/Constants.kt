package com.example.module_base.utils

/**
 * @name AlarmClock
 * @class name：com.example.alarmclock.util
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2020/11/17 14:24
 * @class describe
 */
object Constants {
    // 打开类型
     const val DATA_TYPE_VIDEO = "video/*"

    //第一次
    const val IS_FIRST = "one"
    const val FIRST_TIME = "FIRST_TIME"

    //位置sp 的key
    const val LOCATION_CITY="location_city"
    const val LOCATION_LONGITUDE="location_longitude"
    const val LOCATION_LATITUDE="location_latitude"
    const val LOCATION="location"


    //Intent的key
    const val SET_DEAL1="set_deal1"

    const val APP_NAME="APP_NAME"

    const val USER_ID="user_id"


     val MIME_MapTable = arrayOf(arrayOf(".3gp", "video/3gpp"),
            arrayOf(".apk", "application/vnd.android.package-archive"),
            arrayOf(".asf", "video/x-ms-asf"), arrayOf(".avi", "video/x-msvideo"), arrayOf(".bin", "application/octet-stream"),
            arrayOf(".bmp", "image/bmp"), arrayOf(".c", "text/plain"),
            arrayOf(".class", "application/octet-stream"), arrayOf(".conf", "text/plain"),
            arrayOf(".cpp", "text/plain"), arrayOf(".doc", "application/msword"),
            arrayOf(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"), arrayOf(".xls", "application/vnd.ms-excel"),
            arrayOf(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), arrayOf(".exe", "application/octet-stream"),
            arrayOf(".gif", "image/gif"), arrayOf(".gtar", "application/x-gtar"),
            arrayOf(".gz", "application/x-gzip"), arrayOf(".h", "text/plain"),
            arrayOf(".htm", "text/html"), arrayOf(".html", "text/html"),
            arrayOf(".jar", "application/java-archive"), arrayOf(".java", "text/plain"), arrayOf(".jpeg", "image/jpeg"),
            arrayOf(".jpg", "image/jpeg"), arrayOf(".js", "application/x-javascript"), arrayOf(".log", "text/plain"),
            arrayOf(".m3u", "audio/x-mpegurl"), arrayOf(".m4a", "audio/mp4a-latm"),
            arrayOf(".m4b", "audio/mp4a-latm"), arrayOf(".m4p", "audio/mp4a-latm"),
            arrayOf(".m4u", "video/vnd.mpegurl"), arrayOf(".m4v", "video/x-m4v"), arrayOf(".mov", "video/quicktime"),
            arrayOf(".mp2", "audio/x-mpeg"), arrayOf(".mp3", "audio/x-mpeg"), arrayOf(".mp4", "video/mp4"),
            arrayOf(".mpc", "application/vnd.mpohun.certificate"), arrayOf(".mpe", "video/mpeg"),
            arrayOf(".mpeg", "video/mpeg"), arrayOf(".mpg", "video/mpeg"), arrayOf(".mpg4", "video/mp4"),
            arrayOf(".mpga", "audio/mpeg"), arrayOf(".msg", "application/vnd.ms-outlook"), arrayOf(".ogg", "audio/ogg"),
            arrayOf(".pdf", "application/pdf"), arrayOf(".png", "image/png"), arrayOf(".pps", "application/vnd.ms-powerpoint"),
            arrayOf(".ppt", "application/vnd.ms-powerpoint"), arrayOf(".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"),
            arrayOf(".prop", "text/plain"), arrayOf(".rc", "text/plain"), arrayOf(".rmvb", "audio/x-pn-realaudio"), arrayOf(".rtf", "application/rtf"),
            arrayOf(".sh", "text/plain"), arrayOf(".tar", "application/x-tar"), arrayOf(".tgz", "application/x-compressed"), arrayOf(".txt", "text/plain"),
            arrayOf(".wav", "audio/x-wav"), arrayOf(".wma", "audio/x-ms-wma"), arrayOf(".wmv", "audio/x-ms-wmv"), arrayOf(".wps", "application/vnd.ms-works"),
            arrayOf(".xml", "text/plain"), arrayOf(".z", "application/x-compress"), arrayOf(".zip", "application/x-zip-compressed"), arrayOf("", "*/*"))

}