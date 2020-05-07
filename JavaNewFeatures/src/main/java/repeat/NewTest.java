package repeat;


import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;

public class NewTest {



    @Test
    public void testMethod(){
        @Hint("hint1")
        @Hint("hint2")
        class Person{

        }

        Hint hint = Person.class.getAnnotation(Hint.class);
        System.out.println(hint);
        Hints hints = Person.class.getAnnotation(Hints.class);
        System.out.println(hints);

        Hint[] byType = Person.class.getAnnotationsByType(Hint.class);
        System.out.println(byType.length);
    }

}
