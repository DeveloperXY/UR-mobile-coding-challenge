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
class RepositoryAdapter(@NonNull repositories: MutableList<Repository>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var mRespositoryItems = repositories.map { RepositoryItem(it) }.toMutableList()

    companion object {
        const val VIEW_ITEM_TYPE = 0
        const val VIEW_PROGRESS_TYPE = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == VIEW_ITEM_TYPE)
        RepositoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.single_repository_layout,
                parent,
                false
            )
        )
    else ProgressViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.repository_loading_layout,
            parent,
            false
        )
    )

    override fun getItemCount() = mRespositoryItems.size

    override fun getItemViewType(position: Int) =
        if (mRespositoryItems[position].isProgressIndicator) VIEW_PROGRESS_TYPE else VIEW_ITEM_TYPE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RepositoryViewHolder)
            holder.bind(mRespositoryItems[position])
    }

    fun refresh(repositories: List<Repository>) {
        this.mRespositoryItems.clear()
        this.mRespositoryItems.addAll(repositories.map { RepositoryItem(it) }.toMutableList())
        notifyDataSetChanged()
    }

    fun addProgressItem() {
        mRespositoryItems.add(RepositoryItem(isProgressIndicator = true))
        notifyItemInserted(mRespositoryItems.size - 1)
    }

    fun removeProgressItem() {
        val indexOfLast = mRespositoryItems.size - 1
        mRespositoryItems.removeAt(indexOfLast)
        notifyItemRemoved(indexOfLast)
    }

    fun append(repositories: List<Repository>) {
        mRespositoryItems.addAll(repositories.map { RepositoryItem(it) })
        notifyDataSetChanged()
    }

    inner class RepositoryViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvRepoName: TextView = itemView.tvRepoName
        var tvRepoDescription: TextView = itemView.tvRepoDescription
        var ownerImage: ImageView = itemView.ownerImage
        var tvOwnerName: TextView = itemView.tvOwnerName
        var tvStarsCount: TextView = itemView.tvStarsCount

        fun bind(repositoryItem: RepositoryItem) {
            if (!repositoryItem.isProgressIndicator) {
                val repository = repositoryItem.repository!!

                tvRepoName.text = repository.name
                tvRepoDescription.text = repository.description
                tvOwnerName.text = repository.owner.name
                tvStarsCount.text = "${repository.starsCount}"

                val image = repository.owner.image
                if (image.isNotEmpty()) {
                    Glide.with(itemView.context)
                        .load(image)
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .into(ownerImage)
                }
            }
        }
    }

    inner class ProgressViewHolder(@NonNull itemView: View) : RecyclerView.ViewHolder(itemView)
}

data class RepositoryItem(var repository: Repository? = null, var isProgressIndicator: Boolean = false)