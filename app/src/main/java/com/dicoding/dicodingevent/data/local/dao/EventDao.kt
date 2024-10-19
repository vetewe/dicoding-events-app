package com.dicoding.dicodingevent.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.data.local.entity.FavoriteEvent

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: FavoriteEvent)

    @Query("SELECT * FROM FavoriteEvent WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<FavoriteEvent>

    @Query("DELETE FROM FavoriteEvent WHERE id = :id")
    suspend fun deleteFavoriteById(id: String)

    @Query("SELECT * FROM FavoriteEvent")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>
}
