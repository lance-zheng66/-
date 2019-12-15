## Spring第二章 装配 Bean

### 前言

① 任何一个成功的应用都是由多个为了实现某一个业务目标而相互协作的组件构成的。这些组件必须彼此了解，并且相互协作的组件而构成的。这些组件必须彼此了解，并且相互协作来完成工作。这些组件或许还需要与数据访问组件协作，从数据库读取数据以及把数据库写入数据库。

② 创建应用对象之间的关联关系的传统方法（通过构造器或者查找）通常会导致结构复杂的代码，这些代码很难被复用也很难进行单元测试。如果情况不严重的话，这些对象所做的事情只是超出了它应做的范围；而最坏的情况则是这些对象彼此之间高度耦合，难以复用和测试。

③ 在Spring中，对象无需自己查找或创建于其所关联的其他对象。相反，容器负责把需要相互协作的对象引用赋予各个对象

④创建应用对象之间的协作的关系的行为成为通常称为装配（wiring），这也是依赖注入（DI）的本质。DI是Spring 的最基本要素。

### Spring 配置的可选方案

Spring 容器负责创建应用程序中的bean并通过DI来协调这些对象之间的关系。作为开发人员，你需要告诉Spring要创建那些Bean  并且如何将其装配在一起。当描述Bean如何进行装配时，Spring 具有非常大的灵活性，它提供了三种主要的装配机制：

① 在 XML中进行显示配置；

② 在java中进行显示配置；

③ 隐式的bean发现机制和自动装配。

以上三种装配方案可以混合交叉使用。

**使用建议：**

尽可能使用自动装配的机制。显示配置越少越好，当必须要使用显示配置的时候推荐使用类型安全却比XML更加强大的JavaConfig，最后只有当你想要使用便利的XML命名空间 ，并且在Javaconfig 没有同样的实现时才使用XML。

顺序：

自动配置 > JavaConfig > XML

### **①自动化装配**：

在便利性方面,最强大的还是Spring的自动化配置

Spring从两个角度来实现自动化装配：

组件扫描（component scanning）:Spring 会自动发现应用上下文中所创建的bean。

自动装配（autowiring）:Spring会自动满足bean之间的依赖。





#### **创建可以被发现的Bean**

**流程：**

* 创建一个类**A**，把他定义为**接口**，作为接口，里面定义一些操作方法

* 创建一个类A的 **实现类 B**,(实际上有可以有多个实现类)，类B 上使用了**@Component** 注解。这个注解表明该类会作为组件类，并告知Spring要为这个类创建Bean

* 组件扫描默认是不启用的。我们还需要显示配置一下Spring ，从而命令它去寻找带有@component注解的类，并为其创建Bean

* 定义一个**JavaConfig配置类C**，通过java代码定义了Spring的装配规则，类C上它使用了**@ComponetScan**注解，这个注解能够在Spring中启用组件扫描。

* 如果更倾向于使用XML来启用组件扫描，那么可以使用 Spring context 命名空间的

  < context:component-scan > 元素

* 创建一个**测试类 D**,类上使用注解**@ContextConfiguration**，注解会告诉D需要在 类C中加载配置。因为类C中包含了@componetScan, 因此最终的上下文会包含类A bean.



#### **为组件扫描的Bean 命名**

Spring应用上下文中所有的Bean都会给定一个ID，Spring会根据类名为其指定一个ID。具体来讲，这个bean所给定的ID为类名的第一个字母变小写。

如果想为这个bean设置不同的ID ,你所要做的就是将期望的ID作为值传递给@component注解

如：

@component("XXXXXXX")

还有另外一种为bean命名的方式，这种方式不使用@component注解，而是使用Java依赖注入规范（Java Dependency Injection)中所提供的的@Named 注解来为 bean设置 ID.



#### **设置组件扫描的基础包**：

如果没有为@ComponentScan设置任何属性。这意味着按照默认规则它会以配置类所在的包作为基础包（base packeage) 来扫描组件。

**为了指定不同的基础包，就要在@ComponentScan 的 value属性中指明包的位置：**

如：

@Configuration

@ComponentScan（"xxxxxx"）

**如果想要更加清晰的表明表明你所设置的是基础包，那么你可以通过 basePackages 的属性进行配置**

@Configuration

