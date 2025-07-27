package com.lugumaker.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lugumaker.todo.data.TodoDatabase
import com.lugumaker.todo.data.TodoRepository
import com.lugumaker.todo.ui.TodoScreen
import com.lugumaker.todo.ui.TodoViewModel
import com.lugumaker.todo.ui.TodoViewModelFactory
import com.lugumaker.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 初始化資料庫和 Repository
        val database = TodoDatabase.getDatabase(this)
        val repository = TodoRepository(database.todoDao())
        
        setContent {
            ToDoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: TodoViewModel = viewModel(
                        factory = TodoViewModelFactory(repository)
                    )
                    TodoScreen(viewModel = viewModel)
                }
            }
        }
    }
}