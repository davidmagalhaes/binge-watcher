package br.com.davidmag.bingewatcher.presentation.common.decorator

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import br.com.davidmag.bingewatcher.app.R

class VerticalSpaceItemDecoration(
    resources: Resources,
    @DimenRes dimen : Int = R.dimen.default_margin
) : ItemDecoration() {

    private val verticalSpacePixelValue =
        resources.getDimension(dimen).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if(parent.getChildAdapterPosition(view) == 0){
            outRect.top = verticalSpacePixelValue
        }

        outRect.right = verticalSpacePixelValue
        outRect.left = verticalSpacePixelValue
        outRect.bottom = verticalSpacePixelValue
    }

}