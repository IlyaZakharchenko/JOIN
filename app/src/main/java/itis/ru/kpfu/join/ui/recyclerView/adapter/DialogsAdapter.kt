package ru.sovcombank.mok.ui.adapter.recyclerview

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Dialog
import itis.ru.kpfu.join.ui.recyclerView.viewHolder.DialogViewHolder

class DialogsAdapter(private var dialogs: List<Dialog>, private val onChatClick: (Dialog) -> Unit) :
        RecyclerView.Adapter<DialogViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_dialog, parent, false)
        return DialogViewHolder(v)
    }

    override fun onBindViewHolder(holder: DialogViewHolder, position: Int) {
        holder.bindViewHolder(getItem(position), onChatClick)
    }

    override fun getItemCount(): Int {
        return dialogs.size
    }

    private fun getItem(position: Int): Dialog {
        return dialogs[position]
    }

    fun setItems(items: ArrayList<Dialog>) {
        this.dialogs = items
        notifyDataSetChanged()
    }

    private fun clearList() {
        dialogs = ArrayList()
    }
}
