package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.FilterBean
import com.abc.matting.utils.GetDataUtils
import com.feisukj.base.baseclass.RecyclerViewHolder
import com.feisukj.base.util.BitmapUtils

class FilterAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<FilterBean> = arrayOf()
    private var selectItem = 0
    private var callback: FilterCallback? = null
    private var needVip = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_filter,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setRadiusImage(R.id.image,8f, BitmapUtils.getBitmapFromAssetsFile(mList[position].img))
        holder.setText(R.id.name,mList[position].filterName)
        if (position == selectItem){
            holder.getView<View>(R.id.selectBorder).visibility = View.VISIBLE
        }else{
            holder.getView<View>(R.id.selectBorder).visibility = View.GONE
        }
        if (mList[position].needVip&&needVip&&!GetDataUtils.isVip()){
            holder.getView<TextView>(R.id.vip_tip).visibility = View.VISIBLE
        }else{
            holder.getView<TextView>(R.id.vip_tip).visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            if (!(mList[position].needVip&&needVip&&!GetDataUtils.isVip())){
                val old = selectItem
                selectItem = position
                notifyItemChanged(old)
                notifyItemChanged(selectItem)
            }
            callback?.clickFilterItem(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(mList: Array<FilterBean>){
        this.mList = mList
        notifyDataSetChanged()
    }

    fun setCallBack(callback: FilterCallback){
        this.callback = callback
    }

    fun setNeedVip(needVip : Boolean){
        this.needVip = needVip
    }

    interface FilterCallback{
        fun clickFilterItem(position: Int)
    }
}