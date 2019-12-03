package com.sample.news.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.sample.news.R
import com.sample.news.databinding.ListItemNewsBinding
import com.sample.news.model.Rows
import com.squareup.picasso.Picasso

class NewsListAdapter :
    RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(item: Rows)
    }

    var newsList: List<Rows?> = ArrayList()
    var listener: OnItemClickListener? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): NewsViewHolder {
        val listItemNewsBinding: ListItemNewsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(viewGroup.getContext()),
            R.layout.list_item_news, viewGroup, false
        )
        return NewsViewHolder(listItemNewsBinding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem: Rows? = newsList[position]
        holder.listItemNewsBinding.newsItem = newsItem
        holder.listItemNewsBinding.onClickListener = listener
    }

    fun setItemsAndListener(
        newsList: List<Rows>,
        listener: OnItemClickListener
    ) {
        this.newsList = newsList
        this.listener = listener
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    inner class NewsViewHolder(val listItemNewsBinding: ListItemNewsBinding) :
        RecyclerView.ViewHolder(listItemNewsBinding.getRoot())

    object ImageBindingAdapter {
        @BindingAdapter("bind:imageUrl")
        @JvmStatic
        fun loadImage(view: ImageView, imageUrl: String?) {
            if (imageUrl != null) {
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(view)
            }
        }
    }


}
