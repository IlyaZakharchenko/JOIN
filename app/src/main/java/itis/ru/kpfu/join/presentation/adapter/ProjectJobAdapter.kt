package itis.ru.kpfu.join.presentation.adapter

import android.support.design.chip.Chip
import android.support.design.chip.ChipDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.presentation.util.divideString
import itis.ru.kpfu.join.presentation.util.parseLevelFromInt
import itis.ru.kpfu.join.presentation.util.toPx
import kotlinx.android.synthetic.main.item_project_job.view.*

class ProjectJobAdapter : RecyclerView.Adapter<ProjectJobAdapter.ProjectJobViewHolder>() {

    var items: List<Specialization> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var isMyProject = false
    var isInProject = false

    var onApply: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectJobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_job, parent, false)
        return ProjectJobViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProjectJobViewHolder, position: Int) {
        holder.bindViewHolder()
    }

    inner class ProjectJobViewHolder(
            view: View) : RecyclerView.ViewHolder(view) {

        fun bindViewHolder() = with(itemView) {
            val item = items[adapterPosition]

            chip_container_job.removeAllViews()
            item.technologies?.let { initTechnologies(divideString(it)) }

            tv_job_experience.text = resources.getQuantityString(R.plurals.experience, item.experience, item.experience)
            tv_job_lvl.text = parseLevelFromInt(item.knowledgeLevel)
            tv_job_name.text = item.name
        }

        private fun initTechnologies(items: HashSet<String>) = with(itemView) {
            items.forEach {
                if (it.isNotEmpty()) {
                    val chipDrawable = ChipDrawable.createFromAttributes(context, null, 0, R.style.Widget_MaterialComponents_Chip_Action).apply {
                        rippleColor = null
                    }

                    val chip = Chip(context).apply {
                        setChipDrawable(chipDrawable)
                        chipText = it
                    }
                    chip_container_job.addView(chip)
                }
            }
        }
    }
}