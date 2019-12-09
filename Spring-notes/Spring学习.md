##  Spring学习 第一章

#### 一、Spring的两大核心特性

* Spring实现功能依赖于两大核心特性：

​        **依赖注入（dependency injection,DI)**

​        **面向切面编程（aspect -oriented programming,AOP)**

#### 二、使用Spring开发的目的及方法

* 目的：

  Spring是一个开源框架，使用Spring开发的目的是降低Java开发的复杂性，简化Java开发

* Spring 简化开发的4大关键策略：

  * **基于POJO的轻量级和最小浸入性编程；**
  * **通过依赖注入和面向接口实现松耦合；**
  * **基于切面和惯例进行声明式耦合；**
  * **通过切面和模板减少样式代码。**

#### 三、工作特性

很多框架通过强迫应用继承它们的类或实现它们的接口从而导致应用与框架绑死。

而Spring竭力避免因自身的API而弄乱你的应用代码。**Spring不会强迫你实现Spring规范的接口或继承Spring规范的类**，相反，在基于Spring构建的应用中，它的类通常没有任何痕迹表明你使用了Spring。

**Spring赋予POJO高效简单的方式之一，就是通过DI来装配它们。DI可以帮以帮助应用对象之间保持松散耦合。**

在项目中应用 DI，你会发现你的 代码会变得异常简单并且更容易理解和测试。

#### 四、依赖注入（DI）

* 前提：任何一个有实际意义的应用（肯定比 Hello World 示例更复杂）都会由两个或者更 多的类组成，这些类相互之间进行协作来完成特定的业务逻辑。按照传统的做法，每个 对象负责管理与自己相互协作的对象（即它所依赖的对象）的引用，这将会导致高度耦 合和难以测试的代码。

  耦合具有两面性（two-headed beast）。**一 方面，紧密耦合的代码难以测试、难以复用、 难以理解，并且典型地表现出“打地鼠”式的 bug特性** （修复一个 bug，将会出现一个或者更多新的 bug）。**另 一方面，一定程度的耦合又是必须的—完全没有耦合 的代码什么也做不了**。为了完成有实际意义的功能，不 同的类必须以适当的方式进行交互。总而言之，耦合是 必须的，但应当被小心谨慎地管理。

* 目的：避免高度耦合，或者说是松耦合

* DI功能是如何实现的：

  通过 DI，对象的依赖关系将由系统中负责协调各 对象的第三方组件在创建对象的时候进行设定。**对象无需自行创建或管理它们的依赖关系**

  依赖注入的思想是这样,当一个类A对另一个类B有依赖时,不在该类A内部对依赖的类B进行实例化,而是之前配置一个beans.xml,在配置xml文件中配置好A与B的依赖关系时，告诉容器所依赖的类B,在实例化该类A时,容器自动注入一个所依赖的类B的实例.

  更通俗点就是

  **A需要完成某个动作，需要借助在A的内部创建B的实例化对象 ，但是这样做耦合度太高，所以通过配置xml文件(里面有A与B的依赖关系)告诉一个负责协调各个对象的第三方组件（容器），容器自动注入B的实例化对象，（应用了多态）**

* 装配:创建应用组件之间协作的行为通常称为装配（wiring）。 Spring 有多种装配 bean 的 方式，采用 XML 是很常见的一种装配方式。

* 只 有 Spring 通过它（xml）的配置，能够了解这些组成部分是如何装配起来的。这样的话，就可以 在不改变所依赖的类的情况下，修改依赖关系。 


#### 五、面向切面编程 AOP

* 面向切程编程（aspect-oriented programming,AOP)  允许你把遍布应用各处的功能分离出来形成可重用的组件。

* 面向切面编程往往被定义成促使软件系统实现关注点分离的一项技术。系统由许多不同的组件组成，每一个组件负责一块特定功能。除了实现自身核心的功能之外，这些组件还经常承担着额外的职责。诸如日志，事务管理管理和安全这样的系统服务经常融入到其他自身具有核心功能的组件当中去，这些**系统服务通常被称为横切关注点，因为他们跨越系统的多个组件。**

* 如果将这些关注点分散到多个组件中去，代码就会带来双重复杂性：

  * 实现系统关注点功能的代码将会重复出现在多个组件中。这意味着如果你要改变这些关注点的逻辑，必须修改各个模块中的相关实现。即使你把这些关注点抽象为一个独立的模块，其他模块只是调用它的方法，但方法的调用还是会重 复出现在各个模块中。 
  * 组件会因为那些与自身核心业务无关的代码而变得混乱。一个向地址簿增加地 址条目的方法应该只关注如何添加地址，而不应该关注它是不是安全的或者是 否需要支持事务。 

  注: 一个组件应该关注的是自己的核心功能，不需要与关注点产生过多的交集。

