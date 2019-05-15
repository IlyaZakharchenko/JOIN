package itis.ru.kpfu.join.presentation.adapter

import android.support.design.chip.Chip
import android.support.design.chip.ChipDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.repository.impl.UserRepositoryImpl
import itis.ru.kpfu.join.presentation.model.ProjectModel
import kotlinx.android.synthetic.main.item_project.view.*

/**
 * Created by Ilya Zakharchenko on 05.12.2018.
 */
class ProjectsAdapter : RecyclerView.Adapter<ProjectsAdapter.ProjectsViewHolder>() {

    var items: List<ProjectModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onProjectClick: ((Long) -> Unit)? = null
    var onProjectDelete: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectsViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        holder.bindViewHolder(items[position], onProjectClick)
    }

    inner class ProjectsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(item: ProjectModel, onClick: ((Long) -> Unit)?) = with(itemView) {
            tv_project_name.text = item.name
            tv_project_description.text = item.description

            cg_vacancies.removeAllViews()

            item.vacancies?.forEach {
                if (!it.name.isNullOrEmpty()) {
                    val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.Widget_MaterialComponents_Chip_Action).apply {
                        rippleColor = null
                    }

                    val chip = Chip(context).apply {
                        setChipDrawable(chipDrawable)
                        chipText = it.name
                    }

                    cg_vacancies.addView(chip)
                }
            }

            if (item.leader?.id == UserRepositoryImpl().getUser()?.id) {
                btn_delete_project.visibility = View.VISIBLE
                btn_edit_project.visibility = View.VISIBLE
            } else {
                btn_delete_project.visibility = View.GONE
                btn_edit_project.visibility = View.GONE
            }

            btn_delete_project.setOnClickListener { onProjectDelete?.invoke(item.id ?: -1L) }
            itemView.setOnClickListener { item.id?.let { it1 -> onClick?.invoke(it1) } }
        }
    }
}