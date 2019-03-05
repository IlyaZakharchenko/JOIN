package itis.ru.kpfu.join.presentation.ui.main.projects.all

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.ProjectsAdapter

@Module
class AllProjectsModule {

    @Provides
    fun provideAdapter() = ProjectsAdapter()
}