## Mybatis第一章

#### 一、相关知识

* 什么是框架？

  它是我们软件开发过程中的一套解决方案，不同的框架解决不同的问题

* 使用框架的好处：

  框架封装了很多细节，使开发者可以使用极简的方式实现功能，大大提高开发效率

* 三层架构：

  表现层：是用于展示数据的

  业务层:是处理业务的

  持久层：是和数据库交互的

* 持久层技术解决方案:

  Jdbc技术：

  Connection----->数据库连接对象；

  Prepared Statement ---->执行sql的对象；

  ResultSet---->结果集对象。

  

  Spring的 **JdbcTemplate**----->Spring中对于Jdbc的简单封装；

  ​	 

  Apache的 **DBUtils**------>它和Spring和jdbcTemplate很像，也是Jdbc的简单封装

​	

注：JDBC是规范，而Spring的 JdbcTemplate 和Apache 的DBUtils 都只是工具类



#### 二、mybatis 的概述

**概述：**

Mybatis是一个用java语言编写的**持久层框架**，里面封装了很多jdbc 操作的很多细节，使开发者只需要关注sql语句本身，而无需要关注注册驱动，创建连接等繁杂过程。它使用了ORM思想实现了结果集的封装。



**ORM思想：**

Object Relactional Mapping 即对象关系映射

* 含义：就是把数据库表和实体类及实体类的属性对应起来，让我们可以操作实体类就实现操作数据库表

eg:

| 数据库表字段名 | 实体类属性 |
| -------------- | ---------- |
| user           | User       |
| id             | UserId     |
| user_name      | UserName   |

* 要求：实体类的属性名和数据库的字段名称保持一致

#### 三、Mybatis的入门

* **mybatis的环境搭建：**

  ① 创建 maven 工程并到入坐标

  *② **创建实体类和Dao接口***

  ③ 创建mybatis的主配置文件：SqlMapconfig.xml

  ④ 创建映射配置文件：IUserDao.xml（该xml文件是针对第二步中的dao接口，名字可以自定义）

* **mybatis的环境搭建的注意事项：**

  ① 创建Dao.xml和Dao.java

  在mybatis中它把持久层的操作接口名称和映射文件也叫Mapper

  所以  IUserDao   也叫  IUserMapper

  ② 在IDEA中创建目录,它和包是不一样的。（要一级一级创建）

  eg:

  包在创建时：com.itheima.dao 它是三级结构

  目录在创建时：com.itheima.dao.它是一级目录

  ③ mybatis的映射配置文件位置必须和dao接口的目录结构；

  ④ 映射配置文件的Mapper标签中 namespace的属性取值必须是 Dao接口的全限定类名；

  ⑤映射配置文件的操作配置（select），id属性的取值必须是Dao接口的方法名。

  注：当我们遵守了第三、四、五点之后，我们在开发中就无须再写dao的实现类

#### 四、Mybatis的入门案例

* 具体步骤：

①：读取配置文件

②： 创建 SqlSession Factory 工厂

③：创建SqlSession

④：创建Dao接口的代理对象

⑤：执行Dao接口中的方法

⑥：释放资源。

* 注意事项：

  不要忘记再映射配置里告知mybatis要封装到哪个实体类中

  resulType="..........",引号里配置的方式是 指定实体类的全限定类名

eg:  查询所有方法

```
public static void main(String[] args) {

//1.读取文件
InputStream in = Resource.getResourceAsStream("SqlMapConfig.xml");

//2.创建SQlSessionFactory 工厂
SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
SqlSessionFactory  factory = builder.build(in);

//3.使用工厂生产SqlSession对象
SqlSession session = factory.openSession

//4.使用SqlSession创建Dao接口的代理对象
IUserDao userDao = session.getMapper(IUserDao.class);

//5.使用代理对象执行方法
List<User> users = userDao.findAll();
for(User user : users){
	System.out.println:(user);
}

//6.释放资源
session.close();
in.close;

}
```

**以上案例注意事项如下**：

* a.读取文件时注意：

  绝对路径：d://xxx/xxx/xxx.xml  (电脑有可能没有D盘)；

  相对路径：src/Java/main/xxx.xml(web工程下，src目录就没有了)；

  实际开发过程中，以上两个路径方式都不不用。

  b.使用以下两个方式：

  ① 使用类加载器，它只能读取类路径的配置文件；

  ② 使用ServletContext对象的getRealPath(),可以得到当前应用部署的绝对路径，项目运行在哪里，路径就在哪里。

* 创建工场 ，mybatis 使用了构建者模式SqlSessionFactory

  我们只需要提供读取配置文件的对象 in

  builder.build(in);

  优势：把对象的创建细节隐藏，使用者直接调用方法即可拿到对象

* 使用工厂生产 SqlSession对象

  好处：解耦（降低类之间的依赖关系）

* 创建Dao接口实现类使用了代理模式：

  优势 : 不修改源码的基础上对已有方法增强，通过创建Dao实现类的代理对象，实现了不写Dao实现类也可以实现功能。























