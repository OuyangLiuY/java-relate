package com.caching;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.xml.stream.events.Characters;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * guava的缓存设计的比较巧妙，可以很精巧的使用。
 * guava缓存创建分为两种，一种是CacheLoader,另一种则是callback方式
 */
public class GuavaCache {
    public static void main(String[] args) throws Exception {

        //CacheLoader
        LoadingCache<String, String> cache = CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
            @Override
            public String load(String key) throws Exception {
                return "hello " + key + "!";
            }
        });
        //api中已经把apply声明为过期，声明中推荐使用get方法获取值
        System.out.println(cache.apply("begin code"));
        System.out.println(cache.get("begin code"));
        System.out.println(cache.get("wen"));
        System.out.println(cache.apply("wen"));
        System.out.println(cache.apply("da"));
        cache.put("begin","code");
        System.out.println(cache.get("begin")); //

        //callback
        Cache<Object, Object> build = CacheBuilder.newBuilder().maximumSize(1000).build();
        String code = (String) build.get("code",  () -> "begin code !");
        System.out.println(code);

        LocalDateTime dateTime = LocalDateTime.parse("utf-8", DateTimeFormatter.ofPattern("10:20"));
        System.out.println(dateTime.getMinute());

    }
}
