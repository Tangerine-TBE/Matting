package com.abc.matting.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.AdjustBean
import com.feisukj.base.baseclass.RecyclerViewHolder

class AdjustAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<AdjustBean> = arrayOf()
    private var selectItem = 0
    private var callback: AdjustAdapterCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_adjust,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setText(R.id.name,mList[position].name)
        if (position == selectItem){
            holder.setImage(R.id.image, mList[position].img_y)
            holder.getView<TextView>(R.id.name).setTextColor(Color.parseColor("#FFD23D"))
        }
        else{
            holder.setImage(R.id.image, mList[position].img_n)
            holder.getView<TextView>(R.id.name).setTextColor(Color.parseColor("#C7C7C7"))
        }
        holder.itemView.setOnClickListener {
            val old = selectItem
            selectItem = position
            notifyItemChanged(old)
            notifyItemChanged(selectItem)
            callback?.clickAdjustItem(mList[position].name)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<AdjustBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: AdjustAdapterCallback){
        this.callback = callback
    }

    interface AdjustAdapterCallback{
        fun clickAdjustItem(type: String)
    }
}