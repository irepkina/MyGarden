package com.irepka3.mygarden.ui.util.recycleView

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

/**
 * Хелпер для удаления свайпом и перемещения элементов в RecycleView
 *
 * Created by i.repkina on 03.11.2021.
 */
object ItemTouchHelperFactory {
    /**
     * Создает [ItemTouchHelper]
     * @param deleteFunc функция для удаления элемента из списка, в функцию передается позиция адаптера
     * todo: сделать изменение порядка клумб перетаскиванием
     */
    fun getItemTouchHelper(
        deleteFunc: (Int) -> Unit
    ): ItemTouchHelper {
        val simpleCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.2f
                    viewHolder?.itemView?.scaleX = 3f
                    viewHolder?.itemView?.scaleY = 3f
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                viewHolder.itemView.alpha = 1f
                viewHolder.itemView.scaleX = 1f
                viewHolder.itemView.scaleY = 1f
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.LEFT

                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> deleteFunc(viewHolder.adapterPosition)
                }
            }
        }
        return ItemTouchHelper(simpleCallback)
    }
}