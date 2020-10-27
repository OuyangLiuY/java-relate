## Java代理介绍

Java中代理的实现一般分为三种：JDK静态代理、JDK动态代理以及CGLIB动态代理。在Spring的AOP实现中，主要应用了JDK动态代理以及CGLIB动态代理。但是本文着重介绍JDK动态代理机制，CGLIB动态代理后面会接着探究。

代理一般实现的模式为JDK静态代理：创建一个接口，然后创建被代理的类实现该接口并且实现该接口中的抽象方法。之后再创建一个代理类，同时使其也实现这个接口。在代理类中持有一个被代理对象的引用，而后在代理类方法中调用该对象的方法。

其实就是代理类为被代理类预处理消息、过滤消息并在此之后将消息转发给被代理类，之后还能进行消息的后置处理。代理类和被代理类通常会存在关联关系(即上面提到的持有的被带离对象的引用)，代理类本身不实现服务，而是通过调用被代理类中的方法来提供服务。

## 静态代理

## JDK动态代理

### JDK动态代理实现原理

JDK动态代理其实也是基本接口实现的。因为通过接口指向实现类实例的多态方式，可以有效地将具体实现与调用解耦，便于后期的修改和维护。

通过上面的介绍，我们可以发现JDK静态代理与JDK动态代理之间有些许相似，比如说都要创建代理类，以及代理类都要实现接口等。但是不同之处也非常明显----在静态代理中我们需要对哪个接口和哪个被代理类创建代理类，所以我们在编译前就需要代理类实现与被代理类相同的接口，并且直接在实现的方法中调用被代理类相应的方法；但是动态代理则不同，我们不知道要针对哪个接口、哪个被代理类创建代理类，因为它是在运行时被创建的。

让我们用一句话来总结一下JDK静态代理和JDK动态代理的区别，然后开始探究JDK动态代理的底层实现机制：JDK静态代理是通过直接编码创建的，而JDK动态代理是利用反射机制在运行时创建代理类的。其实在动态代理中，核心是InvocationHandler。每一个代理的实例都会有一个关联的调用处理程序(InvocationHandler)。对待代理实例进行调用时，将对方法的调用进行编码并指派到它的调用处理器(InvocationHandler)的invoke方法。所以对代理对象实例方法的调用都是通过InvocationHandler中的invoke方法来完成的，而invoke方法会根据传入的代理对象、方法名称以及参数决定调用代理的哪个方法。

我们从JDK动态代理的测试类中可以发现代理类生成是通过Proxy类中的newProxyInstance来完成的，下面我们进入这个函数看一看：

**Proxy类中的newProxyInstance**

```java
@CallerSensitive
public static Object newProxyInstance(ClassLoader loader,
                                      Class<?>[] interfaces,
                                      InvocationHandler h)
    throws IllegalArgumentException
{
    //h为空,抛异常
    Objects.requireNonNull(h);
	//拷贝被代理类实现的一些接口，用于后面权限方面的一些检查
    final Class<?>[] intfs = interfaces.clone();
    final SecurityManager sm = System.getSecurityManager();
    if (sm != null) {
          //在这里对某些安全权限进行检查，确保我们有权限对预期的被代理类进行代理
        checkProxyAccess(Reflection.getCallerClass(), loader, intfs);
    }

    /*
     * Look up or generate the designated proxy class.
     * 下面这个方法将产生代理类
     */
    Class<?> cl = getProxyClass0(loader, intfs);

    /*
     * Invoke its constructor with the designated invocation handler.
     * 使用指定的调用处理程序获取代理类的构造函数对象
     */
    try {
        if (sm != null) {
            checkNewProxyPermission(Reflection.getCallerClass(), cl);
        }
		//假如代理类的构造函数是private的，就使用反射来set accessible
        final Constructor<?> cons = cl.getConstructor(constructorParams);
        final InvocationHandler ih = h;
        if (!Modifier.isPublic(cl.getModifiers())) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    cons.setAccessible(true);
                    return null;
                }
            });
        }
        //根据代理类的构造函数来生成代理类的对象并返回
        return cons.newInstance(new Object[]{h});
    } catch (IllegalAccessException|InstantiationException e) {
        throw new InternalError(e.toString(), e);
    } catch (InvocationTargetException e) {
        Throwable t = e.getCause();
        if (t instanceof RuntimeException) {
            throw (RuntimeException) t;
        } else {
            throw new InternalError(t.toString(), t);
        }
    } catch (NoSuchMethodException e) {
        throw new InternalError(e.toString(), e);
    }
}
```

