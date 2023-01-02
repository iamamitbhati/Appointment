package com.iamamitbhati.codingtask.module

import okhttp3.Callback
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor


object NetworkModule {

    const val CONFIG = "config.json"
    const val PETS = "pets.json"
    private const val HOST = "apointment.000webhostapp.com"

    private fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor
    }

    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor = provideHTTPLoggingInterceptor()
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    fun getHttpUrl(path: String): HttpUrl {
        return HttpUrl.Builder()
            .scheme("https")
            .host(HOST)
            .addPathSegment(path)
            .build()
    }

    fun getRequest(url: HttpUrl): Request {
        return Request.Builder()
            .url(url)
            .build()
    }

    fun getWebservice(
        client: OkHttpClient = provideOkHttpClient(),
        request: Request,
        callback: Callback
    ) {
        client.newCall(request).enqueue(callback)
    }
}
