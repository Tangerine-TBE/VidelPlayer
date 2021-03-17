package com.example.videlplayer

import com.example.module_ad.advertisement.TTAdManagerHolder
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
        TTAdManagerHolder.init(applicationContext)
       when(SPUtil.getInstance().getInt(Constants.SP_CORE_TYPE)){
          0-> PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
          1-> PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
          2-> PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
       }

    }
}