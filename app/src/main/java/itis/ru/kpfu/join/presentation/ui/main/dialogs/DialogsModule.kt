package itis.ru.kpfu.join.presentation.ui.main.dialogs

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.DialogsAdapter

@Module
class DialogsModule {

    @Provides
    fun provideAdapter() = DialogsAdapter()
}