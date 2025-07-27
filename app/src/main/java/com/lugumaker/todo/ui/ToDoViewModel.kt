package com.lugumaker.todo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.lugumaker.todo.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

/**
 * 待辦事項 ViewModel
 */
class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    
    // UI 狀態
    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()
    
    // 待辦事項列表
    val activeTodos = repository.getActiveTodos()
    val completedTodos = repository.getCompletedTodos()
    val todayTodos = repository.getTodayTodos()
    val overdueTodos = repository.getOverdueTodos()
    val dueSoonTodos = repository.getDueSoonTodos()
    
    // 統計資訊
    val activeTodoCount = repository.getActiveTodoCount()
    val completedTodoCount = repository.getCompletedTodoCount()
    val overdueTodoCount = repository.getOverdueTodoCount()
    
    init {
        // 載入初始資料
        loadTodos()
    }
    
    /**
     * 載入待辦事項
     */
    private fun loadTodos() {
        viewModelScope.launch {
            combine(
                activeTodos,
                completedTodos,
                activeTodoCount,
                completedTodoCount,
                overdueTodoCount
            ) { active, completed, activeCount, completedCount, overdueCount ->
                TodoUiState(
                    activeTodos = active,
                    completedTodos = completed,
                    activeCount = activeCount,
                    completedCount = completedCount,
                    overdueCount = overdueCount
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }
    
    /**
     * 新增待辦事項
     */
    fun addTodo(
        title: String,
        description: String = "",
        dueDate: LocalDateTime? = null,
        priority: Priority = Priority.MEDIUM,
        category: String = ""
    ) {
        if (title.isNotBlank()) {
            viewModelScope.launch {
                val todo = TodoEntity(
                    title = title,
                    description = description,
                    dueDate = dueDate,
                    priority = priority,
                    category = category
                )
                repository.insertTodo(todo)
            }
        }
    }
    
    /**
     * 更新待辦事項
     */
    fun updateTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.updateTodo(todo.copy(updatedAt = LocalDateTime.now()))
        }
    }
    
    /**
     * 切換完成狀態
     */
    fun toggleComplete(todo: TodoEntity) {
        viewModelScope.launch {
            repository.toggleCompletionStatus(todo)
        }
    }
    
    /**
     * 刪除待辦事項
     */
    fun deleteTodo(todo: TodoEntity) {
        viewModelScope.launch {
            repository.deleteTodo(todo)
        }
    }
    
    /**
     * 刪除已完成待辦事項
     */
    fun deleteCompletedTodos() {
        viewModelScope.launch {
            repository.deleteCompletedTodos()
        }
    }
    
    /**
     * 顯示新增對話框
     */
    fun showAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = true)
    }
    
    /**
     * 隱藏新增對話框
     */
    fun hideAddDialog() {
        _uiState.value = _uiState.value.copy(showAddDialog = false)
    }
    
    /**
     * 更新新待辦事項表單
     */
    fun updateNewTodoForm(
        title: String = _uiState.value.newTodoTitle,
        description: String = _uiState.value.newTodoDescription,
        dueDate: LocalDateTime? = _uiState.value.newTodoDueDate,
        priority: Priority = _uiState.value.newTodoPriority,
        category: String = _uiState.value.newTodoCategory
    ) {
        _uiState.value = _uiState.value.copy(
            newTodoTitle = title,
            newTodoDescription = description,
            newTodoDueDate = dueDate,
            newTodoPriority = priority,
            newTodoCategory = category
        )
    }
    
    /**
     * 清除新待辦事項表單
     */
    private fun clearNewTodoForm() {
        _uiState.value = _uiState.value.copy(
            newTodoTitle = "",
            newTodoDescription = "",
            newTodoDueDate = null,
            newTodoPriority = Priority.MEDIUM,
            newTodoCategory = ""
        )
    }
    
    /**
     * 提交新待辦事項
     */
    fun submitNewTodo() {
        val currentState = _uiState.value
        if (currentState.newTodoTitle.isNotBlank()) {
            addTodo(
                title = currentState.newTodoTitle,
                description = currentState.newTodoDescription,
                dueDate = currentState.newTodoDueDate,
                priority = currentState.newTodoPriority,
                category = currentState.newTodoCategory
            )
            clearNewTodoForm()
            hideAddDialog()
        }
    }
}

/**
 * UI 狀態
 */
data class TodoUiState(
    val activeTodos: List<TodoEntity> = emptyList(),
    val completedTodos: List<TodoEntity> = emptyList(),
    val activeCount: Int = 0,
    val completedCount: Int = 0,
    val overdueCount: Int = 0,
    val showAddDialog: Boolean = false,
    val newTodoTitle: String = "",
    val newTodoDescription: String = "",
    val newTodoDueDate: LocalDateTime? = null,
    val newTodoPriority: Priority = Priority.MEDIUM,
    val newTodoCategory: String = ""
)

/**
 * ViewModel Factory
 */
class TodoViewModelFactory(private val repository: TodoRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TodoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
} 