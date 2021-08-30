package com.abc.matting.adapter

import android.app.Activity
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
import com.abc.matting.ui.dialog.UnlockBackgroundDialog
import com.abc.matting.utils.GetDataUtils
import com.abc.matting.utils.Utils
import com.feisukj.ad.manager.AdController
import com.feisukj.base.baseclass.RecyclerViewHolder
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.util.BitmapUtils
import com.feisukj.base.util.SPUtil

class PortraitBackgroundAdapter: RecyclerView.Adapter<RecyclerViewHolder>(),
    AdController.JILICallback {

    private var mList: Array<BackgroundBean> = arrayOf()
    private var callback: PortraitBackgroundCallback? = null
    private lateinit var mContext: Context
    private lateinit var groupName: String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        mContext = parent.context
        return RecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_portrait_background,parent,false))
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.setRadiusImage(R.id.image_big,8f, BitmapUtils.getBitmapFromAssetsFile(mList[position].imgSmall))
        if (mList[position].needVip&&!GetDataUtils.isVip()&&!Utils.canSetBackground(mList[position].id)){
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.VISIBLE
        }else{
            holder.getView<ImageView>(R.id.vip_tip).visibility = View.INVISIBLE
        }
        holder.itemView.setOnClickListener {
            if (mList[position].needVip&&!GetDataUtils.isVip()&&!Utils.canSetBackground(mList[position].id)){
                if (Utils.haveAD()){
                    groupName = mList[position].id
                    UnlockBackgroundDialog(mContext,{
                        mContext.startActivity(Intent(mContext,PayActivity::class.java))
                    },{
                        AdController.Builder(mContext as Activity,ADConstants.BACKGROUND_PAGE)
                            .create()
                            .showFullVideo(this)
                    }).show()
                }else{
                    mContext.startActivity(Intent(mContext,PayActivity::class.java))
                }
            }
            else
                callback?.clickBackgroundItem(mList[position])
        }
    }

    override fun onViewRecycled(holder: RecyclerViewHolder) {
        super.onViewRecycled(holder)
        val img = holder.getView<ImageView>(R.id.image_big)
        if (img!=null)
            holder.glideClean(img)
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

    override fun close() {
        SPUtil.getInstance().putLong(groupName,System.currentTimeMillis())
        notifyDataSetChanged()
    }
}