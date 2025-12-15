package com.example.notepad.presentation.screen.notes

import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel: ViewModel() {
    private val repository = NotesRepositoryImpl
    private val addNoteUseCase = AddNoteUseCase(repository)
    private val deleteNoteUseCase = DeleteNoteUseCase(repository)
    private val editNoteUseCase = EditNoteUseCase(repository)
    private val switchPinnedStatusUseCase = SwitchPinnedStatusUseCase(repository)
    private val getAllNotesUseCase = GetAllNotesUseCase(repository)
    private val getNoteUseCase = GetNoteUseCase(repository)
    private val searchNoteUseCase = SearchNoteUseCase(repository)

    private val query = MutableStateFlow("")
    private val scope = CoroutineScope(Dispatchers.IO)
    private val _state = MutableStateFlow<NotesScreenState>(
        NotesScreenState()
    )

    val state = _state.asStateFlow()

    init {
        addSomeNotes()
        query
            .onEach { inputQuery ->
                _state.update { it.copy(query = inputQuery) }
            }
            .flatMapLatest{
                if(it.isBlank()){
                    getAllNotesUseCase()
                }
                else{
                    searchNoteUseCase(it)
                }
            }
            .onEach{ notes ->
                val pinnedNotes = notes.filter { it.isPinned }
                val otherNotes = notes.filter {!it.isPinned}
                _state.update { currentState ->
                    currentState.copy(
                        query = query.value,
                        pinnedNotes = pinnedNotes,
                        otherNotes = otherNotes
                    )
                }
            }
            .launchIn(scope)
    }

    fun addSomeNotes(){
        repeat(50){
            addNoteUseCase(title = "Title: $it", content = "Content: $it")
        }
    }

    fun processCommand(command: NotesCommands){
        when(command){
            NotesCommands.AddNote -> {

            }
            is NotesCommands.DeleteNote -> {
                deleteNoteUseCase(command.noteId)
            }
            is NotesCommands.EditNote -> {
                val note = getNoteUseCase(command.note.noteId)
                val title = note.title
                editNoteUseCase(note.copy(title = "$title - edited"))
            }

            is NotesCommands.InputSearchQuery -> {

            }

            is NotesCommands.SwitchPinnedStatus -> {
                val note = getNoteUseCase(command.noteId)
                switchPinnedStatusUseCase(note.noteId)
            }
        }
    }


}

data class NotesScreenState(
    val query: String = "",
    val pinnedNotes: List<Note> = listOf(),
    val otherNotes: List<Note> = listOf()
)


sealed interface NotesCommands{

    data class SwitchPinnedStatus(val noteId: Int): NotesCommands
    data class InputSearchQuery(val query: String): NotesCommands
    data class EditNote(val note: Note): NotesCommands
    data object AddNote: NotesCommands
    data class DeleteNote(val noteId: Int): NotesCommands
}