package br.com.davidmag.bingewatcher.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.davidmag.bingewatcher.GlideApp
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.ViewholderShowBinding
import br.com.davidmag.bingewatcher.presentation.common.decorator.HorizontalSpaceItemDecoration
import br.com.davidmag.bingewatcher.presentation.model.GenrePresentation
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation

class ShowAdapter (
    context : Context,
    private val clickListener : (ShowPresentation) -> Unit
) : PagingDataAdapter<ShowPresentation, ShowViewHolder>(NewsDiffCallback) {

    private val layoutInflater = LayoutInflater.from(context)
    private val resources = context.resources

    companion object {
        val NewsDiffCallback = object : DiffUtil.ItemCallback<ShowPresentation>() {
            override fun areItemsTheSame(
                oldItem: ShowPresentation,
                newItem: ShowPresentation
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ShowPresentation,
                newItem: ShowPresentation
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowViewHolder {
        val views = ViewholderShowBinding.inflate(layoutInflater, parent, false)

        views.showGenres.addItemDecoration(
            HorizontalSpaceItemDecoration(resources, R.dimen.small_margin)
        )

        return ShowViewHolder(views.root, clickListener)
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}

class ShowViewHolder(
    itemView : View,
    private val clickListener : (ShowPresentation) -> Unit
) : RecyclerView.ViewHolder(itemView){

    fun bind(showPresentation: ShowPresentation){
        val views = ViewholderShowBinding.bind(itemView)

        itemView.setOnClickListener {
            clickListener(showPresentation)
        }

        with(showPresentation){
            GlideApp.with(itemView)
                .load(originalImage)
                .placeholder(R.drawable.poster_placeholder)
                .into(views.showPoster)

            views.showRating.rating = ratingAvg
            views.showTitle.text = name
            views.showStatus.text = status
            views.showPremiere.text = premiered
            views.showGenres.adapter = GenreAdapter(
                itemView.context.applicationContext,
                showPresentation.genres.map { GenrePresentation(it) }
            )
        }
    }
}