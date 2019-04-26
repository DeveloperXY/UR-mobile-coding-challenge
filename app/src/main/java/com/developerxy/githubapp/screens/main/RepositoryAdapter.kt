package com.developerxy.githubapp.screens.main

import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.developerxy.githubapp.R
import com.developerxy.githubapp.models.Repository
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.single_repository_layout.view.*

/**
 * Created by Mohammed Aouf ZOUAG on 4/26/2019.
 */
class RepositoryAdapter(var repositories: List<Repository>) : RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val rootView = LayoutInflater.from(parent.context).inflate(R.layout.single_repository_layout, parent, false)
        return RepositoryViewHolder(rootView)
    }

    override fun getItemCount() = repositories.size

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    fun refresh(repositories: List<Repository>) {
        this.repositories = repositories
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRepoName: TextView = itemView.tvRepoName
        var tvRepoDescription: TextView = itemView.tvRepoDescription
        var ownerImage: ImageView = itemView.ownerImage
        var tvOwnerName: TextView = itemView.tvOwnerName
        var tvStarsCount: TextView = itemView.tvStarsCount

        fun bind(repository: Repository) {
            tvRepoName.text = repository.name
            tvRepoDescription.text = repository.description
            tvOwnerName.text = repository.owner.name
            tvStarsCount.text = "${repository.starsCount}"

            Glide.with(itemView.context)
                .load(repository.owner.image)
                .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                .into(ownerImage)
        }
    }
}