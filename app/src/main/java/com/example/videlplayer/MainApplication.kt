package com.example.videlplayer

import com.example.module_ad.advertisement.TTAdManagerHolder
import com.example.module_base.base.BaseApplication

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
    }
}