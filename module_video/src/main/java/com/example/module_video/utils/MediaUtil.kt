package com.example.module_video.utils

import android.content.ContentUris
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.formatTime
import com.example.module_video.domain.MediaInformation
import com.google.android.play.core.assetpacks.dd
import com.tamsiree.rxkit.RxTimeTool
import com.umeng.analytics.pro.cr
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/3 14:29:29
 * @class describe
 */
object MediaUtil {
    fun getAllVideo(): MutableList<MediaInformation> {
        val videoList = ArrayList<MediaInformation>()
        val contentResolver = BaseApplication.application.contentResolver
        contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")?.apply {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                val duration = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)) // 时长
                val name = getString(getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))//名字
                val size = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))//大小
                val date = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN))//添加时间
                val path = getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATA)) // 路径
                val resolution = getString(getColumnIndexOrThrow(MediaStore.Video.Media.RESOLUTION)) // 分辨率
                val bitmap = MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Video.Thumbnails.MICRO_KIND, null)//缩略图
                LogUtils.i("---getAllVideo--${bitmap}--${id}---${name}---${size}---${duration}---${date}---${resolution}---${path}---${uri}---")
                videoList.add(MediaInformation(id, name, "${formatTime(duration / 1000)}", "${String.format("%.2f", size.toDouble() / 1024 / 1024)}MB",
                        "${RxTimeTool.date2String(Date(date), SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))}", resolution
                        ?: "", path, uri.toString(), bitmap))
            }
            close()
        }
        return videoList
    }

    fun getAllAudio(): MutableList<MediaInformation> {
        val videoList = ArrayList<MediaInformation>()
        val contentResolver = BaseApplication.application.contentResolver
        contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")?.apply {

            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                val duration = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)) // 时长
                val name = getString(getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))//名字
                val size = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))//大小
                val date = getLong(getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED))//添加时间
                val path = getString(getColumnIndexOrThrow(MediaStore.Video.Media.DATA)) // 路径
                LogUtils.i("---getAllAudio---${id}---${name}---${size}---${duration}---${date}-----${path}---${uri}---")
                videoList.add(MediaInformation(id, name, "${formatTime(duration / 1000)}", "${String.format("%.2f", size.toDouble() / 1024 / 1024)}MB",
                        "${RxTimeTool.date2String(Date(date), SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))}", "", path, uri.toString(), null))
            }
            close()
        }
        return videoList
    }




}