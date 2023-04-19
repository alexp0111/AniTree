package com.ikbo0621.anitree.di

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.ikbo0621.anitree.model.implementation.ParsingModel
import com.ikbo0621.anitree.model.implementation.TreeModel
import com.ikbo0621.anitree.model.implementation.UserModel
import com.ikbo0621.anitree.model.repository.ParsingRepository
import com.ikbo0621.anitree.model.repository.TreeRepository
import com.ikbo0621.anitree.model.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTreeRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth
    ): TreeRepository {
        return TreeModel(auth, database)
    }

    @Provides
    @Singleton
    fun provideParsingRepository(): ParsingRepository {
        return ParsingModel()
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        database: FirebaseFirestore,
        auth: FirebaseAuth,
        appPreferences: SharedPreferences,
        gson: Gson
    ): UserRepository {
        return UserModel(auth, database, appPreferences, gson)
    }
}