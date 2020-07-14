package com.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.demo.plugin.MyPagePlugin;
import org.apache.ibatis.plugin.Interceptor;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Driver;

@MapperScan({"com.luban.*.mapper","com.luban.*.*.mapper"})
@Configuration
public class MybatisConfig {



    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean(@Autowired DataSource dataSource) throws IOException {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
//        bean.setPlugins(new Interceptor[]{getMyPagePlugin()});
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        String packageXMLConfigPath = PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "/mapper/*/*.xml";
        bean.setMapperLocations(resolver.getResources(packageXMLConfigPath));
        return bean;
    }


    public Interceptor getMyPagePlugin(){
        MyPagePlugin myPagePlugin = new MyPagePlugin();
        myPagePlugin.setDatabaseType("mysql");
        myPagePlugin.setPageSqlId(".*ByPage$");
        return  myPagePlugin;
    }




    @Bean
    public DataSource dataSource()  {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(Driver.class.getName());
        dataSource.setPassword("1234");
        dataSource.setUsername("root");
        dataSource.setUrl("jdbc:mysql://localhost:3306/luban");
//        dataSource.setMaxActive(2);
//        dataSource.setMaxWait(200);
        return dataSource;
    }

}