package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.view.View
import itis.ru.kpfu.join.db.entity.TextMessage
import kotlinx.android.synthetic.main.item_message_header.view.tv_item_chat_date_header
import java.util.*

class DateHeaderViewHolder(itemView: View) : BaseViewHolder<TextMessage>(itemView) {

    override fun bindViewHolder(item: TextMessage) = with(itemView) {
        tv_item_chat_date_header.text = item.dateSend
    }
}
