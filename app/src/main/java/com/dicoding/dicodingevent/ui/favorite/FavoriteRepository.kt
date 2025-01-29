package com.dicoding.dicodingevent.ui.favorite

import com.dicoding.dicodingevent.data.local.entity.EventEntity
import com.dicoding.dicodingevent.data.local.room.EventDao

class FavoriteRepository private constructor(private val eventDao: EventDao) {

    // Mengambil semua data favorit
    suspend fun getAllFavorites(): List<EventEntity> {
        return eventDao.getAllFavorites()
    }

    suspend fun getFavoriteById(id: String): EventEntity? {
        return eventDao.getFavoriteEventById(id)
    }

    suspend fun insertFavorite(favoriteEvent: EventEntity) {
        eventDao.insertFavorite(favoriteEvent)
    }


    suspend fun deleteFavorite(favoriteEvent: EventEntity) {
        eventDao.deleteFavorite(favoriteEvent)
    }

    suspend fun updateFavorite(favoriteEvent: EventEntity) {
        eventDao.updateFavorite(favoriteEvent)
    }

    companion object {
        @Volatile
        private var instance: FavoriteRepository? = null

        fun getInstance(dao: EventDao): FavoriteRepository =
            instance ?: synchronized(this) {
                instance ?: FavoriteRepository(dao).also { instance = it }
            }
    }
}