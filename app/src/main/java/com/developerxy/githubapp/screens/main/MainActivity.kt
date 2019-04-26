package com.developerxy.githubapp.screens.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.developerxy.githubapp.R
import com.developerxy.githubapp.models.Repository
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityContract.View {

    private lateinit var mPresenter: MainActivityContract.Presenter
    private lateinit var mRepositoryAdapter: RepositoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mPresenter = MainActivityPresenter(this).apply { start() }
    }

    override fun setupRecyclerView() {
        mRepositoryAdapter = RepositoryAdapter(mutableListOf())
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mRepositoryAdapter
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

    override fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun hideProgressBar() {
        mProgressBar.visibility = INVISIBLE
    }
}
