package com.developerxy.githubapp.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 *
 * A utility singleton that takes care of configuring Retrofit to forge a [GithubClient].
 */
object ServiceGenerator {
    private const val BASE_URL = "https://api.github.com/search/"

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
    private var retrofit = builder.build()
    private val logging = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)
    private val httpClient = OkHttpClient.Builder()

    private lateinit var githubClient: GithubClient

    /**
     * Creates a new [GithubClient] instance it wasn't already, and returns it.
     */
    fun createGithubClient(): GithubClient {
        if (!::githubClient.isInitialized) {
            httpClient.addInterceptor(logging)
            builder.client(httpClient.build())
            retrofit = builder.build()
            githubClient = retrofit.create(GithubClient::class.java)
        }

        return githubClient
    }
}