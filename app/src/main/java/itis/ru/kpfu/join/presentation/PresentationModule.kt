package itis.ru.kpfu.join.presentation

import android.content.Context
import dagger.Module
import dagger.Provides
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import itis.ru.kpfu.join.presentation.model.PushModel
import itis.ru.kpfu.join.presentation.ui.RootBuilder
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessor
import itis.ru.kpfu.join.presentation.util.exceptionprocessor.ExceptionProcessorImpl
import itis.ru.kpfu.join.presentation.util.pushprocessor.PushProcessor
import itis.ru.kpfu.join.presentation.util.pushprocessor.PushProcessorImpl
import javax.inject.Singleton

@Module(includes = [RootBuilder::class])
class PresentationModule {

    @Singleton
    @Provides
    fun provideExceptionProcessor(context: Context): ExceptionProcessor {
        return ExceptionProcessorImpl(context)
    }

    @Singleton
    @Provides
    fun providePushProcessor(): PushProcessor {
        return PushProcessorImpl()
    }
}