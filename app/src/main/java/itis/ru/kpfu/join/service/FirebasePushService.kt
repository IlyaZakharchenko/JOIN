package itis.ru.kpfu.join.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity

class FirebasePushService : FirebaseMessagingService() {

    companion object {
        private const val CHANNEL_ID = "Уведомления"
    }


    override fun onMessageReceived(mesage: RemoteMessage?) {
        super.onMessageReceived(mesage)
        processNotification(applicationContext)
    }


    private fun processNotification(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createChannel(context)
            }
            val primaryColor = ContextCompat.getColor(context, R.color.colorPrimary)
            val ledColor = ContextCompat.getColor(context, R.color.ledColor)
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setStyle(NotificationCompat.BigTextStyle().bigText("Message"))
                    .setContentTitle("Title")
                    .setContentText("Content")
                    .setAutoCancel(true)
                    .setContentIntent(getIntent(context))
                    .setLights(ledColor, 500, 500)

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.color = primaryColor
                builder.setVibrate(LongArray(0))
            }

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.notify((Math.random() * 1000).toInt(), builder.build())
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }


    private fun getIntent(context: Context): PendingIntent {

        return PendingIntent.getActivity(
                context, 0, Intent(context, FragmentHostActivity::class.java)
                .setFlags(Intent.FLAG_FROM_BACKGROUND),
                // .putExtra(MainActivity.KEY_PUSH_CATEGORY, pushModel),
                PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val ledColor = ContextCompat.getColor(context, R.color.ledColor)
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val description = "Системные сообщения"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, description, importance)
        channel.lightColor = ledColor
        channel.enableLights(true)
        channel.description = description
        channel.setShowBadge(false)
        notificationManager.createNotificationChannel(channel)
    }

}