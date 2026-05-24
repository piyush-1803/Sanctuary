package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.ui.theme.SanctuaryTheme
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class Screen {
    object Onboarding : Screen()
    object Library : Screen()
    object Writing : Screen()
    object Timeline : Screen()
    object Settings : Screen()
}

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: JournalRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = JournalRepository(database.journalDao())
    }

    // --- Onboarding & User configuration states ---
    private val _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private val _googleEmail = MutableStateFlow<String?>(null)
    val googleEmail: StateFlow<String?> = _googleEmail.asStateFlow()

    private val _onboardingComplete = MutableStateFlow(false)
    val onboardingComplete: StateFlow<Boolean> = _onboardingComplete.asStateFlow()

    private val _biometricsEnabled = MutableStateFlow(false)
    val biometricsEnabled: StateFlow<Boolean> = _biometricsEnabled.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Onboarding)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    // --- Dynamic Theme ---
    private val _activeTheme = MutableStateFlow(SanctuaryTheme.VINTAGE_AMBER)
    val activeTheme: StateFlow<SanctuaryTheme> = _activeTheme.asStateFlow()

    // --- Live Data Streams ---
    val journals: StateFlow<List<JournalBook>> = repository.allJournals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allEntries: StateFlow<List<JournalEntry>> = repository.allEntries
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Current book selection triggers ---
    private val _selectedJournalId = MutableStateFlow<Int?>(null)
    val selectedJournalId: StateFlow<Int?> = _selectedJournalId.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val entriesInSelectedJournal: StateFlow<List<JournalEntry>> = _selectedJournalId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getEntriesForJournal(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Current page editing states ---
    private val _selectedEntryId = MutableStateFlow<Int?>(null)
    val selectedEntryId: StateFlow<Int?> = _selectedEntryId.asStateFlow()

    private val _activeEntry = MutableStateFlow<JournalEntry?>(null)
    val activeEntry: StateFlow<JournalEntry?> = _activeEntry.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val activeEntryStickers: StateFlow<List<PlacedSticker>> = _selectedEntryId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getStickersForEntry(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Highlighted sticker ID on the active canvas
    private val _selectedStickerId = MutableStateFlow<Int?>(null)
    val selectedStickerId: StateFlow<Int?> = _selectedStickerId.asStateFlow()

    fun setOnboardingCompleted(name: String, enableBiometrics: Boolean, email: String? = null) {
        viewModelScope.launch {
            _username.value = name
            _biometricsEnabled.value = enableBiometrics
            _googleEmail.value = email
            _onboardingComplete.value = true
            _currentScreen.value = Screen.Library
        }
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun selectTheme(theme: SanctuaryTheme) {
        _activeTheme.value = theme
    }

    // --- Bookshelf Core Actions ---
    fun createJournal(title: String, styleStr: String) {
        viewModelScope.launch {
            val colorHex = when (styleStr) {
                "rainy" -> "#28303C"
                "cottage" -> "#708238"
                "sunset" -> "#D38B70"
                "midnight" -> "#1A1C30"
                "rose" -> "#C86F83"
                else -> "#D48256" // vintage
            }
            repository.createJournal(
                JournalBook(
                    title = title,
                    coverColorHex = colorHex,
                    styleType = styleStr
                )
            )
        }
    }

    fun deleteJournal(journal: JournalBook) {
        viewModelScope.launch {
            if (_selectedJournalId.value == journal.id) {
                _selectedJournalId.value = null
            }
            repository.deleteJournal(journal)
        }
    }

    fun selectJournal(journalId: Int) {
        _selectedJournalId.value = journalId
        _currentScreen.value = Screen.Writing
        // Automatically fetch or initialize the last entry in this journal if exists,
        // otherwise we create an empty one!
        viewModelScope.launch {
            val exist = repository.getEntriesForJournal(journalId).firstOrNull() ?: emptyList()
            if (exist.isNotEmpty()) {
                selectEntry(exist.first().id)
            } else {
                createNewEntryInJournal(journalId)
            }
        }
    }

    // --- Entry Page Core Actions ---
    fun selectEntry(entryId: Int) {
        viewModelScope.launch {
            _selectedEntryId.value = entryId
            val entryObj = repository.getEntryById(entryId)
            _activeEntry.value = entryObj
            if (entryObj != null) {
                _selectedJournalId.value = entryObj.journalId
            }
        }
    }

    fun createNewEntryInJournal(journalId: Int) {
        viewModelScope.launch {
            val newEntryId = repository.saveEntry(
                JournalEntry(
                    journalId = journalId,
                    title = "A New Sunset",
                    content = ""
                )
            )
            selectEntry(newEntryId.toInt())
        }
    }

    fun updateActiveEntry(
        title: String,
        content: String,
        mood: String,
        handwritingStyle: String? = null,
        paperStyle: String? = null
    ) {
        val current = _activeEntry.value ?: return
        viewModelScope.launch {
            val updated = current.copy(
                title = title,
                content = content,
                mood = mood,
                handwritingStyle = handwritingStyle ?: current.handwritingStyle,
                paperStyle = paperStyle ?: current.paperStyle
            )
            _activeEntry.value = updated
            repository.updateEntry(updated)
        }
    }

    fun deleteActiveEntry() {
        val current = _activeEntry.value ?: return
        viewModelScope.launch {
            repository.deleteEntry(current)
            _activeEntry.value = null
            _selectedEntryId.value = null
            _currentScreen.value = Screen.Library
        }
    }

    // --- Notebook Decorator Actions ---
    fun addSticker(type: String) {
        val entryId = _selectedEntryId.value ?: return
        viewModelScope.launch {
            val newStickerId = repository.addSticker(
                PlacedSticker(
                    entryId = entryId,
                    stickerType = type,
                    xOffset = 0.3f, // middle of page initial placement
                    yOffset = 0.4f,
                    scale = 1.0f,
                    rotation = 0.0f
                )
            )
            _selectedStickerId.value = newStickerId.toInt()
        }
    }

    fun selectSticker(stickerId: Int?) {
        _selectedStickerId.value = stickerId
    }

    fun updateStickerPosition(stickerId: Int, x: Float, y: Float, scale: Float, rotation: Float) {
        viewModelScope.launch {
            repository.updateSticker(
                PlacedSticker(
                    id = stickerId,
                    entryId = _selectedEntryId.value ?: return@launch,
                    stickerType = activeEntryStickers.value.find { it.id == stickerId }?.stickerType ?: "🌸",
                    xOffset = x,
                    yOffset = y,
                    scale = scale,
                    rotation = rotation
                )
            )
        }
    }

    fun deleteSticker(stickerId: Int) {
        viewModelScope.launch {
            if (_selectedStickerId.value == stickerId) {
                _selectedStickerId.value = null
            }
            repository.deleteStickerById(stickerId)
        }
    }
}

class JournalViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(JournalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return JournalViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
