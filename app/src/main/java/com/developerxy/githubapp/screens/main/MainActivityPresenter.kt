package com.developerxy.githubapp.screens.main

import com.developerxy.githubapp.models.Repository
import com.developerxy.githubapp.network.GithubClient
import com.developerxy.githubapp.network.ServiceGenerator
import io.reactivex.android.schedulers.AndroidSchedulers
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
    private var mCurrentPage = 1
    private val mRepositories = mutableListOf<Repository>()

    override fun start() {
        mView.setupRecyclerView()
        loadPage(mCurrentPage)
    }

    override fun loadNextPage() {
        mCurrentPage++
        loadPage(mCurrentPage)
    }

    private fun loadPage(page: Int) {
        mCurrentPage = page
        val disposable = mGithubClient.getRepos(page = page)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    mView.showToast("Cannot fetch repositories right now.")
                },
                onNext = {
                    mRepositories.addAll(it.repositories)
                    if (page == 1)
                        mView.showRepos(mRepositories)
                    else
                        mView.appendRepos(it.repositories)
                },
                onComplete = mView::hideProgressBar
            )

        mCompositeDisposable.add(disposable)
    }

    override fun dispose() {
        mCompositeDisposable.dispose()
    }
}