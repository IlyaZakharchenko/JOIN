package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.model.ProjectMember
import kotlinx.android.synthetic.main.item_user.view.civ_user
import kotlinx.android.synthetic.main.item_user.view.tv_name_user
import java.io.File

class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindViewHolder(user: ProjectMember, onClick: (ProjectMember) -> Unit) = with(itemView) {

        tv_name_user.text = user.username

        if (user.profileImage != null) {
            Picasso.with(context)
                    .load(user.profileImage)
                    .placeholder(R.drawable.progress_animation)
                    .into(civ_user)
        }

        itemView.setOnClickListener { onClick(user) }
    }
}