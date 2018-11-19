package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.db.entity.TextMessage
import kotlinx.android.synthetic.main.item_message_right.view.*
import java.util.*

class MessageRightViewHolder(itemView: View) : BaseViewHolder<TextMessage>(itemView) {

    override fun bindViewHolder(item: TextMessage) = with(itemView) {
        tv_item_chat_right_text_message.text = item.text
        tv_item_chat_right_time.text = item.dateSend
    }
}