package com.ouyangliuy.thread_res_async;

public  interface Callback<T>{

     public void onFail(Throwable throwable);

    void onSuccess(T res);
}
