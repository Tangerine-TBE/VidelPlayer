package com.example.module_video.ui.widget.popup

import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.FragmentActivity
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.LogUtils
import com.example.module_video.R
import com.example.module_video.databinding.PopupFunctionWindowBinding
import com.example.module_video.utils.Constants
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager
import com.shuyu.gsyvideoplayer.player.PlayerFactory
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager
import tv.danmaku.ijk.media.exo2.Exo2PlayerManager


/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/17 14:35:11
 * @class describe
 */
class FunctionSelectPopup(activity: FragmentActivity?):BasePopup<PopupFunctionWindowBinding>(
    activity,
    R.layout.popup_function_window,
    ViewGroup.LayoutParams.MATCH_PARENT
) {



    override fun initEvent() {
        mView.apply {
            coreGroup.setOnCheckedChangeListener { group, checkedId ->
                 val type = when (checkedId) {
                     R.id.ijkCore -> {
                         PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
                         0
                     }
                     R.id.exoCore -> {
                         PlayerFactory.setPlayManager(Exo2PlayerManager::class.java)
                         1
                     }
                     R.id.systemCore -> {
                         PlayerFactory.setPlayManager(SystemPlayerManager::class.java)
                         2
                     }else->{
                         PlayerFactory.setPlayManager(IjkPlayerManager::class.java)
                         0
                     }

                 }
                sp.putInt(Constants.SP_CORE_TYPE,type)
            }
        }
    }

    fun setSelectType(){
        mView.apply {
            val radioButton = coreGroup.getChildAt(sp.getInt(Constants.SP_CORE_TYPE)) as? RadioButton
            radioButton?.isChecked=true
        }
    }

}