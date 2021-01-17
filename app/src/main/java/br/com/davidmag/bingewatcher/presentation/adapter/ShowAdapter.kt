package br.com.davidmag.bingewatcher.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.presentation.model.ShowPresentation

class ShowAdapter (
    context : Context,
    var items : List<ShowPresentation> = ArrayList(),
) : PagedListAdapter<ShowPresentation, ShowViewHolder>(NewsDiffCallback) {

    private val layoutInflater = LayoutInflater.from(context)

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
        return ShowViewHolder(
            layoutInflater.inflate(R.layout.viewholder_show, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShowViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}

class ShowViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
    fun bind(showPresentation: ShowPresentation){

    }
}