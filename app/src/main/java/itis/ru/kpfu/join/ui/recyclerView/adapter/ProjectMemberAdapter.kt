package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.model.ProjectMember
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.ProjectMemberViewHolder

class ProjectMemberAdapter(var items: List<ProjectMember>) : RecyclerView.Adapter<ProjectMemberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectMemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_member, parent, false)
        return ProjectMemberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProjectMemberViewHolder, position: Int) {
        holder.bindViewHolder(items[position])
    }
}