package only.proxy.jdk;

public class Test {
    public static void main(String[] args) {
        //将字节码文件显示到当前包下,否则生成的字节码文件会在内存中
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles","true");
        //CalculatorProxy.getProxy()
    }
}
