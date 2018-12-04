package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout.Spec
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.SpecializationEditViewHolder

class SpecializationsEditAdapter(private var items: ArrayList<Specialization>,
        private var onItemRemove: (Int, Specialization) -> Unit,
        private var onItemEdit: (Int, Specialization) -> Unit) :
        RecyclerView.Adapter<SpecializationEditViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecializationEditViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_specialisation_edit, parent, false)
        return SpecializationEditViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SpecializationEditViewHolder, position: Int) {
        holder.bindViewHolder(items[position], onItemRemove, onItemEdit)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }

    fun updateItem(position: Int, item: Specialization) {
        items[position] = item
        notifyItemChanged(position)
    }

    fun addItem(item: Specialization) {
        items.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun getItems(): List<Specialization> {
        return items
    }
}