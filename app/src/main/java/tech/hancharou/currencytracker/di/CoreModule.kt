package tech.hancharou.currencytracker.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.hancharou.currencytracker.core.ResourceMapperImpl
import tech.hancharou.currencytracker.data.DataStorageImpl
import tech.hancharou.currencytracker.data.RepositoryImpl
import tech.hancharou.currencytracker.data.nw.ApiService
import tech.hancharou.currencytracker.data.sw.AppDatabase
import tech.hancharou.currencytracker.domain.DataStorage
import tech.hancharou.currencytracker.domain.Repository
import tech.hancharou.currencytracker.domain.ResourceMapper


@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    private const val BASE_URL = "https://api.frankfurter.dev/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { context.filesDir.resolve("currency_tracker.preferences_pb") }
        )
    }

    @Provides
    @Singleton
    fun provideResourceMapper(@ApplicationContext context: Context): ResourceMapper {
        return ResourceMapperImpl(context)
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStorage(dataStore: DataStore<Preferences>): DataStorage {
        return DataStorageImpl(dataStore)
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "currency_tracker_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideRepository(
        apiService: ApiService,
        dataStorage: DataStorage,
        database: AppDatabase
    ): Repository {
        return RepositoryImpl(apiService, dataStorage, database)
    }
}