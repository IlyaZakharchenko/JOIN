package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Specialization
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.ProjectJobViewHolder

class ProjectJobAdapter(private var items: List<Specialization> = ArrayList()) :
        RecyclerView.Adapter<ProjectJobViewHolder>() {

    private var isMyProject = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectJobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_job, parent, false)
        return ProjectJobViewHolder(view, isMyProject)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProjectJobViewHolder, position: Int) {
        holder.bindViewHolder(items[position])
    }

    fun setJobs(list: List<Specialization>, isMyProject: Boolean){
        this.isMyProject = isMyProject
        this.items = list
        notifyDataSetChanged()
    }
}