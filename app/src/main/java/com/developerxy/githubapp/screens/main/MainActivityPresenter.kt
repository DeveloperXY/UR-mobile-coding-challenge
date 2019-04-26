package com.developerxy.githubapp.screens.main

import com.developerxy.githubapp.models.GithubErrorResponse
import com.developerxy.githubapp.models.Repository
import com.developerxy.githubapp.network.GithubClient
import com.developerxy.githubapp.network.ServiceGenerator
import com.google.gson.Gson
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
                    mView.showToast("Error: cannot fetch repositories.")
                },
                onNext = {
                    if (it.isSuccessful) {
                        val repos = it.body()!!.repositories
                        if (repos != null) {
                            if (repos.isEmpty()) {
                                mView.showSnackBar("All the available repositories have been loaded.")
                                mView.removeScrollListener()
                                mView.hideInfiniteLoadingIndicator()
                            } else {
                                mRepositories.addAll(repos)
                                if (page == 1)
                                    mView.showRepos(mRepositories)
                                else
                                    mView.appendRepos(repos)
                            }
                        }
                    } else {
                        val code = it.code()
                        val error = Gson().fromJson(it.errorBody()?.charStream(), GithubErrorResponse::class.java)
                        if (code == 403) {
                            mView.onQuotaAbused(error.message)
                        } else {
                            mView.showSnackBar("Some weird error occured.")
                            mView.removeScrollListener()
                        }
                    }
                },
                onComplete = mView::hideProgressBar
            )

        mCompositeDisposable.add(disposable)
    }

    override fun dispose() {
        mCompositeDisposable.dispose()
    }
}