package org.example.proxy.dynamic;

import org.example.proxy.dynamic.impl.UserProxyImpl;

public class TestProxy {
    public static void main(String[] args) {
       /* UserProxy instance = (UserProxy) ProxyIns.getNewInstance(UserProxyImpl.class);
        instance.collections("xxxxx");*/
        UserProxy userProxy = new UserProxyImpl();
        UserProxy newInstance = (UserProxy) HandleProxy.getNewInstance(userProxy);
        newInstance.test("principle proxy");
    }
}
