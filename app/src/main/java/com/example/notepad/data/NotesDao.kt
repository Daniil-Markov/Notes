package com.example.notepad.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notepad.domain.entity.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Query("SELECT * FROM notes WHERE noteId==:noteId")
    suspend fun getNote(noteId: Int): NoteDbModel
    @Query("SELECT * FROM notes ORDER BY updateAt DESC")
    fun getAllNotes(): Flow<List<NoteDbModel>>

    @Query("SELECT * FROM notes WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY updateAt DESC")
    fun searchNotes(query: String): Flow<List<NoteDbModel>>

    @Query("DELETE FROM notes WHERE noteId == :noteId")
    suspend fun deleteNote(noteId: Int)

    @Query("UPDATE notes SET isPinned = NOT isPinned WHERE noteId == :noteId")
    suspend fun switchPinnedStatus(noteId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addNote(noteDbModel: NoteDbModel)


}