所以代理类其实是通过getProxyClass方法来生成的：

```java
/**
 * Generate a proxy class.  Must call the checkProxyAccess method
 * to perform permission checks before calling this.
 */
private static Class<?> getProxyClass0(ClassLoader loader,
                                       Class<?>... interfaces) {
    if (interfaces.length > 65535) {
        throw new IllegalArgumentException("interface limit exceeded");
    }

    // If the proxy class defined by the given loader implementing
    // the given interfaces exists, this will simply return the cached copy;
    // otherwise, it will create the proxy class via the ProxyClassFactory
    // 如果在缓存中有对应的代理类，那么直接返回
    // 否则代理类将有 ProxyClassFactory 来创建
    return proxyClassCache.get(loader, interfaces);
}
```

那么ProxyClassFactory是什么呢？

```java
/**
 * A factory function that generates, defines and returns the proxy class given
 * the ClassLoader and array of interfaces.
 * 根据给定ClassLoader和Interface来创建代理类的工厂函数  
 */
private static final class ProxyClassFactory
    implements BiFunction<ClassLoader, Class<?>[], Class<?>>
{
    // prefix for all proxy class names
    // 代理类的名字的前缀统一为“$Proxy”
    private static final String proxyClassNamePrefix = "$Proxy";

    // next number to use for generation of unique proxy class names
    // 每个代理类前缀后面都会跟着一个唯一的编号，如$Proxy0、$Proxy1、$Proxy2
    private static final AtomicLong nextUniqueNumber = new AtomicLong();

    @Override
    public Class<?> apply(ClassLoader loader, Class<?>[] interfaces) {

        Map<Class<?>, Boolean> interfaceSet = new IdentityHashMap<>(interfaces.length);
        for (Class<?> intf : interfaces) {
            /*
             * Verify that the class loader resolves the name of this
             * interface to the same Class object.
             * 验证类加载器加载接口得到对象是否与由apply函数参数传入的对象相同
             */
            Class<?> interfaceClass = null;
            try {
                interfaceClass = Class.forName(intf.getName(), false, loader);
            } catch (ClassNotFoundException e) {
            }
            if (interfaceClass != intf) {
                throw new IllegalArgumentException(
                    intf + " is not visible from class loader");
            }
            /*
             * Verify that the Class object actually represents an
             * interface.
             * 验证这个Class对象是不是接口
             */
            if (!interfaceClass.isInterface()) {
                throw new IllegalArgumentException(
                    interfaceClass.getName() + " is not an interface");
            }
            /*
             * Verify that this interface is not a duplicate.
             * 验证这个接口是否重复
             */
            if (interfaceSet.put(interfaceClass, Boolean.TRUE) != null) {
                throw new IllegalArgumentException(
                    "repeated interface: " + interfaceClass.getName());
            }
        }
		// 声明代理类所在的package
        String proxyPkg = null;     // package to define proxy class in
        int accessFlags = Modifier.PUBLIC | Modifier.FINAL;

        /*
         * Record the package of a non-public proxy interface so that the
         * proxy class will be defined in the same package.  Verify that
         * all non-public proxy interfaces are in the same package.
         * 记录一个非公共代理接口的包，以便在同一个包中定义代理类。
         * 同时验证所有非公共代理接口都在同一个包中
         */
        for (Class<?> intf : interfaces) {
            int flags = intf.getModifiers();
            if (!Modifier.isPublic(flags)) {
                accessFlags = Modifier.FINAL;
                String name = intf.getName();
                int n = name.lastIndexOf('.');
                String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
                if (proxyPkg == null) {
                    proxyPkg = pkg;
                } else if (!pkg.equals(proxyPkg)) {
                    throw new IllegalArgumentException(
                        "non-public interfaces from different packages");
                }
            }
        }

        if (proxyPkg == null) {
            // if no non-public proxy interfaces, use com.sun.proxy package
            // 如果全是公共代理接口，那么生成的代理类就在com.sun.proxy package下
            proxyPkg = ReflectUtil.PROXY_PACKAGE + ".";
        }

        /*
         * Choose a name for the proxy class to generate.
         * 为代理类生成一个name  package name + 前缀+唯一编号
         * 如 com.sun.proxy.$Proxy0.class
         */
        long num = nextUniqueNumber.getAndIncrement();
        String proxyName = proxyPkg + proxyClassNamePrefix + num;

        /*
         * Generate the specified proxy class.
         * 生成指定代理类的字节码文件
         */
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
            proxyName, interfaces, accessFlags);
        try {
            return defineClass0(loader, proxyName,
                                proxyClassFile, 0, proxyClassFile.length);
        } catch (ClassFormatError e) {
            /*
             * A ClassFormatError here means that (barring bugs in the
             * proxy class generation code) there was some other
             * invalid aspect of the arguments supplied to the proxy
             * class creation (such as virtual machine limitations
             * exceeded).
             */
            throw new IllegalArgumentException(e.toString());
        }
    }
}
```

