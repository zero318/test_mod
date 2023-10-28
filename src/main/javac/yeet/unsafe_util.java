package yeet;

#include "util.h"

#define BreakEncapsulation 1
#define AbuseWhiteBox 0

import yeet.Deencapsulator;

#if AbuseWhiteBox
import sun.hotspot.WhiteBox;
import jdk.test.whitebox.WhiteBox;
#endif

import java.lang.reflect.Field;
import java.lang.reflect.Method;

#define UseInternalUnsafe 1

#if !UseBothUnsafes
#if UseInternalUnsafe
import jdk.internal.misc.Unsafe;
#else
import sun.misc.Unsafe;
#endif
#endif
//import jdk.incubator.vector;



public class UnsafeUtil {
	
#if !UseBothUnsafes
	public static final Unsafe UNSAFE;
#else
	public static final SUN_UNSAFE_TYPE SUN_UNSAFE;
	public static final JDK_UNSAFE_TYPE UNSAFE;
#endif
	
	
#if AbuseWhiteBox
	public static final WhiteBox WHITEBOX;
#endif

	//public static void unencapsulate_jvmci() {
		//try_ignore(
			//deencapsulate(Class.forName("jdk.vm.ci.runtime.JVMCI"));
		//);
	//}
	
	static {
#if !UseBothUnsafes
		try {
#if UseInternalUnsafe
			deencapsulate(Unsafe.class);
			UNSAFE = Unsafe.getUnsafe();
#else
			final Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe)theUnsafe.get(null);
#endif
			
#if AbuseWhiteBox
			final Field whitebox_instance = WhiteBox.class.getDeclaredField("instance");
			whitebox_instance.setAccessible(true);
			WHITEBOX = (WhiteBox)whitebox_instance.get();
#endif
		} catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("", e);
        }
#else
		//final Field theUnsafe = JDK_UNSAFE_TYPE.class.getDeclaredField("theUnsafe");
        //theUnsafe.setAccessible(true);
        //UNSAFE = (JDK_UNSAFE_TYPE)theUnsafe.get(null);
		SUN_UNSAFE = Deencapsulator.SUN_UNSAFE;
		UNSAFE = JDK_UNSAFE_TYPE.getUnsafe();
#endif
		
		System.out.printf("SUN Unsafe Methods:\n");
		for (Method method : SUN_UNSAFE_TYPE.class.getDeclaredMethods()) {
			System.out.println(method.toGenericString());
		}
		System.out.printf("JDK Unsafe Methods:\n");
		for (Method method : JDK_UNSAFE_TYPE.class.getDeclaredMethods()) {
			System.out.println(method.toGenericString());
		}
	}
}