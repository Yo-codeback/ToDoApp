package com.lugumaker.todo.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lugumaker.todo.data.Priority
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * 新增待辦事項對話框
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTodoDialog(
    uiState: TodoUiState,
    onDismiss: () -> Unit,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onPriorityChange: (Priority) -> Unit,
    onCategoryChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // 標題
                Text(
                    text = "新增待辦事項",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // 標題輸入
                OutlinedTextField(
                    value = uiState.newTodoTitle,
                    onValueChange = onTitleChange,
                    label = { Text("標題 *") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 描述輸入
                OutlinedTextField(
                    value = uiState.newTodoDescription,
                    onValueChange = onDescriptionChange,
                    label = { Text("描述（可選）") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    minLines = 3,
                    maxLines = 5
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 優先級選擇
                Text(
                    text = "優先級",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                PrioritySelector(
                    selectedPriority = uiState.newTodoPriority,
                    onPrioritySelected = onPriorityChange
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 分類輸入
                OutlinedTextField(
                    value = uiState.newTodoCategory,
                    onValueChange = onCategoryChange,
                    label = { Text("分類（可選）") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    ),
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Category,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("取消")
                    }
                    
                    Button(
                        onClick = onSubmit,
                        enabled = uiState.newTodoTitle.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("新增")
                    }
                }
            }
        }
    }
}

/**
 * 優先級選擇器
 */
@Composable
fun PrioritySelector(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Priority.values().forEach { priority ->
            PriorityChip(
                priority = priority,
                isSelected = priority == selectedPriority,
                onClick = { onPrioritySelected(priority) }
            )
        }
    }
}

/**
 * 優先級晶片
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityChip(
    priority: Priority,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val (backgroundColor, textColor) = when (priority) {
        Priority.LOW -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        Priority.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        Priority.HIGH -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
        Priority.URGENT -> MaterialTheme.colorScheme.errorContainer to MaterialTheme.colorScheme.onErrorContainer
    }
    
    FilterChip(
        onClick = onClick,
        label = {
            Text(
                text = when (priority) {
                    Priority.LOW -> "低"
                    Priority.MEDIUM -> "中"
                    Priority.HIGH -> "高"
                    Priority.URGENT -> "緊急"
                },
                fontSize = 12.sp
            )
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = backgroundColor,
            selectedLabelColor = textColor
        ),
        leadingIcon = if (isSelected) {
            {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        } else null
    )
} 