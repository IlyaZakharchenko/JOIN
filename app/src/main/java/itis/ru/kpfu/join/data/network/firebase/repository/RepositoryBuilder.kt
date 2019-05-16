package itis.ru.kpfu.join.data.network.firebase.repository

import dagger.Binds
import dagger.Module
import itis.ru.kpfu.join.data.network.firebase.repository.DialogListRepository
import itis.ru.kpfu.join.data.network.firebase.repository.MessagesRepository
import itis.ru.kpfu.join.data.network.firebase.repository.impl.DialogListRepositoryImpl
import itis.ru.kpfu.join.data.network.firebase.repository.impl.MessagesRepositoryImpl
import javax.inject.Singleton

@Module
abstract class RepositoryBuilder {

    @Singleton
    @Binds
    abstract fun buildDialogRepository(repositoryImpl: MessagesRepositoryImpl): MessagesRepository

    @Singleton
    @Binds
    abstract fun buildDialogListRepository(repositoryImpl: DialogListRepositoryImpl): DialogListRepository
}