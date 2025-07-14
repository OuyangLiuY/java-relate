package org.example.proxy.jdk;

import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Select("select * from test_table")
    void query();
}
