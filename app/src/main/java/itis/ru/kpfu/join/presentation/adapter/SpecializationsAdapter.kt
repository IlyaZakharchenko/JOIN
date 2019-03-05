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
import kotlinx.android.synthetic.main.item_specialisation.view.*

class SpecializationsAdapter(private var items: List<Specialization> = ArrayList()) :
        RecyclerView.Adapter<SpecializationsAdapter.SpecializationViewHolder>() {

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

    fun setItems(items: List<Specialization>) {
        this.items = items
        notifyDataSetChanged()
    }
    
    inner class SpecializationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(item: Specialization) = with(itemView) {

            chip_container.removeAllViews()
            item.technologies?.let { initTechnologies(divideString(it)) }

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
                if (it.isNotEmpty()) {
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
}