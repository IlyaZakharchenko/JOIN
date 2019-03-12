package itis.ru.kpfu.join.presentation.ui.main.projects.add

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter

@Module
class AddProjectModule {

    @Provides
    fun provideJobsAdapter() = SpecializationsEditAdapter()
}