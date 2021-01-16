package br.com.davidmag.bingewatcher.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject

open class DelegatedAdapter (
    context : FragmentActivity,
    var items : List<PresentationObject> = ArrayList(),
    var extras : HashMap<String, Any?> = HashMap()
) : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    @Inject
    lateinit var adapterDelegateManager: AdapterDelegateManager

    private val layoutInflater = LayoutInflater.from(context)
    private val supportFragmentManager = context.supportFragmentManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapterDelegateManager.onCreateViewHolder(
            layoutInflater,
            parent,
            viewType,
            extras
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapterDelegateManager.onBindViewHolder(
            supportFragmentManager,
            items,
            position,
            holder,
            extras
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return adapterDelegateManager.getViewTypeFor(items[position])
    }
}