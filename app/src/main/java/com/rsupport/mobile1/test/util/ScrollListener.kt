package com.rsupport.mobile1.test.util

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

class ScrollListener(
    private val onLoad: () -> Unit
): RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val lastVisibleItemPosition = (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
        val itemTotalCount = recyclerView.adapter!!.itemCount - 1
        if (lastVisibleItemPosition == itemTotalCount) {
            onLoad()
        }
    }
}