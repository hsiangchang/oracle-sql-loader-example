# 如何處理 Oracle 的大檔案匯入：SQL Loader
<br />
相信很多人都會遇到大檔案匯入資料庫的情況，當然其中不乏很多人會直接使用程式語言（Java、.NET、Python ... ）直接處理。但若考慮到效能或便利性，使用資料庫的相關工具也會是不錯的選擇。在 Oracle 中處理大檔匯入的其中一種方式就是 SQL Loader，以下為處理的範例。

---
<br />
<br />

## 注意

* 範例使用 Docker 來建置 Oracle 的測試資料庫，所以請先準備好 Docker 的基本環境。若已有現成的 Oracle 資料庫，可省略建置測試環境的部份。

---
<br />
<br />

## 建置測試環境

**使用 Docker 建置 Oracle 測試資料庫**

```yml
version: '3'

services: 
  msk_oracle:
    image: oracleinanutshell/oracle-xe-11g
    container_name: oraExam
    ports:
      - 1533:1521
    environment:
      - "NLS_LANG=AMERICAN_AMERICA.UTF8"
    volumes:
      - ./init/0_init.sql:/docker-entrypoint-initdb.d/0_init.sql
      - ./init/1_schema.sql:/docker-entrypoint-initdb.d/1_schema.sql
      - ./init/users.ctl:/home/users.ctl
      - ./init/users.dat:/home/users.dat
      - ./init/vendors.ctl:/home/vendors.ctl
      - ./init/vendors.dat:/home/vendors.dat
    
```


1. 在 docker-compose.yml 的同一層資料夾下，執行指令
```sh
$ docker-compose up -d
```

2. 進入 container ：名稱為 oraExam
```sh
$ docker exec -ti oraExam bash
```


### 匯入檔案：分隔符號範例
```sh
$ sqlldr userid=demo/123456 control=users.ctl direct=TRUE parallel=TRUE
```

### 匯入檔案：固定長度範例
```sh
$ sqlldr userid=demo/123456 control=vendors.ctl direct=TRUE parallel=TRUE
```
