package com.rsupport.mobile1.test.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rsupport.mobile1.test.utils.Utils

class AdapterDecoration(margin : Int) : RecyclerView.ItemDecoration(){

    private val margin = margin

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val postion = parent.getChildAdapterPosition(view)
        outRect.left = margin
        outRect.top = margin

        if (postion % 3 == 2){
           outRect.right = Utils.toDp(parent.context,margin)
        }

    }
}