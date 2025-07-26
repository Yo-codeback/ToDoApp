這是一個基於 **Jetpack Compose** 和 **Material 3.0** 的 Android ToDo 應用程式
不需要編譯程式，使用者會自己編譯

---

## 功能規劃（2025/07/26）
- 最基本的待辦事項通知提醒功能
- 最基本的桌面小工具（Widget）功能

## 開發任務規劃
1. **資料結構設計** - 設計 ToDoItem 資料類
2. **主介面實作** - 使用 Jetpack Compose 和 Material 3.0
3. **通知提醒功能** - 實作本地通知系統
4. **桌面小工具** - 實作 Widget 功能

## 開發日誌

### 2025/07/26 - 專案初始化
- [x] 建立專案文檔
- [x] 規劃開發任務
- [x] 設計資料結構
- [x] 實作主介面
- [x] 實作通知功能
- [ ] 實作小工具功能

#### 已完成功能
1. **資料結構設計**
   - 建立 `ToDoItem` 資料類，包含標題、描述、到期時間、完成狀態等屬性
   - 建立 `ToDoRepository` 管理類，提供增刪改查功能
   - 實作過期檢查和即將到期檢查邏輯

2. **主介面實作**
   - 使用 Jetpack Compose 和 Material 3.0 設計 UI
   - 建立 `ToDoScreen` 主畫面，包含統計資訊和待辦事項列表
   - 實作新增、刪除、完成狀態切換功能
   - 建立 `ToDoViewModel` 管理 UI 狀態

3. **通知功能實作**
   - 建立 `TodoNotificationManager` 管理本地通知
   - 建立 `TodoNotificationService` 服務類別
   - 實作過期和即將到期通知
   - 整合通知功能到 ViewModel 中
   - 加入通知權限設定

#### 技術架構
- **UI 層**: Jetpack Compose + Material 3.0
- **狀態管理**: ViewModel + State
- **資料層**: Repository Pattern
- **通知系統**: NotificationManager + NotificationService
- **架構模式**: MVVM

#### 檔案結構
```
app/src/main/java/com/lugumaker/todo/
├── data/
│   ├── ToDoItem.kt              # 待辦事項資料類
│   ├── ToDoRepository.kt        # 資料管理類
│   └── ToDoRepositoryTest.kt    # 測試檔案
├── notification/
│   ├── TodoNotificationManager.kt    # 通知管理
│   └── TodoNotificationService.kt    # 通知服務
├── ui/
│   ├── ToDoScreen.kt            # 主畫面 UI
│   ├── ToDoViewModel.kt         # ViewModel
│   └── theme/                   # 主題相關
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt              # 主活動
```

#### 下一步開發計劃
1. **桌面小工具功能** - 實作 Widget 顯示今日待辦事項
2. **資料持久化** - 加入 Room 資料庫儲存
3. **進階通知** - 實作排程通知和自訂提醒時間
4. **UI 優化** - 加入動畫效果和更好的使用者體驗