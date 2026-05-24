package com.example.data

import kotlinx.coroutines.flow.Flow

class JournalRepository(private val journalDao: JournalDao) {

    val allJournals: Flow<List<JournalBook>> = journalDao.getAllJournals()
    val allEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()

    fun getEntriesForJournal(journalId: Int): Flow<List<JournalEntry>> =
        journalDao.getEntriesForJournal(journalId)

    suspend fun getEntryById(entryId: Int): JournalEntry? =
        journalDao.getEntryById(entryId)

    fun getStickersForEntry(entryId: Int): Flow<List<PlacedSticker>> =
        journalDao.getStickersForEntry(entryId)

    suspend fun createJournal(journal: JournalBook): Long =
        journalDao.insertJournal(journal)

    suspend fun deleteJournal(journal: JournalBook) =
        journalDao.deleteJournal(journal)

    suspend fun saveEntry(entry: JournalEntry): Long =
        journalDao.insertEntry(entry)

    suspend fun updateEntry(entry: JournalEntry) =
        journalDao.updateEntry(entry)

    suspend fun deleteEntry(entry: JournalEntry) =
        journalDao.deleteEntry(entry)

    suspend fun addSticker(sticker: PlacedSticker): Long =
        journalDao.insertSticker(sticker)

    suspend fun updateSticker(sticker: PlacedSticker) =
        journalDao.updateSticker(sticker)

    suspend fun deleteSticker(sticker: PlacedSticker) =
        journalDao.deleteSticker(sticker)

    suspend fun deleteStickerById(stickerId: Int) =
        journalDao.deleteStickerById(stickerId)
}
