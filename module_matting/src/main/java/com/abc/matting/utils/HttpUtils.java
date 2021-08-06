package com.abc.matting.utils;

import com.abc.matting.Constants;
import com.feisukj.base.util.LogUtils;
import com.feisukj.base.util.MD5Utlis;
import com.feisukj.base.util.maputils.MapUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtils {

    public static void getData(Map<String,String> map,String service,final RequestCallback callback){
        String timestamp = String.valueOf(System.currentTimeMillis());
        String nonce = ""+(int) (Math.random()*10000);
        StringBuilder str = new StringBuilder();
        Map<String,String> remap = MapUtils.sortMapByValue(map);
        if (null != remap) {
            for (Map.Entry<String,String> entry : remap.entrySet()) {
                str.append(entry.getValue());
            }
        }
        String str2 = Constants.TOKEN+timestamp+nonce+service+str;
        LogUtils.INSTANCE.e("拼接的字符串--------------->",str2);
        String tmpStr = MD5Utlis.md5(str2);
        remap.put("timestamp",timestamp);
        remap.put("nonce",nonce);
        remap.put("signature",tmpStr);
        remap.put("service",service);
        //创建okHttpClient对象
        OkHttpClient client = new OkHttpClient();
        //  FormEncodingBuilder builder = new FormEncodingBuilder();
        FormBody.Builder builder = new FormBody.Builder();

        StringBuilder str3 = new StringBuilder();
        if (null != remap) {
            for (Map.Entry<String,String> entry : remap.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = "
                        + entry.getValue());
                builder.add(entry.getKey(), entry.getValue());
                str3.append(entry.getKey()+"="+entry.getValue()+"&");
            }
        }

        String str4 = str3.substring(0,str3.lastIndexOf("&"));
        LogUtils.INSTANCE.e("请求的网址为------------------>"+Constants.URL+"?"+str4);

//        RequestBody body = new FormBody.Builder()
//                .add("name", "xiaoming").build();
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(Constants.URL).post(body).build();
//^x389fhfeahykge15933947438965087passport.loginThird0o28JJwFdUOxOCoOIJHy2gFMBZisM


        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure("", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure("Not Found", null);
                }
            }
        });
    }


    public interface RequestCallback {
        void onSuccess(String response);

        void onFailure(String msg, Exception e);
    }
}
