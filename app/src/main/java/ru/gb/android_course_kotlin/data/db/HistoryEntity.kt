package ru.gb.android_course_kotlin.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: Int,
    val condition: String
)