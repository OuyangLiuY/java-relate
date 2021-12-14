package com.ouyangliuy.thread_res_async;


import com.ouyangliuy.thread_res_async.service.ExecutorService;

public class Bootstrap {

    public static void main(String[] args) throws Exception {
        ExecutorService<Object> executor = new ExecutorService<>();
        Promise<Object> promise = new Promise<>(executor);
        // 自定义个的callback
        CallbackResult<Object> res = new CallbackResult<>();
        promise.addCallback(res);
        // 开始执行任务，
        promise.run();
        // 不用管结果什么时候执行完毕，执行完毕自己会退出
        // 。。。 这里继续执行自己的业务即可，
    }
}
