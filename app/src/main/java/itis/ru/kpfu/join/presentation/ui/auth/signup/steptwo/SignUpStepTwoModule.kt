package itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo

import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.model.RegistrationFormModel

@Module
class SignUpStepTwoModule {

    @Provides
    fun getRegistrationFormModel(fragment: SignUpStepTwoFragment): RegistrationFormModel = fragment.getRegistrationFormModel()
}