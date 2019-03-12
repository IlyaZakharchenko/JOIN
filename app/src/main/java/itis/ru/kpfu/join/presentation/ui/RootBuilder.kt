package itis.ru.kpfu.join.presentation.ui

import dagger.Module
import dagger.android.ContributesAndroidInjector
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.ui.MainBuilder

@Module
abstract class RootBuilder {

    @ContributesAndroidInjector(modules = [FragmentHostModule::class])
    abstract fun buildFragmentHostActivity(): FragmentHostActivity
}