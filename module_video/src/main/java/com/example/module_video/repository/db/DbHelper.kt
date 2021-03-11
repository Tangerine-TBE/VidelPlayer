package com.example.module_video.repository.db

import android.content.ContentValues
import com.example.module_video.domain.PlayListMsgBean
import org.litepal.LitePal

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.repository.db
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/11 9:32:54
 * @class describe
 */
object DbHelper {

    inline fun <reified T> addListFile(condition: String,
                                       value: String,
                                       clazz: PlayListMsgBean?,
                                       crossinline block: () -> ContentValues?):Boolean{
        val find = LitePal.where(condition, value).find(T::class.java)
     return   if (find.isEmpty()) {
            clazz?.save()?:false
        } else {
            block()?.run {
               LitePal.updateAll(T::class.java, this, condition, value) > 0
            }?:false
        }
    }

    inline fun <reified T>updateListFile(condition: String, value: String,block: () -> ContentValues) = LitePal.updateAll(T::class.java,block(),condition,value)



    inline fun <reified T> deleteListFile(condition: String,value: String) =LitePal.deleteAll(T::class.java,condition,value)



   inline fun <reified T>queryAllListFile(): MutableList<T> =LitePal.findAll(T::class.java)



    inline fun <reified T>queryListFile(condition: String,value: String): T =LitePal.where(condition,value).find(T::class.java)[0]
}