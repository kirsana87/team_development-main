package pro.fateeva.pillsreminder.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter<Item>(
    itemList: List<Item>,
    @LayoutRes private val layout: Int,
    private val bindItem: View.(item: Item, position: Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var itemList: List<Item> = itemList
        set(value) {
            val callback = DiffUtilCallback(oldList = field, newList = value)
            field = value
            DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        bindItem(holder.itemView, item, position)
    }

    override fun getItemCount(): Int = itemList.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    class DiffUtilCallback<Item>(val oldList: List<Item>, val newList: List<Item>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return areContentsTheSame(oldItemPosition, newItemPosition)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem: Item = oldList[oldItemPosition]
            val newItem: Item = newList[newItemPosition]
            return oldItem == newItem
        }
    }
}