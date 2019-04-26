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
    }

    interface Presenter {
        fun start()
        fun dispose()
    }
}