package itis.ru.kpfu.join.db.repository

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.db.repository.impl.UserRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun userRepository(): UserRepository {
        return UserRepositoryImpl()
    }
}