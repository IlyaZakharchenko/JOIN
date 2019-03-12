package itis.ru.kpfu.join.presentation.ui.main.profile.edit

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter

@Module
class ProfileEditModule {

    @Provides
    fun provideAdapter() = SpecializationsEditAdapter()
}