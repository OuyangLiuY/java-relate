package com.demo.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

/**
 * 需要咨询java高级VIP课程的同学可以加安其拉老师的QQ：3164703201
 * 需要往期视频的同学可以加木兰老师的QQ：2746251334
 * author：鲁班学院-商鞅老师
 */
@Mapper
public interface ProductMapper {


    //数组分页
    List<Map<String,Object>> getProductByArrayPage();
    //数据库分页
    List<Map<String,Object>> getProductPage(Map<String, Object> map);
    //Rowbounds分页
    List<Map<String,Object>> getProductByRowboundsPage(RowBounds rowBounds);

    //插件分页
    List<Map<String,Object>> getProductByPage(Map<String, Object> map);

    int getProductCount();








}
