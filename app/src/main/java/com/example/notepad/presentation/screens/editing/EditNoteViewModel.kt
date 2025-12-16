package com.example.notepad.presentation.screens.editing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.domain.entity.ContentItem
import com.example.notepad.domain.usecase.DeleteNoteUseCase
import com.example.notepad.domain.usecase.EditNoteUseCase
import com.example.notepad.domain.usecase.GetNoteUseCase
import com.example.notepad.domain.entity.Note
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = EditNoteViewModel.Factory::class)
class EditNoteViewModel @AssistedInject constructor(
    @Assisted("note_id") private val noteId: Int,
    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
): ViewModel() {

    @AssistedFactory
    interface Factory{
        fun create(@Assisted("note_id") noteId: Int): EditNoteViewModel
    }
    private val _state = MutableStateFlow<EditNoteState>(EditNoteState.Initial)

    val state = _state.asStateFlow()


    init{
        viewModelScope.launch {
            _state.update {
                val note = getNoteUseCase(noteId = noteId)
                EditNoteState.Editing(note)
            }
        }
    }
    fun processCommand(command: EditNoteCommand){
        when(command){
            EditNoteCommand.Back -> {
                _state.update { EditNoteState.Finished }
            }
            is EditNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if(previousState is EditNoteState.Editing){
                        val newContent = ContentItem.Text(command.content)
                        val newNote = previousState.note.copy(
                            content = listOf(newContent)
                        )
                        previousState.copy(note = newNote)
                    }
                    else{
                        previousState
                    }
                }
            }
            is EditNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if(previousState is EditNoteState.Editing){
                        val newNote = previousState.note.copy(
                            title = command.title
                        )
                        previousState.copy(note = newNote)
                    }
                    else{
                        previousState
                    }
                }
            }
            EditNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if(previousState is EditNoteState.Editing){
                            val note = previousState.note
                            editNoteUseCase(note)
                            EditNoteState.Finished
                        }
                        else{
                            previousState
                        }

                    }
                }

            }

            EditNoteCommand.Delete -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if(previousState is EditNoteState.Editing){
                            val note = previousState.note
                            deleteNoteUseCase(note.noteId)
                            EditNoteState.Finished
                        }
                        else{
                            previousState
                        }

                    }
                }
            }
        }
    }
}

sealed interface EditNoteCommand{
    data class InputTitle(val title: String): EditNoteCommand
    data class InputContent(val content: String): EditNoteCommand
    data object Save: EditNoteCommand

    data object Back: EditNoteCommand

    data object Delete: EditNoteCommand
}

sealed interface EditNoteState{
    data object Initial: EditNoteState

    data class Editing(
       val note: Note
    ): EditNoteState{
        val isSaveEnabled: Boolean
            get() {
                return when{
                    note.title.isBlank() -> false
                    note.content.isEmpty() -> false
                    else -> {
                        note.content.any {
                            it !is ContentItem.Text || it.text.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished: EditNoteState
}