package com.example.notepad.presentation.screens

import android.content.Context
import com.example.notepad.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentRecomposeScope
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.compose
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notepad.domain.Note
import com.example.notepad.ui.theme.Green
import com.example.notepad.ui.theme.OtherNotesColors
import com.example.notepad.ui.theme.PinnedNotesColors
import com.example.notepad.ui.theme.Yellow100
import com.example.notepad.ui.theme.Yellow200

@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current.applicationContext,
    viewModel: NotesViewModel = viewModel{
        NotesViewModel(context)
    },
    onNoteClick: (Note) -> Unit,
    onAddNoteClick: () -> Unit
){
    val state by viewModel.state.collectAsState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add_note),
                    contentDescription = "Button add note"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding
        ){
            item{
                Title(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "All Notes"
                )
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                SearchBar(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    query = state.query,
                    onQueryChanged = {
                        viewModel.processCommands(NotesViewModel.NoteCommands.InputSearchQuery(it))
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }

            item{
                SubTitle(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Pinned"
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp)
                ) {
                    itemsIndexed(
                        items = state.pinnedNotes,
                        key = {_ , note -> note.noteId}
                    ){index,  note ->
                        NoteCard(
                            modifier = Modifier.widthIn(max = 160.dp),
                            note = note,
                            onNoteClick = onNoteClick,
                            onLongClick = {
                                viewModel.processCommands(NotesViewModel.NoteCommands.SwitchPinnedStatus(noteId = note.noteId))
                            },
                            backgroundColor = PinnedNotesColors[index % PinnedNotesColors.size]
                        )
                    }

                }
            }

            item{
                SubTitle(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    text = "Others")
            }

            item {
                Spacer(modifier = Modifier.height(24    .dp))
            }

            itemsIndexed(
                items = state.otherNotes,
                key = { _ , note ->note.noteId}
            ){index,  note ->
                NoteCard(
                    modifier = Modifier.padding(horizontal = 24.dp)
                        .fillMaxWidth(),
                    note = note,
                    onNoteClick = onNoteClick,
                    onLongClick = {
                        viewModel.processCommands(NotesViewModel.NoteCommands.SwitchPinnedStatus(noteId = note.noteId))
                    },
                    backgroundColor = OtherNotesColors[index % OtherNotesColors.size]
                )
                Spacer(Modifier.height(8.dp))
            }

        }
    }


}


@Composable
private fun Title(
    modifier: Modifier = Modifier,
    text: String
){
    Text(
        modifier = modifier,
        text = text,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )

}

@Composable
private fun SearchBar(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChanged: (String) -> Unit
){
    TextField(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = RoundedCornerShape(10.dp)
            ),
        value = query,
        onValueChange = onQueryChanged,
        placeholder = {
            Text(
                text = "Search...",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.surface,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.surface,
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
@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    backgroundColor: Color,
    onNoteClick: (Note) -> Unit,
    onLongClick: (Note) -> Unit,
){
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .combinedClickable(
                onClick = {
                    onNoteClick(note)
                },
                onLongClick = {
                    onLongClick(note)
                }
            )
            .padding(16.dp)
    ) {
        Text(
            text = note.title,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = DataFormatter.formatDateToString(note.updateAt),
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = note.content,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Medium,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }

}