package com.abc.matting.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.BackgroundBean
import com.abc.matting.ui.activity.PayActivity
import com.abc.matting.utils.GetDataUtils
import com.feisukj.base.baseclass.RecyclerViewHolder
import com.feisukj.base.util.BitmapUtils

class PortraitBackgroundAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<BackgroundBean> = arrayOf()
    private var callback: PortraitBackgroundCallback? = null
    private lateinit var mContext: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        mContext = parent.context
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_portrait_background,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setRadiusImage(R.id.image_big,8f, BitmapUtils.getBitmapFromAssetsFile(mList[position].imgSmall))
        if (mList[position].needVip&&!GetDataUtils.isVip()){
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.VISIBLE
        }else{
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            if (mList[position].needVip&&!GetDataUtils.isVip())
                mContext.startActivity(Intent(mContext,PayActivity::class.java))
            else
                callback?.clickBackgroundItem(mList[position])
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(mList: Array<BackgroundBean>){
        this.mList = mList
        notifyDataSetChanged()
    }

    fun setCallback(callback: PortraitBackgroundCallback){
        this.callback = callback
    }

    interface PortraitBackgroundCallback{
        fun clickBackgroundItem(item: BackgroundBean)
    }
}