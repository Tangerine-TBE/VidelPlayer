package com.example.module_video.ui.widget.popup
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BasePopup
import com.example.module_base.utils.Constants
import com.example.module_base.utils.generateVideoAudioIntent
import com.example.module_video.R
import com.example.module_video.databinding.PopupItemSelectBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.adapter.recycleview.PopupSelectItemAdapter

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/8 16:59:32
 * @class describe
 */
class ItemSelectPopup(activity:FragmentActivity?):BasePopup<PopupItemSelectBinding>(activity,R.layout.popup_item_select,ViewGroup.LayoutParams.MATCH_PARENT) {
    private var mPopupSelectItemAdapter: PopupSelectItemAdapter = PopupSelectItemAdapter()

    init {
        mView.apply {
            itemSelectContainer.apply {
                layoutManager=LinearLayoutManager(activity)
                mPopupSelectItemAdapter.setList(DataProvider.homePopupList)
                adapter=mPopupSelectItemAdapter
            }

            event()
        }
    }

    private var itemMsg:MediaInformation?=null

    fun setTitleText(info: MediaInformation?){
        itemMsg=info
        mView.selectTitle.text="${info?.name}"
    }


    fun event() {
        mPopupSelectItemAdapter.setOnItemClickListener { adapter, view, position ->
            when(position){
                0->{}
                1->{}
                2->{
                    itemMsg?.let {
                      activity?.startActivity(generateVideoAudioIntent(activity,it.path, Constants.DATA_TYPE_VIDEO))
                    }
                }
                3->{}

            }

        }
    }



}