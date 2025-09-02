package com.sikuai.album.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val uri: String,
    val path: String,
    val dateAdded: Long,
    val size: Long,
    val mimeType: String // e.g., "image/jpeg", "video/mp4"
)
