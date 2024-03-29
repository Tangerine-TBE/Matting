package com.feisukj.base.retrofitnet

import androidx.annotation.NonNull
import com.feisukj.base.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 */
class HttpUtils private constructor() {
    private val retrofitForFeisu: Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(host)
                .build()
    }
    val logging = LoggingInterceptor(Logger())
    val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .connectTimeout(9, TimeUnit.SECONDS)
            .build()


    init {
        logging.level = LoggingInterceptor.Level.BODY
//        val interceptor = Interceptor { chain ->
//            val request = chain.request().newBuilder().addHeader("QT-Access-Token", token).build()
//            chain.proceed(request)
//        }

        val okHttpClientForAD = OkHttpClient().newBuilder()
                .addInterceptor(logging)
                .connectTimeout(9, TimeUnit.SECONDS)
                .build()
        retrofitForFeisu = Retrofit.Builder()
                .client(okHttpClientForAD)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BuildConfig.AD_HOST)
                .build()

    }

    private object SingletonHolder {
        internal var INSTANCE = HttpUtils()
    }

    companion object {
        var host: String? = null

        private val instance: HttpUtils
            get() = SingletonHolder.INSTANCE

        /**
         * 用于设置动态host
         */
        fun <T> setService(@NonNull host: String, clazz: Class<T>): T {
            this.host = host
            return HttpUtils.instance.retrofit.create(clazz)
        }

        /**
         * 用于需要添加Header
         */
        fun <T> setServiceWithToken(mToken: String, clazz: Class<T>): T {
            val logging = LoggingInterceptor(Logger())
            logging.level = LoggingInterceptor.Level.BODY

            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder().addHeader("QT-Access-Token", mToken).build()
                chain.proceed(request)
            }

            val okHttpClientForData = OkHttpClient().newBuilder()
                    .addInterceptor(logging)
                    .addInterceptor(interceptor)
                    .connectTimeout(9, TimeUnit.SECONDS)
                    .build()
            return Retrofit.Builder()
                    .client(okHttpClientForData)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BuildConfig.CONTENT_HOST)
                    .build().create(clazz)
        }

        /**
         * 用于广告配置请求
         */
        fun <T> setServiceForFeisuConfig(clazz: Class<T>): T {
            return HttpUtils.instance.retrofitForFeisu.create(clazz)
        }
    }

}
