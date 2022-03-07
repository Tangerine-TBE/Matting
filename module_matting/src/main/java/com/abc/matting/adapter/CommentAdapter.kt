package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.CommentBean
import com.feisukj.base.baseclass.RecyclerViewHolder

class CommentAdapter:RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<CommentBean> = arrayOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pay_comment,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.avatar,mList[position].avatar)
        holder.setText(R.id.title,mList[position].title)
        holder.setText(R.id.body,mList[position].body)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<CommentBean>){
        this.mList = list
    }
}