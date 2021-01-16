package br.com.davidmag.bingewatcher.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

interface AdapterDelegate {
    val target : Pair<Class<*>, Int>

    val onCreate : (
        LayoutInflater,
        ViewGroup,
        Int,
        Map<String, Any?>
    ) -> RecyclerView.ViewHolder

    val onBind : (
        FragmentManager,
        List<PresentationObject>,
        RecyclerView.ViewHolder,
        Int,
        Map<String, Any?>
    ) -> Unit
}