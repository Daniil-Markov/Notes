package com.example.notepad

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.notepad.presentation.navigation.CustomNavGraph
import com.example.notepad.ui.theme.NotePadTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this.applicationContext
        enableEdgeToEdge()
        setContent {
            NotePadTheme {
                CustomNavGraph()
//                CreateNoteScreen(
//                    onFinished = {
//                        Log.d("CreateNoteScreen", "Finished")
//                    }
//                )
            }
        }
    }
}

