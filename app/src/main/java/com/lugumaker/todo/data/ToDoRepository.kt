package com.lugumaker.todo.data

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 待辦事項 Repository
 */
class TodoRepository(private val todoDao: TodoDao) {
    
    // 獲取所有待辦事項
    fun getAllTodos(): Flow<List<TodoEntity>> = todoDao.getAllTodos()
    
    // 獲取活躍待辦事項
    fun getActiveTodos(): Flow<List<TodoEntity>> = todoDao.getActiveTodos()
    
    // 獲取已完成待辦事項
    fun getCompletedTodos(): Flow<List<TodoEntity>> = todoDao.getCompletedTodos()
    
    // 獲取今日待辦事項
    fun getTodayTodos(): Flow<List<TodoEntity>> {
        val today = LocalDateTime.now().toLocalDate().atStartOfDay()
        return todoDao.getTodayTodos(today)
    }
    
    // 獲取過期待辦事項
    fun getOverdueTodos(): Flow<List<TodoEntity>> {
        val now = LocalDateTime.now()
        return todoDao.getOverdueTodos(now)
    }
    
    // 獲取即將到期待辦事項
    fun getDueSoonTodos(): Flow<List<TodoEntity>> {
        val now = LocalDateTime.now()
        val tomorrow = now.plusDays(1)
        return todoDao.getDueSoonTodos(now, tomorrow)
    }
    
    // 根據優先級獲取待辦事項
    fun getTodosByPriority(priority: Priority): Flow<List<TodoEntity>> = 
        todoDao.getTodosByPriority(priority)
    
    // 根據分類獲取待辦事項
    fun getTodosByCategory(category: String): Flow<List<TodoEntity>> = 
        todoDao.getTodosByCategory(category)
    
    // 新增待辦事項
    suspend fun insertTodo(todo: TodoEntity): Long = todoDao.insertTodo(todo)
    
    // 更新待辦事項
    suspend fun updateTodo(todo: TodoEntity) = todoDao.updateTodo(todo)
    
    // 刪除待辦事項
    suspend fun deleteTodo(todo: TodoEntity) = todoDao.deleteTodo(todo)
    
    // 刪除已完成待辦事項
    suspend fun deleteCompletedTodos() = todoDao.deleteCompletedTodos()
    
    // 切換完成狀態
    suspend fun toggleCompletionStatus(todo: TodoEntity) {
        val updatedTodo = todo.copy(
            isCompleted = !todo.isCompleted,
            updatedAt = LocalDateTime.now()
        )
        todoDao.updateTodo(updatedTodo)
    }
    
    // 獲取統計資訊
    fun getActiveTodoCount(): Flow<Int> = todoDao.getActiveTodoCount()
    fun getCompletedTodoCount(): Flow<Int> = todoDao.getCompletedTodoCount()
    fun getOverdueTodoCount(): Flow<Int> {
        val now = LocalDateTime.now()
        return todoDao.getOverdueTodoCount(now)
    }
} 