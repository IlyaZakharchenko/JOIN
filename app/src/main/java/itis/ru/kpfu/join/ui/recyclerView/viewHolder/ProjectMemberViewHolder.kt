package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.api.model.ProjectMember
import kotlinx.android.synthetic.main.item_project_member.view.civ_project_member
import kotlinx.android.synthetic.main.item_project_member.view.iv_is_leader
import kotlinx.android.synthetic.main.item_project_member.view.tv_name_project_member

class ProjectMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindViewHolder(user: ProjectMember, onUserClick: (Long) -> Unit) = with(itemView) {

        if (user.isLeader == true) iv_is_leader.visibility = View.VISIBLE else iv_is_leader.visibility = View.GONE

        tv_name_project_member.text = user.username


        user.profileImage?.let {
            Picasso.with(context)
                    .load(it)
                    .fit()
                    .into(civ_project_member)
        }

        itemView.setOnClickListener { user.id?.let { it1 -> onUserClick(it1) } }
    }
}