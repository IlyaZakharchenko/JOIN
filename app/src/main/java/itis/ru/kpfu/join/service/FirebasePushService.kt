package itis.ru.kpfu.join.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.util.pushprocessor.PushProcessor
import javax.inject.Inject

class FirebasePushService : FirebaseMessagingService() {

    @Inject
    lateinit var pushProcessor: PushProcessor
    @Inject
    lateinit var userRepository: UserRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        if (userRepository.getUser() != null) {
            val data = message?.data
            if (data != null) {
                pushProcessor.processPush(applicationContext, message.data)
            }
        }
    }
}