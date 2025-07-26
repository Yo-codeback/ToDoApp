package com.lugumaker.todo.data

import java.time.LocalDateTime

/**
 * 待辦事項資料類
 * @param id 唯一識別碼
 * @param title 標題
 * @param description 描述（可選）
 * @param dueDate 到期時間（可選）
 * @param isCompleted 是否已完成
 * @param createdAt 建立時間
 * @param updatedAt 更新時間
 */
data class ToDoItem(
    val id: String = "",
    val title: String,
    val description: String = "",
    val dueDate: LocalDateTime? = null,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    /**
     * 檢查是否已過期
     */
    fun isOverdue(): Boolean {
        return dueDate != null && !isCompleted && LocalDateTime.now().isAfter(dueDate)
    }
    
    /**
     * 檢查是否即將到期（24小時內）
     */
    fun isDueSoon(): Boolean {
        if (dueDate == null || isCompleted) return false
        val now = LocalDateTime.now()
        val tomorrow = now.plusDays(1)
        return dueDate.isBefore(tomorrow) && dueDate.isAfter(now)
    }
} 