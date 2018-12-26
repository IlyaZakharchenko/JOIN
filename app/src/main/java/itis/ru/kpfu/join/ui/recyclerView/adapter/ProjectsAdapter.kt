package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Project
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.ProjectsViewHolder

/**
 * Created by Ilya Zakharchenko on 05.12.2018.
 */
class ProjectsAdapter(
        private var list: List<Project> = ArrayList(),
        private var onProjectClick: (Long) -> Unit) : RecyclerView.Adapter<ProjectsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project, parent, false)
        return ProjectsViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProjectsViewHolder, position: Int) {
        holder.bindViewHolder(list[position], onProjectClick)
    }

    fun setItems(list: List<Project>) {
        this.list = list
        notifyDataSetChanged()
    }
}