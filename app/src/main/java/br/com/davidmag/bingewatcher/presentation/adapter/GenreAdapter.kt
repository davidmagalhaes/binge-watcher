package br.com.davidmag.bingewatcher.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.GenreChipBinding

class GenreAdapter(
    context : Context,
    var items : List<String> = arrayListOf()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            layoutInflater.inflate(
                R.layout.genre_chip,
                parent,
                false
            )
        ){}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val views = GenreChipBinding.bind(holder.itemView)

        views.genreChip.text = items[position]
    }

    override fun getItemCount() = items.size
}