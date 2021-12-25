package com.ouyangliuy.thread_res_async;

public class CallbackResult<T> implements Callback<T>{

    @Override
    public void onFail(Throwable throwable) {
        System.out.println("执行失败了...");
        System.out.println(throwable.getMessage());
        throwable.printStackTrace();
        System.out.println("执行回滚操作。。。");
    }

    @Override
    public void onSuccess(T res) {
        System.out.println("执行成功了...");
        System.out.println(res);
        System.out.println("执行提交任务..");
    }
}
