package itis.ru.kpfu.join.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.di.modules.AppContextModule
import itis.ru.kpfu.join.db.repository.RepositoryModule
import itis.ru.kpfu.join.network.NetworkModule
import itis.ru.kpfu.join.presentation.PresentationModule
import itis.ru.kpfu.join.presentation.ui.RootBuilder
import javax.inject.Singleton

@Component(modules = [
    NetworkModule::class,
    AppContextModule::class,
    RepositoryModule::class,
    AndroidSupportInjectionModule::class,
    PresentationModule::class,
    RootBuilder::class
    ]
)
@Singleton
interface AppComponent: AndroidInjector<JoinApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<JoinApplication>() {

        @BindsInstance
        abstract fun application(application: Application): Builder

    }
}