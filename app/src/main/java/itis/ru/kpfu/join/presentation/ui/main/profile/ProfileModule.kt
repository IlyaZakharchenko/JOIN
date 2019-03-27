package itis.ru.kpfu.join.presentation.ui.main.profile

import dagger.Module
import dagger.Provides

@Module
class ProfileModule {

    @Provides
    fun getUserId(fragment: ProfileFragment): Long = fragment.getUserId()
}