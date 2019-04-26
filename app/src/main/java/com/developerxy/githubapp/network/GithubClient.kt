package com.developerxy.githubapp.network

import com.developerxy.githubapp.models.GithubResponse
import com.developerxy.githubapp.utils.toFormattedString
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
interface GithubClient {
    @GET("repositories")
    fun getRepos(
        @Query("q") q: String = Date().toFormattedString(),
        @Query("sort") sort: String = "stars",
        @Query("order") order: String = "desc",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1
    ): Observable<GithubResponse>
}