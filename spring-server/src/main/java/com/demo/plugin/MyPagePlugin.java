package com.demo.plugin;

import com.demo.util.PageUtil;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * type：StatementHandler:拦截的对象，method：拦截的方法是prepare方法，args：传入的参数类型args是，Connection和Integer
 */
@Intercepts(@Signature(type = StatementHandler.class,method = "prepare",args = {Connection.class,Integer.class}))
public class MyPagePlugin implements Interceptor {

    private  String databaseType;
    private  String pageSqlId;

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getPageSqlId() {
        return pageSqlId;
    }

    public void setPageSqlId(String pageSqlId) {
        this.pageSqlId = pageSqlId;
    }

    //需要自己实现的逻辑
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if(!(invocation.getTarget() instanceof StatementHandler))
            return null;
        StatementHandler target = (StatementHandler) invocation.getTarget();
        //需要拿到StatementHandler对象里面的delegate,再从delegate对象里拿到mappedStatement，然后才能得到对象，
        // 该方法太麻烦
        //需要下面方法
        MetaObject metaObject = MetaObject.forObject(target, SystemMetaObject.DEFAULT_OBJECT_FACTORY, SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY, new DefaultReflectorFactory());
        String sqlId  = (String) metaObject.getValue("delegate.mappedStatement.id");
        //1.判断是否有分页
        //2.拿到连接
        //3.预编译SQL语句，拿到绑定的sql语句
        //4.执行count语句，怎么返回需要执行的count结果呢？ 就是使用 select count(0) from (sqlId(执行的sql语句))
        //重写sql select * from collections limit start ,limit
        //2.1 如何知道start和limit
        //2.2 拼接 start 和 limit
        //2.3 替换原来绑定的sql
        //
        if(sqlId.matches(pageSqlId)){
            ParameterHandler parameterHandler = target.getParameterHandler();
            //拿到原来的sql
            String sql = target.getBoundSql().getSql();
            //sql= select * from  product    select count(0) from (select * from  product) as a
            //select * from luban_product where name = #{name}
            //执行一条count语句
            //拿到数据库连接对象
            Connection connection = (Connection) invocation.getArgs()[0];
            String countSql = "select count(0) from ("+ sql+")a";
            System.out.println(countSql);
            //重新渲染参数
            PreparedStatement preparedStatement = connection.prepareStatement(countSql);
            //条件交给mybatis
            parameterHandler.setParameters(preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            if (resultSet.next()){
                count = resultSet.getInt(1);
            }
            resultSet.close();
            preparedStatement.close();
            Map<String, Object> parameterObject = (Map<String, Object>) parameterHandler.getParameterObject();
            //limit  page
            PageUtil pageUtil = (PageUtil) parameterObject.get("page");
            //limit 1 ,10  十条数据   总共可能有100   count 要的是 后面的100
            pageUtil.setCount(count);
            String pageSql = getPageSql(sql, pageUtil);
            metaObject.setValue("delegate.boundSql.sql",pageSql);
            System.out.println(pageSql);
        }
        return null;
    }
    public String getPageSql(String sql,PageUtil pageUtil){
        if(databaseType.equals("mysql")){
            return sql+" limit "+pageUtil.getStart()+","+pageUtil.getLimit();
        }else if(databaseType.equals("oracle")){
            //拼接oracle的分语句
        }

        return sql+" limit "+pageUtil.getStart()+","+pageUtil.getLimit();
    }
    //需要你返回一个动态代理后的对象  target :StatementHandler
    @Override
    public Object plugin(Object target) {

        return Plugin.wrap(target,this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
