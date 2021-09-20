package ru.geekbrains.materialdesignpractice.repository

import com.google.gson.annotations.SerializedName

class MarsPhotosServerResponseData(@field:SerializedName("photos") val photos: ArrayList<MarsResponseData>)