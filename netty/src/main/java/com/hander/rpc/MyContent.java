package com.hander.rpc;

import lombok.Data;

@Data
public class MyContent {
    String name;
    String methodName;
    Class<?>[] parameterTypes;
    Object[] args;
    String res;
}
