package com.example.module_video.utils

import android.content.*
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.contentValuesOf
import androidx.core.util.Pair
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.LogUtils
import com.example.module_base.utils.formatTime
import com.example.module_video.R
import com.example.module_video.domain.MediaInformation
import com.example.module_video.domain.ValueMediaType
import com.example.module_video.ui.activity.PlayVideoActivity
import com.tamsiree.rxkit.RxTimeTool
import com.umeng.analytics.pro.cr
import kotlinx.coroutines.*
import java.io.File
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
    private val contentResolver = BaseApplication.application.contentResolver

    /**
     * 获取视频文件
     * @return MutableList<MediaInformation>
     */
    fun getAllVideo(): MutableList<MediaInformation> {
        val videoList = ArrayList<MediaInformation>()
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
             //   LogUtils.i("---getAllVideo--${bitmap}--${id}---${name}---${size}---${duration}---${date}---${resolution}---${path}---${uri}---")
                videoList.add(MediaInformation(id, name, "${formatTime(duration / 1000)}", "${String.format("%.2f", size.toDouble() / 1024 / 1024)}MB",
                        "${RxTimeTool.date2String(Date(File(path).lastModified()), SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))}", resolution
                        ?: "", path, uri.toString(), bitmap,MediaState.VIDEO))
            }
            close()
        }
        return videoList
    }

    /**
     * 获取视频文件
     * @return MutableList<MediaInformation>
     */
    fun getVideo(block:(ValueMediaType)->Unit) {
        val videoList = ArrayList<MediaInformation>()
        val audioList = ArrayList<MediaInformation>()

        contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")?.apply {
            LogUtils.i("---Audio----------${videoList.size}----------------------------")
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                val duration = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) // 时长
                val name = getString(getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))//名字
                val size = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))//大小
                val path = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) // 路径
                //   LogUtils.i("---getAllAudio---${id}---${name}---${size}---${duration}---${date}-----${path}---${uri}---")
                audioList.add(MediaInformation(id, name, "${formatTime(duration / 1000)}", "${String.format("%.2f", size.toDouble() / 1024 / 1024)}MB",
                        "${RxTimeTool.date2String(Date(File(path).lastModified()), SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))}", "", path, uri.toString(), null,MediaState.AUDIO))
            }
            close()
        }


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
                //   LogUtils.i("---getAllVideo--${bitmap}--${id}---${name}---${size}---${duration}---${date}---${resolution}---${path}---${uri}---")
                videoList.add(MediaInformation(id, name, "${formatTime(duration / 1000)}", "${String.format("%.2f", size.toDouble() / 1024 / 1024)}MB",
                        "${RxTimeTool.date2String(Date(File(path).lastModified()), SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))}", resolution
                        ?: "", path, uri.toString(), bitmap,MediaState.VIDEO))
                block(ValueMediaType(videoList,audioList))
            }
            close()
        }


    }


    /**
     * 获取音频文件
     * @return MutableList<MediaInformation>
     */
    fun getAllAudio(): MutableList<MediaInformation> {
        val videoList = ArrayList<MediaInformation>()
        contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")?.apply {
            while (moveToNext()) {
                val id = getLong(getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)
                val duration = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)) // 时长
                val name = getString(getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))//名字
                val size = getLong(getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))//大小
                val path = getString(getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) // 路径
             //   LogUtils.i("---getAllAudio---${id}---${name}---${size}---${duration}---${date}-----${path}---${uri}---")
                videoList.add(MediaInformation(id, name, "${formatTime(duration / 1000)}", "${String.format("%.2f", size.toDouble() / 1024 / 1024)}MB",
                        "${RxTimeTool.date2String(Date(File(path).lastModified()), SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))}", "", path, uri.toString(), null,MediaState.AUDIO))
            }
            close()
        }
        return videoList
    }

    /**
     * 重命名文件
     * @param uri Uri
     * @param name String
     * @param type MediaState
     * @return Int
     */
    fun reNameToMedia(uri:Uri,name:String,path:String,type:MediaState):Int{
        val contentValues = if (type == MediaState.AUDIO) {
            contentValuesOf(MediaStore.Audio.Media.DISPLAY_NAME to name,MediaStore.Audio.Media.DATA to path)
        } else {
            contentValuesOf(MediaStore.Video.Media.DISPLAY_NAME to name,MediaStore.Video.Media.DATA to path)
        }
        return contentResolver.update(uri, contentValues, null, null)
    }

    /**
     * 删除文件
     * @param uri Uri
     * @return Int
     */
    fun deleteMedia(uri:Uri)= contentResolver.delete(uri,null,null)





}