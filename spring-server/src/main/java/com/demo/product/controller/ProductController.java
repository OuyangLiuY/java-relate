package com.demo.product.controller;


import com.demo.product.service.impl.ProductServiceImpl;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 需要咨询java高级VIP课程的同学可以加安其拉老师的QQ：3164703201
 * 需要往期视频的同学可以加木兰老师的QQ：2746251334
 * author：鲁班学院-商鞅老师
 */
@RestController
public class ProductController {

    @Autowired
    RabbitTemplate rabbitTemplate;


    @RequestMapping("/send")
    public Object send(){
        rabbitTemplate.convertAndSend ("testExchange","asdasd","111");
        return "111";
    }

    @Autowired
    ProductServiceImpl productService;


    @RequestMapping("/getProductByArrayPage")
    public Object getProductByArrayPage(int page,int limit){

        return productService.getProductByArrayPage(page, limit);
    }




    @RequestMapping("/getProductPage")
    public Object getProductPage(int page,int limit){

        return productService.getProductPage(page,limit);
    }

    @RequestMapping("/getProductByRowboundsPage")
    public Object getProductByRowboundsPage(int page,int limit){
        return productService.getProductByRowboundsPage(page,limit);
}

    @RequestMapping("/getProductByPage")
    public Object getProductByPage(int page,int limit){
        return  productService.getProductByPage(page,limit);
    }
}
