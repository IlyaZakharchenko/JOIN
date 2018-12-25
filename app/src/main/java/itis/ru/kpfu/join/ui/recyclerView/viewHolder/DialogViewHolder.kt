package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View
import itis.ru.kpfu.join.api.model.Dialog
import kotlinx.android.synthetic.main.item_dialog.view.*

class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Dialog, onItemClick: (Dialog) -> Unit) = with(itemView) {
        setOnClickListener { onItemClick(item) }

        dialog_last_message.text = item.lastMessage?.text
        dialog_time.text = item.lastMessage?.dateSend
        dialog_title.text = item.dialogName
        item.image?.let { dialog_image.setImageResource(it) }

        if(item.lastMessage?.to == "staff") {
            dialog_you.visibility = View.VISIBLE
        } else {
            dialog_you.visibility = View.GONE
        }
    }
}
