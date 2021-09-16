package ru.geekbrains.materialdesignpractice.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.geekbrains.materialdesignpractice.BuildConfig
import ru.geekbrains.materialdesignpractice.repository.PODRetrofitImpl
import ru.geekbrains.materialdesignpractice.repository.PODServerResponseData

private const val SERVER_ERROR = "Server error"
private const val REQUEST_ERROR = "Request error"
private const val API_KEY_NOT_FOUND = "API key not found"

class PODViewModel(
    private val liveDataToObserve: MutableLiveData<PODData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    fun getLiveData(): LiveData<PODData> {
        return liveDataToObserve
    }

    fun sendServerRequest() {
        liveDataToObserve.postValue(PODData.Loading)
        val apiKey = BuildConfig.NASA_API_KEY
        if (apiKey.isBlank()) {
            PODData.Error(Throwable(API_KEY_NOT_FOUND))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey).enqueue(
                object : Callback<PODServerResponseData> {
                    override fun onResponse(
                        call: Call<PODServerResponseData>,
                        response: Response<PODServerResponseData>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            liveDataToObserve.postValue(PODData.Success(response.body()!!))
                        } else {
                            liveDataToObserve.postValue(PODData.Error(Throwable(SERVER_ERROR)))
                        }
                    }

                    override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                        liveDataToObserve.postValue(PODData.Error(Throwable(t.message ?: REQUEST_ERROR)))
                    }
                }
            )
        }
    }
}