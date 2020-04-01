package com.tt.singleton;

public enum SingletonEnum {

    /**实例*/
    INSTANCE;
    public void method(){

    }

    SingletonEnum() {
    }
    static class Execute{
       private String aa = "ddd";
       public  Execute(){

       }

        public String getAa() {
            return aa;
        }

        public void setAa(String aa) {
            this.aa = aa;
        }
    }
}
class Test{
    public static void main(String[] args) {
        //SingletonEnum.Execute
    }
}
