package itis.ru.kpfu.join.dagger.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppContextModule {

    @Provides
    fun context(application: Application): Context {
        return application.applicationContext
    }
}