package ru.geekbrains.materialdesignpractice.viewmodel

import ru.geekbrains.materialdesignpractice.repository.EarthResponseData
import ru.geekbrains.materialdesignpractice.repository.SatelliteResponseData
import ru.geekbrains.materialdesignpractice.repository.MarsPhotosServerResponseData
import ru.geekbrains.materialdesignpractice.repository.PODServerResponseData

sealed class NASAData {
    data class PODSuccess(val serverResponseData: PODServerResponseData) : NASAData()
    data class Error(val error: Throwable) : NASAData()
    object Loading : NASAData()
    data class SatelliteSuccess(val satelliteResponseData: SatelliteResponseData) : NASAData()
    data class MarsSuccess(val marsResponseData: MarsPhotosServerResponseData) : NASAData()
    data class EarthSuccess(val earthResponseData: List<EarthResponseData>) : NASAData()
}
