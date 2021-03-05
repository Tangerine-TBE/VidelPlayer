package com.example.module_video.ui.adapter.recycleview

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.example.module_video.R
import com.example.module_video.databinding.ItemMediaFileContianerBinding
import com.example.module_video.domain.MediaInformation


/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/3 15:21:11
 * @class describe
 */
class MediaFileAdapter:BaseQuickAdapter<MediaInformation,BaseDataBindingHolder<ItemMediaFileContianerBinding>>(R.layout.item_media_file_contianer) {
    override fun convert(holder: BaseDataBindingHolder<ItemMediaFileContianerBinding>, item: MediaInformation) {
        holder.dataBinding?.apply {
            item?.let {
                Glide.with(context).load(it.bitmap).apply(RequestOptions.bitmapTransform(RoundedCorners(4))).error(R.mipmap.icon_audio_logo).into(mediaPic)
                mediaName.text=it.name
                mediaDuration.text="时长：${it.duration}"
                mediaResolution.text=it.resolution
                mediaDate.text="${it.date}    ${it.size}"
            }
        }
    }
}