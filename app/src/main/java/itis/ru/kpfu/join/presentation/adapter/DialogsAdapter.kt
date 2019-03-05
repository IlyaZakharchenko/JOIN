package itis.ru.kpfu.join.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.network.pojo.Dialog
import kotlinx.android.synthetic.main.item_dialog.view.*

class DialogsAdapter : RecyclerView.Adapter<DialogsAdapter.DialogViewHolder>() {

    var items: List<Dialog> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onChatClick: ((Dialog) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        return DialogViewHolder(v)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bindViewHolder(getItem(position), onChatClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun getItem(position: Int): Dialog {
        return items[position]
    }

    inner class DialogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(item: Dialog, onItemClick: ((Dialog) -> Unit)?) = with(itemView) {
            setOnClickListener { onItemClick?.invoke(item) }

            dialog_last_message.text = item.lastMessage?.text
            dialog_time.text = item.lastMessage?.dateSend
            dialog_title.text = item.dialogName
            item.image?.let { dialog_image.setImageResource(it) }

            if (item.lastMessage?.to == "staff") {
                dialog_you.visibility = View.VISIBLE
            } else {
                dialog_you.visibility = View.GONE
            }
        }
    }
}
