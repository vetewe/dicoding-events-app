package com.dicoding.dicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.dicodingevent.data.local.database.EventDatabase
import com.dicoding.dicodingevent.data.preference.SettingPreferences
import com.dicoding.dicodingevent.data.preference.dataStore
import com.dicoding.dicodingevent.data.repository.EventRepository
import com.dicoding.dicodingevent.ui.detail.DetailViewModel
import com.dicoding.dicodingevent.ui.favorite.FavoriteViewModel
import com.dicoding.dicodingevent.ui.home.HomeViewModel
import com.dicoding.dicodingevent.ui.setting.SettingViewModel

class ViewModelFactory(
    private val repository: EventRepository,
    private val pref: SettingPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel() as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                SettingViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                val database = EventDatabase.getDatabase(context)
                val repository = EventRepository(database.eventDao())
                val pref = SettingPreferences.getInstance(context.dataStore)
                instance ?: ViewModelFactory(repository, pref).also { instance = it }
            }
        }
    }
}
