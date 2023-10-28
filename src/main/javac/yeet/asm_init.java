package yeet;

#include "util.h"

import yeet.UnsafeUtil;
import yeet.CodeString;

import java.lang.reflect.Method;

import one.nalim.Code;
import one.nalim.Link;
import one.nalim.Linker;

public class YeetLinker extends Linker {
	
	private static final boolean is_windows = System.getProperty("os.name").toLowerCase().contains("windows");
	
	public static void linkMethod(Method m) {
		//Linker.checkMethodType(m);

		CodeStringRaw raw_code = m.getAnnotation(CodeStringRaw.class);
		if (raw_code != null) {
			Linker.installCode(m, is_windows ? raw_code.windows() : raw_code.linux());
		}
		CodeString code = m.getAnnotation(CodeString.class);
		if (code != null) {
			//Linker.installCode(m, parseHex(code.value()));
			return;
		}

        Linker.linkMethod(m);
    }
	
#define validate_length(pos) if ((pos) >= this.length) break length_loop
//#define validate_next_pos() validate_length(++this.position)

	
};