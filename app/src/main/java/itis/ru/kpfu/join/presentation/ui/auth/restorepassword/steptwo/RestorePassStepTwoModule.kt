package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo

import dagger.Module
import dagger.Provides

@Module
class RestorePassStepTwoModule {

    @Provides
    fun getEmail(fragment: RestorePassStepTwoFragment): String = fragment.getEmail()
}