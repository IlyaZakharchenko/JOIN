package itis.ru.kpfu.join.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.MessageFromType
import itis.ru.kpfu.join.presentation.model.MessageModel
import itis.ru.kpfu.join.presentation.util.extensions.toPresentationHourMinute
import kotlinx.android.synthetic.main.item_message_left.view.*
import kotlinx.android.synthetic.main.item_message_right.view.*
import java.text.SimpleDateFormat
import java.util.*

class DialogAdapter(options: FirebaseRecyclerOptions<MessageModel>) :
        FirebaseRecyclerAdapter<MessageModel, RecyclerView.ViewHolder>(options) {

    companion object {
        const val TEXT_MESSAGE_RIGHT = 0
        const val TEXT_MESSAGE_LEFT = 1
    }

    var onDataChangeListener: ((MutableList<String>) -> Unit)? = null

    override fun onDataChanged() {
        super.onDataChanged()
        val items = mutableListOf<String>()

        for (i in 0 until itemCount) {
            items.add(getHeaderTitle(getItem(i).createdDate))
        }

        onDataChangeListener?.invoke(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val v: View?
        return when (viewType) {
            TEXT_MESSAGE_LEFT -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_left, parent, false)
                MessageLeftViewHolder(v)
            }
            else -> {
                v = LayoutInflater.from(parent.context).inflate(R.layout.item_message_right, parent, false)
                MessageRightViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, model: MessageModel) {
        (holder as? MessageRightViewHolder)?.bindViewHolder(model)
        when (holder.itemViewType) {
            TEXT_MESSAGE_LEFT -> (holder as? MessageLeftViewHolder)?.bindViewHolder(model)
            TEXT_MESSAGE_RIGHT -> (holder as? MessageRightViewHolder)?.bindViewHolder(model)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) as? MessageModel

        return when {
            item?.messageFrom == MessageFromType.ME -> TEXT_MESSAGE_RIGHT
            else -> TEXT_MESSAGE_LEFT
        }
    }

    private fun getHeaderTitle(date: Date?): String {
        val format = SimpleDateFormat("d MMMM", Locale.getDefault())

        date?.let {
            return when {
                isToday(date) -> "Сегодня"
                isYesterday(date) -> "Вчера"
                else -> format.format(date)
            }
        }
        return ""
    }

    private fun isToday(date: Date): Boolean {
        val sourceCalendar = Calendar.getInstance()
        val currentCalendar = Calendar.getInstance()
        sourceCalendar.time = date
        currentCalendar.time = Date(System.currentTimeMillis())
        return currentCalendar.get(Calendar.YEAR) == sourceCalendar.get(Calendar.YEAR) && currentCalendar.get(Calendar.DAY_OF_YEAR) == sourceCalendar.get(
                Calendar.DAY_OF_YEAR
        )
    }

    private fun isYesterday(date: Date): Boolean {
        val sourceCalendar = Calendar.getInstance()
        val currentCalendar = Calendar.getInstance()
        sourceCalendar.time = date
        currentCalendar.time = Date(System.currentTimeMillis())
        return currentCalendar.get(Calendar.YEAR) == sourceCalendar.get(Calendar.YEAR) && currentCalendar.get(Calendar.DAY_OF_YEAR) == sourceCalendar.get(
                Calendar.DAY_OF_YEAR
        ) + 1
    }

    inner class MessageLeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(item: MessageModel) = with(itemView) {
            tv_item_chat_left_text_message.text = item.content
            tv_item_chat_left_time.text = item.createdDate?.toPresentationHourMinute()
        }
    }

    inner class MessageRightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindViewHolder(item: MessageModel) = with(itemView) {
            tv_item_chat_right_text_message.text = item.content
            tv_item_chat_right_time.text = item.createdDate?.toPresentationHourMinute()
        }
    }
}