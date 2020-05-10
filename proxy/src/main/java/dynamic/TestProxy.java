package dynamic;

import dynamic.impl.UserProxyImpl;
import dynamic.proxy.ProxyIns;

import java.net.Proxy;

public class TestProxy {
    public static void main(String[] args) {
       /* UserProxy instance = (UserProxy) ProxyIns.getNewInstance(UserProxyImpl.class);
        instance.test("xxxxx");*/
        UserProxy userProxy = new UserProxyImpl();
        UserProxy newInstance = (UserProxy) HandleProxy.getNewInstance(userProxy);
        newInstance.test("handle proxy");
    }
}
