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

**①自动化装配**：

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

