package com.iamamitbhati.codingtask.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.data.model.Setting
import com.iamamitbhati.codingtask.repository.AppointmentRepository
import com.iamamitbhati.codingtask.repository.Resource
import java.util.*
import kotlin.collections.ArrayList

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



    /**
     * Function to checkTime is between working hours or not
     */
    fun checkTime(time: String, current:Calendar): Boolean {
        val fromTime: Calendar = current.clone() as Calendar
        val toTime: Calendar = current.clone() as Calendar
        val currentTime: Calendar = current.clone() as Calendar
        try {
            val times = time.split(" ").toTypedArray()
            val week = times[0].split("-").toTypedArray()
            val from = times[1].split(":").toTypedArray()
            val until = times[3].split(":").toTypedArray()

            fromTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(from[0]))
            fromTime.set(Calendar.MINUTE, Integer.valueOf(from[1]))

            toTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(until[0]))
            toTime.set(Calendar.MINUTE, Integer.valueOf(until[1]))

            val currentDay = currentTime.get(Calendar.DAY_OF_WEEK)
            val fromDay = getDayOfWeek(week[0])
            val toDay = getDayOfWeek(week[1])

            if (currentTime.after(fromTime) && currentTime.before(toTime) && currentDay in fromDay..toDay) {
                return true
            }
        } catch (e: Exception) {
            return false
        }
        return false

    }

    /**
     * returns value for day of week
     */
     fun getDayOfWeek(day: String): Int {
        return when (day) {
            "S" -> 1
            "M" -> 2
            "TU" -> 3
            "W" -> 4
            "TH" -> 5
            "F" -> 6
            "SA" -> 7
            else -> 0
        }
    }
}