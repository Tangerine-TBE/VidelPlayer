package com.example.module_video.ui.adapter.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.module_video.R
import com.example.module_video.databinding.ItemMediaFileContianerBinding
import com.example.module_video.domain.MediaInformation

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/8 11:57:37
 * @class describe
 */
class MediaFileAdapter : RecyclerView.Adapter<MediaFileAdapter.MyHolder>() {

    private var mEditAction = false
    private var mSelectItemList = ArrayList<MediaInformation>()

    private val mList = ArrayList<MediaInformation>()

    fun setEditAction(edit: Boolean) {
        mSelectItemList.clear()
        mEditAction = edit
        notifyDataSetChanged()
    }


    fun getSelectList() = mSelectItemList


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemBinding = DataBindingUtil.inflate<ItemMediaFileContianerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_media_file_contianer,
            parent,
            false
        )
        return MyHolder(itemBinding.root, itemBinding)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.setItemData( mList[position], position)
    }

    override fun getItemCount(): Int = mList.size
    fun setList(list: MutableList<MediaInformation>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }


    inner class MyHolder(itemView: View, val binding: ItemMediaFileContianerBinding) :
        RecyclerView.ViewHolder(itemView) {
        fun setItemData(media: MediaInformation, position: Int) {
            binding.apply {
                Glide.with(itemView.context).load(media.bitmap)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(4)))
                    .error(R.mipmap.icon_audio_logo).into(mediaPic)
                mediaName.text = media.name
                mediaDuration.text = "时长：${media.duration}"
                mediaResolution.text = media.resolution
                mediaDate.text = "${media.date}    ${media.size}"

                mediaSelect.visibility = if (mEditAction) View.VISIBLE else View.GONE
                if (mSelectItemList.contains(media)) {
                    mSelectItemList.add(media)
                    mediaSelect.setImageResource(R.mipmap.icon_select1)
                } else {
                    mSelectItemList.remove(media)
                    mediaSelect.setImageResource(R.mipmap.icon_normal)
                }


                itemView.setOnClickListener {
                    if (mEditAction) {
                        if (mSelectItemList.contains(media)) {
                            mSelectItemList.remove(media)
                            mediaSelect.setImageResource(R.mipmap.icon_normal)
                        } else {
                            mSelectItemList.add(media)
                            mediaSelect.setImageResource(R.mipmap.icon_select1)
                        }
                    }
                    mListener?.onItemClick(media, position,itemView)
                }


                mediaMore.setOnClickListener {
                    if (mEditAction) {
                        return@setOnClickListener
                    }
                        mListener?.onItemSubClick(media, position)
                    }
            }

        }

    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(item: MediaInformation, position: Int,view: View)

        fun onItemSubClick(item: MediaInformation, position: Int)
    }
}