package ChainOfResponsibility;

/**
 * 模式的结构:
 * 1.抽象处理者（Handler）角色：定义一个处理请求的接口，包含抽象处理方法和一个后继连接。
 * 2.具体处理者（Concrete Handler）角色：实现抽象处理者的处理方法，判断能否处理本次请求，如果可以处理请求则处理，否则将该请求转给它的后继者。
 * 3.客户类（Client）角色：创建处理链，并向链头的具体处理者对象提交请求，它不关心处理细节和请求的传递过程。
 */
public class ChainOfResponsibilityPattern {
    public static void main(String[] args) {
        ConcreteHandler1 handler1 = new ConcreteHandler1();
        ConcreteHandler2 handler2 = new ConcreteHandler2();
        handler1.setNext(handler2);
        //提交请求
        handler1.handlerRequest("three");
    }
}
//抽象处理者角色
abstract class Handler{
    private Handler next;

    public Handler getNext() {
        return next;
    }

    public void setNext(Handler next) {
        this.next = next;
    }
    // 处理请求的方法
    public abstract void handlerRequest(String request);
}
//具体的处理者角色
class ConcreteHandler1 extends Handler{
    @Override
    public void handlerRequest(String request) {
        if(request.equals("one")){
            System.out.println("具体处理者1,处理具体的业务....");
        }else {
            if(getNext() != null){
                System.out.println("处理传递，"+ getNext());
                getNext().handlerRequest(request);
            }else {
                System.out.println("没有人处理该请求...");
            }
        }
    }
}
//具体的处理者角色
class ConcreteHandler2 extends Handler{
    @Override
    public void handlerRequest(String request) {
        if(request.equals("two")){
            System.out.println("具体处理者2,处理具体的业务....");
        }else {
            if(getNext() != null){
                getNext().handlerRequest(request);
            }else {
                System.out.println("没有人处理该请求...");
            }
        }
    }
}