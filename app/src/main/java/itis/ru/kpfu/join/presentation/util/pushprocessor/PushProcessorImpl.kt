package itis.ru.kpfu.join.presentation.util.pushprocessor

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.widget.RemoteViews
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.PushCategoriesType
import itis.ru.kpfu.join.presentation.model.PushModel
import itis.ru.kpfu.join.presentation.model.PushModelMapper
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity

class PushProcessorImpl() : PushProcessor {

    companion object {
        private const val CHANNEL_ID = "Уведомления"
    }

    override fun processPush(context: Context, data: Map<String, String>) {
        val model = PushModelMapper.map(data)
        createNotification(context, model)
    }

    private fun createNotification(context: Context, pushModel: PushModel) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(context)
            }
            val collapsedView = RemoteViews(context.packageName, R.layout.layout_push_notification)
            collapsedView.setTextViewText(R.id.tv_push_notification_info, getPushMessage(pushModel, context))

            val primaryColor = ContextCompat.getColor(context, R.color.colorPrimary)
            val ledColor = ContextCompat.getColor(context, R.color.ledColor)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setCustomContentView(collapsedView)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    /*.setCustomBigContentView(expandedView)
                     .setStyle(NotificationCompat.BigTextStyle().bigText(getPushMessage(pushModel, context)))
                     .setContentTitle("Уведомление")
                     .setContentText(getPushMessage(pushModel, context))*/
                    .setAutoCancel(true)
                    .setContentIntent(getIntent(context, pushModel))
                    .setLights(ledColor, 500, 500)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.color = primaryColor
                builder.setVibrate(LongArray(0))
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.notify(1, builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    private fun getIntent(context: Context, pushModel: PushModel): PendingIntent {
        return PendingIntent.getActivity(
                context, 0, Intent(context, FragmentHostActivity::class.java)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                .putExtra(FragmentHostActivity.KEY_PUSH_CATEGORY, pushModel),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val ledColor = ContextCompat.getColor(context, R.color.ledColor)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val description = "Системные сообщения"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, description, importance)
        channel.lightColor = ledColor
        channel.enableLights(true)
        channel.description = description
        channel.setShowBadge(false)
        notificationManager.createNotificationChannel(channel)
    }


    private fun getPushMessage(model: PushModel, context: Context): String {
        return when (model.type) {
            PushCategoriesType.ADD_TO_PROJECT_INVITE -> {
                context.getString(R.string.push_add_to_project_invite, model.userName, model.projectName)
            }
            PushCategoriesType.ADD_TO_PROJECT_ACCEPT -> {
                context.getString(R.string.push_add_to_project_accept, model.userName, model.projectName)
            }
            PushCategoriesType.ADD_TO_PROJECT_REFUSE -> {
                context.getString(R.string.push_add_to_project_refuse, model.userName, model.projectName)
            }
            PushCategoriesType.JOIN_TO_PROJECT_INVITE -> {
                context.getString(R.string.push_join_to_project_invite, model.userName, model.projectName)
            }
            PushCategoriesType.JOIN_TO_PROJECT_ACCEPT -> {
                context.getString(R.string.push_join_to_project_accept, model.projectName)
            }
            PushCategoriesType.JOIN_TO_PROJECT_REFUSE -> {
                context.getString(R.string.push_join_to_project_refuse, model.projectName)
            }
            else -> {
                context.getString(R.string.push_unknown)
            }
        }
    }
}