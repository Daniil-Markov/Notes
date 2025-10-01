package com.example.notepad.presentation

import kotlinx.coroutines.flow.MutableStateFlow

class NotesViewModel {

    private val _state = MutableStateFlow<ScreenState>


    sealed interface ScreenState{
        data object SearchNote: ScreenState
        data object 
    }

    sealed interface NoteCommands{
        data object EditNote: NoteCommands
        data object AddNote: NoteCommands
        data object DeleteNote: NoteCommands
    }
}