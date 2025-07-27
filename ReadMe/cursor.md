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
│   ├── TodoEntity.kt            # 待辦事項資料實體
│   ├── TodoDao.kt               # 資料庫存取介面
│   ├── TodoDatabase.kt          # Room 資料庫
│   ├── TodoRepository.kt        # 資料管理類
│   └── Converters.kt            # 類型轉換器
├── ui/
│   ├── TodoScreen.kt            # 主畫面 UI
│   ├── TodoViewModel.kt         # ViewModel
│   ├── AddTodoDialog.kt         # 新增對話框
│   └── theme/                   # 主題相關
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
└── MainActivity.kt              # 主活動
```

#### 下一步開發計劃
1. **重新實作通知功能** - 基於新的資料庫架構
2. **重新實作桌面小工具** - 基於新的資料庫架構
3. **進階功能** - 實作排程通知和自訂提醒時間
4. **資料匯入匯出** - 支援備份和還原功能
5. **主題切換** - 支援深色/淺色主題
6. **搜尋功能** - 實作待辦事項搜尋
7. **統計圖表** - 加入完成率統計圖表

### 2025/07/26 - 圖標更新
- [x] 建立專用通知圖標 `ic_notification.xml`
- [x] 建立應用程式圖標 `ic_todo_app.xml`
- [x] 建立新增按鈕圖標 `ic_add_todo.xml`
- [x] 更新通知管理類別使用新圖標
- [x] 更新 AndroidManifest.xml 使用新應用程式圖標
- [x] 更新 UI 使用自訂圖標

### 2025/07/26 - 桌面小工具功能實作
- [x] 建立 Widget 佈局檔案 `todo_widget.xml`
- [x] 建立 Widget 項目佈局 `todo_widget_item.xml`
- [x] 建立 Widget 背景和圖標
- [x] 實作 `TodoWidgetProvider` 管理 Widget 更新
- [x] 實作 `TodoWidgetService` 提供數據
- [x] 建立 Widget 資訊檔案 `todo_widget_info.xml`
- [x] 更新 AndroidManifest.xml 註冊 Widget
- [x] 整合 Widget 更新到 ViewModel
- [x] 更新 UI 支援 Widget 同步

#### Widget 功能特色
- **今日待辦事項顯示**：只顯示今天的待辦事項
- **完成狀態圖標**：綠色勾選表示已完成，白色方框表示未完成
- **時間顯示**：顯示到期時間（HH:mm 格式）
- **統計資訊**：顯示完成進度（已完成/總數）
- **點擊開啟**：點擊 Widget 或項目可開啟應用程式
- **自動更新**：新增、完成、刪除待辦事項時自動更新 Widget
- **可調整大小**：支援水平和垂直調整大小

### 2025/07/26 - 應用程式完全重構
- [x] 重新設計資料庫架構，使用 Room 持久化儲存
- [x] 建立新的資料實體 `TodoEntity` 和 `Priority` 列舉
- [x] 實作 Room DAO 和資料庫類別
- [x] 重新設計 Repository 模式，使用 Flow 響應式程式設計
- [x] 建立全新的 ViewModel，支援狀態管理
- [x] 重新設計 UI，使用 Material 3.0 和現代化設計
- [x] 加入優先級系統（低、中、高、緊急）
- [x] 加入分類功能
- [x] 實作美觀的統計卡片
- [x] 加入動畫效果和展開/收起功能
- [x] 重新設計新增待辦事項對話框
- [x] 移除舊的通知和 Widget 功能（可後續重新實作）

#### 新功能特色
- **資料持久化**：使用 Room 資料庫，資料永久儲存
- **響應式 UI**：使用 Flow 和 StateFlow，UI 自動更新
- **優先級管理**：四級優先級系統，視覺化優先級指示器
- **分類功能**：支援自訂分類標籤
- **現代化設計**：Material 3.0 設計語言，美觀的卡片佈局
- **動畫效果**：平滑的展開/收起動畫
- **統計資訊**：即時顯示進行中、已完成、已過期數量
- **智慧時間顯示**：今天、明天、完整日期格式
- **完成狀態管理**：可展開查看已完成項目
- **一鍵清除**：快速清除所有已完成項目