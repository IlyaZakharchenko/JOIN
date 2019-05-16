package itis.ru.kpfu.join.data.network.firebase

import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.data.network.firebase.repository.RepositoryBuilder
import javax.inject.Singleton

@Module(includes = [RepositoryBuilder::class])
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseDataBase(): FirebaseDatabase {
        return FirebaseDatabase.getInstance()
    }
}