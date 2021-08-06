package com.abc.matting.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.abc.matting.R
import com.abc.matting.bean.PayBean
import com.abc.matting.utils.Utils
import com.feisukj.base.baseclass.RecyclerViewHolder
import com.feisukj.base.util.DensityUtil
import com.feisukj.base.util.SizeUtils

class PayAdapter:RecyclerView.Adapter<RecyclerViewHolder>() {

    private var mList: Array<PayBean> = arrayOf()
    private var selectItem = 0
    private var callback: PriceCallback? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_vip_price,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val layoutParams1 = RelativeLayout.LayoutParams(DensityUtil.dipTopxAsInt(94f),DensityUtil.dipTopxAsInt(82f))
        val layoutParams2 = RelativeLayout.LayoutParams(DensityUtil.dipTopxAsInt(102f),DensityUtil.dipTopxAsInt(90f))
        layoutParams1.setMargins(DensityUtil.dipTopxAsInt(4f),0,DensityUtil.dipTopxAsInt(4f),0)
        layoutParams2.setMargins(DensityUtil.dipTopxAsInt(4f),0,DensityUtil.dipTopxAsInt(4f),0)
        layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL)
        layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL)
        holder.getView<TextView>(R.id.tv_bottom).apply {
            this.paint.flags = Paint.STRIKE_THRU_TEXT_FLAG
            this.paint.isAntiAlias = true
            this.text = "￥${Utils.moneyFormat(mList[position].oldMoney)}"
        }
        if (position == selectItem){
            holder.getView<TextView>(R.id.tv_top).textSize = 10f
            holder.getView<TextView>(R.id.tv_top).setTextColor(Color.parseColor("#333333"))
            holder.getView<TextView>(R.id.tv_center).textSize = 16f
            holder.getView<TextView>(R.id.tv_center).setTextColor(Color.parseColor("#ffffff"))
            holder.getView<TextView>(R.id.tv_bottom).textSize = 12f
            holder.getView<TextView>(R.id.tv_bottom).setTextColor(Color.parseColor("#66ffffff"))
            holder.getView<ConstraintLayout>(R.id.rootView).apply {
                this.layoutParams = layoutParams2
                this.setBackgroundResource(R.drawable.shape_item_vip_price_y)
            }
        }else{
            holder.getView<TextView>(R.id.tv_top).textSize = 9f
            holder.getView<TextView>(R.id.tv_top).setTextColor(Color.parseColor("#FF5C3C"))
            holder.getView<TextView>(R.id.tv_center).textSize = 14f
            holder.getView<TextView>(R.id.tv_center).setTextColor(Color.parseColor("#9F5E13"))
            holder.getView<TextView>(R.id.tv_bottom).textSize = 11f
            holder.getView<TextView>(R.id.tv_bottom).setTextColor(Color.parseColor("#9F5E13"))
            holder.getView<ConstraintLayout>(R.id.rootView).apply {
                this.layoutParams = layoutParams1
                this.setBackgroundResource(R.drawable.shape_item_vip_price_n)
            }
        }
        when(mList[position].vipType){
            "VIP1" ->{
                holder.getView<TextView>(R.id.tv_top).text = "${SizeUtils.numFormat(mList[position].money/30,1,3,2,2)}/天"
                holder.getView<ImageView>(R.id.fire).visibility = View.GONE
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}/月")
            }
            "VIP6" ->{
                holder.getView<TextView>(R.id.tv_top).text = "${SizeUtils.numFormat(mList[position].money/180,1,3,2,2)}/天"
                holder.getView<ImageView>(R.id.fire).visibility = View.GONE
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}/半年")
            }
            "VIP12" ->{
                holder.getView<TextView>(R.id.tv_top).text = "${SizeUtils.numFormat(mList[position].money/365,1,3,2,2)}/天"
                holder.getView<ImageView>(R.id.fire).visibility = View.GONE
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}/年")
            }
            "VIP13" ->{
                holder.getView<TextView>(R.id.tv_top).text = "限时特惠"
                holder.getView<ImageView>(R.id.fire).visibility = View.VISIBLE
                holder.setText(R.id.tv_center,"￥${Utils.moneyFormat(mList[position].money)}/永久")
            }
        }
        holder.itemView.setOnClickListener {
            selectItem = position
            callback?.clickPriceItem(position)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setData(list: Array<PayBean>){
        this.mList = list
        notifyDataSetChanged()
    }

    fun setCallback(callback: PriceCallback){
        this.callback = callback
    }

    interface PriceCallback{
        fun clickPriceItem(position: Int)
    }
}