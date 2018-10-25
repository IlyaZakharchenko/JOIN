package itis.ru.kpfu.join.di.module

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.db.repository.impl.TestRepositoryImpl
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun testRepository(): TestRepositoryImpl{
        return TestRepositoryImpl()
    }

}