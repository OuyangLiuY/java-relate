package com.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * 跨域可以在前端通过 JSONP 来解决，但是 JSONP 只可以发送 GET 请求，无法发送其他类型的请求
     *
     * 因此我们推荐在后端通过 （CORS，Cross-origin resource sharing） 来解决跨域问题
     * 项目中前后端分离部署，所以需要解决跨域的问题
     *
     * @param registry
     */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
                .maxAge(3600);
    }
}
