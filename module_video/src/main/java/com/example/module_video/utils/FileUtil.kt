@file:Suppress("DEPRECATION")

package com.example.module_video.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.Constants
import com.example.module_base.utils.LogUtils
import com.example.module_video.domain.FileBean

import io.zhuliang.appchooser.AppChooser
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * @name VidelPlayer
 * @class name：com.example.module_video.utils
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/9 10:40:30
 * @class describe
 */

object FileUtil {

    /**
     * 打开文件
     * @param file
     */
    fun openFile(context: Context, file: File?) {
        file?.let {
            try {
                val intent = Intent()
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                //设置intent的Action属性
                intent.action = Intent.ACTION_VIEW
                //获取文件file的MIME类型
                val type = getMIMEType(file)
                //设置intent的data和Type属性。android 7.0以上crash,改用provider
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    val fileUri = FileProvider.getUriForFile(
                        context,
                        context.packageName.toString() + ".myFileProvider",
                        file
                    ) //android 7.0以上
                    intent.setDataAndType(fileUri, type)
                    grantUriPermission(fileUri, intent)
                } else {
                    intent.setDataAndType(Uri.fromFile(file), type)
                }
                //跳转
                context.startActivity(intent)
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    private fun getMIMEType(file: File): String? {
        var type = "*/*"
        val fName = file.name
        //获取后缀名前的分隔符"."在fName中的位置。
        val dotIndex = fName.lastIndexOf(".")
        if (dotIndex < 0) {
            return type
        }
        /* 获取文件的后缀名 */
        val end = fName.substring(dotIndex, fName.length).toLowerCase()
        if (end === "") return type
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (i in Constants.MIME_MapTable.indices) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end == Constants.MIME_MapTable[i][0]) type = Constants.MIME_MapTable[i][1]
        }
        return type
    }

    private fun grantUriPermission(fileUri: Uri, intent: Intent) {
        val resInfoList = BaseApplication.application.packageManager.queryIntentActivities(
            intent,
            PackageManager.MATCH_DEFAULT_ONLY
        )
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            BaseApplication.application.grantUriPermission(
                packageName,
                fileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }


    fun toAppOpenFile(activity: FragmentActivity?, file: File) {
        if (!file.exists() || !file.isFile) {
            return
        }
        AppChooser.from(activity)
            .file(file)
            .authority(BaseApplication.application.packageName.toString() + ".myFileProvider")
            .load()
    }

    private val SD_DIR = Environment.getExternalStorageDirectory().absolutePath
    private val SD_APP_DIR = "$SD_DIR/Video_File"



    /**
     * 新建路径
     * @return String?
     */
     fun createFilePath():String=
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            BaseApplication.application.getExternalFilesDir("Video_File")?.path?:""
        } else {
            SD_APP_DIR
        }

    /**
     * 判断是否有SDCard
     *
     * @return 有为true，没有为false
     */
    private fun hasSDCard(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    /**
     * 新建目录
     */
    fun createFileDir() {
        if (hasSDCard()) {
            val fileDir = File(createFilePath())
            mkdirs(fileDir)
        }
    }


    private fun mkdirs(file: File)=
        if (!file.exists()) {
             file.mkdirs()
        } else {
            false
        }


    /**
     * 移动文件
     * @param source String?
     * @param destination String?
     */
    fun moveFile(source: String?, destination: String?)=File(source).renameTo(File(destination))


    /**
     * 新建文件夹
     * @param fileName String
     */
    fun createFile(fileName:String):Boolean{
        val file = File( "${createFilePath()}/$fileName")
        return  mkdirs(file)
    }

    /**
     * 获取文件数量
     * @return MutableList<FileBean>
     */
    fun getFileList():MutableList<FileBean>{
        val fileList = ArrayList<FileBean>()
        val file = File(createFilePath())
        val listFiles = file.listFiles()
        listFiles?.forEach {
            fileList.add(FileBean(it.name, it.listFiles()?.size?:0,it.lastModified(),it.path))
        }
        fileList.sortByDescending { it.createDate }
        return fileList
    }

    /**
     * 删除文件
     * @param file File
     * @return Boolean
     */
    fun deleteFileDir(file:File):Boolean{
      return  if (file.exists()) {
            if (file.isFile) {
                file.delete()
            } else if (file.isDirectory) {
                file.listFiles()?.forEach {
                    deleteFileDir(it)
                }
            }
           file.delete()
        } else {
            false
        }
    }

    /**
     * 删除文件
     * @param file File
     * @return Boolean
     */
    fun deleteFile(file:File)=if (file.exists()) {
            file.delete()
        } else {
            false
        }

}


