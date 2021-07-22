package by.slavintodron.pomodoro

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import by.slavintodron.pomodoro.databinding.ItemWatchBinding

class WatchAdapter(private val listener: WatchListener) :
    ListAdapter<ItemWatch, WatchViewHolder>(itemComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemWatchBinding.inflate(layoutInflater, parent, false)
        return WatchViewHolder(binding, listener, binding.root.context.resources)
    }

    override fun onBindViewHolder(holder: WatchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private companion object {

        private val itemComparator = object : DiffUtil.ItemCallback<ItemWatch>() {

            override fun areItemsTheSame(oldItem: ItemWatch, newItem: ItemWatch): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ItemWatch, newItem: ItemWatch): Boolean {
                return oldItem.currentMs == newItem.currentMs &&
                        oldItem.isStarted == newItem.isStarted
            }
        }
    }
}