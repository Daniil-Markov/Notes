package com.example.notepad.data

import com.example.notepad.domain.Note

fun Note.toDbModel(): NoteDbModel{
    return NoteDbModel(noteId, title, content, updateAt, isPinned)
}

fun NoteDbModel.toEntity(): Note{
    return Note(noteId, title, content, updateAt, isPinned)
}

fun List<NoteDbModel>.toEntities(): List<Note> {
    return this.map{
        it.toEntity()
    }
}