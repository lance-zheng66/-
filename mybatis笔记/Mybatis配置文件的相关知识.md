### Mybatis配置文件的相关知识

*****

### mybatis Dao的编写

流程：

① 读取配置文件呢；

② 创建 SqlSessionFactory 工厂；

③ 创建 SqlSession对象；

④ 创建Dao接口的代理对象；

⑤ 执行Dao 中的方法；

⑥ 释放资源

注：A.创建工厂 mybatis 使用了构建者模式；

​		B.Dao接口只有方法名和参数，并没有具体功能的实现，所以必须通过SqlSession创建Dao接口的代理对象，

再使用代理对象执行的方法，从而实现功能。

​		c. 也可以创建接口的实体类后，根据实现类创建Dao的对象



*****

配置文件里有mybatis的环境配置信息和映射文件加载地址

映射文件有具体MySql语句的操作方法；

* **读取配置文本的方法:**

  a.使用类加载器。他们只能读取类路径的的配置文件;

  b.使用SerletContex对象的getRealPath( );

* 映射文件名与接口名保持一致

****

### mybatis 的Dao编写【mapper代理方式实现】

Mapper代理的开发方式，程序员只需要编写mapper接口（相当于Dao接口）即可。

Mybatis会自动的为mapper接口生成动态代理实现类，要实现mapper代理的开发方式，需要遵守一些开发规范：

**一、开发规范**

A. mapper接口的全限定名要和mapper映射文件的namespace的值相同；

B .mapper接口的方法名称要和mapper映射文件中的statement的id相同；

C .mapper接口的方法参数只能有一个，且类型要和映射文件的statement的parameterType的值保持一致；

D .mapper接口的返回值类型要和mapper映射文件中statement的rusultType值或resultMap中的值保持一致。

**通过规范式的开发mapper接口，可以解决原始Dao开发中存在的问题：**

a.模板代码已经去掉了

b.剩下去掉的操作数据库的代码，其实就一行代码。这行代码中硬编码的部分,通过A和B两个规范就可以解决。

注：返回的是泛型用Object接收。

****

### **Mybatis的全配置文件的propertis和alias的配置**

① 将数据库的属性文件写在src下或Resources下

如：db.properties

② 在 configuration下添加 '文件名'

`properties resource ='db.properties'`

③ 在dataSource下

使用${},可以引用已经加载的Java配置文件的信息

`<property name ="driver" value="${driver.class}">`

**TypeAliases**

别名是使用时为了映射文件中，更方便的去指定入参和结果集的类型，不用再写很长的一段全限定名

**mybatis支持的别名**

|    别名    |  映射类型  |
| :--------: | :--------: |
|   -byte    |    byte    |
|   -long    |    long    |
|   -short   |   short    |
|    -int    |    int     |
|  -integer  |    int     |
|  -double   |   double   |
|   -float   |   float    |
|  -boolean  |  boolean   |
|   string   |   String   |
|    byte    |    Byte    |
|    long    |    Long    |
|   short    |   Short    |
|    int     |  Integer   |
|   double   |   Double   |
|   float    |   Float    |
|  boolean   |  Boolean   |
|  decimal   | BigDecimal |
| bigdecimal | BigDecimal |

配置别名在全局配置文件下：

① 在configuration下生成typeAliases

eg:

`<typeliases type ="com.gyf.model.user"(全限定类名) alias ="user"(别名)>`

② 第二种方法：生成 TypeAliases

`<指定包名，别名就是类名，第一字母小写>`

eg:

`<package name ="com.gfy.medel"></package>`

别名可以直接写映射文件里，别名就是类名，第一个字母小写

eg :User-user

******

### Mapper 加载映射文件的几种方式

①<Mapper resource=''/>

使用相对于类路径的资源 “ 包名/文件名“

②<Mapper url =''/>

使用完全限定类名，从盘符开始的全路径

③<Mapper class =''/>

使用Mapper接口的全限定类名

注：映射文件名与类名（接口名）必须一致

④ 如果没xml映射文件，必须在Mapper接口中声明注解

方法：直接sql语句，放到对应的方法中

eg:

@Select("select*From user where id #{id}")

public User <u>findUserById</u>(int id)

⑤ 直接写Mapper接口所有的包名

<package name ="com.gfy.mapper"></package>

*****

### mybatis的映射文件

一、输入映射 ParameterType

指定输入参数的Java类型的，可以使用别名或者类的全限定类名，他可以接收简单类型，POJO，HashMap

① 传递简单类型 eg:PatameterType = "int"

② 传递POJO对象 eg:" PatameterType = "user"

③ 传递POJO包装对象

开发中通过POjO传递查询对象查询条件，查询条件是综合的查询条件，不仅包括用户查询条件还包括其他的查询条件（比如将用户购买商品信息也作为查询条件），这时可以使用包装对象传递输入参数。

需求：综合查询用户信息，需求传入查询条件复杂（比如,用户信息、订单信息、商品信息）

④ 定义POJO包装类

a.新建一个包query 或VO

b.新建一个类

c.在Mapper接口中定义方法，并将包装类作为参数传进去

d.在映射文件中，添加对应方法的SQL语句，并将包装类传入 ParameterType 中。

*****

二、ParameterType 传递Map对象

① 接口中定义一个方法，以Map为参数

② 在映射文件中定义SQL语句,并将ParameterType ="HashMap"

****

三、输出映射：resultType/resultMap

* resultType (设置返回值类型)

  使用resultType进行结果映射时，查询的列名和映射的POJO属性完全一致，该列才能才能映射成功

  如果查询的列名和和映射的POJO属性全部都不一致，则不会创建POJO对象

  如果查询的列名和映射的POJO属性有一个一致，就会创建POJO对象

* 输出简单类型

  当输出结果只有一列时，可以使用ResultType指定简单类型作为输出结果类型。



