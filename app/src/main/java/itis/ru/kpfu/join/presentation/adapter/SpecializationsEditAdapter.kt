package itis.ru.kpfu.join.presentation.adapter

import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.utils.divideString
import itis.ru.kpfu.join.utils.parseLevelFromInt
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.item_specialisation_edit.view.*

class SpecializationsEditAdapter(private var items: MutableList<Specialization>,
        private var onItemRemove: (Int, Specialization) -> Unit,
        private var onItemEdit: (Int, Specialization) -> Unit) :
        RecyclerView.Adapter<SpecializationsEditAdapter.SpecializationEditViewHolder>() {

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
    
    inner class SpecializationEditViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(item: Specialization, onItemRemove: (Int, Specialization) -> Unit,
                           onItemEdit: (Int, Specialization) -> Unit)= with(itemView) {

            chip_container.removeAllViews()
            item.technologies?.let { initTechnologies(divideString(it)) }

            btn_edit.setOnClickListener { onItemEdit(adapterPosition, item) }
            btn_remove.setOnClickListener { onItemRemove(adapterPosition, item) }

            tv_experience.text = item.experience.toString()
            tv_lvl.text = parseLevelFromInt(item.knowledgeLevel)
            tv_name.text = item.name

            val rem = item.experience.rem(10)

            if (rem == 1 || item.experience > 20) {
                tv_experience_years.text = "год"
            } else if ((rem == 2 || rem == 3 || rem == 4) && item.experience < 20) {
                tv_experience_years.text = "года"
            } else {
                tv_experience_years.text = "лет"
            }
        }

        private fun initTechnologies(items: HashSet<String>) = with(itemView) {
            items.forEach {
                val chip = Chip(context)
                chip.chipText = it
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(toPx(8, context), 0, 0, toPx(8, context))
                chip.layoutParams = layoutParams
                chip_container.addView(chip)
            }
        }
    }
}