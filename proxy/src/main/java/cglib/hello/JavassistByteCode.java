package cglib.hello;

import javassist.*;

import java.lang.reflect.Field;

class JavassistByteCode{
    public static void main(String[] args) throws Exception {
        ByteCodeAPIImpl im = new ByteCodeAPIImpl();
       // ByteCodeAPI byteCodeAPI = createJavassistBytecodeDynamicProxy(im);
        ByteCodeAPI byteCodeAPI = createJavassistByteCodeDynamicProxy();
        byteCodeAPI.sayHello();
    }

    private static ByteCodeAPI createJavassistByteCodeDynamicProxy() throws Exception{

        //运行时上下文
        ClassPool classPool = ClassPool.getDefault();
        //动态创建需要实现接口的类
        CtClass ctClass = classPool.makeClass(ByteCodeAPI.class.getName() + "$demo");
        ctClass.addInterface(classPool.get(ByteCodeAPI.class.getName()));
        //创建属性
        CtField field = CtField.make("private String a;", ctClass);
        ctClass.addField(field);
        //创建方法
        CtMethod method = CtMethod.make("public String sayHello () {return \"hello\";}", ctClass);
        ctClass.addMethod(method);
        //构造器
        ctClass.addConstructor(CtNewConstructor.defaultConstructor(ctClass));
        Class aClass = ctClass.toClass();
        ByteCodeAPI o = (ByteCodeAPI) aClass.newInstance();
        return o;
    }
    private static ByteCodeAPI createJavassistBytecodeDynamicProxy(ByteCodeAPI delegate) throws Exception {
        ClassPool mPool = new ClassPool(true);
        CtClass mCtc = mPool.makeClass(ByteCodeAPI.class.getName() + "JavaassistProxy");
        mCtc.addInterface(mPool.get(ByteCodeAPI.class.getName()));
        mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
        mCtc.addField(CtField.make("public " + ByteCodeAPI.class.getName() + " delegate;", mCtc));
        mCtc.addMethod(CtNewMethod.make("public int count() { return delegate.count(); }", mCtc));
        Class<?> pc = mCtc.toClass();
        ByteCodeAPI bytecodeProxy = (ByteCodeAPI) pc.newInstance();
        Field filed = bytecodeProxy.getClass().getField("delegate");
        filed.set(bytecodeProxy, delegate);
        return bytecodeProxy;
    }
}
