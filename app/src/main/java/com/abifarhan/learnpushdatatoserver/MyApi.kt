package com.abifarhan.learnpushdatatoserver

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface MyApi {

    @Multipart
    @PUT("contact/upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
//        @Part("photo") desc: RequestBody
    ) : Call<UploadResponse>

    companion object{
        operator fun invoke(): MyApi{
            return Retrofit.Builder()
                .baseUrl("http://10.115.6.149:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MyApi::class.java)
        }
    }
}