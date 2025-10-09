package com.example.notepad.data

import android.content.Context
import androidx.compose.runtime.currentRecomposeScope
import com.example.notepad.domain.Note
import com.example.notepad.domain.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.concurrent.locks.Lock

//object NotesRepositoryImpl: NoteRepository {
//
//    private val testData = mutableListOf<Note>().apply {
//        repeat(10){
//            add(Note(it, "Title $it", "Content $it", System.currentTimeMillis(), false))
//        }
//    }
//
//    private val notesListFlow = MutableStateFlow<List<Note>>(testData)
//
//    override fun getAllNotes(): Flow<List<Note>> {
//        return notesListFlow.asStateFlow()
//    }
//
//
//    override suspend fun deleteNote(noteId: Int) {
//        notesListFlow.update { oldList ->
//            oldList.toMutableList().apply {
//                removeIf { it.noteId == noteId }
//            }
//        }
//    }
//
//    override suspend fun editNote(note: Note) {
//        notesListFlow.update{ oldList ->
//            oldList.map{
//                if(it.noteId == note.noteId){
//                    note
//                }else{
//                    it
//                }
//            }
//        }
//    }
//
//    override fun searchNote(query: String): Flow<List<Note>> {
//        return notesListFlow.map { currentList ->
//            currentList.filter {
//                it.title.contains(query) || it.content.contains(query)
//            }
//        }
//
//    }
//
//    override suspend fun addNote(title: String, content: String, isPinned: Boolean, updateAt: Long) {
//        notesListFlow.update { oldList ->
//            val note = Note(
//                noteId = oldList.size,
//                title = title,
//                content = content,
//                updateAt = updateAt,
//                isPinned = isPinned
//            )
//            oldList + note
//        }
//    }
//
//    override suspend fun switchPinnedStatus(noteId: Int) {
//        notesListFlow.update{ oldList ->
//            oldList.map{
//                if(it.noteId == noteId){
//                    it.copy(isPinned = !it. isPinned)
//                }else{
//                    it
//                }
//            }
//        }
//    }
//
//    override suspend fun getNote(noteId: Int): Note {
//        return notesListFlow.value.first{
//            it.noteId == noteId
//        }
//    }
//}

class NotesRepositoryImpl private constructor( context: Context): NoteRepository{
    private val notesDatabase = NotesDatabase.getInstance(context)
    override fun getAllNotes(): Flow<List<Note>> {
        return notesDao.getAllNotes().map {
            it.toEntities()
        }
    }

    override suspend fun addNote(
        title: String,
        content: String,
        isPinned: Boolean,
        updateAt: Long
    ) {
        val noteDbModel = NoteDbModel(0, title, content, updateAt, isPinned)
        notesDao.addNote(noteDbModel)
    }

    override suspend fun deleteNote(noteId: Int) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun editNote(note: Note) {
        notesDao.addNote(note.toDbModel())
    }

    override fun searchNote(query: String): Flow<List<Note>> {
        return notesDao.searchNotes(query).map { it.toEntities() }
    }

    override suspend fun switchPinnedStatus(noteId: Int) {
        notesDao.switchPinnedStatus(noteId)
    }

    override suspend fun getNote(noteId: Int): Note {
        return notesDao.getNote(noteId).toEntity()
    }

    private val notesDao = notesDatabase.notesDao()

    companion object{
        private var instance: NotesRepositoryImpl? =  null
        private val LOCK = Any()

        fun getInstance(context: Context): NotesRepositoryImpl{

            instance?.let { return it }

            synchronized(LOCK){
                instance?.let { return it }
                return NotesRepositoryImpl(context).also {
                    instance = it
                }
            }
        }
    }
}