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
import com.lugumaker.todo.ui.ToDoScreen
import com.lugumaker.todo.ui.ToDoViewModel
import com.lugumaker.todo.ui.theme.ToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        setContent {
            ToDoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: ToDoViewModel = viewModel()
                    
                    // 初始化通知服務
                    LaunchedEffect(Unit) {
                        viewModel.initializeNotificationService(this@MainActivity)
                    }
                    
                    ToDoScreen(viewModel = viewModel)
                }
            }
        }
    }
}