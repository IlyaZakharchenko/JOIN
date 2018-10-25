package itis.ru.kpfu.join.di.module

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppContextModule(private val applcation: Application) {

    @Singleton
    @Provides
    fun context(): Context {
        return applcation.applicationContext
    }
}