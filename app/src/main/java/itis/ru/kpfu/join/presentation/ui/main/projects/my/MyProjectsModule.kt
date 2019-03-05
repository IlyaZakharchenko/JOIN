package itis.ru.kpfu.join.presentation.ui.main.projects.my

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.ProjectsAdapter

@Module
class MyProjectsModule {

    @Provides
    fun provideAdapter() = ProjectsAdapter()
}