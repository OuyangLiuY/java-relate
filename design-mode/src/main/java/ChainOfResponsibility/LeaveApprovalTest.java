package ChainOfResponsibility;

/**
 * 分析：假如规定学生请假小于或等于 2 天，班主任可以批准；
 * 小于或等于 7 天，系主任可以批准；
 * 小于或等于 10 天，院长可以批准；
 * 其他情况不予批准；这个实例适合使用职责链模式实现。
 */
public class LeaveApprovalTest {
    public static void main(String[] args) {
        ClassAdviser adviser = new ClassAdviser();
        HeaderTeacher teacher = new HeaderTeacher();
        Dean dean = new Dean();
        DeanOfStudies deanOfStudies = new DeanOfStudies();
        adviser.setNext(teacher);
        teacher.setNext(dean);
        dean.setNext(deanOfStudies);
        adviser.handlerRequest("张三",8);
    }
}
// 抽象领导类
abstract class Leader{
    private Leader next;

    public Leader getNext() {
        return next;
    }

    public void setNext(Leader next) {
        this.next = next;
    }
    public abstract void handlerRequest(String name ,int leaveDays);
}
// 班主任处理类
class ClassAdviser extends Leader{
    @Override
    public void handlerRequest(String name ,int leaveDays) {
        if(leaveDays <= 2){
            System.out.println("班主任批准"+name+"请假[" + leaveDays + "]天。");
        }else {
            if(getNext() != null){
                System.out.println("班主任无权限处理，请求传递中..." + name);
                getNext().handlerRequest(name,leaveDays);
            }else {
                System.out.println("请假天数太多，没有人能批准该假条！");
            }
        }
    }
}

// 系班主任处理类
class HeaderTeacher extends Leader{
    @Override
    public void handlerRequest(String name ,int leaveDays) {
        if(leaveDays <= 7){
            System.out.println("系班主任批准"+name+"请假[" + leaveDays + "]天。");
        }else {
            if(getNext() != null){
                System.out.println("系班主任无权限处理，请求传递中..." + name);
                getNext().handlerRequest(name,leaveDays);
            }else {
                System.out.println("请假天数太多，没有人能批准该假条！");
            }
        }
    }
}
// 院长处理类
class Dean extends Leader{
    @Override
    public void handlerRequest(String name ,int leaveDays) {
        if(leaveDays <= 10){
            System.out.println("院长批准"+name+"请假[" + leaveDays + "]天。");
        }else {
            if(getNext() != null){
                System.out.println("院长无权限处理，请求传递中..." + name);
                getNext().handlerRequest(name,leaveDays);
            }else {
                System.out.println("请假天数太多，没有人能批准该假条！");
            }
        }
    }
}
// 教务处长处理类
class DeanOfStudies extends Leader{
    @Override
    public void handlerRequest(String name ,int leaveDays) {
        if(leaveDays <= 20){
            System.out.println("教务处长批准"+name+"请假[" + leaveDays + "]天。");
        }else {
            if(getNext() != null){
                System.out.println("教务处长无权限处理，请求传递中..." + name);
                getNext().handlerRequest(name,leaveDays);
            }else {
                System.out.println("请假天数太多，没有人能批准该假条！");
            }
        }
    }
}
