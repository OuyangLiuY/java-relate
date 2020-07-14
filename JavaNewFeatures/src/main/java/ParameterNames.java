import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParameterNames
{
    @MySchedule()
    public void test(String str1 ,String str2){

    }
    static class Test{
         public static void main(String[] args) {
        try {
            /*Method method = ParameterNames.class.getMethod("collections",String.class,String.class);
            for (Parameter parameter : method.getParameters()) {
                System.out.println(parameter.getName());
            }
            System.out.println(method.getParameterCount());
            for (Annotation annotation : method.getAnnotations()) {
                System.out.println(annotation);
            }
            //Test[] annotationsByType = method.getAnnotationsByType(Test.Schedules.class);
            for (Schedule schedule : method.getAnnotationsByType(Schedule.class)) {
                System.out.println(schedule.dayOfWeek() + "->" + schedule.hour());
            }*/
            MySchedules mySchedule = ScheduleService.class.getAnnotation(MySchedules.class);
            System.out.println(mySchedule.value().length);


            Method[] methods = ScheduleService.class.getMethods();
            for (Method method : methods) {
               // System.out.println(method.getName());
                if(method.getName().equals("start")){
                    System.out.println(method.getName());
                   // method.get
                    MySchedule schedule = method.getDeclaredAnnotation(MySchedule.class);
                    for (Annotation annotation :method.getDeclaredAnnotations()) {
                        System.out.println(annotation);
                    }
                }
            }

            Method method = ScheduleService.class.getMethod("start");
            for(Annotation annotation : method.getAnnotations()){
                System.out.println(annotation);
            }
            //Schedule[] annotationsByType = method.getAnnotationsByType(Schedule.class);
           /* for(Annotation s : method.getAnnotations(Schedule.Schedules.class)){
                System.out.println(s.);
            }*/
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    }


    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        CompletableFuture result = CompletableFuture.supplyAsync(() -> {
            int sum=0;
            System.out.println("...");
            for (int i=0; i<100; i++) {
                sum = sum + i;
            }
            try {
                Thread.sleep(TimeUnit.SECONDS.toSeconds(3));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" 计算完毕");
            return sum;
        }, executor).thenApplyAsync(sum -> {
            System.out.println(Thread.currentThread().getName()+"打印"+sum);
            return sum;
        }, executor);
        System.out.println("...");
        try {
            System.out.println("result:" + result.get());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("...");
    }
}
