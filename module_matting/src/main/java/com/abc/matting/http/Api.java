package com.abc.matting.http;

import com.abc.matting.bean.ImageCategoryBean;
import com.abc.matting.bean.ImageListBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    //获取商品分类
    @POST("gameskin/commodity/category")
    Call<ImageCategoryBean> getCategory();

    //获取商品列表
    @FormUrlEncoded
    @POST("manysmall/lockscreenclock/imageList")
    Call<ImageListBean> getImageList(@FieldMap Map<String,Object> params);

}
