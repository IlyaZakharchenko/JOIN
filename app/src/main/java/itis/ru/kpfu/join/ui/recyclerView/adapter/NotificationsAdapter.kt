package itis.ru.kpfu.join.ui.recyclerView.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Notification
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.NotificationsViewHolder

class NotificationsAdapter(
        private var list: List<Notification> = ArrayList(),
        private var onAccept: (Long) -> Unit,
        private var onDecline: (Long) -> Unit,
        private var onProjectClick: (Long) -> Unit,
        private var onUsernameClick: (Long) -> Unit) : RecyclerView.Adapter<NotificationsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
        return NotificationsViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {
        holder.bindViewHolder(list[position], onAccept, onDecline, onProjectClick, onUsernameClick)
    }

    fun setItems(list: List<Notification>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun removeElement(position: Int) {
        list.drop(position)
        notifyDataSetChanged()
    }
}