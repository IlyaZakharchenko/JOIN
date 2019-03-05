package itis.ru.kpfu.join.network

import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import itis.ru.kpfu.join.network.request.JoinApi
import itis.ru.kpfu.join.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Singleton

@Module
class NetworkModule {

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
                .readTimeout(15, SECONDS)
                .connectTimeout(15, SECONDS)
                .addInterceptor(interceptor)
                .build()
    }

    @Singleton
    @Provides
    fun retrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .client(httpClient)
                .build()
    }

    @Singleton
    @Provides
    fun provideJoinApi(retrofit: Retrofit): JoinApi {
        return retrofit.create(JoinApi::class.java)
    }
}