package com.ouyangliuye.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
        System.out.println(String.class.getClassLoader());
    }

    @RequestMapping(value = "res")
    public Object getString(){

        return String.class.getClassLoader();
    }

}