@ComponentScan（basePackages="xxxxxx"）

**由于basePackages属性使用的是负数形式。意味着可以设置多个基础包，只需要将basePackages属性设置为要扫描包的一个数组即可：**

@Configuation

@ComponentScan（basePackages={"aaaaaa","bbbbbb"}）

**除了将包设置为简单的 String 类型之外，@ComponentScan 还提供了另外一种方 法，那就是将其指定为包中所包含的类或接口：** 

@Configuation

@ComponentScan（basePackageClasses={CDPlayer.class,DVDPlayer.class}）

可以看到，basePackages 属性被替换成了 basePackageClasses。同时，我 们不是再使用 String 类型的名称来指定包，为 basePackageClasses 属性所设置 的数组中包含了类。这些类所在的包将会作为组件扫描的基础包。

在样例中为basePackageClasses设置的是组件类，但是你可以考虑在 包中创建一个用来进行扫描的空标记接口（marker interface）。通过标记接口的方式，你 依然能够保持对重构友好的接口引用，但是可以避免引用任何实际的应用程序代码



#### 通过为Bean添加注解实现自动装配

自动装配就是让Spring自动满足Bean依赖的一种方法，在满足依赖的过程中，会在Spring应用上下文中寻找

匹配某个bean需求的其他bean。为了声明要进行自动装配，我们可以借助Spring的**@Autowird**注解

例如：

一个CDPlay 类 **它在构造器上添加了@Autowired注解**，这表明当Spring创建CDPlay bean的时候，会通过这个构造器来进行实例化并且会传入一个可设置给 CompactDIsc类型的bean

如下：通过自动装配，将一个Compactisc注入到CDPlayer之间

`private CompactDisc cd;`

` @Autowired`

`public CDplayer(CompactDisc cd){`

this.cd = cd`};`

**@Autowired注解不仅能够用在构造器上，还能用在属性的Setter方法上**。比如说，如果CDPlay有一个setCompactDIsc（）方法，那么可以采用如下注解形式进行自动装配：

`@Autowired`

`public void setCompactisc(CompactDisc cd) {`

`this.cd = cd;}`

**不管是构造器、Setter 方法还是其他的方法，Spring 都会尝试满足方法参数上所声明的依赖。假如有且只有一个 bean 匹配依赖需求的话，那么这个 bean 将会被装配进来。**

**如果没有匹配的 bean，那么在应用上下文创建的时候，Spring 会抛出一个异常。为 了避免异常的出现，你可以将@Autowired的required属性设置为false：** 

`@Autowied(required =false);`

## 

**注：将 required属性设置为 false时，Spring 会尝试执行自动装配，但是如果没有 匹配的 bean 的话，Spring 将会让这个 bean 处于未装配的状态。但是，把required属 性设置为 false 时，你需要谨慎对待。如果在你的代码中没有进行 null 检查的话，这 个处于未装配状态的属性有可能会出现NullPointerException。 如果有多个 bean 都能满足依赖关系的话，Spring 将会抛出一个异常，表明没有明确指 定要选择哪个 bean 进行自动装配如果有多个 bean 都能满足依赖关系的话，Spring 将会抛出一个异常，表明没有明确指 定要选择哪个 bean 进行自动装配**



**@Inject 注解**：

@Autowired是Spring特有的注解，如果不愿意在代码到处使用Spring的特定注解来完成自动装配任务的话，可以考虑将其替换为@Inject;

@Inject来源于Java依赖注入规范,该规范同时还为我们定义了@Named注解。在自动装配中，Spring同时支持@Injecth和@Autowired.尽管@Inject和@Autowired之间有一些细微的差别。但是大多数场景是可以互换的



*******

### ② 通过java 代码装配bean

**前言：**

尽管在很多场景下通过组件扫描和自动装配实现 Spring 的自动化配置是更为推荐 的方式，但有时候自动化配置的方案行不通，因此需要明确配置 Spring。比如说，你想 要将第三方库中的组件装配到你的应用中，在这种情况下，是没有办法在它的类上添加 @Component和@Autowired注解的，因此就不能使用自动化装配的方案了。 

在进行显式配置的时候，有两种可 选方案：Java 和 XML

