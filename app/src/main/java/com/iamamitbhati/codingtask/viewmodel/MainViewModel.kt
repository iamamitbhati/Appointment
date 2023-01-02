package com.iamamitbhati.codingtask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.data.model.Setting
import com.iamamitbhati.codingtask.repository.AppointmentRepository
import com.iamamitbhati.codingtask.repository.Resource

class MainViewModel(private val appointmentRepository: AppointmentRepository) : ViewModel() {
    val petList: MutableLiveData<Resource<ArrayList<Pet>>> = MutableLiveData()
    val config: MutableLiveData<Resource<Setting>> = MutableLiveData()
    fun fetchAllData() {
        appointmentRepository.getAllPets {
            petList.postValue(it)
        }
        appointmentRepository.getConfig {
            config.postValue(it)
        }
    }
}