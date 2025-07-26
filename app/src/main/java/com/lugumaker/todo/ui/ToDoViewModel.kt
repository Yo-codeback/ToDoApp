package com.lugumaker.todo.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.lugumaker.todo.data.ToDoItem
import com.lugumaker.todo.data.ToDoRepository
import java.time.LocalDateTime

/**
 * 待辦事項 ViewModel
 */
class ToDoViewModel : ViewModel() {
    private val repository = ToDoRepository()
    
    // UI 狀態
    private val _todos = mutableStateListOf<ToDoItem>()
    val todos: List<ToDoItem> get() = _todos
    
    var showAddDialog by mutableStateOf(false)
        private set
    
    var newTodoTitle by mutableStateOf("")
        private set
    
    var newTodoDescription by mutableStateOf("")
        private set
    
    var newTodoDueDate by mutableStateOf<LocalDateTime?>(null)
        private set
    
    init {
        // 載入初始資料
        loadTodos()
    }
    
    /**
     * 載入待辦事項
     */
    private fun loadTodos() {
        _todos.clear()
        _todos.addAll(repository.todos)
    }
    
    /**
     * 新增待辦事項
     */
    fun addTodo() {
        if (newTodoTitle.isNotBlank()) {
            val todo = repository.addTodo(
                title = newTodoTitle,
                description = newTodoDescription,
                dueDate = newTodoDueDate
            )
            _todos.add(todo)
            clearNewTodoForm()
            showAddDialog = false
        }
    }
    
    /**
     * 切換完成狀態
     */
    fun toggleComplete(id: String) {
        if (repository.toggleComplete(id)) {
            loadTodos()
        }
    }
    
    /**
     * 刪除待辦事項
     */
    fun deleteTodo(id: String) {
        if (repository.deleteTodo(id)) {
            loadTodos()
        }
    }
    
    /**
     * 顯示新增對話框
     */
    fun showAddDialog() {
        showAddDialog = true
    }
    
    /**
     * 隱藏新增對話框
     */
    fun hideAddDialog() {
        showAddDialog = false
        clearNewTodoForm()
    }
    
    /**
     * 更新新待辦事項標題
     */
    fun updateNewTodoTitle(title: String) {
        newTodoTitle = title
    }
    
    /**
     * 更新新待辦事項描述
     */
    fun updateNewTodoDescription(description: String) {
        newTodoDescription = description
    }
    
    /**
     * 更新新待辦事項到期時間
     */
    fun updateNewTodoDueDate(dueDate: LocalDateTime?) {
        newTodoDueDate = dueDate
    }
    
    /**
     * 清除新待辦事項表單
     */
    private fun clearNewTodoForm() {
        newTodoTitle = ""
        newTodoDescription = ""
        newTodoDueDate = null
    }
    
    /**
     * 取得未完成的待辦事項
     */
    fun getIncompleteTodos(): List<ToDoItem> {
        return _todos.filter { !it.isCompleted }
    }
    
    /**
     * 取得已完成的待辦事項
     */
    fun getCompletedTodos(): List<ToDoItem> {
        return _todos.filter { it.isCompleted }
    }
    
    /**
     * 取得即將到期的待辦事項
     */
    fun getDueSoonTodos(): List<ToDoItem> {
        return _todos.filter { it.isDueSoon() }
    }
    
    /**
     * 取得已過期的待辦事項
     */
    fun getOverdueTodos(): List<ToDoItem> {
        return _todos.filter { it.isOverdue() }
    }
} 