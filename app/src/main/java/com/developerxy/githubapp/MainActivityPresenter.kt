package com.developerxy.githubapp

import com.developerxy.githubapp.network.GithubClient
import com.developerxy.githubapp.network.ServiceGenerator

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
class MainActivityPresenter(private var mView: MainActivityContract.View,
                            private var mGithubClient: GithubClient = ServiceGenerator.createGithubClient()): MainActivityContract.Presenter {
    override fun start() {

    }
}