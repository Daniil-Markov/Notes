package com.example.notepad.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notepad.presentation.screens.notes.NotesScreen
import com.example.notepad.presentation.screens.creation.CreateNoteScreen
import com.example.notepad.presentation.screens.editing.EditNoteScreen
@Composable
fun NavGraph(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Notes.route
    ){
        composable(Screen.Notes.route){
            NotesScreen(
                onNoteClick = {note ->
                    navController.navigate(Screen.EditNote.createRoute(note.noteId))
                },
                onAddNoteClick = {
                    navController.navigate(Screen.CreateNote.route)
                }

            )
        }
        composable(Screen.CreateNote.route) {
            CreateNoteScreen(
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            Screen.EditNote.route,
            arguments = listOf(
                navArgument("note_id") {
                    type = NavType.IntType
                }
            )) {backStackEntry ->
            val noteId = backStackEntry.arguments?.getInt("note_id") ?:0
            EditNoteScreen(
                noteId = noteId,
                onFinished = {
                    navController.popBackStack()
                }
            )
        }
    }
}

sealed class Screen(val route: String){
    data object Notes: Screen("notes")
    data object CreateNote: Screen("create_note")
    data object EditNote: Screen("edit_note/{note_id}"){
        fun createRoute(noteId: Int): String{
            return "edit_note/$noteId"
        }

        fun getNoteId(arguments: Bundle?): Int{
            return arguments?.getInt("note_id") ?:0
        }
    }
}