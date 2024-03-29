package com.example.module_base.base

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alibaba.android.arouter.launcher.ARouter
import com.example.module_base.utils.PackageUtil

import com.example.module_base.utils.SPUtil

import com.umeng.commonsdk.UMConfigure
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.litepal.LitePal


/**
 * @name Wifi_Manager
 * @class name：com.example.module_base.base
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/1/7 10:59:12
 * @class describe
 */
open class BaseApplication : Application() {

    companion object {
        lateinit var application: BaseApplication
        lateinit var mHandler: Handler
        lateinit var mContext: Context
        lateinit var mPackName: String
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        mContext = applicationContext
        mHandler = Handler(Looper.getMainLooper())
        mPackName = packageName
        SPUtil.init(this@BaseApplication)
        GlobalScope.launch {
            LitePal.initialize(this@BaseApplication)
            LitePal.getDatabase()
            ARouter.openDebug()
            ARouter.openLog()
            ARouter.init(this@BaseApplication)
           /* //用户反馈
            FeedbackAPI.init(this@BaseApplication, "25822454", "7a8bb94331a5141dcea61ecb1056bbbd")
            val jsonObject = JSONObject()
            try {
                jsonObject.put("appId", packageName)
                jsonObject.put(
                    "appName", PackageUtil.getAppMetaData(
                        this@BaseApplication,
                        ModuleProvider.APP_NAME
                    )
                )
                jsonObject.put("ver", PackageUtil.packageCode2(applicationContext))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            FeedbackAPI.setAppExtInfo(jsonObject)*/
            UMConfigure.preInit(application,"605b0b9cb8c8d45c13ae24a4",PackageUtil.getAppMetaData(
                application,"UMENG_CHANNEL"))
        }

        initData()

    }

    open fun initData() {

    }
}