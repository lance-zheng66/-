# JDBC 

#### JDBC的概念

- 概念：Java  database Connectivity 即Java语言操作数据库
- 期望使用一套统一的代码可以操作所有的关系型数据库。

本质：其实是官方（SUN公司）定义的一套操作操作所有数据库的规则。（接口）。各个数据库厂商去实现这个接口。提供数据库Jar包，我们可以使用(JDBC)接口（JDBC）编程，真正执行的代码是驱动 JAR 包。我们可以使用（JDBC）接口(JDBC)编程。真正执行的代码是驱动 Jar 包的实现类

例如：Person 接口  worker类

Person P = new  worker( );

父类引用指向子类对象

P.eat( ); 多态

------

### 快速入门

步骤：① 导入驱动 Jar包（maven 工程下导入）

​			② 注册驱动

`class.forname("com.mysql.jdbc.Driver")`

​			③ 获取数据库连接对象（Connection）

`Connection conn =DriverManager.getConnection（"url,user,password"）`

​			④ 定义Sql语句

`String sql = "sql语句"`

​			⑤获取执行Sql语句的对象 Statement

Statement stmt = conn.creatStatement( );