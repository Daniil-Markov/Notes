package com.example.notepad.data.mapper

import com.example.notepad.domain.entity.Note
import kotlinx.serialization.json.Json
import com.example.notepad.data.ContentItemDbModel
import com.example.notepad.data.NoteDbModel
import com.example.notepad.domain.entity.ContentItem

fun Note.toDbModel(): NoteDbModel {
    val contentAsString = Json.encodeToString(content.toContentItemDbModel())
    return NoteDbModel(noteId, title, contentAsString, updateAt, isPinned)
}

fun List<ContentItem>.toContentItemDbModel(): List<ContentItemDbModel>{
    return map{
        contentItem ->
        when(contentItem){
            is ContentItem.Image -> ContentItemDbModel.Image(url = contentItem.url)
            is ContentItem.Text -> ContentItemDbModel.Text(text = contentItem.text)
        }
    }
}


fun List<ContentItemDbModel>.toContentItem(): List<ContentItem>{
    return map{
            contentItem ->
        when(contentItem){
            is ContentItemDbModel.Image -> ContentItem.Image(url = contentItem.url)
            is ContentItemDbModel.Text -> ContentItem.Text(text = contentItem.text)
        }
    }
}

fun NoteDbModel.toEntity(): Note{
    val contentItemDbModels = Json.decodeFromString<List<ContentItemDbModel>>(content)
    return Note(noteId, title, contentItemDbModels.toContentItem(), updateAt, isPinned)
}

fun List<NoteDbModel>.toEntities(): List<Note> {
    return this.map{
        it.toEntity()
    }
}