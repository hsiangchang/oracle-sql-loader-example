# 如何處理 Oracle 的大檔案匯入：SQL Loader
<br />
相信很多人都會遇到大檔案匯入資料庫的情況，當然其中不乏很多人會直接使用程式語言（Java、.NET、Python ... ）直接處理。但若考慮到效能或便利性，使用資料庫的相關工具也會是不錯的選擇。在 Oracle 中處理大檔匯入的其中一種方式就是 SQL Loader，以下為處理的範例。

---
<br />
<br />

## 注意

* 範例使用 Docker 來建置 Oracle 的測試資料庫，所以請先準備好 Docker 的基本環境。若已有現成的 Oracle 資料庫，可省略建置測試環境的部份。
* **範例資料庫帳號密碼：demo/123456**
---
<br />
<br />

## 建置測試環境(使用 Docker 建置 Oracle 測試資料庫)

**1. docker-compose.yml 檔案結構**
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
<br />

| 屬性名稱        |屬性範例        | 說明  |
| -------------  | ------------- | ----- |
| container_name | oraExam      | 非必要，為 Docker Container 的名稱，方便管理 |
| ports          | 1533:1521    | 必要：1533 為連線的 PORT |
| volumes      | 0_init.sql | 建立使用者資料庫語法 |
|              | 1_schema.sql | 建立資料庫 TABLE 語法 |
|              | users.ctl  | SQL Loader 的 CTL 檔 |
|              | users.dat  | 測試資料：100筆分隔符號範例 |
|              | vendors.ctl  | SQL Loader 的 CTL 檔 |
|              | vendors.dat  | 測試資料：100筆固定長度範例 |




<br />
<br />


**2. 在 docker-compose.yml 的同一層資料夾下，執行指令**
```sh
$ docker-compose up -d
```
![](img/001.png)  

**▲ 自動建置的二個資料表**
![](img/000.png)

**3. 進入 container ：名稱為 oraExam**
```sh
$ docker exec -ti oraExam bash
```
![](img/002.png)  

**4. 進入 container 裡的 home 資料夾**
```sh
$ cd /home
$ ls
```
**列出的檔案，為建置 Container 時匯入，提供測試使用：**<br />
* **users.ctl :** 為逗點分隔處理的 Control 檔
* **users.dat :** 為逗點分隔處理的範例資料檔（100萬筆）
* **vendors.ctl :** 為固定長度處理的 Control 檔
* **vendors.dat :** 為固定長度處理的範例資料檔（100萬筆）
![](img/003.png)  

### 匯入檔案：分隔符號範例

**▲ user.ctl**
```sh
load data
INFILE 'users.dat'
INTO TABLE USERS
APPEND
FIELDS TERMINATED BY ',' TRAILING NULLCOLS
(ID CHAR "TO_NUMBER(:ID)",
 NAME,
 BIRTH DATE "YYYYMMDD")
```

**▲ user.dat**
```sh
1,徐日伪,19840310
2,彭崔椰,19920617
3,王声苔,19870402
4,洪镰胺,19791110
5,石粱方,20150415
6,阮番犁,19960925
7,莊校盅,19940716
8,蕭颤概,19940302
9,田竭婿,19980409
10,蔡犯奢,19740113
(以下省略，共100萬筆)
```

**▲ 執行匯入指令**
```sh
$ sqlldr userid=demo/123456 control=users.ctl direct=TRUE
```

**▲ 成功匯入 100 萬筆資料（約 1 ～ 3 秒）**

![](img/005.png)

<br />
<br />

### 匯入檔案：固定長度範例

**▲ user.ctl**
```sh
load data
INFILE 'vendors.dat'
INTO TABLE VENDORS
TRUNCATE
FIELDS TERMINATED BY ',' TRAILING NULLCOLS
(ID POSITION(1:10) CHAR "TO_NUMBER(:ID)",
 NAME POSITION(11:30) CHAR,
 BIRTH POSITION(31:38) DATE "YYYYMMDD")
```

**▲ user.dat**
```sh
0000000001謝玫毫           19930518
0000000002嚴闰虞           20160622
0000000003葉柿钩           20000606
0000000004胡翁惜           19971207
0000000005邵摘娥           19940730
0000000006郭惫峭           19700802
0000000007游钟鞘           20120118
0000000008鄭胶蠕           19730730
0000000009馮迢勒           19950408
0000000010蔣港冯           19910630
(以下省略，共100萬筆)
```

**▲ 執行匯入指令**
```sh
$ sqlldr userid=demo/123456 control=vendors.ctl direct=TRUE
```

**▲ 成功匯入 100 萬筆資料（約 1 ～ 3 秒）**

![](img/004.png)
