package ru.geekbrains.materialdesignpractice.viewmodel

import android.os.Build
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.materialdesignpractice.BuildConfig
import ru.geekbrains.materialdesignpractice.repository.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private const val SERVER_ERROR = "Server error"
private const val REQUEST_ERROR = "Request error"
private const val API_KEY_NOT_FOUND = "API key not found"

class NASAViewModel(
    private val liveDataToObserve: MutableLiveData<NASAData> = MutableLiveData(),
    private val retrofitImpl: NASARetrofitImpl = NASARetrofitImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<NASAData> {
        return liveDataToObserve
    }

    fun sendPODServerRequest() {
        liveDataToObserve.postValue(NASAData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            NASAData.Error(Throwable(API_KEY_NOT_FOUND))
        } else {
            retrofitImpl.getPictureOfTheDay(apiKey, podCallback)
        }
    }

    fun sendSatelliteImageRequest() {
        liveDataToObserve.postValue(NASAData.Loading)
        val date = getDayBeforeYesterday()
        val lon = 37.618423
        val lat = 55.751244
        val dim = 0.1
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            NASAData.Error(Throwable(API_KEY_NOT_FOUND))
        } else {
            retrofitImpl.getSatelliteImage(lon, lat, dim, date, apiKey, satelliteCallback)
        }
    }

    fun sendMarsRoverImageRequest() {
        liveDataToObserve.postValue(NASAData.Loading)
        val earthDate = getDayBeforeYesterday()
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            NASAData.Error(Throwable(API_KEY_NOT_FOUND))
        } else {
            retrofitImpl.getMarsRoverImage(earthDate, apiKey, marsCallback)
        }
    }

    fun sendEarthImageRequest() {
        liveDataToObserve.postValue(NASAData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            NASAData.Error(Throwable(API_KEY_NOT_FOUND))
        } else {
            retrofitImpl.getEarthImage(apiKey, earthCallback)
        }
    }

    private fun getDayBeforeYesterday(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yesterday = LocalDateTime.now().minusDays(2)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            yesterday.format(formatter)
        } else {
            val cal: Calendar = Calendar.getInstance()
            val s = SimpleDateFormat("yyyy-MM-dd")
            cal.add(Calendar.DAY_OF_YEAR, -2)
            s.format(cal.time)
        }
    }


    private val podCallback = object : Callback<PODServerResponseData> {
        override fun onResponse(
            call: Call<PODServerResponseData>,
            response: Response<PODServerResponseData>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(NASAData.PODSuccess(response.body()!!))
            } else {
                liveDataToObserve.postValue(NASAData.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
            liveDataToObserve.postValue(NASAData.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private val satelliteCallback = object : Callback<SatelliteResponseData> {
        override fun onResponse(
            call: Call<SatelliteResponseData>,
            response: Response<SatelliteResponseData>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(NASAData.SatelliteSuccess(response.body()!!))
            } else {
                liveDataToObserve.postValue(NASAData.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<SatelliteResponseData>, t: Throwable) {
            liveDataToObserve.postValue(NASAData.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private val marsCallback = object : Callback<MarsPhotosServerResponseData> {
        override fun onResponse(
            call: Call<MarsPhotosServerResponseData>,
            response: Response<MarsPhotosServerResponseData>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(NASAData.MarsSuccess(response.body()!!))
            } else {
                liveDataToObserve.postValue(NASAData.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<MarsPhotosServerResponseData>, t: Throwable) {
            liveDataToObserve.postValue(NASAData.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }

    private val earthCallback = object : Callback<List<EarthResponseData>> {
        override fun onResponse(
            call: Call<List<EarthResponseData>>,
            response: Response<List<EarthResponseData>>
        ) {
            if (response.isSuccessful && response.body() != null) {
                liveDataToObserve.postValue(NASAData.EarthSuccess(response.body()!!))
            } else {
                liveDataToObserve.postValue(NASAData.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: Call<List<EarthResponseData>>, t: Throwable) {
            liveDataToObserve.postValue(NASAData.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }
}