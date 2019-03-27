package itis.ru.kpfu.join.presentation.ui.main.notifications

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.NotificationsAdapter

@Module
class NotificationsModule {

    @Provides
    fun provideAdapter() = NotificationsAdapter()
}