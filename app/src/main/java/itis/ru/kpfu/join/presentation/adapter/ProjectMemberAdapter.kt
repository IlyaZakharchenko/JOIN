package itis.ru.kpfu.join.presentation.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.ProjectMemberModel
import kotlinx.android.synthetic.main.item_project_member.view.*

class ProjectMemberAdapter : RecyclerView.Adapter<ProjectMemberAdapter.ProjectMemberViewHolder>() {

    var items: List<ProjectMemberModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onUserClick: ((Long) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectMemberViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_project_member, parent, false)
        return ProjectMemberViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ProjectMemberViewHolder, position: Int) {
        holder.bindViewHolder()
    }

    inner class ProjectMemberViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindViewHolder() = with(itemView) {
            val user = items[adapterPosition]

            if (user.isLeader == true) iv_is_leader.visibility = View.VISIBLE else iv_is_leader.visibility = View.GONE

            tv_name_project_member.text = user.username


            user.profileImage?.let {
                Picasso.with(context)
                        .load(it)
                        .placeholder(R.drawable.download_progress)
                        .fit()
                        .into(civ_project_member)
            }

            itemView.setOnClickListener { user.id?.let { onUserClick?.invoke(it) } }
        }
    }
}