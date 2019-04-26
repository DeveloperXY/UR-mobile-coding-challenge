package com.developerxy.githubapp.screens.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.INVISIBLE
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.developerxy.githubapp.R
import com.developerxy.githubapp.models.Repository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var mRepositoryAdapter: RepositoryAdapter
    private var visibleThreshold = 5
    private var lastVisibleItem = -1
    private var totalItemCount = -1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainActivityPresenter(this).apply { start() }
    }

    override fun setupRecyclerView() {
        mRepositoryAdapter = RepositoryAdapter(mutableListOf())
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mRepositoryAdapter

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = mRecyclerView.layoutManager as LinearLayoutManager
                totalItemCount = layoutManager.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    mRepositoryAdapter.addProgressItem()
                    mPresenter.loadNextPage()
                    isLoading = true
                }
            }
        })
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
}
