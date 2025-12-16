package com.example.notepad.presentation.screens.creation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepad.domain.entity.ContentItem
import com.example.notepad.domain.usecase.AddNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CreateNoteCommand {
    data class InputTitle(val title: String) : CreateNoteCommand
    data class InputContent(val content: String, val index: Int) : CreateNoteCommand
    data object Save : CreateNoteCommand
    data object Back : CreateNoteCommand

    data class AddImage(val url: Uri) : CreateNoteCommand
}

sealed interface CreateNoteState {
    data class Creation(
        val title: String = "",
        val content: List<ContentItem> = listOf(ContentItem.Text("")),
    ) : CreateNoteState {

        val isSaveEnabled: Boolean
            get() {
                return when {
                    title.isBlank() -> false
                    content.isEmpty() -> false
                    else -> {
                        content.any {
                            it !is ContentItem.Text || it.text.isNotBlank()
                        }
                    }
                }
            }
    }

    data object Finished : CreateNoteState
}

@HiltViewModel
class CreateNoteViewModel @Inject constructor(
    private val addNoteUseCase: AddNoteUseCase
) : ViewModel() {


    private val _state = MutableStateFlow<CreateNoteState>(CreateNoteState.Creation())

    val state = _state.asStateFlow()

    fun processCommand(command: CreateNoteCommand) {
        when (command) {
            CreateNoteCommand.Back -> {
                _state.update { CreateNoteState.Finished }
            }

            is CreateNoteCommand.InputContent -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        val newContent = previousState.content
                            .mapIndexed { index, contentItem ->
                                if (index == command.index && contentItem is ContentItem.Text) {
                                    contentItem.copy(text = command.content)
                                } else {
                                    contentItem
                                }
                            }
                        previousState.copy(
                            content = newContent,
                        )
                    } else {
                        previousState
                    }
                }
            }

            is CreateNoteCommand.InputTitle -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        previousState.copy(
                            title = command.title
                        )
                    } else {
                        previousState
                    }
                }
            }

            CreateNoteCommand.Save -> {
                viewModelScope.launch {
                    _state.update { previousState ->
                        if (previousState is CreateNoteState.Creation) {
                            val title = previousState.title
                            val content = previousState.content.filter {
                                it !is ContentItem.Text || it.text.isNotBlank()
                            }
                            addNoteUseCase(title, content)
                            CreateNoteState.Finished
                        } else {
                            previousState
                        }

                    }
                }

            }

            is CreateNoteCommand.AddImage -> {
                _state.update { previousState ->
                    if (previousState is CreateNoteState.Creation) {
                        val newItems = previousState.content.toMutableList()
                        val lastElement = newItems.last()
                        if (lastElement is ContentItem.Text && lastElement.text.isBlank()) {
                            newItems.removeAt(newItems.lastIndex)
                        }
                        newItems.add(ContentItem.Image(command.url.toString()))
                        newItems.add(ContentItem.Text(""))
                        previousState.copy(content = newItems)
                    } else {
                        previousState
                    }
                }
            }
        }
    }
}

