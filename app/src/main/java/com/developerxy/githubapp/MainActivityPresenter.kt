package com.developerxy.githubapp

import android.util.Log
import com.developerxy.githubapp.network.GithubClient
import com.developerxy.githubapp.network.ServiceGenerator
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
class MainActivityPresenter(
    private var mView: MainActivityContract.View,
    private var mGithubClient: GithubClient = ServiceGenerator.createGithubClient()
) : MainActivityContract.Presenter {

    private var mCompositeDisposable = CompositeDisposable()

    override fun start() {
        val disposable = mGithubClient.getRepos()
            .subscribeOn(Schedulers.computation())
            .subscribeBy(
                onError = {
                    Log.i("TESTING", "Error: $it")
                },
                onNext = {
                    Log.i("TESTING", "RESPONSE: $it")
                }
            )

        mCompositeDisposable.add(disposable)
    }

    override fun dispose() {
        mCompositeDisposable.dispose()
    }
}