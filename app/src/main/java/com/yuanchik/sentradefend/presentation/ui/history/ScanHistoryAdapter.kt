package com.yuanchik.sentradefend.presentation.ui.history

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.entity.ScanResultEntity

class ScanHistoryAdapter :
    PagingDataAdapter<ScanResultEntity, ScanHistoryAdapter.ScanViewHolder>(DIFF_CALLBACK) {

    inner class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateView: TextView = itemView.findViewById(R.id.tvDate)
        val timeView: TextView = itemView.findViewById(R.id.tvTime)
        val resultView: TextView = itemView.findViewById(R.id.tvResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_scan_result, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val scan = getItem(position) ?: return
        holder.dateView.text = scan.date
        holder.timeView.text = scan.time
        holder.resultView.text = scan.result
        holder.resultView.setTextColor(
            if (scan.result.contains("No threats", true)) Color.GREEN
            else Color.RED
        )
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ScanResultEntity>() {
            override fun areItemsTheSame(oldItem: ScanResultEntity, newItem: ScanResultEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ScanResultEntity, newItem: ScanResultEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}

