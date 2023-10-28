package yeet;

import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
public class Deencapsulator {
 public static final sun.misc.Unsafe SUN_UNSAFE;
    private static final MethodHandle METHOD_MODIFIERS;
 public static void open(String class_name) {
        try {
   Class classBase = Class.forName(class_name);
            Method export = Module.class.getDeclaredMethod("implAddOpens", String.class);
            setMethodModifiers(export, Modifier.PUBLIC);
            HashSet<Module> modules = new HashSet<>();
   Module base = classBase.getModule();
            if (base.getLayer() != null) {
                modules.addAll(base.getLayer().modules());
   }
            modules.addAll(ModuleLayer.boot().modules());
            for (ClassLoader cl = classBase.getClassLoader(); cl != null; cl = cl.getParent()) {
    modules.add(cl.getUnnamedModule());
            }
            for (Module module : modules) {
                for (String name : module.getPackages()) {
                    try {
                        export.invoke(module, name);
                    } catch (Exception e) {
                        throw new AssertionError(e);
                    }
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not export packages", e);
        }
    }
    private static void setMethodModifiers(Method method, int modifiers) {
        try {
            METHOD_MODIFIERS.invokeExact(method, modifiers);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }
 public static void init() {}
 static {
  try {
   final Field sun_misc_unsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            sun_misc_unsafe.setAccessible(true);
            SUN_UNSAFE = (sun.misc.Unsafe)sun_misc_unsafe.get(null);
   Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            MethodHandles.Lookup lookup = (MethodHandles.Lookup)SUN_UNSAFE.getObject(SUN_UNSAFE.staticFieldBase(field), SUN_UNSAFE.staticFieldOffset(field));
            MethodType type = MethodType.methodType(Module.class);
            METHOD_MODIFIERS = lookup.findSetter(Method.class, "modifiers", Integer.TYPE);
   open("jdk.internal.misc.Unsafe");
  } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("", e);
        }
 }
}
