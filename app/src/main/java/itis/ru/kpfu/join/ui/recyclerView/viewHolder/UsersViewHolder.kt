package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.ProjectMember
import kotlinx.android.synthetic.main.item_user.view.btn_add_user
import kotlinx.android.synthetic.main.item_user.view.civ_user
import kotlinx.android.synthetic.main.item_user.view.tv_name_user

class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindViewHolder(
            user: ProjectMember,
            onInviteClick: (ProjectMember) -> Unit,
            onUserClick: (Long) -> Unit) = with(itemView) {

        civ_user.setImageResource(R.drawable.ic_no_avatar)

        tv_name_user.text = user.username

        if (user.profileImage != null) {
            Picasso.with(context)
                    .load(user.profileImage)
                    .placeholder(R.drawable.progress_animation)
                    .into(civ_user)
        }

        itemView.setOnClickListener { user.id?.let { it1 -> onUserClick(it1) } }

        if (user.status == 1 || user.status == 2) {
            btn_add_user.visibility = View.GONE
        } else {
            btn_add_user.visibility = View.VISIBLE
            btn_add_user.setOnClickListener { onInviteClick(user) }
        }
    }
}