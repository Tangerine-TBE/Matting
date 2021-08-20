package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.BackgroundGroupBean
import com.abc.matting.utils.GetDataUtils
import com.abc.matting.utils.Utils
import com.feisukj.base.baseclass.RecyclerViewHolder
import com.feisukj.base.util.BitmapUtils

class PortraitBackgroundGroupAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<BackgroundGroupBean> = arrayOf()
    private var callback: PortraitBackgroundGroupCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_portrait_background_group,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setText(R.id.name,mList[position].name)
        holder.setImage(R.id.image_big, BitmapUtils.getBitmapFromAssetsFile(mList[position].imgBig))
//        holder.setRadiusImage(R.id.image_big,8f,BitmapUtils.getBitmapFromAssetsFile(mList[position].imgBig))
//        holder.setRadiusImage(R.id.image_small,4f,mList[position].imgSmall)
        holder.getView<ImageView>(R.id.text_bg).visibility = View.VISIBLE
        if (mList[position].needVip&&!GetDataUtils.isVip()&&!Utils.canSetBackground(mList[position].id)){
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.VISIBLE
        }else{
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            callback?.clickBackgroundGroupItem(position)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(mList: Array<BackgroundGroupBean>){
        this.mList = mList
    }

    fun setCallback(callback: PortraitBackgroundGroupCallback){
        this.callback = callback
    }

    interface PortraitBackgroundGroupCallback{
        fun clickBackgroundGroupItem(position: Int)
    }
}