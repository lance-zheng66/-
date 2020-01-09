## Spring 第三章高级装配

Spring 提供了多种技巧，借助他们可以实现更为高级的bean装配的功能

#### 环境与profile

在开发软件的时候，有一个很大的挑战就是将应用程序从一个环境迁移到另外一个环境。在开发环境中，某些环境相关做法可能并不适合迁移到生产环境中，甚至即便迁移过去也无法正常工作。数据库配置、加密算法以及外部系统的集成是跨环境部署时会发生变化的几个典型例子。

它可以理解为我们在Spring容器中所定义的Bean的逻辑组名称，只有当这些Profile被激活的时候，才会将Profile中所对应的Bean注册到Spring容器中。举个更具体的例子，我们以前所定义的Bean，当Spring容器一启动的时候，就会一股脑的全部加载这些信息完成对Bean的创建；而使用了Profile之后，它会将Bean的定义进行更细粒度的划分，将这些定义的Bean划分为几个不同的组，当Spring容器加载配置信息的时候，首先查找激活的Profile，然后只会去加载被激活的组中所定义的Bean信息，而不被激活的Profile中所定义的Bean定义信息是不会加载用于创建Bean的。

**①比如数据库配置**，在开发环境中，我们可能会使用嵌入式数据库，并预先加载测试数据。

在Spring配置类中，我们可能会在一个带@Bean**注解的方法上使用EmbeddedDatabaseBulider**:

`@Bean（destoryMethod="shutdown"）`

`public DataSource dataSource(){`

`	return new EmbeddedDatabaseBuilder();`

`					.addScript("classpath:schema.sql");`		

`				.addScript("class:test-data.sql");`				

`.bulid()`

`}`	

这会创建一个类型为Javax.sql.DataSpurce的bean，这个bean是如何创建出来你的才是最有意思的。					

使用 EmbeddedDatabaseBuilder，会搭建一个嵌入式的 Hypersonic数据库,它的模式（schema）定义在

schema.sql中，测试数据则是通过 test-data.sql 加载的。

当你在**开发环境中**运行集成测试或者启动应用进行手动测试的时候，这个DataSource是很有用的。每次启动它的时候，度能让数据库处于一个给定的状态。

**②在生产环境**的配置中，可以使用JNDI从容器中获取一个DataSource。在这样场景中，如下的@Bean方法会更适合：

`@Bean`

`public DataSource dataSoucre( ){`

`JndiObjectFactoryBean jndiObjectFactoryBean =`

`	new JndiObjectFactryBean();`	

`jndiObjectFactoryBean.setJndiName("jdbc/myDS");`

`jndiObjectFactoryBean.setResourcesRdf(true);`

