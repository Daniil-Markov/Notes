package com.example.notepad.presentation.screen.notes

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notepad.domain.Note

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel = viewModel()
){
    val state by viewModel.state.collectAsState()


    LazyColumn(
        modifier = modifier
            .padding(top = 48.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ){
        item{
            Title(titleText = "All Notes")
        }

        item{
            SearchBar(
                modifier = Modifier.fillMaxWidth(),
                query = state.query,
                onQueryChange = {
                    viewModel.processCommand(NotesCommands.InputSearchQuery(it))
                }
            )
        }

        item {
            SubTitle(
                text = "Pinned"
            )
        }

        item{
            LazyRow(
                modifier = modifier,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                items(
                    items = state.pinnedNotes,
                    key = {it.noteId}){note->
                    NoteCard(
                        note = note,
                        onNoteClick = {
                            viewModel.processCommand(NotesCommands.EditNote(note))
                        },
                        onLongClick = {
                            viewModel.processCommand(NotesCommands.SwitchPinnedStatus(note.noteId))
                        },
                        onDoubleClick = {
                            viewModel.processCommand(NotesCommands.DeleteNote(note.noteId))
                        },
                        backgroundColor = Color.Yellow
                    )
                }

            }
        }

        item {
            SubTitle(
                text = "Other"
            )
        }

        items(
            items = state.otherNotes,
            key = {it.noteId}
            ){ note ->
            NoteCard(
                modifier = Modifier.fillMaxWidth(),
                note = note,
                onNoteClick = {
                    viewModel.processCommand(NotesCommands.EditNote(note))
                },
                onLongClick = {
                    viewModel.processCommand(NotesCommands.SwitchPinnedStatus(note.noteId))
                },
                onDoubleClick = {
                    viewModel.processCommand(NotesCommands.DeleteNote(note.noteId))
                },
                backgroundColor = Color.Green
            )
        }
    }

}

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
    onDoubleClick: (Note) -> Unit
){

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onDoubleClick = {
                    onDoubleClick(note)
                },
                onLongClick = {
                    onLongClick(note)
                }
            )
    ) {
        Text(
            text = note.title,
            fontSize = 24.sp
        )
        Text(
            text = note.updateAt.toString(),
            fontSize = 24.sp
        )
        Text(
            text = note.content,
            fontSize = 24.sp
        )
    }

}


@Composable
private fun Title(
    modifier: Modifier = Modifier,
    titleText: String
){
    Text(
        modifier = modifier,
        text = titleText,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun SearchBar(
    modifier: Modifier= Modifier,
    query: String,
    onQueryChange: (String) -> Unit
){
    TextField(
        modifier = modifier.fillMaxWidth(),
        value = query,
        onValueChange = onQueryChange,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Notes"
            )
        },
        shape = RoundedCornerShape(10.dp)
    )
}

@Composable
private fun SubTitle(
    modifier: Modifier = Modifier,
    text: String
){
    Text(
        modifier = modifier,
        text = text,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}