package br.com.davidmag.bingewatcher.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.davidmag.bingewatcher.app.R
import br.com.davidmag.bingewatcher.app.databinding.GenreChipBinding
import br.com.davidmag.bingewatcher.presentation.model.GenrePresentation
import com.google.android.material.chip.Chip

class GenreAdapter(
    context : Context,
    var items : List<GenrePresentation> = arrayListOf(),
    private val isSelectable: Boolean = false,
    val onClick : (GenrePresentation, () -> Unit) -> Unit = {_, callback -> callback() }
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
        val genrePresentation = items[position]

        views.genreChip.text = items[position].id
        views.genreChip.setOnClickListener {
            genrePresentation.selected = !genrePresentation.selected
            onClick(genrePresentation) {
                selectChip(views.genreChip, genrePresentation.selected)
            }
        }

        selectChip(views.genreChip, genrePresentation.selected)
    }

    override fun getItemCount() = items.size

    private fun selectChip(chip: Chip, selected: Boolean) {
        if(isSelectable) chip.isSelected = selected
    }
}