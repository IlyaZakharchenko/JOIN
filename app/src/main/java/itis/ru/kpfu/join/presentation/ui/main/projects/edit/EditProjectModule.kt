package itis.ru.kpfu.join.presentation.ui.main.projects.edit

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter

@Module
class EditProjectModule {

    @Provides
    fun provideJobsAdapter() = SpecializationsEditAdapter()

    @Provides
    fun getProjectId(fragment: EditProjectFragment): Long = fragment.getProjectId()
}