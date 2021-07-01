package com.example.videlplayer

import android.app.Activity
import android.os.Bundle
import com.example.module_ad.advertisement.TTAdManagerHolder
import com.example.module_ad.utils.BaseBackstage
import com.example.module_base.base.BaseApplication
import com.example.module_base.utils.SPUtil
import com.example.module_video.utils.Constants
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager

/**
 * @name VidelPlayer
 * @class name：com.example.videlplayer
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/16 11:26:39
 * @class describe
 */
class MainApplication : BaseApplication() {
    override fun initData() {

       when(SPUtil.getInstance().getInt(Constants.SP_CORE_TYPE)){
          0-> PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
          1-> PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
          2-> PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
       }

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var activityStartCount = 0

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

            }

            override fun onActivityStarted(activity: Activity) {
                activityStartCount++;
                //数值从0 变到 1 说明是从后台切到前台
                if (activityStartCount == 1) {
                    //从后台切到前台
                   BaseBackstage.setBackstage(this@MainApplication)

                }
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                activityStartCount--;
                //数值从1到0说明是从前台切到后台
                if (activityStartCount == 0) {
                    //从前台切到后台
                    BaseBackstage.setStop(this@MainApplication)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })

    }
}