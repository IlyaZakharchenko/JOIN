package itis.ru.kpfu.join.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module(includes = [ServiceBuilder::class])
class AppContextModule {

    @Provides
    fun context(application: Application): Context {
        return application.applicationContext
    }
}