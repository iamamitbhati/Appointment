package com.iamamitbhati.codingtask.repository

import com.iamamitbhati.codingtask.domain.getConfigTranslation
import com.iamamitbhati.codingtask.domain.getPetTranslation
import com.iamamitbhati.codingtask.module.NetworkModule
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.data.model.Setting
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException


class AppointmentRepositoryImpl : AppointmentRepository {
    private val networkModule = NetworkModule

    override fun getConfig(listener: (Resource<Setting>) -> Unit) {
        listener(Resource.Loading())
        val url = networkModule.getHttpUrl(NetworkModule.CONFIG)
        val request = networkModule.getRequest(url)
        val client = networkModule.provideOkHttpClient()
        networkModule.getWebservice(client, request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener(Resource.Failed())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.let { getConfigTranslation(it.string()) } ?: Setting()
                listener(Resource.Success(body))
            }
        })

    }

    override fun getAllPets(listener: (Resource<ArrayList<Pet>>) -> Unit) {
        listener(Resource.Loading())
        val url = networkModule.getHttpUrl(NetworkModule.PETS)
        val request = networkModule.getRequest(url)
        val client = networkModule.provideOkHttpClient()

        networkModule.getWebservice(client, request, object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                listener(Resource.Failed())
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.let { getPetTranslation(it.string()) } ?: arrayListOf()
                listener(Resource.Success(body))
            }
        })
    }
}