**字节码生成**

由上方代码byte[] proxyClassFile = ProxyGenerator.generateProxyClass(proxyName, interfaces, accessFlags);可以看到，其实生成代理类字节码文件的工作是通过 ProxyGenerate类中的generateProxyClass方法来完成的。

```java
public static byte[] generateProxyClass(final String var0, Class<?>[] var1, int var2) {
    ProxyGenerator var3 = new ProxyGenerator(var0, var1, var2);
    // 真正用来生成代理类字节码文件的方法在这里
    final byte[] var4 = var3.generateClassFile();
    // 保存代理类的字节码文件
    if (saveGeneratedFiles) {
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                try {
                    int var1 = var0.lastIndexOf(46);
                    Path var2;
                    if (var1 > 0) {
                        Path var3 = Paths.get(var0.substring(0, var1).replace('.', File.separatorChar));
                        Files.createDirectories(var3);
                        var2 = var3.resolve(var0.substring(var1 + 1, var0.length()) + ".class");
                    } else {
                        var2 = Paths.get(var0 + ".class");
                    }

                    Files.write(var2, var4, new OpenOption[0]);
                    return null;
                } catch (IOException var4x) {
                    throw new InternalError("I/O exception saving generated file: " + var4x);
                }
            }
        });
    }

    return var4;
}
```

下面来看看真正用于生成代理类字节码文件的generateClassFile方法:

