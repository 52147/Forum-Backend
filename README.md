# Forum-Backend
1. 設計功能: 
   - 1. 增加貼文
   - 2. 使用者註冊
2. 在MySql中創建新的Database，名稱為Forum
3. 創建SpringBoot 專案，加入以下依賴：Spring Web, Spring Data JPA, MySQL Driver
4. 實現功能：
 - 定義實體類: Post class, User class。建立論壇中的各種數據，在sql中創建對應的表
 - 定義數據庫操作介面: PostRespository, UserRespository。
 - 定義控制器：PostController, UserController。 
5. 測試功能

## 問題
### Error 1
錯誤：
```
java: cannot find symbol
  symbol:   method name()
  location: @interface org.hibernate.annotations.Table
```
原因：引用包錯誤
解法：
在新版的 Hibernate 中，@Table 標註已經改成了引入 javax.persistence.Table，不再是 org.hibernate.annotations.Table。
因此，Table 標註的引入包改為 javax.persistence.
```
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
```
### Error 2
錯誤：
原因：
在 Post 實體類別中同時使用了 jakarta.persistence.Id 和 org.springframework.data.annotation.Id 這兩種 ID 標註，這可能會引起衝突並導致編譯錯誤。建議只保留其中一個標註，通常建議使用 Jakarta Persistence API 的 @Id 標註。
解法：
```
import jakarta.persistence.*;
```
### Error 3
錯誤：
原因：
缺失引用或依賴
找不到 HttpStatus 符號
解法：
```
import org.springframework.http.HttpStatus;
```
### Error 4
UserRepository 介面的 save() 方法返回類型為 void，但是它應該返回一個 User 對象，以便在保存之後可以獲取保存的實體對象。
將 save() 方法的返回類型更改為 User
```
User save(User user);
```
### Error 5
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).


Process finished with exit code 1
```
原因：
無法正確配置數據源所致。Spring Boot 默認使用 application.properties 或 application.yml 檔案中的配置來配置數據源，但是在您的應用程序中似乎找不到相關配置。

解法：
```
spring.datasource.url=jdbc:mysql://localhost:3306/forum?allowMultiQueries=true

```
