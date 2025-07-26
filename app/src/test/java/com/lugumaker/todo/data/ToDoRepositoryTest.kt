package com.lugumaker.todo.data

import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime

/**
 * ToDoRepository 測試類別
 */
class ToDoRepositoryTest {
    
    @Test
    fun testAddTodo() {
        val repository = ToDoRepository()
        val todo = repository.addTodo("測試待辦事項", "測試描述")
        
        assertNotNull(todo)
        assertEquals("測試待辦事項", todo.title)
        assertEquals("測試描述", todo.description)
        assertFalse(todo.isCompleted)
        assertEquals(1, repository.todos.size)
    }
    
    @Test
    fun testToggleComplete() {
        val repository = ToDoRepository()
        val todo = repository.addTodo("測試待辦事項")
        
        assertFalse(todo.isCompleted)
        
        val result = repository.toggleComplete(todo.id)
        assertTrue(result)
        
        val updatedTodo = repository.getTodo(todo.id)
        assertNotNull(updatedTodo)
        assertTrue(updatedTodo!!.isCompleted)
    }
    
    @Test
    fun testDeleteTodo() {
        val repository = ToDoRepository()
        val todo = repository.addTodo("測試待辦事項")
        
        assertEquals(1, repository.todos.size)
        
        val result = repository.deleteTodo(todo.id)
        assertTrue(result)
        
        assertEquals(0, repository.todos.size)
    }
    
    @Test
    fun testIsOverdue() {
        val repository = ToDoRepository()
        val pastDate = LocalDateTime.now().minusDays(1)
        val todo = repository.addTodo("過期待辦事項", dueDate = pastDate)
        
        assertTrue(todo.isOverdue())
    }
    
    @Test
    fun testIsDueSoon() {
        val repository = ToDoRepository()
        val futureDate = LocalDateTime.now().plusHours(12)
        val todo = repository.addTodo("即將到期待辦事項", dueDate = futureDate)
        
        assertTrue(todo.isDueSoon())
    }
} 