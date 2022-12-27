package com.example.shopcartadminapp.api_manager

//import com.squareup.okhttp.OkHttpClient
//import okhttp3.logging.HttpLoggingInterceptor


import com.example.shopcartadminapp.api_manager.WebServices
import com.google.gson.GsonBuilder
import com.squareup.okhttp.OkHttpClient
import io.reactivex.Observable
import okhttp3.logging.HttpLoggingInterceptor
//import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.nio.file.attribute.AclEntry.newBuilder
import java.util.concurrent.TimeUnit

interface RestClient {



    companion object {
        fun create(): RestClient {

            val okHttpBuilder = okhttp3.OkHttpClient.Builder()

            val logging = HttpLoggingInterceptor() // Live
            logging.level = HttpLoggingInterceptor.Level.BODY // Live

            okHttpBuilder.connectTimeout(2, TimeUnit.MINUTES)
            okHttpBuilder.readTimeout(2, TimeUnit.MINUTES).build()
            okHttpBuilder.writeTimeout(2, TimeUnit.MINUTES)
            okHttpBuilder.addInterceptor(logging) // Live
            val okHttpClient = okHttpBuilder.build()
            val gson = GsonBuilder().setLenient().create()

            val retrofit =
                Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(WebServices.Domain).client(okHttpClient).build()

            return retrofit.create(RestClient::class.java)
                  }
    }
}