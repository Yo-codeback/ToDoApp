package com.lugumaker.todo.notification

import android.content.Context
import com.lugumaker.todo.data.ToDoRepository

/**
 * 待辦事項通知服務
 */
class TodoNotificationService(private val context: Context) {
    
    private val notificationManager = TodoNotificationManager(context)
    private val repository = ToDoRepository()
    
    /**
     * 檢查並發送通知
     */
    fun checkAndSendNotifications() {
        // 檢查過期的待辦事項
        val overdueTodos = repository.getOverdueTodos()
        overdueTodos.forEach { todo ->
            notificationManager.sendOverdueNotification(todo)
        }
        
        // 檢查即將到期的待辦事項
        val dueSoonTodos = repository.getDueSoonTodos()
        dueSoonTodos.forEach { todo ->
            notificationManager.sendDueSoonNotification(todo)
        }
    }
    
    /**
     * 為特定待辦事項設定通知
     */
    fun scheduleNotificationForTodo(todo: com.lugumaker.todo.data.ToDoItem) {
        // 這裡可以實作更複雜的排程邏輯
        // 目前先實作基本的即時檢查
        if (todo.isOverdue()) {
            notificationManager.sendOverdueNotification(todo)
        } else if (todo.isDueSoon()) {
            notificationManager.sendDueSoonNotification(todo)
        }
    }
    
    /**
     * 取消特定待辦事項的通知
     */
    fun cancelNotificationForTodo(todoId: String) {
        // 可以實作取消特定待辦事項的通知邏輯
        // 目前先取消所有通知
        notificationManager.cancelAllNotifications()
    }
} 