```java
private byte[] generateClassFile() {
        //下面一系列的addProxyMethod方法是将接口中的方法和Object中的方法添加到代理方法中(proxyMethod)
        this.addProxyMethod(hashCodeMethod, Object.class);
        this.addProxyMethod(equalsMethod, Object.class);
        this.addProxyMethod(toStringMethod, Object.class);
        Class[] var1 = this.interfaces;
        int var2 = var1.length;   
		int var3;
    	Class var4;
   //获得接口中所有方法并添加到代理方法中
    for(var3 = 0; var3 < var2; ++var3) {
        var4 = var1[var3];
        Method[] var5 = var4.getMethods();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Method var8 = var5[var7];
            this.addProxyMethod(var8, var4);
        }
    }

    Iterator var11 = this.proxyMethods.values().iterator();
    //验证具有相同方法签名的方法的返回类型是否一致
    List var12;
    while(var11.hasNext()) {
        var12 = (List)var11.next();
        checkReturnTypes(var12);
    }

    //后面一系列的步骤用于写代理类Class文件
    Iterator var15;
    try {
         //生成代理类的构造函数
        this.methods.add(this.generateConstructor());
        var11 = this.proxyMethods.values().iterator();

        while(var11.hasNext()) {
            var12 = (List)var11.next();
            var15 = var12.iterator();

            while(var15.hasNext()) {
                ProxyGenerator.ProxyMethod var16 = (ProxyGenerator.ProxyMethod)var15.next();
                //将代理类字段声明为Method，并且字段修饰符为 private static.
               //因为 10 是 ACC_PRIVATE和ACC_STATIC的与运算 故代理类的字段都是 private static Method ***
                this.fields.add(new ProxyGenerator.FieldInfo(var16.methodFieldName, 
                               "Ljava/lang/reflect/Method;", 10));
               //生成代理类的方法
                this.methods.add(var16.generateMethod());
            }
        }
       //为代理类生成静态代码块对某些字段进行初始化
        this.methods.add(this.generateStaticInitializer());
    } catch (IOException var10) {
        throw new InternalError("unexpected I/O Exception", var10);
    }

    if(this.methods.size() > '\uffff') { //代理类中的方法数量超过65535就抛异常
        throw new IllegalArgumentException("method limit exceeded");
    } else if(this.fields.size() > '\uffff') {// 代理类中字段数量超过65535也抛异常
        throw new IllegalArgumentException("field limit exceeded");
    } else {
        // 后面是对文件进行处理的过程
        this.cp.getClass(dotToSlash(this.className));
        this.cp.getClass("java/lang/reflect/Proxy");
        var1 = this.interfaces;
        var2 = var1.length;

        for(var3 = 0; var3 < var2; ++var3) {
            var4 = var1[var3];
            this.cp.getClass(dotToSlash(var4.getName()));
        }

        this.cp.setReadOnly();
        ByteArrayOutputStream var13 = new ByteArrayOutputStream();
        DataOutputStream var14 = new DataOutputStream(var13);

        try {
            var14.writeInt(-889275714);
            var14.writeShort(0);
            var14.writeShort(49);
            this.cp.write(var14);
            var14.writeShort(this.accessFlags);
            var14.writeShort(this.cp.getClass(dotToSlash(this.className)));
            var14.writeShort(this.cp.getClass("java/lang/reflect/Proxy"));
            var14.writeShort(this.interfaces.length);
            Class[] var17 = this.interfaces;
            int var18 = var17.length;

            for(int var19 = 0; var19 < var18; ++var19) {
                Class var22 = var17[var19];
                var14.writeShort(this.cp.getClass(dotToSlash(var22.getName())));
            }

            var14.writeShort(this.fields.size());
            var15 = this.fields.iterator();

            while(var15.hasNext()) {
                ProxyGenerator.FieldInfo var20 = (ProxyGenerator.FieldInfo)var15.next();
                var20.write(var14);
            }

            var14.writeShort(this.methods.size());
            var15 = this.methods.iterator();

            while(var15.hasNext()) {
                ProxyGenerator.MethodInfo var21 = (ProxyGenerator.MethodInfo)var15.next();
                var21.write(var14);
            }

            var14.writeShort(0);
            return var13.toByteArray();
        } catch (IOException var9) {
            throw new InternalError("unexpected I/O Exception", var9);
        }
    }
}
```
### 代理类的方法调用

下面是将接口与Object中一些方法添加到代理类中的addProxyMethod方法：

```java
private void addProxyMethod(Method var1, Class<?> var2) {
    String var3 = var1.getName(); //获得方法名称
    Class[] var4 = var1.getParameterTypes(); //获得方法参数类型
    Class var5 = var1.getReturnType();//获得方法返回类型
    Class[] var6 = var1.getExceptionTypes();//异常类
    String var7 = var3 + getParameterDescriptors(var4);//获得方法签名
    Object var8 = (List)this.proxyMethods.get(var7);//根据方法前面获得proxyMethod的value
    if (var8 != null) { //处理多个代理接口中方法重复的情况
        Iterator var9 = ((List)var8).iterator();

        while(var9.hasNext()) {
            ProxyGenerator.ProxyMethod var10 = (ProxyGenerator.ProxyMethod)var9.next();
            if (var5 == var10.returnType) {
                ArrayList var11 = new ArrayList();
                collectCompatibleTypes(var6, var10.exceptionTypes, var11);
                collectCompatibleTypes(var10.exceptionTypes, var6, var11);
                var10.exceptionTypes = new Class[var11.size()];
                var10.exceptionTypes = (Class[])var11.toArray(var10.exceptionTypes);
                return;
            }
        }
    } else {
        var8 = new ArrayList(3);
        this.proxyMethods.put(var7, var8);
    }

    ((List)var8).add(new ProxyGenerator.ProxyMethod(var3, var4, var5, var6, var2));
}
```

