package ru.geekbrains.materialdesignpractice.repository

import com.google.gson.annotations.SerializedName

data class MarsResponseData (
    @field:SerializedName("img_src") val imgSrc: String?,
    @field:SerializedName("earth_date") val earth_date: String?
)
