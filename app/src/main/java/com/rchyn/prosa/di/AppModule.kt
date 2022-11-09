package com.rchyn.prosa.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStore
import androidx.datastore.dataStoreFile
import androidx.room.Room
import com.rchyn.prosa.BuildConfig
import com.rchyn.prosa.data.local.data_source.StoryLocalDataSource
import com.rchyn.prosa.data.local.data_store.UserPrefDataStore
import com.rchyn.prosa.data.local.room.RemoteKeysDao
import com.rchyn.prosa.data.local.room.StoryDao
import com.rchyn.prosa.data.local.room.StoryDatabase
import com.rchyn.prosa.data.remote.data_source.auth.UserRemoteDataSource
import com.rchyn.prosa.data.remote.data_source.stories.StoriesRemoteDataSource
import com.rchyn.prosa.data.remote.retrofit.StoryApi
import com.rchyn.prosa.data.repository.stories.StoriesRepositoryImpl
import com.rchyn.prosa.data.repository.user.UserRepositoryImpl
import com.rchyn.prosa.domain.model.user.User
import com.rchyn.prosa.domain.repository.stories.StoriesRepository
import com.rchyn.prosa.domain.repository.user.UserRepository
import com.rchyn.prosa.utils.Constant
import com.rchyn.prosa.utils.Constant.STORY_DB
import com.rchyn.prosa.utils.Constant.USER_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        }else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }


    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

    @Singleton
    @Provides
    fun provideStoryApi(okHttpClient: OkHttpClient): StoryApi =
        Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(StoryApi::class.java)

    @Singleton
    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        userPrefDataStore: UserPrefDataStore
    ): UserRepository =
        UserRepositoryImpl(userRemoteDataSource, userPrefDataStore)


    @Singleton
    @Provides
    fun provideStoriesRepository(
        userPrefDataStore: UserPrefDataStore,
        storiesLocalDataSource: StoryLocalDataSource,
        storiesRemoteDataSource: StoriesRemoteDataSource
    ): StoriesRepository =
        StoriesRepositoryImpl(userPrefDataStore, storiesLocalDataSource, storiesRemoteDataSource)

    @Singleton
    @Provides
    fun provideUserSettings(
        @ApplicationContext appContext: Context
    ): DataStore<User> {
        return DataStoreFactory.create(
            serializer = UserPrefDataStore.UserPrefSerializer,
            produceFile = { appContext.dataStoreFile(USER_PREF) },
            corruptionHandler = null,
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        )
    }

    @Singleton
    @Provides
    fun provideUserPrefDataStore(userPrefSerializer: UserPrefDataStore.UserPrefSerializer) =
        dataStore(USER_PREF, userPrefSerializer)

    @Singleton
    @Provides
    fun provideStoryDatabase(
        @ApplicationContext context: Context
    ): StoryDatabase =
        Room.databaseBuilder(
            context,
            StoryDatabase::class.java,
            STORY_DB
        ).fallbackToDestructiveMigration().build()

    @Provides
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao =
        storyDatabase.storyDao()

    @Provides
    fun provideRemoteKeysDao(storyDatabase: StoryDatabase): RemoteKeysDao =
        storyDatabase.remoteKeysDao()
}