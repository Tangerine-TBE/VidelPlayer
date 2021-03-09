package com.example.module_video.ui.widget.popup
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.module_base.base.BasePopup
import com.example.module_video.R
import com.example.module_video.databinding.PopupItemSelectBinding
import com.example.module_video.domain.MediaInformation
import com.example.module_video.repository.DataProvider
import com.example.module_video.ui.adapter.recycleview.PopupSelectItemAdapter
import com.example.module_video.utils.FileUtil
import java.io.File

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
        event()
    }

    private var itemMsg:MediaInformation?=null

    fun setTitleText(info: MediaInformation?){
        mView. itemSelectContainer.apply {
            adapter=mPopupSelectItemAdapter
            layoutManager=LinearLayoutManager(activity)
            mPopupSelectItemAdapter.setList(DataProvider.homePopupList)
            itemMsg=info
            mView.selectTitle.text="${info?.name}"
        }

    }

    fun setTitleNormal(name:String){
        mView.itemSelectContainer.apply {
            adapter=mPopupSelectItemAdapter
            layoutManager=LinearLayoutManager(activity)
            mPopupSelectItemAdapter.setList(DataProvider.listPopup)
        }
        mView.selectTitle.text="$name"
    }


    fun event() {
        mPopupSelectItemAdapter.setOnItemClickListener { adapter, view, position ->
            when(position){
                0->{
                }
                1->{

                }
                2->{
                    itemMsg?.let {
                        FileUtil.toAppOpenFile(activity, File(it.path))
                    }
                }
                3->{

                }

            }
            dismiss()
        }
    }



}