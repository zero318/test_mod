package yeet;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CodeStringRaw {
    byte[] windows() default {(byte)0xCC};
 byte[] linux() default {(byte)0xCC};
}
