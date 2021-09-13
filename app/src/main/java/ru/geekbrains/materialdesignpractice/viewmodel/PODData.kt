package ru.geekbrains.materialdesignpractice.viewmodel

import ru.geekbrains.materialdesignpractice.repository.PODServerResponseData

sealed class PODData {
    data class Success(val serverResponseData: PODServerResponseData) : PODData()
    data class Error(val error: Throwable) : PODData()
    object Loading : PODData()
}
