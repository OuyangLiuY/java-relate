package repeat;


import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Repeatable(Hints.class)
//@Target({ElementType.TYPE_PARAMETER, ElementType.TYPE_USE})
public @interface Hint {
    String value();

}
@interface Hints{
    Hint[] value();
}
