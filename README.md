# spring-boot-starter-dao
该项目，集成spring-boot,mybatis,通用mapper,pagehelper,druid。致力于降低数据层的开发复杂度，让mybatis配置彻底告别xml，让基于mybatis的开发嵌入jpa的身影，极大降低mybatis的入门门槛，也极大的增加mybatis的可用性。让mybatis新手使用mybatis轻轻松松，让mybatis老手开发效率和编码质量更上一层楼。


### 本项目特点
##### 1. 支持读写分离；
##### 2. 支持多数据源;
##### 3. 支持注解方式指定枚举与数据库存储值的对应；
##### 4. 基于通用mapper快速实现单表的增删查改；
##### 5. 基于pagehelper便捷易用的数据库物理分页实现；
##### 6. 基于druid的服务器监控。 
##### 7. 可以根据数据库表以及表中字段注释，生成带完整注释的model，生成基于通用mapper的dao层

### [示例程序](https://gitee.com/lei0719/spring-boot-starter-dao-example)

#### 1.克隆示例程序，导入eclipse
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
在项目更目录执行 `mvn spring-boot:run `