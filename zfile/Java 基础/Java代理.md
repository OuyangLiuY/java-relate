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

private byte[] generateClassFile() {
        //下面一系列的addProxyMethod方法是将接口中的方法和Object中的方法添加到代理方法中(proxyMethod)
        this.addProxyMethod(hashCodeMethod, Object.class);
        this.addProxyMethod(equalsMethod, Object.class);
        this.addProxyMethod(toStringMethod, Object.class);
        Class[] var1 = this.interfaces;
        int var2 = var1.length;

```java
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
https://juejin.im/post/6844903998080679949#heading-4