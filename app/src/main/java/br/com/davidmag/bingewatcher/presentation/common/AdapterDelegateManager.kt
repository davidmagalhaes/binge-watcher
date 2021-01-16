package br.com.davidmag.bingewatcher.presentation.common

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class AdapterDelegateManager(
    adapterDelegateSet : Set<AdapterDelegate> = HashSet(),
    private val shiftOrder : Int = 100
) {
    private val adapterDelegates = SparseArray<AdapterDelegate>()

    private val classViewTypeMap = HashMap<Class<*>, Int>()
    private var nextClassId : Int = 1

    init {
        adapterDelegateSet.forEach {
            addDelegate(it)
        }
    }

    fun addDelegate(adapterDelegate: AdapterDelegate) {
        val classId = classViewTypeMap[adapterDelegate.target.first] ?: shiftOrder * nextClassId++

        classViewTypeMap[adapterDelegate.target.first] = classId

        adapterDelegates.put(
            adapterDelegate.target.second + classId,
            adapterDelegate
        )

        listOf("c", "b").sorted()
    }

    fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent : ViewGroup,
        viewType : Int,
        extras : Map<String, Any?>
    ) : RecyclerView.ViewHolder{
        val adapterDelegate = adapterDelegates[viewType]

        return adapterDelegate?.onCreate?.invoke(
            inflater,
            parent,
            viewType,
            extras
        ) ?: error("br.com.davidmag.bingewatcher.presentation.common.AdapterDelegate not found for creating viewholder to viewtype $viewType")
    }

    fun onBindViewHolder(
        supportFragmentManager: FragmentManager,
        items: List<PresentationObject>,
        position : Int,
        viewHolder : RecyclerView.ViewHolder,
        extras : Map<String, Any?>
    ) {
        return adapterDelegates[viewHolder.itemViewType]?.onBind?.invoke(
            supportFragmentManager,
            items,
            viewHolder,
            position,
            extras
        ) ?: run {
            val item = items[position]
            error("br.com.davidmag.bingewatcher.presentation.common.AdapterDelegate not found for class ${item::class.qualifiedName} and viewType ${item.viewType}")
        }
    }

    fun getViewTypeFor(item : PresentationObject) : Int {
        return item.viewType + (
                classViewTypeMap[item.javaClass] ?:
                classViewTypeMap[PresentationObject::class.java] ?:
                error("Viewtype mapping not found for class ${item.javaClass.name}")
        )
    }
}