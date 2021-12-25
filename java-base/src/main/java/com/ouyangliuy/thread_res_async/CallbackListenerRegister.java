package com.ouyangliuy.thread_res_async;

import java.util.LinkedList;
import java.util.Queue;

public class CallbackListenerRegister<T> {
    // 成功队列
    public Queue<Callback<T>> success = new LinkedList<>();
    // 失败队列
    public Queue<Callback<T>> fail = new LinkedList<>();

    public Object T;

    public void addCallback(Callback<T> callback){
        addFail(callback);
        addSuccess(callback);
    }

    void addSuccess(Callback<T> callback){
        success.add(callback);
    }

    void addFail(Callback<T> callback){
        fail.add(callback);
    }

    void fireSuccess(T res){
        Callback<T> callback;
        while ((callback = success.poll()) != null){
            callback.onSuccess(res);
        }

    }

    void fireFail(Throwable throwable){
        Callback<T> callback;
        while ((callback = fail.poll()) != null){
            callback.onFail(throwable);
        }
    }

    enum State{
        NEW,SUCCESS,FAIL;
        State() {
        }
    }

}
