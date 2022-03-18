package br.com.davidmag.bingewatcher.presentation.common

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// Workaround needed because of a PagingLib3 bug
fun RecyclerView.scrollToPositionAbsolute(index: Int) {
    stopScroll()

    var firstView = getChildAt(0)
    scrollToPosition(0)

    post(object : Runnable {
        override fun run() {
            if(firstView != getChildAt(0)) {
                firstView = getChildAt(0)
                scrollToPosition(0)
                post(this)
            }
        }
    })
}

var FloatingActionButton.shouldShow: Boolean
    get() = isOrWillBeShown
    set(value) {
        if(value && isOrWillBeHidden) show()
        else if(!value && isOrWillBeShown) hide()
    }