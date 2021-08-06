package com.abc.matting.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.PictureBean
import com.feisukj.base.baseclass.RecyclerViewHolder

class MainPicAdapter : RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: MutableList<PictureBean> = arrayListOf()
    private var callback: MainPicCallback? = null

    override fun getItemViewType(position: Int): Int {
        if (position == 7)
            return 1
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_main_pic,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setImage(R.id.img,mList[position].filePath)
        if (getItemViewType(position) == 1)
            holder.getView<LinearLayout>(R.id.more).visibility = View.VISIBLE
        else
            holder.getView<LinearLayout>(R.id.more).visibility = View.GONE
        holder.getView<ImageView>(R.id.img).setOnClickListener {
            callback?.clickPicItem(position)
        }
        holder.getView<LinearLayout>(R.id.more).setOnClickListener {
            callback?.clickMore()
        }
    }

    override fun getItemCount(): Int {
        return if (mList.size<8) mList.size else 8
    }

    fun setData(list: MutableList<PictureBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: MainPicCallback){
        this.callback = callback
    }

    interface MainPicCallback{
        fun clickPicItem(position: Int)
        fun clickMore()
    }
}