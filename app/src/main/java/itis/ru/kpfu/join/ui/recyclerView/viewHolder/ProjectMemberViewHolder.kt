package itis.ru.kpfu.join.ui.recyclerView.viewHolder


import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.api.model.ProjectMember
import kotlinx.android.synthetic.main.item_project_member.view.civ_project_member
import kotlinx.android.synthetic.main.item_project_member.view.tv_name_project_member

class ProjectMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindViewHolder(user: ProjectMember) = with(itemView) {

        tv_name_project_member.text = user.username

        Picasso.with(context)
                .load(user.profileImage)
                .fit()
                .into(civ_project_member)
    }

}