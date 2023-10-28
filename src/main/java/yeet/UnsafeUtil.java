package yeet;
import yeet.Deencapsulator;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
public class UnsafeUtil {
 public static final sun.misc.Unsafe SUN_UNSAFE;
 public static final jdk.internal.misc.Unsafe UNSAFE;
 static {
  SUN_UNSAFE = Deencapsulator.SUN_UNSAFE;
  UNSAFE = jdk.internal.misc.Unsafe.getUnsafe();
  System.out.printf("SUN Unsafe Methods:\n");
  for (Method method : sun.misc.Unsafe.class.getDeclaredMethods()) {
   System.out.println(method.toGenericString());
  }
  System.out.printf("JDK Unsafe Methods:\n");
  for (Method method : jdk.internal.misc.Unsafe.class.getDeclaredMethods()) {
   System.out.println(method.toGenericString());
  }
 }
}
