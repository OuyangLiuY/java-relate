package com.ouyangliuy.thread_res_async;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 线程执行完毕，异步返回当前的结果
 */
public class Promise<T> extends FutureTask<T>{

    CallbackListenerRegister<T> callback = new CallbackListenerRegister<>();

    public Promise(Callable<T> callable) {
        super(callable);
    }

    public Promise(Runnable runnable,T result) {
        super(runnable, result);
    }

    public void addCallback(Callback<T> callback){
        this.callback.addCallback(callback);
    }


    @Override
    protected void done() {
        Throwable throwable = null;
        try {
            T res = get();
            callback.fireSuccess(res);
            return;
        } catch (InterruptedException e) {
            // 停止当前线程
            Thread.currentThread().interrupt();
            return;
        } catch (ExecutionException e) {
            throwable = e.getCause();
        }
        this.callback.fireFail(throwable);
    }

    public static class Test{

    }

}
