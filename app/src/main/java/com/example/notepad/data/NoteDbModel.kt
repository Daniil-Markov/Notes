package com.example.notepad.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("notes")
data class NoteDbModel(
    @PrimaryKey(autoGenerate = true)
    val noteId: Int,
    val title: String,
    val content: String,
    val updateAt: Long,
    val isPinned: Boolean
)