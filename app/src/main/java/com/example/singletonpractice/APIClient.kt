package com.example.singletonpractice

import android.provider.SyncStateContract
import retrofit2.converter.gson.GsonConverterFactory

import retrofit2.Retrofit

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor




class APIClient {

    private var retrofit: Retrofit? = null
    private var client: OkHttpClient? = null

    private var aPIInterface: APIInterface? = null

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level= HttpLoggingInterceptor.Level.BODY
        client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        retrofit = Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/gh/fawazahmed0/currency-api@1/latest/currencies/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        aPIInterface = retrofit!!.create(APIInterface::class.java)
    }

    companion object {
        private var instance: APIClient? = null

        private fun getInstance(): APIClient? {
            if (instance == null) {
                instance = APIClient()
            }
            return instance
        }

        fun getAPIInterface(): APIInterface? {
            return getInstance()?.aPIInterface
        }
    }
}