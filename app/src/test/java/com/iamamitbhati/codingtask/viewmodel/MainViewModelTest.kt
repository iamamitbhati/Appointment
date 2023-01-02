package com.iamamitbhati.codingtask.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.data.model.Setting
import com.iamamitbhati.codingtask.getOrAwaitValue
import com.iamamitbhati.codingtask.repository.AppointmentRepository
import com.iamamitbhati.codingtask.repository.Resource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.reset
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var appointmentRepository: AppointmentRepository

    private lateinit var mainViewModel: MainViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        this.mainViewModel = MainViewModel(appointmentRepository)
    }

    @After
    fun tearDown() {
        reset(appointmentRepository)
    }

    @Test
    fun fetchAllDataSuccess() {
        `when`(appointmentRepository.getAllPets(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<ArrayList<Pet>>) -> Unit)
            completion.invoke(Resource.Success(mockList))
        }
        `when`(appointmentRepository.getConfig(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<Setting>) -> Unit)
            completion.invoke(Resource.Success(mockSetting))
        }
        mainViewModel.fetchAllData()

        val observerList = mainViewModel.petList.getOrAwaitValue()
        val observerConfig = mainViewModel.config.getOrAwaitValue()

        assertEquals(mockList.size, observerList.data?.size)
        assertEquals(mockList[0].title, observerList.data?.get(0)?.title)
        assertEquals(mockSetting.isCallEnabled, observerConfig.data?.isCallEnabled)
        assertEquals(mockSetting.isChatEnabled, observerConfig.data?.isChatEnabled)
        assertEquals(mockSetting.workHours, observerConfig.data?.workHours)
    }

    @Test
    fun fetchAllDataPetsFail() {
        `when`(appointmentRepository.getAllPets(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<ArrayList<Pet>>) -> Unit)
            completion.invoke(Resource.Failed(API_ERROR))
        }
        `when`(appointmentRepository.getConfig(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<Setting>) -> Unit)
            completion.invoke(Resource.Success(mockSetting))
        }
        mainViewModel.fetchAllData()

        val observerList = mainViewModel.petList.getOrAwaitValue()
        val observerConfig = mainViewModel.config.getOrAwaitValue()

        assertEquals(API_ERROR, observerList.errorMessage)
        assertEquals(mockSetting.isCallEnabled, observerConfig.data?.isCallEnabled)
        assertEquals(mockSetting.isChatEnabled, observerConfig.data?.isChatEnabled)
        assertEquals(mockSetting.workHours, observerConfig.data?.workHours)
    }

    @Test
    fun fetchAllDataConfigFail() {
        `when`(appointmentRepository.getAllPets(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<ArrayList<Pet>>) -> Unit)
            completion.invoke(Resource.Success(mockList))
        }
        `when`(appointmentRepository.getConfig(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<Setting>) -> Unit)
            completion.invoke(Resource.Failed(API_ERROR))
        }
        mainViewModel.fetchAllData()

        val observerList = mainViewModel.petList.getOrAwaitValue()
        val observerConfig = mainViewModel.config.getOrAwaitValue()

        assertEquals(mockList.size, observerList.data?.size)
        assertEquals(mockList[0].title, observerList.data?.get(0)?.title)
        assertEquals(API_ERROR, observerConfig.errorMessage)
    }

    @Test
    fun fetchAllDataFail() {
        `when`(appointmentRepository.getAllPets(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<ArrayList<Pet>>) -> Unit)
            completion.invoke(Resource.Failed(API_ERROR))
        }
        `when`(appointmentRepository.getConfig(MockitoHelper.anyObject())).thenAnswer {
            val argument = it.arguments[0]
            val completion = argument as ((Resource<Setting>) -> Unit)
            completion.invoke(Resource.Failed(API_ERROR))
        }
        mainViewModel.fetchAllData()

        val observerList = mainViewModel.petList.getOrAwaitValue()
        val observerConfig = mainViewModel.config.getOrAwaitValue()

        assertEquals(API_ERROR, observerList.errorMessage)
        assertEquals(API_ERROR, observerConfig.errorMessage)
    }
}

private val mockList = arrayListOf<Pet>().apply {
    add(Pet().apply {
        title = "Cat"
        imageUrl =
            "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/Cat_poster_1.jpg/1200px-Cat_poster_1.jpg"
        contentUrl = "https://en.wikipedia.org/wiki/Cat"
    })
}

private val mockSetting = Setting().apply {
    isCallEnabled = true
    isCallEnabled = true
    workHours = "M-F 9:00 - 18:00"
}

private const val API_ERROR = "404: Something went wrong"

object MockitoHelper {
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> uninitialized(): T = null as T
}