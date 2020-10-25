package practice.app.mypaging3networkexample.presentation

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import practice.app.mypaging3networkexample.data.model.Repo

class MainAdapter(
    private val listener: MainAdapterListener
) : PagingDataAdapter<Repo, MainViewHolder>(diffItem()) {

    companion object {
        fun diffItem() = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }

        }
    }

    interface MainAdapterListener {
        fun onClicked(url: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}