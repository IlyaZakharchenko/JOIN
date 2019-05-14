package itis.ru.kpfu.join.data

import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import itis.ru.kpfu.join.data.network.NetworkModule
import itis.ru.kpfu.join.data.network.firebase.FirebaseModule
import itis.ru.kpfu.join.data.network.firebase.repository.RepositoryBuilder
import javax.inject.Singleton

@Module(includes = [NetworkModule::class, FirebaseModule::class])
class DataModule {

    @Provides
    @Singleton
    fun provideEventSubject(): Subject<EventType> = PublishSubject.create<EventType>()
}