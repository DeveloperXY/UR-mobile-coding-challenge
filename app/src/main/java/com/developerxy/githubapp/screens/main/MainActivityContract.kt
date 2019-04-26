package com.developerxy.githubapp.screens.main

import com.developerxy.githubapp.models.Repository

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
interface MainActivityContract {
    interface View {
        fun setupRecyclerView()
        fun showRepos(repositories: List<Repository>)
        fun showToast(message: String)
        fun hideProgressBar()
        fun appendRepos(repositories: List<Repository>)
        fun showSnackBar(message: String)
        fun removeScrollListener()
        fun onQuotaAbused(message: String?)
        fun hideInfiniteLoadingIndicator()
    }

    interface Presenter {
        fun start()
        fun dispose()
        fun loadNextPage()
    }
}