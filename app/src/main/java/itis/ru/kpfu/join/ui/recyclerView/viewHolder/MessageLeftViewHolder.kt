package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.db.entity.TextMessage
import kotlinx.android.synthetic.main.item_message_left.view.*
import java.util.*

class MessageLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: TextMessage) = with(itemView) {
        tv_item_chat_left_text_message.text = item.text
        tv_item_chat_left_time.text = item.dateSend
    }
}