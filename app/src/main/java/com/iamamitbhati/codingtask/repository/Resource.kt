package com.iamamitbhati.codingtask.repository

sealed class Resource<T>(
    val data: T? = null,
    val errorCode: Int? = null,
    val errorMessage: String? = null,
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Failed<T>(message: String) : Resource<T>(errorMessage = message)
    class Loading<T> : Resource<T>()
}