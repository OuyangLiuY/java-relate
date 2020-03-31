package com.ywb.synchronize;

public class SynchronizedOne {

    //method1 = method2
    //写法一修饰的是一个方法，写法二修饰的是一个代码块，
    // 但写法一与写法二是等价的，都是锁定了整个方法时的内容。
    public synchronized void method1() {
        // todo
    }

    public void method2() {
        synchronized (this) {
            // todo
        }
    }


    //这两种写法是等价的

    /**
     * synchronized关键字不能继承。
     * 虽然可以使用synchronized来定义方法，但synchronized并不属于方法定义的一部分，
     * 因此，synchronized关键字不能被继承。如果在父类中的某个方法使用了synchronized关键字，
     * 而在子类中覆盖了这个方法，在子类中的这个方法默认情况下并不是同步的，
     * 而必须显式地在子类的这个方法中加上synchronized关键字才可以。
     * 当然，还可以在子类方法中调用父类中相应的方法，这样虽然子类中的方法不是同步的，
     * 但子类调用了父类的同步方法，因此，子类的方法也就相当于同步了。
     */
    class Parent {
        public synchronized void method() {
        }
    }

    class Child extends Parent {
        public synchronized void method() {
        }
    }

    class Parent1 {
        public synchronized void method() {
        }
    }

    class Child2 extends Parent1 {
        public void method() {
            super.method();
        }
    }
    /**
     * 1.在定义接口方法时不能使用synchronized关键字。
     * 2.构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步。
     */

}
