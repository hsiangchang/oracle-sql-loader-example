# 如何處理 Oracle 的大檔案匯入：SQL Loader
<br />
相信很多人都會遇到大檔案匯入資料庫的情況，當然其中不乏很多人會直接使用程式語言（Java、.NET、Python ... ）直接處理。但若考慮到效能或便利性，使用資料庫的相關工具也會是不錯的選擇。在 Oracle 中處理大檔匯入的其中一種方式就是 SQL Loader，以下為處理的範例。

---

## 注意

* 範例使用 Docker 來建置 Oracle 的測試資料庫，所以請先準備好 Docker 的基本環境。若已有現成的 Oracle 資料庫，可省略建置測試環境的部份。

---

## 建置測試環境

▲ 使用 Docker 建置 Oracle 測試資料庫

在 docker-compose.yml 的同一層資料夾下，執行指令
```
$ docker-compose up -d
```


### 執行指令（分隔符號範例）
```
sqlldr userid=demo/123456 control=users.ctl direct=TRUE parallel=TRUE
```

### 執行指令（固定長度範例）
```
sqlldr userid=demo/123456 control=vendors.ctl direct=TRUE parallel=TRUE
```
