import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ScheduleService {

    int aa;
    public ScheduleService(){

    }

    @Schedule(dayOfWeek = 3,hour = 12)
    @Schedule(dayOfWeek = 4,hour = 13)
    public void start(){
        System.out.println("定时运行任务！");
    }

}
