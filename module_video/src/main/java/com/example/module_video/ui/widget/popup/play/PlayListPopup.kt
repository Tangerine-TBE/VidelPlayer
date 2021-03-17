package com.example.module_video.ui.widget.popup.play

import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.module_video.R
import com.example.module_video.domain.MediaInformation
import com.example.module_video.ui.adapter.recycleview.PopupPlayListAdapter
import com.example.module_video.utils.GridItemDecoration
import com.tamsiree.rxkit.RxDeviceTool
import razerdp.basepopup.BasePopupWindow

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.widget.popup
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/16 16:37:05
 * @class describe
 */
class PlayListPopup(context: Context) : BasePopupWindow(context) {
    private val mAdapter by lazy {
        PopupPlayListAdapter()
    }

    private lateinit var mListContainer:RecyclerView
    //private lateinit var mIvArrow:ImageView
    override fun onCreateContentView(): View = createPopupById(R.layout.popup_play_list_window)
    override fun onViewCreated(contentView: View) {
      //  mIvArrow=contentView.findViewById(R.id.videoPlayList)
        mListContainer=contentView.findViewById(R.id.videoPlayList)
    }



    override fun onPopupLayout(popupRect: Rect, anchorRect: Rect) {
        val gravity = computeGravity(popupRect, anchorRect)
        var verticalCenter = false
      /*  when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth() shr 1).toFloat())
                mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight()).toFloat())
                mIvArrow.setRotation(0f)
            }
            Gravity.BOTTOM -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth() shr 1).toFloat())
                mIvArrow.setTranslationY(0f)
                mIvArrow.setRotation(180f)
            }
            Gravity.CENTER_VERTICAL -> verticalCenter = true
        }
        when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
            Gravity.LEFT -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX((popupRect.width() - mIvArrow.getWidth()).toFloat())
                mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight() shr 1).toFloat())
                mIvArrow.setRotation(270f)
            }
            Gravity.RIGHT -> {
                mIvArrow.setVisibility(View.VISIBLE)
                mIvArrow.setTranslationX(0f)
                mIvArrow.setTranslationY((popupRect.height() - mIvArrow.getHeight() shr 1).toFloat())
                mIvArrow.setRotation(90f)
            }
            Gravity.CENTER_HORIZONTAL -> mIvArrow.setVisibility(if (verticalCenter) View.INVISIBLE else View.VISIBLE)
        }*/
    }

     fun setListData(list:List<MediaInformation>,position:Int){
        mListContainer.apply {
            val divider: GridItemDecoration = GridItemDecoration.Builder(context)
                .setColorResource(R.color.white_40)
                .setShowLastLine(false)
                .setHorizontalSpan(1f)
                .build()
            addItemDecoration(divider)
            layoutManager=LinearLayoutManager(context).apply {
                layoutParams.height=RxDeviceTool.getScreenHeight(context)/4
            }
            mAdapter.setList(list)
            mAdapter.setPosition(position)
            adapter=mAdapter

        }
    }

    fun setListPosition(position:Int){
        mAdapter.setPosition(position)
    }


    fun selectMedia(block:(position:Int)->Unit){
        mAdapter.setOnItemClickListener { adapter, view, position ->
            block(position)
        }
    }
}