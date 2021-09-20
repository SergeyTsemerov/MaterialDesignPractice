package ru.geekbrains.materialdesignpractice.repository

import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NASARetrofitImpl {

    private val baseUrl = "https://api.nasa.gov/"

    private val api by lazy {
        Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build().create(RetrofitAPI::class.java)
    }

    fun getPictureOfTheDay(apiKey: String, podCallback: Callback<PODServerResponseData>) {
        api.getPictureOfTheDay(apiKey).enqueue(podCallback)
    }

    fun getSatelliteImage(lon: Double, lat: Double, dim: Double, date: String, apiKey: String, satelliteCallback: Callback<SatelliteResponseData>) {
        api.getSatelliteImage(apiKey, lon, lat, date, dim).enqueue(satelliteCallback)
    }

    fun getMarsRoverImage(earth_date: String, apiKey: String, marsCallback: Callback<MarsPhotosServerResponseData>) {
        api.getMarsImage(earth_date, apiKey).enqueue(marsCallback)
    }

    fun getEarthImage(apiKey: String, earthCallback: Callback<List<EarthResponseData>>) {
        api.getEarthImage(apiKey).enqueue(earthCallback)
    }
}