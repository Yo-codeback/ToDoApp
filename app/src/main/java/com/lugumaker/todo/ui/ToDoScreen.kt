package com.lugumaker.todo.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lugumaker.todo.data.Priority
import com.lugumaker.todo.data.TodoEntity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

/**
 * 待辦事項主畫面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(
    viewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "我的待辦事項",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = { viewModel.deleteCompletedTodos() }) {
                        Icon(
                            Icons.Default.DeleteSweep,
                            contentDescription = "清除已完成",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.showAddDialog() },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "新增待辦事項")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 統計卡片
            item {
                StatsCard(uiState)
            }
            
            // 今日待辦事項
            item {
                Text(
                    text = "今日待辦",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            // 活躍待辦事項
            items(uiState.activeTodos) { todo ->
                TodoItem(
                    todo = todo,
                    onToggleComplete = { viewModel.toggleComplete(todo) },
                    onDelete = { viewModel.deleteTodo(todo) }
                )
            }
            
            // 已完成待辦事項（可展開）
            if (uiState.completedTodos.isNotEmpty()) {
                item {
                    CompletedTodosSection(
                        completedTodos = uiState.completedTodos,
                        onToggleComplete = { viewModel.toggleComplete(it) },
                        onDelete = { viewModel.deleteTodo(it) }
                    )
                }
            }
            
            // 底部間距
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        
        // 新增待辦事項對話框
        if (uiState.showAddDialog) {
            AddTodoDialog(
                uiState = uiState,
                onDismiss = { viewModel.hideAddDialog() },
                onTitleChange = { viewModel.updateNewTodoForm(title = it) },
                onDescriptionChange = { viewModel.updateNewTodoForm(description = it) },
                onPriorityChange = { viewModel.updateNewTodoForm(priority = it) },
                onCategoryChange = { viewModel.updateNewTodoForm(category = it) },
                onSubmit = { viewModel.submitNewTodo() }
            )
        }
    }
}

/**
 * 統計卡片
 */
@Composable
fun StatsCard(uiState: TodoUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Default.Schedule,
                label = "進行中",
                value = uiState.activeCount.toString(),
                color = MaterialTheme.colorScheme.primary
            )
            StatItem(
                icon = Icons.Default.CheckCircle,
                label = "已完成",
                value = uiState.completedCount.toString(),
                color = MaterialTheme.colorScheme.tertiary
            )
            StatItem(
                icon = Icons.Default.Warning,
                label = "已過期",
                value = uiState.overdueCount.toString(),
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

/**
 * 統計項目
 */
@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
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
    todo: TodoEntity,
    onToggleComplete: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                todo.isCompleted -> MaterialTheme.colorScheme.surfaceVariant
                todo.priority == Priority.URGENT -> MaterialTheme.colorScheme.errorContainer
                todo.priority == Priority.HIGH -> MaterialTheme.colorScheme.tertiaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 優先級指示器
            PriorityIndicator(priority = todo.priority)
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // 完成狀態勾選框
            Checkbox(
                checked = todo.isCompleted,
                onCheckedChange = { onToggleComplete() },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary,
                    uncheckedColor = MaterialTheme.colorScheme.outline
                )
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
                    textDecoration = if (todo.isCompleted) TextDecoration.LineThrough else null,
                    color = if (todo.isCompleted) 
                        MaterialTheme.colorScheme.onSurfaceVariant 
                    else 
                        MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (todo.description.isNotBlank()) {
                    Text(
                        text = todo.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
                
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (todo.dueDate != null) {
                        Icon(
                            Icons.Default.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = when {
                                todo.isCompleted -> MaterialTheme.colorScheme.onSurfaceVariant
                                todo.dueDate.isBefore(LocalDateTime.now()) -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = formatDateTime(todo.dueDate),
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                todo.isCompleted -> MaterialTheme.colorScheme.onSurfaceVariant
                                todo.dueDate.isBefore(LocalDateTime.now()) -> MaterialTheme.colorScheme.error
                                else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            }
                        )
                    }
                    
                    if (todo.category.isNotBlank()) {
                        if (todo.dueDate != null) {
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        CategoryChip(category = todo.category)
                    }
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
 * 優先級指示器
 */
@Composable
fun PriorityIndicator(priority: Priority) {
    val color = when (priority) {
        Priority.LOW -> Color(0xFF4CAF50)
        Priority.MEDIUM -> Color(0xFFFF9800)
        Priority.HIGH -> Color(0xFFFF5722)
        Priority.URGENT -> Color(0xFFE91E63)
    }
    
    Box(
        modifier = Modifier
            .size(4.dp, 40.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(color)
    )
}

/**
 * 分類標籤
 */
@Composable
fun CategoryChip(category: String) {
    Surface(
        modifier = Modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}

/**
 * 已完成待辦事項區塊
 */
@Composable
fun CompletedTodosSection(
    completedTodos: List<TodoEntity>,
    onToggleComplete: (TodoEntity) -> Unit,
    onDelete: (TodoEntity) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "已完成 (${completedTodos.size})",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (expanded) "收起" else "展開",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    completedTodos.forEach { todo ->
                        TodoItem(
                            todo = todo,
                            onToggleComplete = { onToggleComplete(todo) },
                            onDelete = { onDelete(todo) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * 格式化日期時間
 */
private fun formatDateTime(dateTime: LocalDateTime): String {
    val now = LocalDateTime.now()
    val today = now.toLocalDate()
    val tomorrow = today.plusDays(1)
    val todoDate = dateTime.toLocalDate()
    
    return when {
        todoDate == today -> "今天 ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        todoDate == tomorrow -> "明天 ${dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))}"
        else -> dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
    }
} 