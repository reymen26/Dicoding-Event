package com.dicoding.dicodingevent.data.local.entity

import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_event")
@Parcelize
data class EventEntity(
    @field:PrimaryKey(autoGenerate = false)
    @field:ColumnInfo(name = "id")
    var id: Int,

    @field:ColumnInfo(name = "name")
    var name: String = "",

    @field:ColumnInfo(name = "mediaCover")
    var mediaCover: String? = null,
) : Parcelable