在进行显式配置时，JavaConfig 是更好的方案，因为它更为强 大、类型安全并且对重构友好。因为它就是 Java 代码，就像应用程序中的其他 Java 代 码一样

JavaConfig 与其他的 Java 代码又有所区别，在概念上，它与应用程序中的业 务逻辑和领域代码是不同的。尽管它与其他的组件一样都使用相同的语言进行表述，但 JavaConfig 是配置代码。这意味着它不应该包含任何业务逻辑，JavaConfig 也不应该侵 入到业务逻辑代码之中。尽管不是必须的，但通常会将 JavaConfig 放到单独的包中，使 它与其他的应用程序逻辑分离开来，这样对于它的意图就不会产生困惑了。

**创建配置类：**

创建 **JavaConfig** 类的关键在于为其添加@Configuration 注解，**@Configuration** 注解表明这个类是一个配置类，该类应该包含在Spring应用上下文中如何创建bean的细节。

**声明简单的bean：**

要在 JavaConfig 中声明 bean，我们需要编写一个方法，这个方法会创建所需类型的实 例，然后给这个方法添加@Bean注解。

@Bean 注解会告诉 Spring 这个方法将会返回一个对象，该对象要注册为 Spring 应 用上下文中的 bean。方法体中包含了最终产生 bean 实例的逻辑。 默认情况下，bean 的 ID 与带有@Bean注解的方法名是一样的

**借助JavaCongig实现注入：**

在 JavaConfig 中装配 bean 的最简单方式就是引用创建 bean 的方法。

再次强调一遍，带有@Bean 注解的方法可以采用任何必要的 Java 功能来产生 bean 实例。**构造器和 Setter 方法只是@Bean方法的两个简单样例。这里所存在的可能性仅仅 受到 Java 语言的限制。** 

******

### ③ 通过XML 装配bean

**创建xml 配置规范**:

在使用 XML 为 Spring 装配 bean 之前，你需要创建一个新的配置规范。

在使用 JavaConfig 的时候，这意味着要创建一个带有@Configuration注解的类，而在 XML 配置中，这意味着要创建一个 XML 文件，并且要以<beans>元素为根。

但在使用 XML 时，需要在 配置文件的顶部声明多个 XML 模式（ XSD）文件，这些文件定义了配置Spring的XML 元素。
借助 Spring Tool Suite 创建 XML 配置文件创建和管理 Spring XML 配置文件的一 种简便方式是使用 Spring Tool Suite（https://spring.io/tools/sts）。

在 Spring Tool Suite 的菜单中，选择 File>New>Spring Bean Configuration File，能够创建 Spring XML 配 置文件，并且可以选择可用的配置命名空间。 

用来装配 bean 的最基本的 XML 元素包含在spring-beans模式之中，在上面这 个 XML 文件中，它被定义为根命名空间。<beans>是该模式中的一个元素，它是所有 Spring 配置文件的根元素.

**声明简单的bean：**

要在基于 XML 的 Spring 配置中声明一个 bean，我们要使用spring-beans模式 中的另外一个元素：<bean>。<bean>元素类似于 JavaConfig 中的@Bean 注解

声明了一个很简单的 bean，创建这个 bean 的类通过 class 属性来指定的，并且 要使用全限定的类名。因为没有明确给定 ID，所以这个 bean 将会根据全限定类名来进行命名。

简单 bean 声明的一些特征：

第一件需要注意的事情就是你不再需要直接负责创建实例，

另外一个需要注意到的事情就是，在这个简单的<bean>声明中，我们将 bean 的类 型以字符串的形式设置在了class属性中。谁能保证设置给class属性的值是真正的 类呢？Spring 的 XML 配置并不能从编译期的类型检查中受益。即便它所引用的是实际 的类型，如果你重命名了类，会发生什么呢？ 借助IDE检查XML的合法性使用能够感知Spring功能的IDE，如 Spring Tool Suite， 能够在很大程度上帮助你确保 Spring XML 配置的合法性。 

**借助构造器注入初始化Bean:**

在 Spring XML 配置中，只有一种声明 bean 的方式：使用<bean>元素并指定class 属性。Spring 会从这里获取必要的信息来创建 bean。 

在XML声明DI时，具体到构造器注入有两种基本的配置方案：

* <constructor-arg>元素 

* 使用 Spring 3.0 所引入的 c-命名空间 

