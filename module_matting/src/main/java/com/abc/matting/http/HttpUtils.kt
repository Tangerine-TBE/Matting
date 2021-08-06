package com.abc.matting.http

import com.abc.matting.Constants
import com.abc.matting.bean.ImageCategoryBean
import com.abc.matting.bean.ImageListBean
import retrofit2.Callback
import retrofit2.Retrofit

class HttpUtils {
    private var mApi: Api? = null
    private var sInstance: HttpUtils? = null

    fun getInstance(): HttpUtils? {
        if (sInstance == null) {
            sInstance = HttpUtils()
        }
        return sInstance
    }

    init {
        val retrofitUser: Retrofit = RetrofitManager.getInstance().getmRetrofit()
        mApi = retrofitUser.create(Api::class.java)
    }

    //获取商品分类
    fun doCategory(callback : Callback<ImageCategoryBean>){
        mApi?.getCategory()?.enqueue(callback)
    }

    //获取商品列表
    fun doGoods(category_id : Int,page : Int,size : Int,callback: Callback<ImageListBean>){
        val map : MutableMap<String,String> = HashMap()
        map["category_id"] = "$category_id"
        val map_ : MutableMap<String,String> = HashMap()
        map_["page"] = "$page"
        map_["size"] = "$size"
        mApi?.getImageList(ApiMapUtil.setMapValues(Constants.IMAGE_LIST,map,map_))?.enqueue(callback)
    }

}