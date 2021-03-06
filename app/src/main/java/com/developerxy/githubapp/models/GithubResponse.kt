package com.developerxy.githubapp.models

import com.google.gson.annotations.SerializedName

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
data class RepositoryOwner(
    @SerializedName("login") var name: String,
    @SerializedName("avatar_url") var image: String
)

data class Repository(
    var name: String,
    @SerializedName("stargazers_count") var starsCount: Int,
    var description: String,
    var owner: RepositoryOwner
)

data class GithubResponse(
    @SerializedName("items") var repositories: List<Repository>?
)

data class GithubErrorResponse(
    val message: String?
)