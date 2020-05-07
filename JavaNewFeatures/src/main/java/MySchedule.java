import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;

@Repeatable(MySchedules.class)
//@Retention(Ru)
public @interface MySchedule {
    int dayOfWeek() default 1; //周几？

    int hour() default 0;   //几点？

}

@interface MySchedules {
    MySchedule[] value();

    //boolean dayOfWeek();
}