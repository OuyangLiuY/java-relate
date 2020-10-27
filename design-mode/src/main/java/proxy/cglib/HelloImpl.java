package proxy.cglib;

public class HelloImpl implements Hello{
    @Override
    public String hello(String msg) {

        System.out.println("HelloImpl.hello =  " + msg);
        return null;
    }
}