这就是最终真正的代理类，它继承自Proxy并实现了我们定义的Subject接口。我们通过

```java
HelloInterface hello = (HelloInterface ) Proxy.newProxyInstance(loader, interfaces, handler);
hello.hello("Tom");
//实际上就是执行上面类的相应方法，也就是：
public final void hello(String paramString)
  {
    try
    {
      this.h.invoke(this, m3, new Object[] { paramString });
      //就是调用我们自定义的InvocationHandlerImpl的 invoke方法：
      return;
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
//注意这里的this.h.invoke中的h，它是类Proxy中的一个属性
 protected InvocationHandler h;
//因为这个代理类继承了Proxy，所以也就继承了这个属性，而这个属性值就是我们定义的
    InvocationHandler handler = new InvocationHandlerImpl(hello);
//同时我们还发现，invoke方法的第一参数在底层调用的时候传入的是this，也就是最终生成的代理对象ProxySubject，这是JVM自己动态生成的，而不是我们自己定义的代理对象。
```

## 深入理解CGLIB动态代理机制

**Cglib是什么?**

Cglib是一个强大的,高性能的代码生成包,它广泛被许多AOP框架使用,为他们提供方法的拦截,

![cglib](../../images/netty/cglib.png)

对此图总结一下：

- 最底层的是字节码Bytecode,字节码是Java为了保证“一次编译、到处运行”而产生的一种虚拟指令格式，例如iload*0、iconst*1、if_icmpne、dup等
- 位于字节码之上的是ASM，这是一种直接操作字节码的框架，应用ASM需要对Java字节码、Class结构比较熟悉
- 位于ASM之上的是CGLIB、Groovy、BeanShell，后两种并不是Java体系中的内容而是脚本语言，它们通过ASM框架生成字节码变相执行Java代码，这说明在JVM中执行程序并不一定非要写Java代码----只要你能生成Java字节码，JVM并不关心字节码的来源，当然通过Java代码生成的JVM字节码是通过编译器直接生成的，算是最“正统”的JVM字节码
- 位于CGLIB、Groovy、BeanShell之上的就是Hibernate、Spring AOP这些框架了，这一层大家都比较熟悉
- 最上层的是Applications，即具体应用，一般都是一个Web项目或者本地跑一个程序

本文是基于CGLIB 3.1进行探究的

cglib is a powerful, high performance and quality Code Generation Library, It is used to extend JAVA classes and implements interfaces at runtime.

在Spring AOP中，通常会用它来生成AopProxy对象。不仅如此，在Hibernate中PO(Persistant Object 持久化对象)字节码的生成工作也要靠它来完成。

