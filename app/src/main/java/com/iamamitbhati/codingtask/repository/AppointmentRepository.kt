package com.iamamitbhati.codingtask.repository

import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.data.model.Setting


interface AppointmentRepository {
    fun getConfig(listener: (Resource<Setting>) -> Unit)
    fun getAllPets(listener: (Resource<ArrayList<Pet>>) -> Unit)

}