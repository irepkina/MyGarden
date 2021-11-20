package com.irepka3.mygarden.ui.util.recycleView

import android.content.res.Resources
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes

import androidx.recyclerview.widget.RecyclerView

/**
 * Декоратор для GridLayoutManager
 * @param spanCount количество колонок
 * @param spacingDp расстояние между колонками
 * @param includeEdge добавить отступ с края
 *
 * Created by i.repkina on 05.11.2021.
 */
class GridSpacingItemDecorator(
    resources: Resources,
    private val spanCount: Int,
    @DimenRes spacingDp: Int,
    private val includeEdge: Boolean
) : RecyclerView.ItemDecoration() {
    private val spacing: Int

    init {
        spacing = resources.getDimensionPixelSize(spacingDp)
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view) // позиция элемента
        val column = position % spanCount // колонка
        // вычисляем размер колонки, взависимости от того, нужно ли добавлять отступ от края
        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount
            outRect.right =
                (column + 1) * spacing / spanCount
            if (position < spanCount) {
                outRect.top = spacing
            }
            outRect.bottom = spacing
        } else {
            outRect.left = column * spacing / spanCount
            outRect.right =
                spacing - (column + 1) * spacing / spanCount
            if (position >= spanCount) {
                outRect.top = spacing
            }
        }
    }
}