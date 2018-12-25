package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.api.model.TextMessage
import kotlinx.android.synthetic.main.item_message_header.view.tv_item_chat_date_header

class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: TextMessage) = with(itemView) {
        tv_item_chat_date_header.text = item.dateSend
    }
}
