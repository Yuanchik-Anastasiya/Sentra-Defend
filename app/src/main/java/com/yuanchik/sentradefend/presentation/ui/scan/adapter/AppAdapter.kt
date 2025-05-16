package com.yuanchik.sentradefend.presentation.ui.scan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.presentation.ui.scan.model.AppInfo
import android.widget.Filter

class AppAdapter(
    private val originalList: List<AppInfo>,
    private val onAppCheckedChanged: () -> Unit
) : RecyclerView.Adapter<AppAdapter.AppViewHolder>(), Filterable {

    private var filteredList = originalList.toMutableList()

    inner class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.appIcon)
        val name: TextView = itemView.findViewById(R.id.appName)
        val checkbox: CheckBox = itemView.findViewById(R.id.appCheckbox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
        return AppViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        val app = filteredList[position]
        holder.icon.setImageDrawable(app.icon)
        holder.name.text = app.appName

        holder.checkbox.setOnCheckedChangeListener(null)
        holder.checkbox.isChecked = app.isSelected

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            app.isSelected = isChecked
            onAppCheckedChanged()
        }
    }

    override fun getItemCount(): Int = filteredList.size

    fun getFilteredList(): List<AppInfo> = filteredList

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(query: CharSequence?): FilterResults {
                val search = query.toString().trim().lowercase()
                val results = if (search.isEmpty()) {
                    originalList
                } else {
                    originalList.filter {
                        it.appName.lowercase().contains(search)
                    }
                }

                return FilterResults().apply { values = results }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = (results?.values as? List<AppInfo>)?.toMutableList() ?: mutableListOf()
                notifyDataSetChanged()
            }
        }
    }
}