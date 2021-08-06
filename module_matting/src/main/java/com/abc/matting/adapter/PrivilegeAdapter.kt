package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.PrivilegeBean
import com.feisukj.base.baseclass.RecyclerViewHolder

class PrivilegeAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<PrivilegeBean> = arrayOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vip_privilege,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.img,mList[position].img)
        holder.setText(R.id.title_text,mList[position].title)
        holder.setText(R.id.body,mList[position].body)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<PrivilegeBean>){
        this.mList = list
        notifyDataSetChanged()
    }
}