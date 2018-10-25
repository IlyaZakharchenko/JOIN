package itis.ru.kpfu.join.di.component

import dagger.Component
import itis.ru.kpfu.join.di.module.AppContextModule
import itis.ru.kpfu.join.di.module.RepositoryModule
import itis.ru.kpfu.join.di.module.TestApiModule
import itis.ru.kpfu.join.ui.fragment.SignInFragment
import javax.inject.Singleton

@Singleton
@Component(modules = [TestApiModule::class, AppContextModule::class, RepositoryModule::class])
interface AppComponent {

    fun inject(fragment: SignInFragment)

}