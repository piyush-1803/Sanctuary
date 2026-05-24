package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {

    // --- Journals ---
    @Query("SELECT * FROM journals ORDER BY createdAt DESC")
    fun getAllJournals(): Flow<List<JournalBook>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournal(journal: JournalBook): Long

    @Delete
    suspend fun deleteJournal(journal: JournalBook)

    // --- Entries ---
    @Query("SELECT * FROM journal_entries WHERE journalId = :journalId ORDER BY date DESC")
    fun getEntriesForJournal(journalId: Int): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries ORDER BY date DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE id = :entryId LIMIT 1")
    suspend fun getEntryById(entryId: Int): JournalEntry?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: JournalEntry): Long

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    // --- Stickers ---
    @Query("SELECT * FROM placed_stickers WHERE entryId = :entryId")
    fun getStickersForEntry(entryId: Int): Flow<List<PlacedSticker>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSticker(sticker: PlacedSticker): Long

    @Update
    suspend fun updateSticker(sticker: PlacedSticker)

    @Delete
    suspend fun deleteSticker(sticker: PlacedSticker)

    @Query("DELETE FROM placed_stickers WHERE id = :stickerId")
    suspend fun deleteStickerById(stickerId: Int)
}
