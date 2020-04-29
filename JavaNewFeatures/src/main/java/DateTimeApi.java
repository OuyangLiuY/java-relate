import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

public class DateTimeApi {


    public static void main(String[] args) {
        //当前日期
        LocalDate date = LocalDate.now();
        //日期增加一天
        date = date.plusDays(1);
        //日期增加一个月
        date = date.plusMonths(1);
        //日期减少一天
        date = date.minusDays(1);
        //日期减少一个月
        date = date.minusMonths(1);
        //日期减少一年
        date = date.minusYears(1);
        System.out.println(date);

        //当期时间
        LocalTime time = LocalTime.now();
        //日期增减一分钟
        time = time.plusMinutes(1);
        //时间增加一小时
        time = time.plusHours(1);
        //时间增加一秒
        time = time.plusSeconds(1);
        //时间增加一纳秒
        time = time.plusNanos(100);
        System.out.println(time);

        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        System.out.println(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        ZonedDateTime shanghai  = ZonedDateTime.now(ZoneId.of("Asia/Shanghai"));
        ZonedDateTime usa = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
        System.out.println(shanghai);
        System.out.println(usa);

        //协调世界时间，又称世界标准时间
        Clock utc = Clock.systemUTC();
        Clock beijing = Clock.system(ZoneId.of("Asia/Tokyo"));
        System.out.println(LocalDateTime.now(utc));
        System.out.println(LocalDateTime.now(beijing));

        LocalDateTime from = LocalDateTime.parse("2020-04-29 18:50:50", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime to = LocalDateTime.parse("2020-05-29 18:50:50", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        Duration duration = Duration.between(from, to);
        System.out.println("Duration in days :" + duration.toDays());
        System.out.println("Duration in hours :" + duration.toHours());


    }
}
