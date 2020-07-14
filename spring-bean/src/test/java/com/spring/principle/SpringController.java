package com.spring.principle;

public class SpringController {


    @Autowired
    private SpringService springService;

    public void setSpringService(SpringService springService) {
        this.springService = springService;
    }

    public SpringService getSpringService() {
        return springService;
    }

    void print(){
        System.out.println(springService.toString());
    }
}