本文将深入探究CGLIB动态代理的实现机制，配合下面这篇文章一起食用口味更佳：[深入理解JDK动态代理机制](https://www.jianshu.com/p/471c80a7e831)

### CGLIB动态代理示例

下面由一个简单的示例开始我们对CGLIB动态代理的介绍：

```
 <dependency>
    <groupId>cglib</groupId>
     <artifactId>cglib</artifactId>
    <version>3.3.0</version>
</dependency>
```

实现MethodInterceptor接口生成方法拦截器:

```java
public class HelloImpl implements Hello{
    @Override
    public String hello(String msg) {
        System.out.println("HelloImpl.hello =  " + msg);
        return null;
    }
}
public class HelloMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("before = " + method.getName());
        Object obj = methodProxy.invokeSuper(o, objects);
        System.out.println("after = " + method.getName());
        return obj;
    }
}


public class client {
    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(HelloImpl.class);
        enhancer.setCallback(new HelloMethodInterceptor());
        HelloImpl hello = (HelloImpl) enhancer.create();
        hello.hello("这是cglib的测试");
    }
}
/* 输出结果:
 * before = hello
 * HelloImpl.hello =  这是cglib的测试
 * after = hello
 */
```

**JDK代理要求被代理的类必须实现接口，有很强的局限性。而CGLIB动态代理则没有此类强制性要求。简单的说，CGLIB会让生成的代理类继承被代理类，并在代理类中对代理方法进行强化处理(前置处理、后置处理等)。在CGLIB底层，其实是借助了ASM这个非常强大的Java字节码生成框架。**

### 生成代理类对象

从图1.3中我们看到，代理类对象是由Enhancer类创建的。Enhancer是CGLIB的字节码增强器，可以很方便的对类进行拓展，如图1.3中的为类设置Superclass。

创建代理对象的几个步骤:

- 生成代理类的二进制字节码文件；
- 加载二进制字节码，生成Class对象( 例如使用Class.forName()方法 )；
- 通过反射机制获得实例构造，并创建代理类对象

我们来看看将代理类Class文件反编译之后的Java代码

```java
package proxy;

import java.lang.reflect.Method;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class HelloServiceImpl$EnhancerByCGLIB$82ef2d06
  extends HelloServiceImpl
  implements Factory
{
  private boolean CGLIB$BOUND;
  private static final ThreadLocal CGLIB$THREAD_CALLBACKS;
  private static final Callback[] CGLIB$STATIC_CALLBACKS;
  private MethodInterceptor CGLIB$CALLBACK_0;
  private static final Method CGLIB$sayHello$0$Method;
  private static final MethodProxy CGLIB$sayHello$0$Proxy;
  private static final Object[] CGLIB$emptyArgs;
  private static final Method CGLIB$finalize$1$Method;
  private static final MethodProxy CGLIB$finalize$1$Proxy;
  private static final Method CGLIB$equals$2$Method;
  private static final MethodProxy CGLIB$equals$2$Proxy;
  private static final Method CGLIB$toString$3$Method;
  private static final MethodProxy CGLIB$toString$3$Proxy;
  private static final Method CGLIB$hashCode$4$Method;
  private static final MethodProxy CGLIB$hashCode$4$Proxy;
  private static final Method CGLIB$clone$5$Method;
  private static final MethodProxy CGLIB$clone$5$Proxy;

  static void CGLIB$STATICHOOK1()
  {
    CGLIB$THREAD_CALLBACKS = new ThreadLocal();
    CGLIB$emptyArgs = new Object[0];
    Class localClass1 = Class.forName("proxy.HelloServiceImpl$EnhancerByCGLIB$82ef2d06");
    Class localClass2;
    Method[] tmp95_92 = ReflectUtils.findMethods(new String[] { "finalize", "()V", "equals", "(Ljava/lang/Object;)Z", "toString", "()Ljava/lang/String;", "hashCode", "()I", "clone", "()Ljava/lang/Object;" }, (localClass2 = Class.forName("java.lang.Object")).getDeclaredMethods());
    CGLIB$finalize$1$Method = tmp95_92[0];
    CGLIB$finalize$1$Proxy = MethodProxy.create(localClass2, localClass1, "()V", "finalize", "CGLIB$finalize$1");
    Method[] tmp115_95 = tmp95_92;
    CGLIB$equals$2$Method = tmp115_95[1];
    CGLIB$equals$2$Proxy = MethodProxy.create(localClass2, localClass1, "(Ljava/lang/Object;)Z", "equals", "CGLIB$equals$2");
    Method[] tmp135_115 = tmp115_95;
    CGLIB$toString$3$Method = tmp135_115[2];
    CGLIB$toString$3$Proxy = MethodProxy.create(localClass2, localClass1, "()Ljava/lang/String;", "toString", "CGLIB$toString$3");
    Method[] tmp155_135 = tmp135_115;
    CGLIB$hashCode$4$Method = tmp155_135[3];
    CGLIB$hashCode$4$Proxy = MethodProxy.create(localClass2, localClass1, "()I", "hashCode", "CGLIB$hashCode$4");
    Method[] tmp175_155 = tmp155_135;
    CGLIB$clone$5$Method = tmp175_155[4];
    CGLIB$clone$5$Proxy = MethodProxy.create(localClass2, localClass1, "()Ljava/lang/Object;", "clone", "CGLIB$clone$5");
    tmp175_155;
    Method[] tmp223_220 = ReflectUtils.findMethods(new String[] { "sayHello", "()V" }, (localClass2 = Class.forName("proxy.HelloServiceImpl")).getDeclaredMethods());
    CGLIB$sayHello$0$Method = tmp223_220[0];
    CGLIB$sayHello$0$Proxy = MethodProxy.create(localClass2, localClass1, "()V", "sayHello", "CGLIB$sayHello$0");
    tmp223_220;
    return;
  }

  final void CGLIB$sayHello$0()
  {
    super.sayHello();
  }

  public final void sayHello()
  {
    MethodInterceptor tmp4_1 = this.CGLIB$CALLBACK_0;
    if (tmp4_1 == null)
    {
      tmp4_1;
      CGLIB$BIND_CALLBACKS(this);
    }
    if (this.CGLIB$CALLBACK_0 != null) {
      return;
    }
    super.sayHello();
  }

  final void CGLIB$finalize$1()
    throws Throwable
  {
    super.finalize();
  }

  protected final void finalize()
    throws Throwable
  {
    MethodInterceptor tmp4_1 = this.CGLIB$CALLBACK_0;
    if (tmp4_1 == null)
    {
      tmp4_1;
      CGLIB$BIND_CALLBACKS(this);
    }
    if (this.CGLIB$CALLBACK_0 != null) {
      return;
    }
    super.finalize();
  }

  final boolean CGLIB$equals$2(Object paramObject)
  {
    return super.equals(paramObject);
  }

  public final boolean equals(Object paramObject)
  {
    MethodInterceptor tmp4_1 = this.CGLIB$CALLBACK_0;
    if (tmp4_1 == null)
    {
      tmp4_1;
      CGLIB$BIND_CALLBACKS(this);
    }
    MethodInterceptor tmp17_14 = this.CGLIB$CALLBACK_0;
    if (tmp17_14 != null)
    {
      Object tmp41_36 = tmp17_14.intercept(this, CGLIB$equals$2$Method, new Object[] { paramObject }, CGLIB$equals$2$Proxy);
      tmp41_36;
      return tmp41_36 == null ? false : ((Boolean)tmp41_36).booleanValue();
    }
    return super.equals(paramObject);
  }

  final String CGLIB$toString$3()
  {
    return super.toString();
  }

  public final String toString()
  {
    MethodInterceptor tmp4_1 = this.CGLIB$CALLBACK_0;
    if (tmp4_1 == null)
    {
      tmp4_1;
      CGLIB$BIND_CALLBACKS(this);
    }
    MethodInterceptor tmp17_14 = this.CGLIB$CALLBACK_0;
    if (tmp17_14 != null) {
      return (String)tmp17_14.intercept(this, CGLIB$toString$3$Method, CGLIB$emptyArgs, CGLIB$toString$3$Proxy);
    }
    return super.toString();
  }

  final int CGLIB$hashCode$4()
  {
    return super.hashCode();
  }

  public final int hashCode()
  {
    MethodInterceptor tmp4_1 = this.CGLIB$CALLBACK_0;
    if (tmp4_1 == null)
    {
      tmp4_1;
      CGLIB$BIND_CALLBACKS(this);
    }
    MethodInterceptor tmp17_14 = this.CGLIB$CALLBACK_0;
    if (tmp17_14 != null)
    {
      Object tmp36_31 = tmp17_14.intercept(this, CGLIB$hashCode$4$Method, CGLIB$emptyArgs, CGLIB$hashCode$4$Proxy);
      tmp36_31;
      return tmp36_31 == null ? 0 : ((Number)tmp36_31).intValue();
    }
    return super.hashCode();
  }

  final Object CGLIB$clone$5()
    throws CloneNotSupportedException
  {
    return super.clone();
  }

  protected final Object clone()
    throws CloneNotSupportedException
  {
    MethodInterceptor tmp4_1 = this.CGLIB$CALLBACK_0;
    if (tmp4_1 == null)
    {
      tmp4_1;
      CGLIB$BIND_CALLBACKS(this);
    }
    MethodInterceptor tmp17_14 = this.CGLIB$CALLBACK_0;
    if (tmp17_14 != null) {
      return tmp17_14.intercept(this, CGLIB$clone$5$Method, CGLIB$emptyArgs, CGLIB$clone$5$Proxy);
    }
    return super.clone();
  }

  public static MethodProxy CGLIB$findMethodProxy(Signature paramSignature)
  {
    String tmp4_1 = paramSignature.toString();
    switch (tmp4_1.hashCode())
    {
    case -1574182249: 
      if (tmp4_1.equals("finalize()V")) {
        return CGLIB$finalize$1$Proxy;
      }
      break;
    }
  }

  public HelloServiceImpl$EnhancerByCGLIB$82ef2d06()
  {
    CGLIB$BIND_CALLBACKS(this);
  }

  public static void CGLIB$SET_THREAD_CALLBACKS(Callback[] paramArrayOfCallback)
  {
    CGLIB$THREAD_CALLBACKS.set(paramArrayOfCallback);
  }

  public static void CGLIB$SET_STATIC_CALLBACKS(Callback[] paramArrayOfCallback)
  {
    CGLIB$STATIC_CALLBACKS = paramArrayOfCallback;
  }

  private static final void CGLIB$BIND_CALLBACKS(Object paramObject)
  {
    82ef2d06 local82ef2d06 = (82ef2d06)paramObject;
    if (!local82ef2d06.CGLIB$BOUND)
    {
      local82ef2d06.CGLIB$BOUND = true;
      Object tmp23_20 = CGLIB$THREAD_CALLBACKS.get();
      if (tmp23_20 == null)
      {
        tmp23_20;
        CGLIB$STATIC_CALLBACKS;
      }
      local82ef2d06.CGLIB$CALLBACK_0 = (// INTERNAL ERROR //

```

### 对委托类进行代理

我们上面贴出了生成的代理类源码。以我们上面的例子为参考，下面我们总结一下CGLIB在进行代理的时候都进行了哪些工作呢

- 生成的代理类HelloServiceImpl$EnhancerByCGLIB$82ef2d06继承被代理类HelloServiceImpl。在这里我们需要注意一点：**如果委托类被final修饰**，那么它不可被继承，即不可被代理；同样，如果委托类中存在**final修饰的方法**，那么该方法也不可被代理；
- 代理类会为委托方法生成两个方法，一个是重写的sayHello方法，另一个是CGLIB$sayHello$0方法，我们可以看到它是直接调用父类的sayHello方法；
- 当执行代理对象的sayHello方法时，会首先判断一下是否存在实现了MethodInterceptor接口的CGLIB$CALLBACK_0;，如果存在，则将调用MethodInterceptor中的intercept方法，如图2.1。

我们知道，在JDK动态代理中方法的调用是通过**反射**来完成的。如果有对此不太了解的同学，可以看下我之前的博客----[深入理解JDK动态代理机制](https://www.jianshu.com/p/471c80a7e831)。但是在CGLIB中，方法的调用并不是通过反射来完成的，而是**直接对方法进行调用**：FastClass对Class对象进行特别的处理，比如将会用**数组保存method**的引用，每次调用方法的时候都是通过一个index下标来保持对方法的引用。比如下面的getIndex方法就是通过方法签名来获得方法在存储了Class信息的数组中的下标。


到此为止CGLIB动态代理机制就介绍完了，下面给出三种代理方式之间对比。

| 代理方式      | 实现                                                         | 优点                                                         | 缺点                                                         | 特点                                                       |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------------------------------------------- |
| JDK静态代理   | 代理类与委托类实现同一接口，并且在代理类中需要硬编码接口     | 实现简单，容易理解                                           | 代理类需要硬编码接口，在实际应用中可能会导致重复编码，浪费存储空间并且效率很低 | 没啥特点                                                   |
| JDK动态代理   | 代理类与委托类实现同一接口，主要是通过代理类实现InvocationHandler并重写invoke方法来进行动态代理的，在invoke方法中将对方法进行增强处理 | 不需要硬编码接口，代码复用率高                               | 只能够代理实现了接口的委托类                                 | 底层使用反射机制进行方法的调用                             |
| CGLIB动态代理 | 代理类将委托类作为自己的父类并为其中的非final委托方法创建两个方法，一个是与委托方法签名相同的方法，它在方法中会通过super调用委托方法；另一个是代理类独有的方法。在代理方法中，它会判断是否存在实现了MethodInterceptor接口的对象，若存在则将调用intercept方法对委托方法进行代理 | 可以在运行时对类或者是接口进行增强操作，且委托类无需实现接口 | 不能对final类以及final方法进行代理                           | 底层将方法全部存入一个数组中，通过数组索引直接进行方法调用 |

