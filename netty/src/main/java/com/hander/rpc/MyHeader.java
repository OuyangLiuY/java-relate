package com.hander.rpc;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyHeader implements Serializable {
    //通信上的协议
    /*
    1，值
    2，UUID:requestID
    3，DATA_LEN

     */
    int flag;  //32bit可以设置很多信息。。。
    long requestID;
    long dataLen;
}
