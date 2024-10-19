package com.dicoding.dicodingevent.util

import android.content.Context
import com.dicoding.dicodingevent.data.local.database.EventDatabase
import com.dicoding.dicodingevent.data.preference.SettingPreferences
import com.dicoding.dicodingevent.data.preference.dataStore
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.ui.ViewModelFactory

object Injection {

    private fun provideDatabase(context: Context): EventDatabase {
        return EventDatabase.getDatabase(context)
    }

    private fun provideEventRepository(context: Context): EventRepository {
        val database = provideDatabase(context)
        val dao = database.eventDao()
        return EventRepository(dao)
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory {
        val repository = provideEventRepository(context)
        return ViewModelFactory(repository, pref = SettingPreferences.getInstance(context.dataStore))
    }
}
