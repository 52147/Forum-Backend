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
### Error 6
錯誤：
```
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'postController': Unsatisfied dependency expressed through field 'postRepository': Error creating bean with name 'postRepository' defined in com.forum.backend.respository.PostRepository defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Could not create query for public abstract java.util.List com.forum.backend.respository.PostRepository.findAllByOrderByCreateTimeDesc(); Reason: Failed to create query for method public abstract java.util.List com.forum.backend.respository.PostRepository.findAllByOrderByCreateTimeDesc(); No property 'createTime' found for type 'Post'
```
原因：
應用程序無法為 PostController 創建 bean，因為通過“postRepository”表示的field 未滿足依賴關係。  	

該錯誤是由 com.forum.backend.repository 包中定義的 PostRepository 類所引起的。  	


更具體地說，錯誤消息表明沒有找到類型“Post”的屬性“createTime”，並且無法為方法“findAllByOrderByCreateTimeDesc()”創建查詢。 
這表明應用程序中定義的 Post 實體沒有名為“createTime”的屬性，或該屬性未使用 @Column 或 @Temporal 註釋。  	


結果我發現數據庫中col名稱為 created_at 而非 create_time，將name 改為"created_at"就好。  	

解法：  	

在 PostRepository 中，修改 @Column 屬性 為 name = "created_at"
```
    @Column(name = "created_at")
    private LocalDateTime createTime;
```    

### Error 7
```
2023-04-02T14:56:03.424-04:00 ERROR 19039 --- [           main] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Web server failed to start. Port 8080 was already in use.

Action:

Identify and stop the process that's listening on port 8080 or configure this application to listen on another port.


Process finished with exit code 1
```

原因：port8080被佔用
解法：
```
lsof -i :8080
kill <PID>
```

### Error 8
400 Bad Request

為了進一步診斷 400 Bad Request 錯誤，可以在服務器代碼中添加日誌記錄Logger，這將記錄請求處理期間發生的任何錯誤或異常。

將postRepository.save(post) 的調用包裝在一個 try-catch 塊中，該block捕獲發生的任何異常。 
如果捕獲到異常，該方法會使用logger instance記錄異常消息，並向客戶端返回 400 Bad Request 響應以及包含異常消息的錯誤消息。

有了這個錯誤處理，能夠在服務器日誌中看到有關 400 Bad Request 錯誤原因的更多信息。 這可以幫助確定錯誤的根本原因並採取適當的步驟來修復它。
```
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
```
```
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

```
```
    @PostMapping("/")
    public ResponseEntity<String> createPost(@RequestBody Post post) {
        try {
            postRepository.save(post);
            return new ResponseEntity<>("Post created successfully", HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception message
            logger.error("Error creating post: {}", e.getMessage());
            // Return an error response
            return new ResponseEntity<>("Error creating post: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
```

### Error 9
錯誤：
```
Table 'forum.posts_seq' doesn't exist
	at com.mysql.cj.jdbc.exceptions.SQLError.createSQLException(SQLError.java:120) ~[mysql-connector-j-8.0.32.jar:8.0.32]
	at com.mysql.cj.jdbc.exceptions.SQLExceptionsMapping.translateException(SQLExceptionsMapping.java:122) ~[mysql-connector-j-8.0.32.jar:8.0.32]
	at com.mysql.cj.jdbc.ClientPreparedStatement.executeInternal(ClientPreparedStatement.java:916) ~[mysql-connector-j-8.0.32.jar:8.0.32]
	at com.mysql.cj.jdbc.ClientPreparedStatement.execut
```
解法：
1. 利用 @GeneratedValue 自動生成id，strategy 屬性指主鍵生成方式。 IDENTITY 指數據庫將自動生成主鍵id的值。

```
@GeneratedValue(strategy = GenerationType.IDENTITY)
```
2. 更改數據庫中主鍵的設定為 主鍵自增：AUTO_INCREMENT PRIMARY KEY

```
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```
https://stackoverflow.com/questions/32968527/hibernate-sequence-doesnt-exist
### Error 10
```
com.fasterxml.jackson.databind.exc.InvalidDefinitionException: No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) (through reference chain: java.util.ArrayList[0]->com.forum.backend.entity.Post["user"]->com.forum.backend.entity.User$HibernateProxy$BfYO5Tgz["hibernateLazyInitializer"])
```
當Jackson嘗試序列化一個對象，但找不到 序列化器的屬性時，就會發生錯誤。
有錯誤的屬性是user，他是一個Hibernate proxy object。

此種錯誤的解決方法是將 @JsonIgnoreProperties 添加到Post class中的user 屬性，這個註釋告訴Jackson忽略Hibernate proxy object的hibernateLazyInitializer 屬性，因為他會導致序列化錯誤。
```
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private User user;
```
