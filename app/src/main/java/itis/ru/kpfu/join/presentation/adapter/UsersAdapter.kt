package itis.ru.kpfu.join.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.network.pojo.ProjectMember
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter(
        private var users: MutableList<ProjectMember>,
        private val onInviteClick: (ProjectMember) -> Unit,
        private val onUserClick: (Long) -> Unit) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindViewHolder(users[position], onInviteClick, onUserClick)
    }

    fun setUsers(list: MutableList<ProjectMember>) {
        this.users = list
        notifyDataSetChanged()
    }

    fun setUserInvited(user: ProjectMember) {
        val position = users.indexOf(user)
        user.status = 2
        users[position] = user
        notifyItemChanged(position)
    }
    
    inner class UsersViewHolder(view: View) : RecyclerView.ViewHolder(view) {

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
}