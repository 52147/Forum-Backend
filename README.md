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
## API
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
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'postController': Unsatisfied dependency expressed through field 'postRepository': Error creating bean with name 'postRepository' defined in com.forum.backend.repository.PostRepository defined in @EnableJpaRepositories declared on JpaRepositoriesRegistrar.EnableJpaRepositoriesConfiguration: Could not create query for public abstract java.util.List com.forum.backend.repository.PostRepository.findAllByOrderByCreateTimeDesc(); Reason: Failed to create query for method public abstract java.util.List com.forum.backend.repository.PostRepository.findAllByOrderByCreateTimeDesc(); No property 'createTime' found for type 'Post'
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
出現此錯誤是因為 Spring Boot 使用的 Jackson 序列化程序無法序列化為延遲加載創建的 Hibernate 代理對象。  	
要解決此問題，可以使用 @JsonIgnoreProperties 註釋，將 Jackson 序列化程序配置為忽略這些屬性。  	


使用 @JsonIgnoreProperties 註釋加在user屬性，會告訴 Jackson 在序列化 Post 實體時忽略 User 實體的 hibernateLazyInitializer 屬性。
```
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer"})
    private User user;
```
## Error 11
```
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'postController': Unsatisfied dependency expressed through field 'postService': Error creating bean with name 'postServiceImpl': Unsatisfied dependency expressed through field 'postMapper': Error creating bean with name 'postMapper' defined in file [/Users/shou-tzuhan/Downloads/backend/target/classes/com/forum/backend/mapper/PostMapper.class]: Unsatisfied dependency expressed through bean property 'sqlSessionFactory': Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/forum/backend/config/MyBatisConfig.class]: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: org/springframework/core/NestedIOException
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:713) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:693) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:133) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:482) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1416) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:597) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:520) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:973) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:917) ~[spring-context-6.0.7.jar:6.0.7]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:584) ~[spring-context-6.0.7.jar:6.0.7]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146) ~[spring-boot-3.0.5.jar:3.0.5]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:732) ~[spring-boot-3.0.5.jar:3.0.5]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:434) ~[spring-boot-3.0.5.jar:3.0.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:310) ~[spring-boot-3.0.5.jar:3.0.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1304) ~[spring-boot-3.0.5.jar:3.0.5]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1293) ~[spring-boot-3.0.5.jar:3.0.5]
	at com.forum.backend.BackendApplication.main(BackendApplication.java:11) ~[classes/:na]
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'postServiceImpl': Unsatisfied dependency expressed through field 'postMapper': Error creating bean with name 'postMapper' defined in file [/Users/shou-tzuhan/Downloads/backend/target/classes/com/forum/backend/mapper/PostMapper.class]: Unsatisfied dependency expressed through bean property 'sqlSessionFactory': Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/forum/backend/config/MyBatisConfig.class]: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: org/springframework/core/NestedIOException
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:713) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:693) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:133) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessProperties(AutowiredAnnotationBeanPostProcessor.java:482) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1416) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:597) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:520) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1417) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1337) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:710) ~[spring-beans-6.0.7.jar:6.0.7]
	... 20 common frames omitted
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'postMapper' defined in file [/Users/shou-tzuhan/Downloads/backend/target/classes/com/forum/backend/mapper/PostMapper.class]: Unsatisfied dependency expressed through bean property 'sqlSessionFactory': Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/forum/backend/config/MyBatisConfig.class]: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: org/springframework/core/NestedIOException
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByType(AbstractAutowireCapableBeanFactory.java:1513) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1407) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:597) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:520) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1417) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1337) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.resolveFieldValue(AutowiredAnnotationBeanPostProcessor.java:710) ~[spring-beans-6.0.7.jar:6.0.7]
	... 34 common frames omitted
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/forum/backend/config/MyBatisConfig.class]: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: org/springframework/core/NestedIOException
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:657) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:491) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1332) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1162) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:560) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:520) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:326) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:324) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:200) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:254) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1417) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1337) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.autowireByType(AbstractAutowireCapableBeanFactory.java:1498) ~[spring-beans-6.0.7.jar:6.0.7]
	... 45 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.apache.ibatis.session.SqlSessionFactory]: Factory method 'sqlSessionFactory' threw exception with message: org/springframework/core/NestedIOException
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:171) ~[spring-beans-6.0.7.jar:6.0.7]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:653) ~[spring-beans-6.0.7.jar:6.0.7]
	... 58 common frames omitted
Caused by: java.lang.NoClassDefFoundError: org/springframework/core/NestedIOException
	at com.forum.backend.config.MyBatisConfig.sqlSessionFactory(MyBatisConfig.java:22) ~[classes/:na]
	at com.forum.backend.config.MyBatisConfig$$SpringCGLIB$$0.CGLIB$sqlSessionFactory$0(<generated>) ~[classes/:na]
	at com.forum.backend.config.MyBatisConfig$$SpringCGLIB$$2.invoke(<generated>) ~[classes/:na]
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:258) ~[spring-core-6.0.7.jar:6.0.7]
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:331) ~[spring-context-6.0.7.jar:6.0.7]
	at com.forum.backend.config.MyBatisConfig$$SpringCGLIB$$0.sqlSessionFactory(<generated>) ~[classes/:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:na]
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77) ~[na:na]
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:na]
	at java.base/java.lang.reflect.Method.invoke(Method.java:568) ~[na:na]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:139) ~[spring-beans-6.0.7.jar:6.0.7]
	... 59 common frames omitted
Caused by: java.lang.ClassNotFoundException: org.springframework.core.NestedIOException
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641) ~[na:na]
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188) ~[na:na]
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:520) ~[na:na]
	... 70 common frames omitted


Process finished with exit code 1
```
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'postController':  	

這個錯誤原因不知道什麼，所有controller, service, service implementation, mapper映射關係我都確認了沒有缺少，sqlSessionFactory 的 path 也沒有錯，不懂。  	

https://www.cnblogs.com/zhengzhaoxiang/p/13975791.html. 	

https://stackoverflow.com/questions/45773769/mybatis-spring-boot-starter-giving-error-when-maven-package. 	

https://www.baeldung.com/spring-unsatisfied-dependency. 	

https://blog.csdn.net/RkieCoder57/article/details/119485965. 	



