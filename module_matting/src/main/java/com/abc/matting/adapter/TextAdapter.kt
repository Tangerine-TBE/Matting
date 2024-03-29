package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.StickerBean
import com.feisukj.base.baseclass.RecyclerViewHolder

class TextAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {
    private var mList : Array<StickerBean> = arrayOf()
    private var callback : TextCallback? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_text,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.img,mList[position].showResId)
        if (mList[position].needVip)
            holder.getView<TextView>(R.id.tv_vip).visibility = View.VISIBLE
        else
            holder.getView<TextView>(R.id.tv_vip).visibility = View.GONE
        holder.itemView.setOnClickListener {
            callback?.clickTextItem(position,mList[position])
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list : Array<StickerBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: TextCallback){
        this.callback = callback
    }

    interface TextCallback{
        fun clickTextItem(position: Int,stickerBean: StickerBean)
    }

}