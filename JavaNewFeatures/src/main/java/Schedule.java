import java.lang.annotation.Repeatable;

@Repeatable(value = Schedule.Schedules.class)
public @interface Schedule {
    int dayOfWeek() default 1; //周几？

    int hour() default 0;   //几点？

    @interface Schedules {
        Schedule[] value();

        //boolean dayOfWeek();
    }
}
