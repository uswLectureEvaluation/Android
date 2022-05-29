package com.kunize.uswtimetable.ui.write

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kunize.uswtimetable.databinding.ItemDialogSpinnerSemesterBinding
import com.kunize.uswtimetable.databinding.ItemDialogSpinnerTestBinding

class TestListAdapter(private val viewModel: WriteViewModel) : ListAdapter<String, TestListAdapter.SortViewHolder>(diffUtil) {
    inner class SortViewHolder(private val binding: ItemDialogSpinnerTestBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(text: String) {
                binding.viewModel = viewModel
                binding.text = text
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SortViewHolder {
        return SortViewHolder(ItemDialogSpinnerTestBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: SortViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        private val diffUtil = object: DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}