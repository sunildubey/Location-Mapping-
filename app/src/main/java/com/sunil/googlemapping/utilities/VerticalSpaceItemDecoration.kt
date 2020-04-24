package com.sunil.googlemapping.utilities


import android.content.Context
import android.graphics.Rect
import android.util.DisplayMetrics
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.sunil.googlemapping.R

class VerticalSpaceItemDecoration() : RecyclerView.ItemDecoration() {

    var horizontalSpaceHeight = 0

    constructor (mContext: Context) : this() {
        this.horizontalSpaceHeight = mContext.resources.getDimension(R.dimen.normal_margin).toInt()
    }

    constructor(horizontalSpaceHeight: Int) : this() {
        this.horizontalSpaceHeight = horizontalSpaceHeight
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        return Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT).toFloat())
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view!!) != parent.adapter!!.itemCount - 1) {
            outRect.bottom = horizontalSpaceHeight
        }
    }
}
