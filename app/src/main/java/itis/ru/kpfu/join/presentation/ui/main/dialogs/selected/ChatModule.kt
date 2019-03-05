package itis.ru.kpfu.join.presentation.ui.main.dialogs.selected

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.MessagesAdapter

@Module
class ChatModule {

    @Provides
    fun provideAdapter() = MessagesAdapter()
}