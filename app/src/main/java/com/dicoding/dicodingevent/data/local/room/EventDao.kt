package com.dicoding.dicodingevent.data.local.room

import androidx.room.*
import com.dicoding.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {

    @Query("SELECT * FROM favorite_event WHERE id = :id LIMIT 1")
    suspend fun getFavoriteEventById(id: String): EventEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(event: EventEntity)

    @Delete
    suspend fun deleteFavorite(event: EventEntity)

    @Update
    suspend fun updateFavorite(event: EventEntity)

    @Query("SELECT * FROM favorite_event")
    suspend fun getAllFavorites(): List<EventEntity>

}