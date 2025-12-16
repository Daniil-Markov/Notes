package com.example.notepad.presentation.screens.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.notepad.domain.usecase.GetAllNotesUseCase
import com.example.notepad.domain.entity.Note
import com.example.notepad.domain.usecase.SearchNoteUseCase
import com.example.notepad.domain.usecase.SwitchPinnedStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel @Inject constructor(
    private val searchNoteUseCase: SearchNoteUseCase,
    private val switchPinnedStatusUseCase: SwitchPinnedStatusUseCase,
    private val getAllNotesUseCase: GetAllNotesUseCase
): ViewModel() {

    private val query = MutableStateFlow("")
    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()

    init {

        query
            .onEach {
               input -> _state.update {
                it.copy(query = input)
            }
            }
            .flatMapLatest{
                if(it.isBlank()){

                    getAllNotesUseCase()
                }
                else{
                    searchNoteUseCase(it)
                }
            }
            .onEach {
                val pinnedNotes = it.filter {note -> note.isPinned }
                val otherNotes = it.filter { note -> !note.isPinned }
                _state.update { it.copy(pinnedNotes = pinnedNotes, otherNotes = otherNotes) }
            }
            .launchIn(viewModelScope)
    }


    fun processCommands(commands: NoteCommands){
        viewModelScope.launch {
            when(commands){
                is NoteCommands.InputSearchQuery -> {
                    query.update {
                        commands.query.trim()
                    }
                }
                is NoteCommands.SwitchPinnedStatus -> {
                    switchPinnedStatusUseCase(commands.noteId)
                }
            }
        }

    }

    data class NotesScreenState(
        val query: String = " ",
        val pinnedNotes: List<Note> = listOf(),
        val otherNotes: List<Note> = listOf()
    )

    sealed interface NoteCommands{
        data class InputSearchQuery(val query: String): NoteCommands
        data class SwitchPinnedStatus(val noteId: Int): NoteCommands
    }
}