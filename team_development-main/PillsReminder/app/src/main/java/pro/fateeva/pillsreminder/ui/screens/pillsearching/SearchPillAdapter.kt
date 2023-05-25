package pro.fateeva.pillsreminder.ui.screens.pillsearching

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pro.fateeva.pillsreminder.clean.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.databinding.ItemSearchPillBinding

class SearchPillAdapter(private val clickListener: SearchItemClickListener) :
    RecyclerView.Adapter<SearchPillAdapter.SearchPillViewHolder>() {

    private var dataList = listOf<DrugDomain>()

    fun setData(dataList: List<DrugDomain>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchPillViewHolder {
        return SearchPillViewHolder((ItemSearchPillBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
            .root)
    }

    override fun onBindViewHolder(holder: SearchPillViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    inner class SearchPillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(drugDomain: DrugDomain) {
            ItemSearchPillBinding.bind(itemView).apply {
                searchPillItemPillNameTextView.text = drugDomain.drugName
            }

            itemView.setOnClickListener {
                clickListener.onSearchItemClick(drugDomain)
            }
        }
    }
}

interface SearchItemClickListener {
    fun onSearchItemClick(drugDomain: DrugDomain)
}