package com.lugumaker.todo.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * 待辦事項 DAO
 */
@Dao
interface TodoDao {
    
    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAllTodos(): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY dueDate ASC, priority DESC")
    fun getActiveTodos(): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY updatedAt DESC")
    fun getCompletedTodos(): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE dueDate IS NOT NULL AND dueDate >= :today ORDER BY dueDate ASC")
    fun getTodayTodos(today: LocalDateTime): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE dueDate IS NOT NULL AND dueDate < :now AND isCompleted = 0")
    fun getOverdueTodos(now: LocalDateTime): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE dueDate IS NOT NULL AND dueDate BETWEEN :now AND :tomorrow AND isCompleted = 0")
    fun getDueSoonTodos(now: LocalDateTime, tomorrow: LocalDateTime): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE priority = :priority ORDER BY dueDate ASC")
    fun getTodosByPriority(priority: Priority): Flow<List<TodoEntity>>
    
    @Query("SELECT * FROM todos WHERE category = :category ORDER BY dueDate ASC")
    fun getTodosByCategory(category: String): Flow<List<TodoEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoEntity): Long
    
    @Update
    suspend fun updateTodo(todo: TodoEntity)
    
    @Delete
    suspend fun deleteTodo(todo: TodoEntity)
    
    @Query("DELETE FROM todos WHERE isCompleted = 1")
    suspend fun deleteCompletedTodos()
    
    @Query("UPDATE todos SET isCompleted = :completed, updatedAt = :updatedAt WHERE id = :todoId")
    suspend fun updateCompletionStatus(todoId: Long, completed: Boolean, updatedAt: LocalDateTime)
    
    @Query("SELECT COUNT(*) FROM todos WHERE isCompleted = 0")
    fun getActiveTodoCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM todos WHERE isCompleted = 1")
    fun getCompletedTodoCount(): Flow<Int>
    
    @Query("SELECT COUNT(*) FROM todos WHERE dueDate IS NOT NULL AND dueDate < :now AND isCompleted = 0")
    fun getOverdueTodoCount(now: LocalDateTime): Flow<Int>
} 