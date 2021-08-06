package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.PictureBean
import com.feisukj.base.baseclass.RecyclerViewHolder

class MoreAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: MutableList<PictureBean> = arrayListOf()
    private var callback: MoreCallback? = null
    private var showDelete = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_more,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.img,mList[position].filePath)
        holder.setText(R.id.fileName,mList[position].fileName)
        if (showDelete){
            holder.getView<ImageView>(R.id.delete).visibility = View.VISIBLE
        }else{
            holder.getView<ImageView>(R.id.delete).visibility = View.GONE
        }
        holder.getView<ImageView>(R.id.img).setOnClickListener {
            callback?.itemClick(position)
        }
        holder.getView<ImageView>(R.id.rename).setOnClickListener {
            callback?.rename(position)
        }
        holder.getView<ImageView>(R.id.delete).setOnClickListener {
            callback?.delete(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: MutableList<PictureBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: MoreCallback){
        this.callback = callback
    }

    fun showDelete(isShowDelete: Boolean){
        this.showDelete = isShowDelete
        notifyDataSetChanged()
    }

    interface MoreCallback{
        fun itemClick(position: Int)
        fun rename(position: Int)
        fun delete(position: Int)
    }
}