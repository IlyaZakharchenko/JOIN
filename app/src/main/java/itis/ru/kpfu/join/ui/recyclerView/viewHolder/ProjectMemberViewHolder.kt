package itis.ru.kpfu.join.ui.recyclerView.viewHolder


import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.model.ProjectMember
import kotlinx.android.synthetic.main.item_project_member.view.tv_name

class ProjectMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindViewHolder(user: ProjectMember) = with(itemView) {

        tv_name.text = user.name
    }

}