* **AOP是如何处理改变关注点分散到多个组件的问题？**

  AOP能够将这些服务模块化，并以声明的方法式将它们应用到它们需要影响影响的组件中去。所造成的结果就是这些组件会具有更高的内聚性并且会更加关注自身的业务，完全不需要了解系统服务所带来得复杂性。总之AOP能够确保POJO的简单性

  我们可以**把切面想象为覆盖在组件之上的一个外壳**，应用是由那么实现各自业务功能的模块组成的。借助AOP，可以使用各种功能层去包裹核心业务。这些层以声明的方式灵活的应用到系统中，你的核心应用甚至不知道他们的存在。**这是一个非常强大的理念，可以将安全、事务、日志关注点与核心业务逻辑相分离**

**前提：**

这些系统服务声明为一个Bean

方式：

通过Spring配置文件把相关bean声明为一个切面，并定义切点,以及声明前置通知（切点前调用切面的方法）和后置通知（切点后调用切面的方法）。

注：为SpringBean注入依赖也可以应用到Spring切面中

#### 六、使用模板消除样板式代码

样板式的代码（boilerplate code）。通常为了实现通用的和简单的任 务，你不得不一遍遍地重复编写这样的代码。 遗憾的是，它们中的很多是因为使用 Java API 而导致的样板式代码。样板式代码的 一个常见范例是使用 JDBC 访问数据库查询数据。

JDBC 不是产生样板式代码的唯一场景。在许多编程场景中往往都会导致类似的样 板式代码，JMS、JNDI 和使用 REST 服务通常也涉及大量的重复代码。 Spring 旨在通过模板封装来消除样板式代码。Spring 的 JdbcTemplate 使得执行数据 库操作时，避免传统的 JDBC 样板代码成为了可能

#### 七、容器

* 在基于 Spring 的应用中，你的应用对象生存于 Spring 容器（container）中。Spring 容器负责创建对象，装配它们，配置它们并管理它们的整个生命周期，从生存到死亡在这里。

注： 在Spring应用中，对象由Spring 容器创建和装配，并存在容器之中 

*  容器是 Spring 框架的核心。Spring 容器使用 DI 管理构成应用的组件，它会创建 相互协作的组件之间的关联。

* Spring 容器并不是只有一个。Spring 自带 了多个容器实现，

  Spring可以归为两种不同的类型:

  ①、**bean 工厂**（由 org.springframework. beans.factory.BeanFactory 接口定义）是最简单的容器，提供基本的 DI 支持。

  ②、**应用上下文**（由org.springframework.context.ApplicationContext接口定义） 基于 BeanFactory构建，并提供应用框架级别的服务，例如从属性文件解析文本信息以及发布 应用事件给感兴趣的事件监听者。

* **应用上下文：**

  Spring 自带了多种类型的应用上下文。以下几个是比较常见的：

  **①**

  *  AnnotationConfigWebApplicationContext：从一个或多个基于 Java 的配置类中加载 Spring Web 应用上下文。
  * XmlWebApplicationContext：从 Web 应用下的一个或多个 XML 配置文件 中加载上下文定义 

  **注：以上两个应用上下文是基于web的spring应用**

  **②**

  * ClassPathXmlApplicationContext：从类路径下的一个或多个 XML 配 置文件中加载上下文定义，把应用上下文的定义文件作为类资源
  * FileSystemXmlApplicationContext：从文件系统下的一个或多个 XML 配置文件中加载上下文定义。

  注： **FileSystemXmlApplicationContext**在**指定的** 文件系统路径下查找 knight.xml 文件

  **ClassPathXmlApplicationContext** 是 在**所有的**类路径（包含 JAR 文件）下查找 knight.xml 文件。 

  **③**

  * AnnotationConfigApplicationContext：从一个或多个基于 Java 的配 置类中加载 Spring 应用上下文。

  注：在这里没有指定加载 Spring 应用上下文所需的 XML 文件，AnnotationConfig- ApplicationContext通过一个配置类加载 bean

  应用上下文准备就绪之后，我们就可以调用上下文的**getBean()**方法从 Spring 容 器中获取 bean。

*  **bean的生命周期**：

  图示：![bean的整个流程图](C:\Users\lance\Desktop\bean的整个流程图.PNG)

详细描述：

1．Spring 对 bean 进行实例化；

 2．Spring 将值和 bean 的引用注入到 bean 对应的属性中；

 3．如果 bean 实现了BeanNameAware接口，Spring 将bean 的ID 传递给setBean- Name()方法； 

4．如果 bean实现了BeanFactoryAware接口，Spring将调用setBeanFactory() 方法，将 BeanFactory 容器实例传入； 

5．如果 bean 实现了 ApplicationContextAware 接口，Spring 将调用 setApplicationContext()方法，将 bean 所在的应用上下文的引用传入进来； 

6．如果 bean 实现了 BeanPostProcessor 接口，Spring 将调用它们的 post- ProcessBeforeInitialization()方法； 

7．如果 bean 实现了 InitializingBean 接口，Spring 将调用它们的 after- PropertiesSet()方法。类似地，如果 bean 使用init-method声明了初始化方法， 该方法也会被调用；

