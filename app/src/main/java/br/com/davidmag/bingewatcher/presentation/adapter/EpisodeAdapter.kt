package br.com.davidmag.bingewatcher.presentation.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import br.com.davidmag.bingewatcher.GlideApp
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.ViewholderEpisodeBinding
import br.com.davidmag.bingewatcher.presentation.model.EpisodePresentation

class EpisodeAdapter(
    context : Context,
    var items : List<EpisodePresentation> = emptyList(),
    private val clickListener : (EpisodePresentation) -> Unit
) : RecyclerView.Adapter<EpisodeViewHolder>() {
    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            layoutInflater.inflate(
                R.layout.viewholder_episode,
                parent,
                false
            ),
            clickListener
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class EpisodeViewHolder(
    itemView : View,
    private val clickListener : (EpisodePresentation) -> Unit
) : RecyclerView.ViewHolder(itemView) {
    fun bind(episode : EpisodePresentation){
        val views = ViewholderEpisodeBinding.bind(itemView)

        itemView.setOnClickListener {
            clickListener(episode)
        }

        with(episode){
            views.episodeTitle.text = name
            views.episodeSummary.text = summary
            views.episodeNumberPremiered.text = subtitle

            GlideApp.with(views.episodePoster)
                .load(imageMediumUrl)
                .placeholder(R.drawable.placeholder_image_horizontal)
                .into(views.episodePoster)
        }
    }
}