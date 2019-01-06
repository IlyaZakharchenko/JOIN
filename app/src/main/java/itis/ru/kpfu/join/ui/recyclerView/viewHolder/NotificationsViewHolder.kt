package itis.ru.kpfu.join.ui.recyclerView.viewHolder

import android.support.v7.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import itis.ru.kpfu.join.api.model.Notification
import kotlinx.android.synthetic.main.item_notification.view.btn_accept
import kotlinx.android.synthetic.main.item_notification.view.btn_decline
import kotlinx.android.synthetic.main.item_notification.view.ll_button_line
import kotlinx.android.synthetic.main.item_notification.view.tv_message

class NotificationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bindViewHolder(item: Notification, onAccept: (Long) -> Unit, onDecline: (Long) -> Unit,
            onProjectClick: (Long) -> Unit, onUsernameClick: (Long) -> Unit) = with(itemView) {
        val message: SpannableString
        val startUser: Int
        val endUser: Int
        val startProject: Int
        val endProject: Int

        val userSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                item.user?.id?.let { onUsernameClick(it) }
            }
        }

        val projectSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                item.project?.id?.let { onProjectClick(it) }
            }
        }

        when (item.type) {
            0 -> {
                message = SpannableString("Пользователь ${item.user?.username} приглашает вас присоединиться к " +
                        "проекту ${item.project?.name}")
                startUser = message.indexOf(item.user?.username.toString())
                endUser = startUser + item.user?.username.toString().length
                startProject = message.indexOf(item.project?.name.toString())
                endProject = startProject + item.project?.name.toString().length
                message.setSpan(userSpan, startUser, endUser, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                message.setSpan(projectSpan, startProject, endProject, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_message.text = message
                tv_message.movementMethod = LinkMovementMethod.getInstance()
            }

            1 -> {
                message = SpannableString("Пользователь ${item.user?.username} принял приглашение о вступлении в " +
                        "проект ${item.project?.name}")
                startUser = message.indexOf(item.user?.username.toString())
                endUser = startUser + item.user?.username.toString().length
                startProject = message.indexOf(item.project?.name.toString())
                endProject = startProject + item.project?.name.toString().length

                message.setSpan(userSpan, startUser, endUser, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                message.setSpan(projectSpan, startProject, endProject, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_message.text = message
                tv_message.movementMethod = LinkMovementMethod.getInstance()

                ll_button_line.visibility = View.GONE
            }

            2 -> {
                message = SpannableString("Пользователь ${item.user?.username} хочет присоединиться к " +
                        "проекту ${item.project?.name}")
                startUser = message.indexOf(item.user?.username.toString())
                endUser = startUser + item.user?.username.toString().length
                startProject = message.indexOf(item.project?.name.toString())
                endProject = startProject + item.project?.name.toString().length

                message.setSpan(userSpan, startUser, endUser, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                message.setSpan(projectSpan, startProject, endProject, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_message.text = message
                tv_message.movementMethod = LinkMovementMethod.getInstance()
            }

            3 -> {
                message = SpannableString("Пользователь ${item.user?.username} отклонил приглашение о вступлении в"
                        + " проект ${item.project?.name}")
                startUser = message.indexOf(item.user?.username.toString())
                endUser = startUser + item.user?.username.toString().length
                startProject = message.indexOf(item.project?.name.toString())
                endProject = startProject + item.project?.name.toString().length

                message.setSpan(userSpan, startUser, endUser, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                message.setSpan(projectSpan, startProject, endProject, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_message.text = message
                tv_message.movementMethod = LinkMovementMethod.getInstance()

                ll_button_line.visibility = View.GONE
            }

            4 -> {
                message = SpannableString("Вы были приняты в проект ${item.project?.name}")
                startProject = message.indexOf(item.project?.name.toString())
                endProject = startProject + item.project?.name.toString().length

                message.setSpan(projectSpan, startProject, endProject, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_message.text = message
                tv_message.movementMethod = LinkMovementMethod.getInstance()

                ll_button_line.visibility = View.GONE
            }

            5 -> {
                message = SpannableString("Вас не приняли в проект ${item.project?.name}")
                startProject = message.indexOf(item.project?.name.toString())
                endProject = startProject + item.project?.name.toString().length

                message.setSpan(projectSpan, startProject, endProject, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                tv_message.text = message
                tv_message.movementMethod = LinkMovementMethod.getInstance()

                ll_button_line.visibility = View.GONE
            }
        }

        btn_accept.setOnClickListener { item.id?.let { it1 -> onAccept(it1) } }
        btn_decline.setOnClickListener { item.id?.let { it2 -> onDecline(it2) } }
    }
}