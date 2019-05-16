package itis.ru.kpfu.join.presentation.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.DialogModel
import itis.ru.kpfu.join.presentation.model.MessageFromType
import itis.ru.kpfu.join.presentation.util.color.ColorGenerator
import itis.ru.kpfu.join.presentation.util.extensions.toPresentationHourMinute
import itis.ru.kpfu.join.presentation.util.getDialogAvatar
import kotlinx.android.synthetic.main.item_dialog.view.*

class DialogListAdapter : RecyclerView.Adapter<DialogListAdapter.DialogViewHolder>() {

    var items: List<DialogModel> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onChatClick: ((DialogModel) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        return DialogViewHolder(v)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bindViewHolder()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder() = with(itemView) {
            val item = items[adapterPosition]

            setOnClickListener { onChatClick?.invoke(item) }

            dialog_last_message.text = item.lastMessage
            dialog_time.text = item.lastMessageDate?.toPresentationHourMinute()
            dialog_title.text = item.username

            when (item.lastMessageFrom) {
                MessageFromType.ME -> dialog_you.visibility = View.VISIBLE
                else -> dialog_you.visibility = View.GONE
            }

            tv_dialog_avatar.text = item.username?.getDialogAvatar()
            container_dialog_avatar.background.setTint(ColorGenerator.getColor(context))
        }
    }
}
