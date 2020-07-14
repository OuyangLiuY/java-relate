package com.app;

import com.service.Inter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InterClass {

    @Autowired
    Inter  inter;

    public Inter getInter() {
        return inter;
    }
}
