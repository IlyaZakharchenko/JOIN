package itis.ru.kpfu.join.di.modules

import dagger.Module
import dagger.android.ContributesAndroidInjector
import itis.ru.kpfu.join.service.FirebasePushService

@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract fun buildFirebasePushService(): FirebasePushService
}