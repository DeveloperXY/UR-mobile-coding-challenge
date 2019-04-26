package com.developerxy.githubapp.screens.main

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.*
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.developerxy.githubapp.R
import com.developerxy.githubapp.models.Repository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    companion object {
        const val RETRY_GAP_DURATION = 40_000L
    }

    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var mRepositoryAdapter: RepositoryAdapter
    private var visibleThreshold = 5
    private var lastVisibleItem = -1
    private var totalItemCount = -1
    private var isLoading = false
    private lateinit var mScrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainActivityPresenter(this).apply { start() }
    }

    override fun setupRecyclerView() {
        mRepositoryAdapter = RepositoryAdapter(mutableListOf(), mRecyclerView)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mRepositoryAdapter

        attachScrollListener()
    }

    override fun showRepos(repositories: List<Repository>) {
        val controller = AnimationUtils.loadLayoutAnimation(mRecyclerView.context, R.anim.layout_animation_fall_down)
        mRecyclerView.apply {
            layoutAnimation = controller
            mRepositoryAdapter.refresh(repositories)
            post {
                scheduleLayoutAnimation()
            }
        }
    }

    override fun appendRepos(repositories: List<Repository>) {
        mRepositoryAdapter.removeProgressItem()
        mRepositoryAdapter.append(repositories)
        isLoading = false
    }

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = INVISIBLE
    }

    override fun hideInfiniteLoadingIndicator() {
        mRepositoryAdapter.removeProgressItem()
    }

    override fun showSnackBar(message: String) {
        Snackbar.make(window.decorView.findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
    }

    override fun removeScrollListener() {
        mRecyclerView.removeOnScrollListener(mScrollListener)
        isLoading = false
    }

    override fun onQuotaAbused(message: String?) {
        removeScrollListener()

        val dialog = AlertDialog.Builder(this).apply {
            setTitle("403 FORBIDDEN")
            setMessage(message)
            setPositiveButton("I promise to scroll slowly next time") { _, _ ->

            }
        }.create()

        dialog.setOnDismissListener {
            Handler().post {
                mRepositoryAdapter.removeProgressItem()
                showRetryNotice()

                object : CountDownTimer(RETRY_GAP_DURATION, 1000L) {

                    override fun onFinish() {
                        hideRetryNotice()
                        loadMoreData()
                        mRecyclerView.post {
                            attachScrollListener()
                        }
                    }

                    override fun onTick(millisUntilFinished: Long) {
                        updateRetryNoticeText("Retrying in ${millisUntilFinished / 1000} seconds...")
                    }

                }.start()
            }
        }

        dialog.show()
    }

    private fun updateRetryNoticeText(message: String) {
        tvRetryNotice.post {
            tvRetryNotice.text = message
        }
    }

    private fun hideRetryNotice() {
        tvRetryNotice.post {
            tvRetryNotice.visibility = GONE
        }
    }

    private fun showRetryNotice() {
        tvRetryNotice.post {
            tvRetryNotice.visibility = VISIBLE
        }
    }

    private fun loadMoreData() {
        mRepositoryAdapter.addProgressItem()
        mPresenter.loadNextPage()
        isLoading = true
    }

    private fun attachScrollListener() {
        mScrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
                totalItemCount = layoutManager.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold))
                    loadMoreData()

            }
        }
        mRecyclerView.addOnScrollListener(mScrollListener)
    }
}
