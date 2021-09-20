package ru.geekbrains.materialdesignpractice.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitAPI {
    @GET("planetary/apod")
    fun getPictureOfTheDay(@Query("api_key") apiKey: String): Call<PODServerResponseData>

    @GET("planetary/earth/assets")
    fun getSatelliteImage(
        @Query("api_key") apiKey: String,
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("date") date: String,
        @Query("dim") dim: Double
    ): Call<SatelliteResponseData>

    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsImage(
        @Query("earth_date") earth_date: String,
        @Query("api_key") apiKey: String,
    ): Call<MarsPhotosServerResponseData>

    @GET("EPIC/api/natural")
    fun getEarthImage(
        @Query("api_key") apiKey: String,
    ): Call<List<EarthResponseData>>
}