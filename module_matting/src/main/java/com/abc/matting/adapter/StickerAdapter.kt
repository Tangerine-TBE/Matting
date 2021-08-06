package com.abc.matting.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.StickerBean
import com.abc.matting.ui.activity.PayActivity
import com.abc.matting.utils.GetDataUtils
import com.feisukj.base.baseclass.RecyclerViewHolder
import com.feisukj.base.util.ToastUtil

class StickerAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {
    private var mList : Array<StickerBean> = arrayOf()
    private var callback : StickerCallback? = null
    private lateinit var mContext: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        mContext = parent.context
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_sticker,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.img,mList[position].showResId)
        if (mList[position].needVip&&!GetDataUtils.isVip())
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.VISIBLE
        else
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.GONE
        holder.itemView.setOnClickListener {
            callback?.clickStickerItem(mList[position])
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list : Array<StickerBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback : StickerCallback){
        this.callback = callback
    }

    interface StickerCallback{
        fun clickStickerItem(stickerBean: StickerBean)
    }
}