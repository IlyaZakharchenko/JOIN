package ru.sovcombank.mok.ui.adapter.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.TextMessage
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.BaseViewHolder
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.DateHeaderViewHolder
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.MessageLeftViewHolder
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.MessageRightViewHolder

class MessagesAdapter(private var messages: List<TextMessage>) :
        RecyclerView.Adapter<BaseViewHolder<TextMessage>>() {

    companion object {
        const val TEXT_MESSAGE_RIGHT = 0
        const val TEXT_MESSAGE_LEFT = 1
        const val DATE_HEADER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<TextMessage> {

        val v: View

        return when (viewType) {
            TEXT_MESSAGE_LEFT -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
                MessageLeftViewHolder(v)
            }
            TEXT_MESSAGE_RIGHT -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
                MessageRightViewHolder(v)
            }
            else -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_header, parent, false)
                DateHeaderViewHolder(v)
            }
        }
    }

    override fun getItemCount() = messages.size

    override fun onBindViewHolder(holder: BaseViewHolder<TextMessage>, position: Int) {
        holder.bindViewHolder(messages[position])
    }

    override fun getItemViewType(position: Int): Int {
            when {
                messages[position].text.isNullOrEmpty() -> return DATE_HEADER
                messages[position].to.equals("staff") -> return TEXT_MESSAGE_RIGHT
                messages[position].from.equals("staff") -> return TEXT_MESSAGE_LEFT
            }

        return 0
    }

    fun updMessages(messages: List<TextMessage>) {
        this.messages = messages
        notifyDataSetChanged()
    }
}