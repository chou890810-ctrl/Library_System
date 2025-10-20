# 📚 Library Management System (圖書館管理系統)

---

## 🧩 專案操作步驟（老師測試專用）

1️⃣ **解壓縮專案後**  
　確認資料夾中包含 `src/`、`images/`、`Library_System.jar`。  

2️⃣ **啟動資料庫**  
　開啟 MySQL，建立名為 `library_system` 的資料庫：  
```sql
CREATE DATABASE library_system CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
3️⃣ 匯入資料表
　使用以下 SQL（或附檔中的 library_system.sql）：

sql

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
4️⃣ 設定連線帳密（DbConnection.java）
　請修改你的 MySQL 帳號密碼，例如：

java

private static final String URL = "jdbc:mysql://localhost:3306/library_system?serverTimezone=UTC";
private static final String USER = "root";
private static final String PASSWORD = "你的密碼";
5️⃣ 執行方式

若使用 Eclipse：
開啟 src/ui/LoginUI.java → 按右鍵 → Run As → Java Application

若使用 jar：

bash

java -jar Library_System.jar
6️⃣ 測試流程

步驟	操作	預期結果
①	點選「註冊」，建立新帳號	顯示「註冊成功」
②	登入系統	顯示主畫面與帳號資訊
③	點選「借書」	顯示書籍清單，可借閱庫存書
④	點選「還書」	可選擇借閱紀錄進行歸還
⑤	點選「借閱紀錄」	顯示歷史清單，可匯出 Excel
⑥	點選「登出」	返回登入畫面

7️⃣ 測試帳號範例

Email	密碼
test@demo.com	1234

🧰 技術重點
Java Swing 圖形介面

MySQL 資料庫

SHA-256 密碼雜湊

MVC + DAO 架構

Apache POI 匯出 Excel

Session 管理登入狀態

🧑‍💻 作者資訊
開發者： 周志陽 (Zhiyang Chou)
GitHub Repo： 👉 Library_System
開發時間： 2025 年
課程名稱： 巨匠 Java 雲端程式設計實務班
