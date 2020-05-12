# 如何處理 Oracle 的大檔案匯入：SQL Loader
<br />
相信很多人都會遇到大檔案匯入資料庫的情況，當然其中不乏很多人會直接使用程式語言（Java、.NET、Python ... ）直接處理。但若考慮到效能或便利性，使用資料庫的相關工具也會是不錯的選擇。在 Oracle 中處理大檔匯入的其中一種方式就是 SQL Loader，以下為處理的範例。

---

## 使用工具：
* Docker : 建置 Oracle 的測試環境

### 執行指令（分隔符號範例）
```
sqlldr userid=demo/123456 control=users.ctl direct=TRUE parallel=TRUE
```

### 執行指令（固定長度範例）
```
sqlldr userid=demo/123456 control=vendors.ctl direct=TRUE parallel=TRUE
```
