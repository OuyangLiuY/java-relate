package com.ouyangliuy.thread_res_async.service;

import java.util.concurrent.Callable;

public class ExecutorService<T> implements Callable<T> {
    @Override
    public T call() throws Exception {
        double random = Math.random();
        System.out.println("随机数：" + random);
        if (random <= 0.5) {
            return (T) BusinessService.callRemoteServer();
        }
        return (T) BusinessService.callRemoteServerError();
    }
}
