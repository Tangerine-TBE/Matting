package com.abc.matting.http;

import com.abc.matting.BuildConfig;
import com.abc.matting.Constants;
import com.feisukj.base.util.LogUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    private static RetrofitManager sInstance;

    private final Retrofit mRetrofit;
    public static RetrofitManager getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitManager();
        }
        return sInstance;
    }

    public RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getClient().build())
                .build();
    }

    public Retrofit getmRetrofit() {
        return mRetrofit;
    }

    private OkHttpClient.Builder getClient(){
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        //add log record
        if (BuildConfig.DEBUG) {
            //打印网络请求日志
            LoggingInterceptor httpLoggingInterceptor = new LoggingInterceptor();
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
        }
        return httpClientBuilder;
    }

    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //这个chain里面包含了request和response，所以你要什么都可以从这里拿
            Request request = chain.request();

            long t1 = System.nanoTime();//请求发起的时间
            LogUtils.INSTANCE.i(String.format("LoggingInterceptor 发送请求 %s on %s%n%s",
                    request.url(), chain.connection(), request.headers(),request.body()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();//收到响应的时间

            //这里不能直接使用response.body().string()的方式输出日志
            //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一
            //个新的response给应用层处理
            ResponseBody responseBody = response.peekBody(1024 * 1024);

            LogUtils.INSTANCE.i(String.format("LoggingInterceptor 接收响应: [%s] %n返回json:【%s】 %.1fms%n%s",
                    response.request().url(),
                    responseBody.string(),
                    (t2 - t1) / 1e6d,
                    response.headers()));

            return response;
        }
    }
}
