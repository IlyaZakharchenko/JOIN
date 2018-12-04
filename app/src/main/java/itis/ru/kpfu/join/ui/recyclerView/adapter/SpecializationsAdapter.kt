package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.SpecializationViewHolder

class SpecializationsAdapter(private var items: List<Specialization>) :
        RecyclerView.Adapter<SpecializationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecializationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_specialisation, parent, false)
        return SpecializationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpecializationViewHolder, position: Int) {
        holder.bindViewHolder(items[position])
    }
}