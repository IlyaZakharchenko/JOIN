package itis.ru.kpfu.join.di.module

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import itis.ru.kpfu.join.api.TestApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class TestApiModule {

    @Singleton
    @Provides
    fun httpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Singleton
    @Provides
    fun httpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Singleton
    @Provides
    fun retrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://umorili.herokuapp.com/")
                .client(httpClient)
                .build()
    }

    @Singleton
    @Provides
    fun provideTestApi(retrofit: Retrofit): TestApi {
        return retrofit.create(TestApi::class.java)
    }

}