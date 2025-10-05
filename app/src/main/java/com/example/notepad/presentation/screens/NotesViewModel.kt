package com.example.notepad.presentation.screens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.notepad.data.NotesRepositoryImpl
import com.example.notepad.domain.AddNoteUseCase
import com.example.notepad.domain.DeleteNoteUseCase
import com.example.notepad.domain.EditNoteUseCase
import com.example.notepad.domain.GetAllNotesUseCase
import com.example.notepad.domain.GetNoteUseCase
import com.example.notepad.domain.Note
import com.example.notepad.domain.SearchNoteUseCase
import com.example.notepad.domain.SwitchPinnedStatusUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel: ViewModel() {

    private val repository = NotesRepositoryImpl
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val searchNoteUseCase = SearchNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)



    private val query = MutableStateFlow("")


    private val _state = MutableStateFlow(NotesScreenState())
    val state = _state.asStateFlow()

    private val scope = CoroutineScope(Dispatchers.IO)
    init {
        addSomeNotes()
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
            .launchIn(scope)
    }

    fun addSomeNotes(){
        repeat(50){
        addNoteUseCase(title = "Title: $it", content = "Content $it")
        }
    }

    fun processCommands(commands: NoteCommands){
        when(commands){
            is NoteCommands.DeleteNote -> {
                deleteNoteUseCase(commands.id)
            }
            is NoteCommands.EditNote -> {
                val note = getNoteUseCase(commands.note.noteId)
                val title = note.title
                editNoteUseCase(note.copy(title = "$title edited"))
            }
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

    data class NotesScreenState(
        val query: String = " ",
        val pinnedNotes: List<Note> = listOf(),
        val otherNotes: List<Note> = listOf()
    )

    sealed interface NoteCommands{
        data class InputSearchQuery(val query: String): NoteCommands
        data class SwitchPinnedStatus(val noteId: Int): NoteCommands
        data class DeleteNote(val id: Int): NoteCommands
        data class EditNote(val note: Note): NoteCommands
    }
}