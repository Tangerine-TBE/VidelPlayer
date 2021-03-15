package com.example.module_video.domain

import com.example.module_video.utils.InputPwdState

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.domain
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/15 14:20:37
 * @class describe
 */
data class ValuePwdState(val state:InputPwdState,val list: MutableList<ItemBean>)
