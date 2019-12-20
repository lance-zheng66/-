## Mybatis 第二章

### 1.xml 文件的配置方式

`< mapper namespace = "com.itheima.mapper.userMapper>"`

`<!--select 查询-->`

​	`<select id = "findAll"`

​				`resultType="com.itheima.domain.User>"`（引号里面是返回值类型）

​				`select * from user;`  (这里是sql语句)

​				`</select>`

`</mapper>`

*******

#### 2.maybatis的CRUD操作

① 保存方法

* 先在接口中写方法 insert 方法
* 打开映射配置文件，在配置保存方法的相关信息

insert方法，记得添加parametertype=" ......"即为保存参数的类型

`<insert id ="saveUser" parameterType="com.itheima.domain.User"`

`insert into user (username,address,sex,birthday`)

`value(#{usrname},#{address},#{sex},#{birthday})`

`</insert>`

注:保存方法记得要提交事务

② 配置拆入操作后，获取插入的数据的id

```
<insert id="saveUser" parameterType="com.itheima.domain.User">   
<!--配置插入操作后，获取插入数据的id-->   
<selectKey keyProperty="id" keyColumn="id" resultType="int" order="AFTER">       
select last_insert_id();    
</selectKey>    
insert into user(username,address,sex,birthday)values (#{username},#{address},#{sex},#{birthday});
</insert>
```

注入：先插入数据后获取id

******

#### 3.myBatis 的参数深入

ParameterType(指定输入参数的java类型，可以填写java类的全限定类名或别名)：

**① 传递简单类型（int 、String)**

**② 传递pojo对象（也就是JavaBean或者说实体类对象）**

mybatis使 O G N L 表达式解析对象字段的值，#{ } 或者$ { } 括号中的值为POJO属性的名称

O G N L 表达式： Object Graphic Navgation Language 对象 图 导航 语言

* 它是通过对象的取值方法来获取数据在写法上，把get 给省略了

  比如：我们获取用户的名称

  类中的写法： 

  user.getUsername( );

  O G N L 表达式的写法：user.username?

* mybatis中为什么能直接写username,而不用user.?

​        如 update user set username = #{usrname}

因为在ParamemerType已经提供了属性所属的类，所以此时不需要写对象名

**③ 传递POJO包装对象**

开发中通过POJO传递查询条件，查询条件是综合的查询条件，包括用户的查询条件和其他的查询条件。

（比如将用户购买的商品信息也作为查询条件）这是可以使用包装对象传递参数

POJO类中包含POJO

*****

#### 4.Mybatis的输出结果封装

resultType（指定输出结果的java类型，可以填写别名或Java的全限定类名）：

① 输出简单对象（int String)

②输出 pojo 对象（实体类对象）（查询一个）

③输出 pojo列表（实体类列表 ） （查询所有）

使用查询封装时，要求实体类属性和数据库列表一致，不一致会报错。

注：mysql数据库不区分大小，linux系统除外。

******

#### 5.解决实体类属性和数据库列名不对应的两种方式：

① 起别名的方式：

在DAO.xml 中的SQl 语句中写：

例如： 

`select id as UserId; usrname as userName`

② 单独配置

`< resultMap id ="useMap"(名字随便起) type="com.itheima.domain.user"(对应实体类)>`

`<!--主键字段的对相应-->`

`<id property = "userid" (属性) column ="id"(列名)></id>`

`<!-- 非主键字段的对应-->`

`result property ="userName" column = "username"></result>`

......

`</resultMap>`

使用时，resultType类型，需要改成 resultMap

****

### 6.#{ }和 ${ }

**#{}:** 相当于预处理中的占位符 ？

① # {}里面的参数表示接收 Java输入参数的名称 

② # {}里面可以接收HashMap，POJO类型的参数。

③当接收简单简单类型的参数时，#{}里面可以value，也可以是其他的

④ #{}可以防止SQL注入

**${}:**相当于拼接SQl串，对传入的值，不做任何的解析的原样输入

①、${}会引入SQL注入，所以要谨慎使用

②、${}可以接收HashMap,POJO类型的参数

③、当接收简单的类型的参数，${}里面只能是value

### 







