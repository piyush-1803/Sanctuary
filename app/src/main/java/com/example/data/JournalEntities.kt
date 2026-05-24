package com.example.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "journals")
data class JournalBook(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val coverColorHex: String,
    val styleType: String = "vintage", // vintage, rainy, cottage, sunset, midnight
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "journal_entries",
    foreignKeys = [
        ForeignKey(
            entity = JournalBook::class,
            parentColumns = ["id"],
            childColumns = ["journalId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["journalId"])]
)
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val journalId: Int,
    val title: String,
    val content: String,
    val mood: String = "Calm", // Peaceful, Nostalgic, Dreamy, Cozy, Melancholy
    val date: Long = System.currentTimeMillis(),
    val handwritingStyle: String = "organic", // organic, slow_cursive, classic
    val paperStyle: String = "ruled", // ruled, blank, dots, grid
    val imageUrl: String? = null // For vintage photo card insertion
)

@Entity(
    tableName = "placed_stickers",
    foreignKeys = [
        ForeignKey(
            entity = JournalEntry::class,
            parentColumns = ["id"],
            childColumns = ["entryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["entryId"])]
)
data class PlacedSticker(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val entryId: Int,
    val stickerType: String, // 🌸, 🌿, 🦋, tape, 📎, polaroid, leaf, star
    val xOffset: Float, // 0.0 to 1.0 for responsive layout
    val yOffset: Float, // 0.0 to 1.0
    val scale: Float = 1.0f,
    val rotation: Float = 0.0f
)
