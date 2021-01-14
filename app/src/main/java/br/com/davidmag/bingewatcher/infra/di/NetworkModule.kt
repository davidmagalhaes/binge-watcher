package br.com.davidmag.bingewatcher.infra.di

import br.com.davidmag.bingewatcher.app.BuildConfig
import br.com.davidmag.bingewatcher.data.source.remote.api.ShowApi
import br.com.davidmag.bingewatcher.data.util.GsonDateTimeTypeAdapter
import br.com.davidmag.bingewatcher.data.util.GsonSimpleDateTypeAdapter
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.threeten.bp.LocalDate
import org.threeten.bp.OffsetDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .connectionPool(ConnectionPool(10, 1, TimeUnit.MINUTES))
            .readTimeout(BuildConfig.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(BuildConfig.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(BuildConfig.NETWORK_TIMEOUT, TimeUnit.SECONDS)
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(OffsetDateTime::class.java, GsonDateTimeTypeAdapter)
            .registerTypeAdapter(LocalDate::class.java, GsonSimpleDateTypeAdapter)
            .create()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpUrlLoader() =
        OkHttpUrlLoader.Factory(okHttpClient)

    @Singleton
    @Provides
    fun provideShowApi() : ShowApi =
        retrofit.create(ShowApi::class.java)

}