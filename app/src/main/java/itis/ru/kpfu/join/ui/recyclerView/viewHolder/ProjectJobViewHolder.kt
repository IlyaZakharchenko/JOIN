package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.design.chip.Chip
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.utils.divideString
import itis.ru.kpfu.join.utils.parseLevelFromInt
import itis.ru.kpfu.join.utils.toPx
import kotlinx.android.synthetic.main.item_project_job.view.btn_send_apply
import kotlinx.android.synthetic.main.item_project_job.view.chip_container_job
import kotlinx.android.synthetic.main.item_project_job.view.tv_job_experience
import kotlinx.android.synthetic.main.item_project_job.view.tv_job_experience_years
import kotlinx.android.synthetic.main.item_project_job.view.tv_job_lvl
import kotlinx.android.synthetic.main.item_project_job.view.tv_job_name

class ProjectJobViewHolder(
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
                val layoutParams = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                layoutParams.setMargins(toPx(8, context), 0, 0, toPx(8, context))
                chip.layoutParams = layoutParams
                chip_container_job.addView(chip)
            }
        }
    }
}