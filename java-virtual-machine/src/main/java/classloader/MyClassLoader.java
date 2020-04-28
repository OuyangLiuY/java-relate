package classloader;

import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;

import java.io.*;

public class MyClassLoader extends ClassLoader {

    private String root;

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        byte[] classData = loadClassData(name);
        if (classData == null)
            throw new ClassNotFoundException();
        return defineClass(name, classData, 0, classData.length);
    }

    private byte[] loadClassData(String className) {
        String fileName = root + File.separator + className.replace('.', File.separatorChar) + ".class";
        try {
            InputStream inputStream = new FileInputStream(fileName);
            ByteOutputStream out = new ByteOutputStream();
            int buffsize = 1024;
            byte[] buffer = new byte[buffsize];
            int length = 0;
            while ((length = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            return out.getBytes();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public static void main(String[] args) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        MyClassLoader loader = new MyClassLoader();
        loader.setRoot("/home/temp/temp");
        Class testClass = null;
        testClass = loader.loadClass("");
        Object obj = testClass.newInstance();
        System.out.println(obj.getClass().getClassLoader());
    }
}
