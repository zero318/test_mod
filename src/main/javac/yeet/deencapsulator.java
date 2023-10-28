package yeet;

#include "util.h"

import java.lang.Class;
import java.lang.ClassLoader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

#define TryToBeSafer 1

public class Deencapsulator {
	
	public static final SUN_UNSAFE_TYPE SUN_UNSAFE;
	
#if !TryToBeSafer
	private static final MethodHandle CLASS_MODULE;
    private static final MethodHandle CLASS_LOADER_MODULE;
#endif
    private static final MethodHandle METHOD_MODIFIERS;
	
	public static void open(String class_name) {
        try {
			Class classBase = Class.forName(class_name);
            Method export = Module.class.getDeclaredMethod("implAddOpens", String.class);
            setMethodModifiers(export, Modifier.PUBLIC);
            HashSet<Module> modules = new HashSet<>();
#if TryToBeSafer
			Module base = classBase.getModule();
#else
            Module base = getClassModule(classBase);
#endif
            if (base.getLayer() != null) {
                modules.addAll(base.getLayer().modules());
			}
            modules.addAll(ModuleLayer.boot().modules());
            for (ClassLoader cl = classBase.getClassLoader(); cl != null; cl = cl.getParent()) {
#if TryToBeSafer
				modules.add(cl.getUnnamedModule());
#else
                modules.add(getLoaderModule(cl));
#endif
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

#if !TryToBeSafer
    private static Module getClassModule(Class<?> klass) {
        try {
            return (Module) CLASS_MODULE.invokeExact(klass);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    private static Module getLoaderModule(ClassLoader loader) {
        try {
            return (Module) CLASS_LOADER_MODULE.invokeExact(loader);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }
#endif

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
			final Field sun_misc_unsafe = SUN_UNSAFE_TYPE.class.getDeclaredField("theUnsafe");
            sun_misc_unsafe.setAccessible(true);
            SUN_UNSAFE = (SUN_UNSAFE_TYPE)sun_misc_unsafe.get(null);
			
			Field field = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
            MethodHandles.Lookup lookup = (MethodHandles.Lookup)SUN_UNSAFE.getObject(SUN_UNSAFE.staticFieldBase(field), SUN_UNSAFE.staticFieldOffset(field));
            MethodType type = MethodType.methodType(Module.class);
#if !TryToBeSafer
            CLASS_MODULE = lookup.findVirtual(Class.class, "getModule", type);
            CLASS_LOADER_MODULE = lookup.findVirtual(ClassLoader.class, "getUnnamedModule", type);
#endif
            METHOD_MODIFIERS = lookup.findSetter(Method.class, "modifiers", Integer.TYPE);
			
			open(MACRO_STR(JDK_UNSAFE_TYPE));
		} catch (IllegalAccessException | NoSuchFieldException /* | NoSuchMethodException */ e) {
            throw new RuntimeException("", e);
        }
	}
}