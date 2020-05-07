import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NewFeaturesTest
{
    public static void main(String[] args) {
        try {
            try {
                Method method = ScheduleService.class.getMethod("start");
                for (Annotation annotation : method.getAnnotations()) {
                    System.out.println(annotation);
                }
                for (MySchedule s : method.getAnnotationsByType(MySchedule.class)) {
                    System.out.println(s.dayOfWeek() + "|" + s.hour());
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }






            ScheduleService service = new ScheduleService();

            Class<? extends ScheduleService> aClass = service.getClass();
            Field[] declaredFields = aClass.getDeclaredFields();
            Method[] methods = aClass.getMethods();
            for(Method method : methods){
                if (method.getName().equals("start")){
                    for(Annotation annotation : method.getAnnotations()){
                        System.out.println(annotation);
                    }
                }
            }
            Class al = ScheduleService.class;

            Method method = al.getMethod("start");
            for(Annotation annotation : method.getAnnotations()){
                System.out.println(annotation);
            }
            //Schedule[] annotationsByType = method.getAnnotationsByType(Schedule.class);
            for(MySchedule s : method.getAnnotationsByType(MySchedule.class)){
                System.out.println(s.dayOfWeek() + "->" + s.hour());
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
