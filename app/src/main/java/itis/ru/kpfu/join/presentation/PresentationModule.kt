package itis.ru.kpfu.join.presentation

import android.content.Context
import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessorImpl
import javax.inject.Singleton

@Module
class PresentationModule {

    @Singleton
    @Provides
    fun provideExceptionProcessor(context: Context): ExceptionProcessor {
        return ExceptionProcessorImpl(context)
    }
}