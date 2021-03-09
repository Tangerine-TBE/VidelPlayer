package com.example.module_video.ui.adapter.recycleview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.module_video.R
import com.example.module_video.databinding.ItemFileListContainerBinding
import com.example.module_video.domain.FileBean
import com.example.module_video.domain.MediaInformation
import com.tamsiree.rxkit.RxTimeTool
import java.text.SimpleDateFormat
import java.util.*

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.ui.adapter.recycleview
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/9 16:35:03
 * @class describe
 */


class FileListAdapter : RecyclerView.Adapter<FileListAdapter.MyHolder>() {

    private var mEditAction = false
    private var mSelectItemList = ArrayList<FileBean>()

    private val mList = ArrayList<FileBean>()

    fun setEditAction(edit: Boolean) {
        mSelectItemList.clear()
        mEditAction = edit
        notifyDataSetChanged()
    }

    fun getSelectList() = mSelectItemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileListAdapter.MyHolder {
        val itemBinding = DataBindingUtil.inflate<ItemFileListContainerBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_file_list_container,
            parent,
            false
        )
        return MyHolder(itemBinding.root, itemBinding)
    }

    override fun onBindViewHolder(holder: FileListAdapter.MyHolder, position: Int) {
        holder.setItemData(mList[position], position)
    }

    override fun getItemCount(): Int = mList.size
    fun setList(list: MutableList<FileBean>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }


    inner class MyHolder(itemView: View, val binding: ItemFileListContainerBinding) :
        RecyclerView.ViewHolder(itemView) {
        fun setItemData(item: FileBean, position: Int) {
            binding.apply {
                listName.text = "${item.name}"
                listDate.text = "${
                    RxTimeTool.date2String(
                        Date(item.createDate),
                        SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    )
                }"
                listSize.text = "${item.subProject}个项目"


                listSelect.visibility = if (mEditAction) View.VISIBLE else View.GONE
                if (mSelectItemList.contains(item)) {
                    mSelectItemList.add(item)
                    listSelect.setImageResource(R.mipmap.icon_select1)
                } else {
                    mSelectItemList.remove(item)
                    listSelect.setImageResource(R.mipmap.icon_normal)
                }


                itemView.setOnClickListener {
                    if (mEditAction) {
                        if (mSelectItemList.contains(item)) {
                            mSelectItemList.remove(item)
                            listSelect.setImageResource(R.mipmap.icon_normal)
                        } else {
                            mSelectItemList.add(item)
                            listSelect.setImageResource(R.mipmap.icon_select1)
                        }
                    }
                    mListener?.onItemClick(item, position)
                }


                listMore.setOnClickListener {
                    if (mEditAction) {
                        return@setOnClickListener
                    }
                    mListener?.onItemSubClick(item, position)
                }
            }

        }

    }

    private var mListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(item: FileBean, position: Int)

        fun onItemSubClick(item: FileBean, position: Int)
    }
}