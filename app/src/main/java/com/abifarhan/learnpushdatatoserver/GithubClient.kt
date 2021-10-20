package com.abifarhan.learnpushdatatoserver

import okhttp3.RequestBody

import okhttp3.MultipartBody
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.http.*


interface GithubClient {

    @Multipart
    @POST("/contact/upload")
    suspend fun sendImage(@Part image: MultipartBody.Part)
}