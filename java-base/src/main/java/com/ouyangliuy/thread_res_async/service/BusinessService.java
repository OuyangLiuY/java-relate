package com.ouyangliuy.thread_res_async.service;

public class BusinessService {
    public static final int N = 100;
    public static Object callRemoteServer(){
        System.out.println("运行了正确服务。");
        for (int i = 0; i < N; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "success";
    }

    public static Object callRemoteServerError(){
        // 运行错误服务
        System.out.println("运行了错误服务。");
        for (int i = 0; i < N; i++) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(i == 88){
                int res = 0;
                try {
                    res = i / 0;
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("出错了..");
                }
            }
            if(i == 99){
                throw  new RuntimeException("运行出错了");
            }
        }
        return "error";
    }
}
