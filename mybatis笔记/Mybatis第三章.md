## Mybatis 第三章节

#### 一、使用mybatis完成Dao层的开发

准备工作： 

使用mybatis的相关配置文件

Dao接口：里面定义了各种操作方法

#### 二、定义一个DAO接口实现类 类名（xxxDaoImpl）

1.定义一个类实现Dao接口并重写Dao里面的方法【shift+alt+enter】

2.定义一个 SqlSessionFactor  factory,并为了保证 factory 一定有值，通过构造函数给factory赋值

`private  SqlSessionFactory  factory;`

`public  xxxDaoImpl(SqlSessionFactory factory){`

`this.factory=factry`；`}`



3.具体到每个操作方法

eg:

1.根据factory获取SqlSession对象

2.调用SqlSession中的方法，实现查询列表

***注：参数就是能获取配置信息的key,即配置文件中nameSpace名称加上方法名称***

3.释放资源

`public List<User> finAll(){`

`SqlSession session = factory.openSesion`;

`List<User> users = session.selectList(statement:"com...XXXDao.findAll");`

`session.close();`

`return users;`

`}`

#### 三、测试类

在@Before注解类中

需要使用工厂对象创建Dao对象

在@after中

不再需要提交事务commit命令以及释放资源命令close。

*****

#### 使用代理DAO的方式来实现mybatis的CRUD操作与编写DAO的实现类的方式来实现mybatis的CRUD操作的区别于相似之处：

首先看以下代码：

①

`@override(查询所有的方法)`

`public List<User> findAll(){`

`session.selectList(".........")`

`}`

②











