package classloader;

public class ClassLoaderTest {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
//        ClassLoader classLoader = HelloWord.class.getClassLoader();
//        System.out.println(classLoader);

//        Class<?> aClass = Class.forName("classloader.HelloWord");
//        System.out.println(aClass);
//        System.out.println(aClass.getClassLoader());
//        Class<?> aClasss = Class.forName("classloader.HelloWord",false,ClassLoader.getSystemClassLoader());
//        System.out.println(aClasss);
//        System.out.println(aClasss.getClassLoader());

        ClassLoader loader = ClassLoader.getSystemClassLoader(); // 来加载类，不会执行初始化代码块
        Class<?> helloWord = loader.loadClass("classloader.HelloWord");
        System.out.println(helloWord);
        HelloWord o = (HelloWord) helloWord.newInstance();
        System.out.println(o);
    }
}
