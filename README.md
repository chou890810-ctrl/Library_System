# 📚 Library Management System (圖書館管理系統)

一個使用 **Java Swing + MySQL** 開發的桌面應用程式，提供會員登入、註冊、借書、還書、查詢借閱紀錄與匯出 Excel 功能。  
採用 **MVC 架構 + DAO 模式**，以 **Maven** 管理相依套件，並實作會員登入驗證與 Session 管理。

---

## 🧩 專案操作步驟

### 1️⃣ 解壓縮專案
確認資料夾中包含以下內容：
src/
images/
Library_System.jar

yaml


---

### 2️⃣ 建立資料庫
在 MySQL 中執行以下指令建立資料庫：
```sql
CREATE DATABASE library_system CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
```
3️⃣ 匯入資料表
```sql

CREATE TABLE member (
  id INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(50),
  email VARCHAR(100) UNIQUE,
  password_hash VARCHAR(256),
  created_at DATETIME
);

CREATE TABLE book (
  id INT PRIMARY KEY AUTO_INCREMENT,
  isbn VARCHAR(50),
  title VARCHAR(100),
  author VARCHAR(100),
  stock INT,
  price DOUBLE
);

CREATE TABLE loan (
  id INT PRIMARY KEY AUTO_INCREMENT,
  member_id INT,
  book_id INT,
  borrowed_at DATETIME,
  due_at DATETIME,
  returned_at DATETIME,
  status VARCHAR(20),
  fine_amount DOUBLE
);
```
4️⃣ 修改資料庫設定
請打開 util/DbConnection.java，依照你的環境修改資料庫連線設定：

```java

private static final String URL = "jdbc:mysql://localhost:3306/library_system?serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "你的MySQL密碼";

```
5️⃣ 執行方式
✅ 方法一：執行 JAR 檔
```bash

java -jar Library_System.jar

```
✅ 方法二：使用 Eclipse / IntelliJ
匯入 Maven 專案

右鍵執行 src/ui/LoginUI.java

6️⃣ 系統操作流程
步驟	操作說明	預期結果
①	點選「註冊」新增會員帳號	顯示「註冊成功」
②	登入系統	顯示主畫面與帳號資訊
③	點選「借書」	顯示書籍清單，可借閱庫存書
④	點選「還書」	可選擇借閱紀錄進行歸還
⑤	點選「借閱紀錄」	顯示歷史清單，可匯出 Excel
⑥	點選「登出」	返回登入畫面
7️⃣ 測試帳號（可自行新增）
Email	密碼
test@demo.com
	1234
⚙️ 技術架構
模組	功能
DAO 層	負責資料庫操作（BookDao、MemberDao、LoanDao）
Service 層	處理商業邏輯（借書、還書、罰金、Session 管理）
UI 層	使用 Java Swing 建構圖形介面
Util 工具類	包含 DbConnection、PasswordUtil、SessionManager
🧱 系統功能說明
模組	功能說明
🔐 LoginUI	會員登入（支援 SHA-256 雜湊驗證）
📝 RegisterUI	新會員註冊並自動加密密碼
🏠 LibraryMainUI	主選單：顯示登入資訊與四大功能入口
📖 BorrowBookUI	借書功能：檢查庫存、減少庫存、建立借閱紀錄
📕 ReturnBookUI	還書功能：更新狀態、恢復庫存、計算罰金
📜 BorrowHistoryUI	借閱紀錄查詢與 Excel 匯出
📊 ExcelReport	使用 Apache POI 將紀錄輸出成報表
🧰 使用技術
類別	名稱
語言	Java
介面	Java Swing
資料庫	MySQL 8.x
加密	SHA-256（PasswordUtil）
架構	MVC + DAO
套件	Apache POI（Excel 匯出）
IDE	Eclipse / IntelliJ
Build 工具	Maven
🖼️ 介面展示
登入畫面	主畫面
(可放入 LoginUI 截圖)	(可放入 LibraryMainUI 截圖)
📦 專案結構
```bash
src/
 ├── dao/                  # DAO 介面層
 ├── dao/impl/             # DAO 實作層
 ├── model/                # 資料模型 (Book, Member, Loan)
 ├── service/              # 業務邏輯介面
 ├── service/impl/         # 業務邏輯實作
 ├── ui/                   # Swing 介面
 └── util/                 # 工具類 (DbConnection, PasswordUtil, SessionManager)
```
👨‍💻 作者資訊

開發者：周志陽 (Zhiyang Chou)

GitHub Repo： Library_System

開發時間：2025 年

課程名稱：巨匠 Java 雲端程式設計實務班