`jndiObjectFactoryBean.setProxyInerface(javax.sql.DataSource.class)\`

`return(DataSource) jndiObjectFactoryBean.getObject`

`}`

通过 JNDI 获取DataSource能够让容器决定如何创建这个 DataSource， 甚至包括切换为容器管理的连接池。即便如此，JNDI管理的DataSource更加适合于生产环境，对于简单的集成和开发测试来说，这会带来不必要的复杂性。

③**在QA环境中** ,你可以选择完全不同的DataSource配置，可以配置为Commons DBCP连接池，如下所示：

`@Bean(destroyMethod = "close")`

`public DataSource DataSoucre(){`
`BasicDataSource dataSource = new BasiDataSource();`

`dataSource.setUrl("jdbc:h2:tcp：//dbserver/~/test")`

`dataSource.setDriverClassName("org.h2.Driver");`

`dataSource.setUsername("sa");`

`dataSource.setPassword("password");`

`dataSource.setInitialSize(20);`

`dataSource.setMaxActive(30);`

`return dataSource;`

`}`

它表现了在不同的环境某个bean会有所不同，我们必须要有一种方法来配置 DataSource,使其在每种环境都会选择最为合适的配置。

​	其中一种方式就是就是单独的配置类（或XML文件）中配置每一个bean,然后在构建阶段（可能会使用 Maven 的 profiles）确定要将哪一个配置编译可部署的的应用中。这种方式的问题在于要为每种环境重新构建应用。当从开发阶段迁移到QA阶段时，重新构建也许算不上什么大问题。但是，从QA阶段迁移到生产阶级时，重新构建可能会引入 bug 并且会在QA团队的成员中带来不安的情绪。值得庆幸的是，Spring所提供的解决方案并不需要重新构建。

### 配置 profile bean

Spring 为环境相关的 bean 所提供的解决方案其实与构建时的方案没有太大的差别。 当然，在这个过程中需要根据环境决定该创建哪个 bean和不创建哪个bean。不过Spring并不是在构建的时候做出这样的决策，而是等到运行再来确定。这样的结果就是同一部署单元（可能会是WAR文件）能够适用于所有的环境，没有必要进行重新够建.

Spring引入了 bean profile的功能。要使用profile的功能。要使用profile,你首先要将所有的不同都bean定义整理到一个或多个 profile之中，在将应用部署到每一个环境时，要确保对应的profile处于激活（active)的状态。

**在Java配置中，可以使用@profile注解来指定某一个bean属于哪一个profile。**

例如，在配置类中，嵌入式数据库的DataSource可能会配置成如下所示：

`@Configuration`

`@Profile("dev")`

`public class DevelopmentProfileConfig {`

`@Bean(destroyMethod = "shutdown")`

`	public DataSource dataSource(){`	

`	return new EmbeddedDatabaseBuilder()`	

`.setType(EmbeddedDatabaseType.H2)`

`.addScript("classpath:schema.sql")`

`.addScript("classpath:test-data.sql")`

`.bulid();`

`}`

`}`

注意：@Profile注解应用在了类级别上。它会告诉Spring这个配置类中的bean只有在  dev profile 激活时才会创建。如果dev profile 没有激活的话，那么带有@Bean注解的方法都会被忽略掉。

**同时，你可能还需要有一个适用于生产环境的配置，如下所示：**

`@Configuration`

`profile("prod")`

`public class ProductionProfileConfig{`

`@Bean`

`public DataSource DataSource(){`

`JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();`

`jndiObjectFactoryBean.setJndiName("jdbc/myDS");`

`jndiObjectFactoryBean.serResourceRef(true);`

`jndiObjectFactoryBean.setProxyInterface(`

`javax.sql.DataSource.class);`

`return(DataSource) jndiObjectFactroyBean.getObject();`

`}`

`}`

在本例中，只有prod profile激活的时候，才会创建对应的bean。

**在Spring 3.1中，只能在类别上使用@Profile注解。不过，从Spring3.2开始，你也可以在方法级别上使用@Profile注解，与@Bean注解一同使用。这样的话就能将这两个bean的声明放到同一个配置类之中，如下所示：**

`@Configuration`

`public class DataSourceConfig{`

`@Bean(destoryMethod = "shutdown")`

`@Profile("dev") //为 dev profile装配的bean`

`public DataSource embeddedDataSource() {`

`	return new EmbeddedDataSourceBuilder()`	

`.setType(EmbeddedDataSourceType.H2)`

`.addScirpt("classpath:schema.sql")`

`addScirpt("classpath:test-data.sql")`

`.build();`

`}`

`@Bean`

`@Profile("prod")//为prod profile装配的bean`

`public DataSource jndiDataSource(){`

`JndiObjectFactoryBean JndiObjectFactoryBean = new JndiObjectFactoryBean();`

`jndiObjectFactoryBean.setJndiName("jdbc/myDS");`

`jndiObjectFactoryBean.setResourceRef(true)`

`jndiObjectFactoryBean.serProxyInterface(javax.sql.DataSource.class);`

`return(DataSource) jndiObjectFactoryBean.getObject();`

`}`

`}`

注：尽管每个DataSource bean 都被声明在一个profile中，并且只有当规定的 profile 激活时，相应的bean才会被创建，但是可能会其他的bean 并没有声明在一个给定的profile范围内。没有指定的profile 的bean 始终都会被创建，与激活哪一个profile没有关系。

*************************************

## 在xml中配置profile

可以通过<beans>元素的profile属性，在xml中配置 profile bean.

我们也可以将profile设置为prod，创建适用于生产环境的从 JNDI 获 取的DataSource bean。同样，可以创建基于连接池定义的DataSource bean，将其放 在另外一个 XML 文件中，并标注为qa profile。所有的配置文件都会放到部署单元之中（如 WAR 文件），但是只有profile属性与当前激活 profile 相匹配的配置文件才会被用到。 你还可以在根<beans>元素中嵌套定义<beans>元素，而不是为每个环境都创建一 个profile XML 文件。这能够将所有的 profile bean 定义放到同一个 XML 文件

除了所有的 bean 定义到了同一个 XML 文件之中，这种配置方式与定义在单独的 XML 文件中的实际效果是一样的。这里有三个 bean，类型都是 javax.sql.DataSource， 并且 ID 都是dataSource。但是在运行时，只会创建一个 bean，这取决于处于激活状 态的是哪个 profile。 那么问题来了：我们该怎样激活某个 profile 呢？ 

*****

### 激活 profile

Spring 在确定哪个 profile 处于激活状态时，需要依赖两个独立的属性： spring.profiles.active 和spring.profiles.default 。如果设置了 spring.profiles.active 属性的话，那么它的值就会用来确定哪个 profile 是激活 的。但如果没有设置 spring.profiles.active 属性的话，那 Spring 将会查找 spring.profiles.default 的 值。如果 spring.profiles.active 和 spring.profiles.default均没有设置的话，那就没有激活的 profile，因此只会创 建那些没有定义在 profile 中的 bean。 有多种方式来设置这两个属性：

*  作为DispatcherServlet的初始化参数； 
*  作为 Web 应用的上下文参数； 
*  作为 JNDI 条目；
*  作为环境变量； 
* 作为 JVM 的系统属性； 
* 在集成测试类上，使用@ActiveProfiles注解设置。

 你尽可以选择spring.profiles.active和spring.profiles.default的 最佳组合方式以满足需求， 我所喜欢的一种方式是使用DispatcherServlet的参数将spring.profiles. default 设置为开发环境的 profile，我会在 Servlet 上下文中进行设置（为了兼顾到 ContextLoaderListener）。

![](C:\Users\lance\Pictures\profile激活.PNG)

按照这种方式设置spring.profiles.default，所有的开发人员都能从版本控 制软件中获得应用程序源码，并使用开发环境的设置（如嵌入式数据库）运行代码，而 不需要任何额外的配置。

当应用程序部署到 QA、生产或其他环境之中时，负责部署的人根据情况使用系统。属性、环境变量或 JNDI 设置 spring.profiles.active 即可。当设置 spring.profiles.active以后，至于 spring.profiles.default设置成什么 值就已经无所谓了；系统会优先使用spring.profiles.active中所设置的 profile。
 你可能已经注意到了，在 spring.profiles.active 和 spring.profiles. default中，profile 使用的都是复数形式。这意味着你可以同时激活多个 profile，这可 以通过列出多个 profile 名称，并以逗号分隔来实现。当然，同时启用dev和prod profile 可能也没有太大的意义，不过你可以同时设置多个彼此不相关的 profile。 

****

### 使用 profile 进行测试

当运行集成测试时，通常会希望采用与生产环境（或者是生产环境的部分子集）相 同的配置进行测试。但是，如果配置中的 bean 定义在了 profile 中，那么在运行测试时， 我们就需要有一种方式来启用合适的 profile。 Spring 提供了@ActiveProfiles注解，我们可以使用它来指定运行测试时要激活 哪个 profile。在集成测试时，通常想要激活的是开发环境的 profile。例如，下面的测试 类片段展现了使用@ActiveProfiles激活dev profile： 

`@RunWith(SpringJUnit4ClassRunner.class)`

`@ContextConfiguration(classes={PersistenceTestConfig.class})`

`@ActiveProfiles(*dev*)`

`public class PersistenceTest {`

`...`

`}`

在条件化创建 bean 方面，Spring 的 profile 机制是一种很棒的方法，这里的条件要 基于哪个 profile 处于激活状态来判断。Spring 4.0 中提供了一种更为通用的机制来实现 条件化的 bean 定义，在这种机制之中，条件完全由你来确定。让我们看一下如何使用 Spring 4 和@Conditional注解定义条件化的 bean

*****

### 3.2 条件化的bean

希望一个或多个 bean 只有在应用的类路径下包含特定的库时才创建。或者 我们希望某个 bean 只有当另外某个特定的 bean 也声明了之后才会创建。我们还可能要 求只有某个特定的环境变量设置之后，才会创建某个 bean。**简而言之，就是在可以满足特定的条件下才可以创建的bean**

 **Spring 4 引入了一个新的 @Conditional 注解，它可以用到带有@Bean 注解的方法上。如果给定的条件计算结 果为true，就会创建这个 bean，否则的话，这个 bean 会被忽略**

假设有一个名为 MagicBean 的类，我们希望只有设置了 magic 环境属性 的时候，Spring 才会实例化这个类。如果环境中没有这个属性，那么MagicBean将会被忽略

**条件化地创建bean**:

`@Bean`

`@Conditiona(MagicExistCondintion.class)`

`public MagicBean magicBean(){`

`return new MagicBean();`

`}`

由上述代码可见，，@Conditional中给定了一个 Class，它指明了条件——在本例中， 也就是MagicExistsCondition。@Conditional将 会通过Condition接 口进行 条件对比： 

`public interface Condition{`

`boolean matches(ConditionContext ctxt,`

`AnnotatedTypeMetadata metadata);`

`}`

**设置给@Conditional的类可是任意实现了Condition接口的类型。**可以看出来。这个接口实现起来很简单很直接，只需要提供matches()方法的实现即可。如果matches()方法返回true,那么就会创建带有@Conditional注解的bean.如果matchers()返回false,将不会创建这些bean.

在上述实例中，我们需要创建Condition的实现并根据环境中是否存在magic属性来做出决策，以下代码就是完成该功能的Condition 实现类

`public class MagicExistCondition implement Condition {`

`public boolean matches(ConditionContext context,AnntatedTypeMetadata metadata){`

`Environment env = context.getEnvironment();`

`return env.containsProperty("magic");//检查magic的属性`

`}`

`}`

在以上的程序中，matches()方法很简单但很功能强大，它通过给定的ConditionContext对象进而得到 Environment对象，并使用这个对象检测环境中是否存在名为magic的环境属性。

在本例中，属性的值是什么根本无所谓，只要属性存在即可满足要求。如果满足这个条件的话，matches()方法就会返回true.所带来的结果就是条件能够得到满足，所有@Conditional注解上引用MagicExistCondtion 的bean 都会被创建

如果这个属性不存在，就无法满足条件，matches（）方法就会返回 false，这协bean都不会被创建。

实际上，MagicExistsCondition中只是使用了ConditionContext得到的Environment,但 Condition实现考量的因素比这个更多。

matches () 方法会得到 ConditionContext 和 AnntatedTypeMetadata对象来做决策

**ConditionContext**是一个接口，大致如下所示：

`public interface ConditionContext{`

`BeanDefinitionResitry getRegistry();`

`ConfigurableListableBeanFactory getBeanFactory();`

`Environment getEnvirment();`

`ResourceLoader getResuorceLoader();`

`ClassLoader getClassLoader();`

`}`



**借助ConditionContext里的方法，我们可以做到以下：**

* 借助 getRegistry() 返回的 BeanDefinitionRegistry 检查 bean定义；
* 借助 getBeanFactory()  返回的 ConfigurableListableBeanFactory 检查bean是否存在，甚至探查 bean的属性
* 借助 getEnviroment () 返回的 Environment 检查环境变量是否存在以及他的值是什么；
* 读取并探查 getResourcesLoader() 返回的ResourceLoader 所加载的资源
* 借助 getClassLoader() 返回的ClassLoder 加载并检查类是否存在

**AnnotatedTypeMetadata接口，则能够让我们检查带有@Bean注解的方法上还有什么其他的注解。**

所示如下：

`public interface AnnotatedTypeMetadata{`
`boolean isAnnotated(String annotationType);`

`Map<String,Object> getAnnotationAttributes(String annotationType);`

`Map<String,Object>  getAnnotationAttributes(String  annotationType,`

`boolean classValuesAsString);`

`MultiValueMap<String,object> getAllAnnotationAttributes(String annotationType);`

`MultiValuesMap<String,object> getAllAnnotationAttributes(String annotationType,`

`boolean classValuesAsString);`

`}`

借助 isAnnotated()方法，我们能判断带有@Bean注解的方法是不是还有其他特定的注解。

而借助其他的方法，我们能够检查@Bean注解的方法上，其他注解的属性

*****

**从Spring 4 开始@Profile注解进行了重构，使其基于@Conditional和 Condition实现**

@Profile 注解如下所示：

`@Retention(RetentionPolicy.RUNTIME)`

`@Target({ElementType.TYPE,ElementType.METHOD})`

`@Documented`

`@Conditional(ProfileCondition.class)`

`public @interface Profile{`

`String [] values();`

`}`

注意：@Profile 本身也使用了@Conditional注解，并且引用ProfileCondition 作为Condition实现。如下，

**ProfileCondition 实现了 Condition 接口，并且在做出决策的过程中，考 虑到了 ConditionContext 和AnnotatedTypeMetadata 中的多个因素。 **

ProfileCondition j检查 某个bean profile 是否可用

`class ProfileCondition implements Condtion{`

`public boolean matches(ConditionConText context, AnnotatedTypeMetadata metadata) {`

`if(context.getEnvironment ()!=null){`

`MultiValueMap<String,Object> attrs `

`= metadata,getAllAnnotationAttributes(Profile.class.getName());`

`if(attrs!=nulls){for(Object values: attrs.get("value")){`

`if(context.getEnvirment().accpetsProfiles(((String[]) value))){ return true;`
`}`

`}`

`return false`

`}`

`}`

`return ture;`

`}`

`}`



ProfileCondition通过AnnotatedTypeMetadata得到了用 于@Profile 注解的所有属性。

借助该信息，它会明确地检查 value 属性，该属性包 含了bean的profile名称。

然后，它根据通过ConditionContext得到的Environment 来检查

［借助acceptsProfiles()方法］该 profile 是否处于激活状态

