package com.ywb.controller;


import com.ywb.annotation.*;
import com.ywb.service.MyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@CustomController
@CustomRequestMapping("/custom")
public class MyController {

    @CustomQualifier("MyServiceImpl")
    MyService myService;

    @CustomRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @CustomRequestParam("name") String name, @CustomRequestParam("age") String age)
    {
        PrintWriter printWriter;
        PrintWriter writer = null;
        try {
            writer = response.getWriter();
            String result = myService.query(name, Integer.parseInt(age));
            writer.write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @CustomRequestMapping("/insert")
    @CustomResponseBody
    public String insert(@CustomRequestParam("name") String name,HttpServletResponse response) throws IOException {
        PrintWriter  writer = response.getWriter();
        writer.write(name);
        return "SUCCESS";
    }
}
