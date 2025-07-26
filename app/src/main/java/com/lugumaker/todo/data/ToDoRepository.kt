package com.lugumaker.todo.data

import java.time.LocalDateTime
import java.util.UUID

/**
 * 待辦事項資料管理類
 */
class ToDoRepository {
    private val _todos = mutableListOf<ToDoItem>()
    val todos: List<ToDoItem> get() = _todos.toList()
    
    /**
     * 新增待辦事項
     */
    fun addTodo(title: String, description: String = "", dueDate: LocalDateTime? = null): ToDoItem {
        val todo = ToDoItem(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            dueDate = dueDate
        )
        _todos.add(todo)
        return todo
    }
    
    /**
     * 更新待辦事項
     */
    fun updateTodo(todo: ToDoItem): Boolean {
        val index = _todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            _todos[index] = todo.copy(updatedAt = LocalDateTime.now())
            return true
        }
        return false
    }
    
    /**
     * 刪除待辦事項
     */
    fun deleteTodo(id: String): Boolean {
        val index = _todos.indexOfFirst { it.id == id }
        if (index != -1) {
            _todos.removeAt(index)
            return true
        }
        return false
    }
    
    /**
     * 取得待辦事項
     */
    fun getTodo(id: String): ToDoItem? {
        return _todos.find { it.id == id }
    }
    
    /**
     * 切換完成狀態
     */
    fun toggleComplete(id: String): Boolean {
        val todo = getTodo(id) ?: return false
        val updatedTodo = todo.copy(
            isCompleted = !todo.isCompleted,
            updatedAt = LocalDateTime.now()
        )
        return updateTodo(updatedTodo)
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