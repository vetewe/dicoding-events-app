package com.dicoding.dicodingevent.data.repository

import androidx.lifecycle.LiveData
import com.dicoding.dicodingevent.data.local.dao.EventDao
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val eventDao: EventDao) {

    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent> = eventDao.getFavoriteEventById(id)

    suspend fun insertFavorite(event: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            eventDao.insertFavorite(event)
        }
    }

    suspend fun deleteFavoriteById(id: String) {
        withContext(Dispatchers.IO) {
            eventDao.deleteFavoriteById(id)
        }
    }

    fun getAllFavorites(): LiveData<List<FavoriteEvent>> = eventDao.getAllFavorites()
}
