package itis.ru.kpfu.join.presentation.ui.main.projects.details

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.ProjectJobAdapter
import itis.ru.kpfu.join.presentation.adapter.ProjectMemberAdapter

@Module
class ProjectDetailsModule {

    @Provides
    fun getProjectId(fragment: ProjectDetailsFragment): Long = fragment.getProjectId()

    @Provides
    fun provideProjectMemberAdapter(): ProjectMemberAdapter = ProjectMemberAdapter()

    @Provides
    fun provideProjectJobAdapter(): ProjectJobAdapter = ProjectJobAdapter()
}