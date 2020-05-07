public class ScheduleService {

    int aa;
    @MySchedule(dayOfWeek = 3,hour = 12)
    @MySchedule(dayOfWeek = 4,hour = 13)
    public ScheduleService(){

    }

    @MySchedule(dayOfWeek = 3,hour = 12)
    @MySchedule(dayOfWeek = 4,hour = 13)
    public void start(){
        System.out.println("定时运行任务！");
    }

}
