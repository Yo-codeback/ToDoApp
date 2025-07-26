package com.lugumaker.todo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.lugumaker.todo.R
import com.lugumaker.todo.data.ToDoItem
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * 待辦事項主畫面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoScreen(viewModel: ToDoViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("待辦事項") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add_todo),
                    contentDescription = "新增待辦事項"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 統計資訊
            TodoStats(viewModel)
            
            // 待辦事項列表
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(viewModel.todos) { todo ->
                    TodoItem(
                        todo = todo,
                        onToggleComplete = { viewModel.toggleComplete(todo.id) },
                        onDelete = { viewModel.deleteTodo(todo.id) }
                    )
                }
            }
        }
        
        // 新增待辦事項對話框
        if (viewModel.showAddDialog) {
            AddTodoDialog(viewModel)
        }
    }
}

/**
 * 待辦事項統計資訊
 */
@Composable
fun TodoStats(viewModel: ToDoViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                label = "總計",
                value = viewModel.todos.size.toString()
            )
            StatItem(
                label = "未完成",
                value = viewModel.getIncompleteTodos().size.toString()
            )
            StatItem(
                label = "已完成",
                value = viewModel.getCompletedTodos().size.toString()
            )
        }
    }
}

/**
 * 統計項目
 */
@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
        )
    }
}

/**
 * 待辦事項項目
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoItem(
    todo: ToDoItem,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                todo.isOverdue() -> MaterialTheme.colorScheme.errorContainer
                todo.isDueSoon() -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 完成狀態勾選框
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggleComplete() }
            )
            
            // 待辦事項內容
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null
                )
                
                if (todo.description.isNotBlank()) {
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                todo.dueDate?.let { dueDate ->
                    Text(
                        text = "到期時間: ${dueDate.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            todo.isOverdue() -> MaterialTheme.colorScheme.error
                            todo.isDueSoon() -> MaterialTheme.colorScheme.tertiary
                            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        }
                    )
                }
            }
            
            // 刪除按鈕
            IconButton(onClick = onDelete) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "刪除",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * 新增待辦事項對話框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(viewModel: ToDoViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.hideAddDialog() },
        title = { Text("新增待辦事項") },
        text = {
            Column {
                OutlinedTextField(
                    value = viewModel.newTodoTitle,
                    onValueChange = { viewModel.updateNewTodoTitle(it) },
                    label = { Text("標題") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = viewModel.newTodoDescription,
                    onValueChange = { viewModel.updateNewTodoDescription(it) },
                    label = { Text("描述（可選）") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { viewModel.addTodo() },
                enabled = viewModel.newTodoTitle.isNotBlank()
            ) {
                Text("新增")
            }
        },
        dismissButton = {
            TextButton(onClick = { viewModel.hideAddDialog() }) {
                Text("取消")
            }
        }
    )
} 