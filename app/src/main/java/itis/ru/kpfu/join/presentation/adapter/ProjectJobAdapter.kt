package itis.ru.kpfu.join.presentation.adapter

import android.support.design.chip.Chip
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

class ProjectJobAdapter(
        private var items: List<Specialization> = ArrayList(),
        private var onApply: () -> Unit) : RecyclerView.Adapter<ProjectJobAdapter.ProjectJobViewHolder>() {

    private var isMyProject = false
    private var isInProject = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectJobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_job, parent, false)
        return ProjectJobViewHolder(view, isMyProject, isInProject)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProjectJobViewHolder, position: Int) {
        holder.bindViewHolder(items[position], onApply)
    }

    fun setJobs(list: List<Specialization>, isMyProject: Boolean, isInProject: Boolean) {
        this.isMyProject = isMyProject
        this.items = list
        this.isInProject = isInProject
        notifyDataSetChanged()
    }

    inner class ProjectJobViewHolder(
            view: View,
            private var isMyProject: Boolean,
            private var isInProject: Boolean) : RecyclerView.ViewHolder(view) {

        fun bindViewHolder(item: Specialization, onApply: () -> Unit) = with(itemView) {

            chip_container_job.removeAllViews()
            item.technologies?.let { initTechnologies(divideString(it)) }

            tv_job_experience.text = item.experience.toString()
            tv_job_lvl.text = parseLevelFromInt(item.knowledgeLevel)
            tv_job_name.text = item.name

            btn_send_apply.setOnClickListener {
                onApply()
                btn_send_apply.visibility = View.GONE
            }

            if (isMyProject or isInProject) {
                btn_send_apply.visibility = View.GONE
            }

            val rem = item.experience.rem(10)

            if (rem == 1 || item.experience > 20) {
                tv_job_experience_years.text = "год"
            } else if ((rem == 2 || rem == 3 || rem == 4) && item.experience < 20) {
                tv_job_experience_years.text = "года"
            } else {
                tv_job_experience_years.text = "лет"
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
                    chip_container_job.addView(chip)
                }
            }
        }
    }
}