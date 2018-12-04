package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.utils.divideString
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.item_specialisation.view.chip_container
import kotlinx.android.synthetic.main.item_specialisation_edit.view.btn_edit
import kotlinx.android.synthetic.main.item_specialisation_edit.view.btn_remove
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_experience
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_experience_years
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_lvl
import kotlinx.android.synthetic.main.item_specialisation_edit.view.tv_spec_name

class SpecializationEditViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Specialization, onItemRemove: (Int, Specialization) -> Unit,
            onItemEdit: (Int, Specialization) -> Unit)= with(itemView) {

        chip_container.removeAllViews()
        item.technologies?.let { initTechnologies(divideString(it)) }

        btn_edit.setOnClickListener { onItemEdit(adapterPosition, item) }
        btn_remove.setOnClickListener { onItemRemove(adapterPosition, item) }

        tv_experience.text = item.experience.toString()
        tv_lvl.text = item.knowledgeLevel
        tv_spec_name.text = item.name

        val rem = item.experience.rem(10)

        if (rem == 1 && item.experience > 20) {
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
            val layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(toPx(8, context), 0, 0, toPx(8, context))
            chip.layoutParams = layoutParams
            chip_container.addView(chip)
        }
    }
}