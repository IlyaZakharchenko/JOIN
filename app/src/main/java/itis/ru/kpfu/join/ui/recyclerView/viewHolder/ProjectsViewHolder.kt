package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.api.model.Project
import kotlinx.android.synthetic.main.item_project.view.cg_vacancies
import kotlinx.android.synthetic.main.item_project.view.tv_project_description
import kotlinx.android.synthetic.main.item_project.view.tv_project_name

class ProjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Project, onClick: (Long) -> Unit) = with(itemView) {
        tv_project_name.text = item.name
        tv_project_description.text = item.description

        cg_vacancies.removeAllViews()

        item.vacancies?.forEach {
            if(!it.name.isNullOrEmpty()) {
                val chip = Chip(context)
                chip.chipText = it.name
                cg_vacancies.addView(chip)
            }
        }

        itemView.setOnClickListener { item.id?.let { it1 -> onClick(it1) } }
    }
}