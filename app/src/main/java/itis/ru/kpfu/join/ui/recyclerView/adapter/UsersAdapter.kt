package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.ProjectMember
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.UsersViewHolder

class UsersAdapter(
        private var users: MutableList<ProjectMember>,
        private val onInviteClick: (ProjectMember) -> Unit,
        private val onUserClick: (Long) -> Unit) : RecyclerView.Adapter<UsersViewHolder>() {

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
}