8．如果 bean 实现了 BeanPostProcessor 接口，Spring 将调用它们的 post- ProcessAfterInitialization()方法； 

9．此时，bean 已经准备就绪，可以被应用程序使用了，它们将一直驻留在应用上 下文中，直到该应用上下文被销毁；

 10．如果 bean 实现了DisposableBean接口，Spring 将调用它的destroy()接口 方法。同样，如果 bean 使用destroy-method声明了销毁方法，该方法也会被调用。 

#### 七、Spring生态介绍

 

* Spring 框架的发布版本包括了 20 个不 同的模块，每个模块会有 3个JAR 文件（二进 制类库、源码的 JAR 文件以及 JavaDoc 的JAR 文件）

* 这些模块依据其所属的功能可以划分为 6 类不同的功能；

* **六大模块：**

  **① 核心容器:**

  容器是 Spring 框架最核心的部分，它管理着 Spring 应用中 bean 的创建、配置和管 理。在该模块中，包括了 Spring bean 工厂，它为 Spring 提供了 DI 的功能。基于 bean 工厂，我们还会发现有多种 Spring 应用上下文的实现，每一种都提供了配置 Spring 的 不同方式。 除了 bean 工厂和应用上下文，该模块也提供了许多企业服务，例如 E-mail、JNDI 访问、EJB 集成和调度。 所有的 Spring 模块都构建于核心容器之上。当你配置应用时，其实你隐式地使用了 这些类。

  ② **AOP 模块** 

  在 AOP 模块中，Spring 对面向切面编程提供了丰富的支持。这个模块是 Spring 应 用系统中开发切面的基础。与 DI 一样，AOP 可以帮助应用对象解耦。借助于 AOP，可 以将遍布系统的关注点（例如事务和安全）从它们所应用的对象中解耦出来。 

  ③**数据访问与集成** 

  使用 JDBC 编写代码通常会导致大量的样板式代码，例如获得数据库连接、创建语 句、处理结果集到最后关闭数据库连接。Spring 的 JDBC 和 DAO（Data Access Object） 模块抽象了这些样板式代码，使我们的数据库代码变得简单明了，还可以避免因为关闭 数据库资源失败而引发的问题。该模块在多种数据库服务的错误信息之上构建了一个语 义丰富的异常层，以后我们再也不需要解释那些隐晦专有的 SQL 错误信息了！ 对于那些更喜欢 ORM（Object-Relational Mapping）工具而不愿意直接使用 JDBC 的 开发者，Spring 提供了 ORM 模块。Spring 的ORM 模块建立在对 DAO 的支持之上，并为 多个 ORM 框架提供了一种构建 DAO 的简便方式。Spring 没有尝试去创建自己的 ORM 解决方案，而是对许多流行的 ORM 框架进行了集成，包括 Hibernate、Java Persisternce API、 Java Data Object 和iBATIS SQL Maps。Spring 的事务管理支持所有的 ORM 框架以及 JDBC。

  本模块同样包含了在 JMS（Java Message Service）之上构建的 Spring 抽象层，它会 使用消息以异步的方式与其他应用集成。从 Spring 3.0 开始，本模块还包含对象到 XML 映射的特性，它最初是 Spring Web Service 项目的一部分。

  本模块会使用 Spring AOP 模块为 Spring 应用中的对象提供事务管理服务。

  

  **④ Web 与远程调用** 

  MVC（Model-View-Controller）模式是一种普遍被接受的构建 Web 应用的方法，它 可以帮助用户将界面逻辑与应用逻辑分离。Java从来不缺少MVC框架，Apache的Struts、 JSF、WebWork 和 Tapestry 都是可选的最流行的 MVC 框架。 虽然 Spring 能够与多种流行的 MVC 框架进行集成，但它的 Web 和远程调用模块自 带了一个强大的 MVC 框架，有助于在 Web 层提升应用的松耦合水平。 Spring 的 MVC 框架。 除了面向用户的 Web 应用，该模块还提供了多种构建与其他应用交互的远程调用方 案。Spring 远程调用功能集成了 RMI（Remote Method Invocation）、 Hessian、Burlap、 JAX-WS，同时 Spring 还自带了一个远程调用框架：HTTP invoker。Spring 还提供了暴 露和使用 REST API 的良好支持。

​        **⑤ Instrumentation** 

​		Spring 的 Instrumentation 模块提供了为 JVM 添加代理（agent）的功能。具体来讲， 它为 Tomcat 提供      		了一个织入代理，能够为 Tomcat 传递类文件，就像这些文件是被类加 载器加载的一样。

​		**⑥ 测试**

​		鉴于开发者自测的重要性，Spring 提供了测试模块以致力于 Spring 应用的测试。 通过该模块，发现Spring 		为使用 JNDI、Servlet 和 Portlet 编写单元测试提供了 一系列的 mock 对象实现。对于集成测试，该模块为加		载 Spring 应用上下文中的 bean 集合以及与 Spring 上下文中的 bean 进行交互提供了支持。 

​		

​		 