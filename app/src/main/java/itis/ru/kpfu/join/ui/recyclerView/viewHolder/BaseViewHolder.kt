package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindViewHolder(item: T)

}