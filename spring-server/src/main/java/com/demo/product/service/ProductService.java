package com.demo.product.service;


import com.demo.util.R;

/**
 * 需要咨询java高级VIP课程的同学可以加安其拉老师的QQ：3164703201
 * 需要往期视频的同学可以加木兰老师的QQ：2746251334
 * author：鲁班学院-商鞅老师
 */
public interface ProductService {


    //数组分页
    public R getProductByArrayPage(int pageNum, int pageSize);
    //数据库分页
    public R getProductPage(int pageNum, int pageSize);
    //Rowbounds分页
    public R getProductByRowboundsPage(int startNum, int limit);
    //插件分页
    public R getProductByPage(int page, int limit);


}
