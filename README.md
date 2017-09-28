# spring-boot-starter-dao
该项目，集成spring-boot,mybatis,通用mapper,pagehelper,druid。致力于降低数据层的开发复杂度，让mybatis配置彻底告别xml，让基于mybatis的开发嵌入jpa的身影，极大降低mybatis的入门门槛，也极大的增加mybatis的可用性。让mybatis新手使用mybatis轻轻松松，让mybatis老手开发效率和编码质量更上一层楼。


### 本项目特点
###### 1. 配置及其简单,非常容易上手；
###### 2. 支持读写分离；
###### 3. 支持多数据源;
###### 4. 支持注解方式指定枚举与数据库存储值的对应；
###### 5. 基于通用mapper快速实现单表的增删查改；
###### 6. 基于pagehelper便捷易用的数据库物理分页实现；
###### 7. 基于druid的服务器监控。 
###### 8. 可以根据数据库表以及表中字段注释，生成带完整注释的model，生成基于通用mapper的dao层

### 快速入门
快速入门[点击这里](http://blog.csdn.net/hulei19900322/article/details/78118965)

### 更新记录
```log
1.0.1
发布时间： 2017年9月27日
更新内容：
	1.增加mybatis-generator依赖，实现项目内快速生成dao层

1.0.0
发布时间： 2017年9月24日
更新内容：
	1. 支持读写分离；
	2. 支持多数据源;
	3. 基于druid的服务器监控。 
	4. 支持注解方式指定枚举与数据库存储值的对应；
	5. 基于通用mapper快速实现单表的增删查改；
	6. 基于pagehelper便捷易用的数据库物理分页实现；

```


### 示例程序

#### 1.克隆[示例程序](https://gitee.com/lei0719/spring-boot-starter-dao-example)，导入eclipse
```cmd
git clone https://gitee.com/lei0719/spring-boot-starter-dao-example.git
``` 
#### 2.生成dao，model，mapper代码
修改`generator.properties`中参数，使jdbc参数指向你的数据库。修改表参数，把计划生成的表改为你的数据库中存在。
以Application方式启动项目，选择包含main方法的`com.reger.mybatis.generator.GeneratorMain`，运行。
稍等，如果没有出现异常，基本就生成好了你数据表的数据层。

#### 3.运行示例项目
运行示例项目，你首先需要导入数据文件`test.sql`。
然后修改'application.yml'中db链接的配置参数，如果没有从库，可以先把从库的配置注释掉
在项目跟目录执行 `mvn spring-boot:run `

### 更多配置参数
```yml
spring:
  druid:
    stat:
      enable: true                        # 启用druid监控控制台
      allow: ''                           # IP白名单 (没有配置或者为空，则允许所有访问)
      deny: ''                            # IP黑名单 (存在共同时，deny优先于allow)
      loginUsername: 'leige'              # 用户名
      loginPassword: 'leige'              # 密码
      exclusions: '*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*' # WebStatFilter忽略资源
      resetEnable: false                  #禁用HTML页面上的“Reset All”功能
      filter-url-patterns: '/*'
      
    default:                              # druid连接池默认参数，具体主库或者从库的配置会继承该配置
      enable: true
      initial-size: 5                     # 链接池初始化大小
      max-active: 500                     # 链接池中最大的链接数
      min-idle: 5                         # 链接池中最少要保留的链接数
      max-wait: 3000                      # 链接池中最大等待的链接数
      filters: wall,stat                  # 启用过滤器
      username: reger                     # 数据库登陆名
      password: reger_123                 # 数据库登陆密码
      default-auto-commit: true           # 是否自动提交事务，如果设置为false，代码中不启用事务数据将没法持久化
      access-to-underlying-connection-allowed: 
      async-close-connection-enable: 
      break-after-acquire-failure: 
      clear-filters-enable: 
      connection-error-retry-attempts: 
      default-read-only: 
      default-transaction-isolation: 
      driver-class-name: 
      dup-close-log-enable: 
      fail-fast: 
      log-abandoned: 
      log-different-thread: 
      login-timeout: 
      max-create-task-count: 
      max-evictable-idle-time-millis: 
      max-open-prepared-statements: 
      max-pool-prepared-statement-per-connection-size: 
      min-evictable-idle-time-millis: 
      not-full-timeout-retry-count: 
      pool-prepared-statements: 
      query-timeout: 
      remove-abandoned: 
      remove-abandoned-timeout-millis: 
      test-on-borrow: 
      test-on-return: 
      test-while-idle: false
      time-between-connect-error-millis: 
      time-between-eviction-runs-millis: 
      time-between-log-stats-millis: 
      url: 
      validation-query: 
      validation-query-timeout: 
      connect-properties:
        
       
  mybatis:
    configuration:                        # mybatis具体的配置参数,所有的mybatis实例都会使用该配置
      cache-enabled: true                 # 是否启用缓存
      local-cache-scope: session          # 缓存范围
      lazy-loading-enabled: true          # 是否启用懒加载
      default-executor-type: simple       # sql执行模式
      map-underscore-to-camel-case: true  # 开启驼峰转换
      aggressive-lazy-loading: 
      auto-mapping-behavior: 
      call-setters-on-nulls: 
      default-fetch-size: 
      default-statement-timeout: 
      log-prefix: 
      use-generated-keys: 
      log-impl: 
      multiple-result-sets-enabled: 
      return-instance-for-empty-row: 
      use-column-label: 
      safe-result-handler-enabled: 
      safe-row-bounds-enabled: 
      use-actual-param-name: 
      
    nodes:
      user:                                                       # 该数据点的名字
        order: after                                              # mybatis生成主键的顺序，如果要在insert into 数据前获得主键，配置为 BEFORE，否则配置为 AFTER 默认值为 BEFORE
        mapper: MYSQL                                             # mybatis通用mapper使用的mapper接口集合,mysqldb时使用使用 MYSQL,SqlServer 时使用MSSQL,其它db类型时使用DEFAULT， 默认值是 DEFAULT
        primary: false                                            # 是否是默认的数据对象， 最多只有一个为true，如果配置多个只有第一个生效
        base-package: com.reger.test.user.dao                     # mybatis mapper接口文件的配置位置，该值不可以为空，可以用正则匹配，多个用逗号分隔
        mapper-package: com.reger.test.user.mapper                # mybatis mapper 的xml配置文件的位置 ，该值最好不要为空，可以用正则匹配，多个用逗号分隔
        type-aliases-package: 'com.reger.test.user.model'         # model类所在的包，该值可以为空，可以用正则匹配，多个用逗号分隔
        master:                                                   # 主库配置，不可为空，参数默认继承spring.druid.default，可以写参数覆盖默认配置
          url: jdbc:mysql://127.0.0.1:3306/user?useSSL=true       # 主库的jdbc地址
        slaves:                                                   # 从库配置，可以为空，可以配置多个，参数默认继承spring.druid.default，可以写参数覆盖默认配置
          - url: jdbc:mysql://127.0.0.1:3307/user?useSSL=true
          - url: jdbc:mysql://127.0.0.1:3308/user?useSSL=true
          - url: jdbc:mysql://127.0.0.1:3309/user?useSSL=true

      base:
        order: after
        mapper: MYSQL
        primary: true
        base-package: com.reger.test.base.dao
        mapper-package: com.reger.test.base.mapper
        type-aliases-package: 'com.reger.test.base.model'
        master:
          url: jdbc:mysql://127.0.0.1:3306/base?useSSL=true
        slaves:
          - url: jdbc:mysql://127.0.0.1:3307/base?useSSL=true
          - url: jdbc:mysql://127.0.0.1:3308/base?useSSL=true
          - url: jdbc:mysql://127.0.0.1:3309/base?useSSL=true

```