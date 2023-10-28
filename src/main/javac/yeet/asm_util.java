package yeet;

#include "util.h"

import yeet.Deencapsulator;
import yeet.UnsafeUtil;
import yeet.YeetLinker;
import yeet.Expression;
import yeet.TestStruct;

import one.nalim.Code;
import one.nalim.Link;
import one.nalim.Linker;

public class JankyHackMate {
	
#define infinite_loop_asm	"EBFE"
	
	/*
	@Link
	public static native intptr_t malloc(size_t size);
	@Link(name = "malloc")
	public static native intptr_t malloc(size_t size);
	@Link(name = "calloc")
	public static native intptr_t calloc(size_t count, size_t size);
	@Link(name = "realloc")
	public static native intptr_t realloc(intptr_t addr, size_t size);
	@Link(name = "free")
	public static native void free(intptr_t addr);
	@Link(name = "memcpy")
	public static native intptr_t memcpy(intptr_t dst, intptr_t src, size_t size);
	@Link(name = "memmove")
	public static native intptr_t memmove(intptr_t dst, intptr_t src, size_t size);
	@Link(name = "memset")
	public static native intptr_t memset(intptr_t dst, int val, size_t size);
	@Link(name = "memchr")
	public static native intptr_t memchr(intptr_t addr, int find, size_t size);
	@Link(name = "memcmp")
	public static native int memcmp(intptr_t left, intptr_t right, size_t size);
	*/
		
	@Code(
		"CC"+	//	INT3
		"C3"	//	RETN
	)
	public static native void breakpoint();
	
	@Code(
							// loop:
		infinite_loop_asm+	//	JMP loop
		"C3"				//	RETN
	)
	public static native void infinite_spin_loop();
	
	public static void infinite_spin_loop_log() {
		System.out.println("Entering infinite spin loop...");
		infinite_spin_loop();
	}
	
#define RAX_INDEX 0x0
#define RCX_INDEX 0x1
#define RDX_INDEX 0x2
#define RBX_INDEX 0x3
#define RSP_INDEX 0x4
#define RBP_INDEX 0x5
#define RSI_INDEX 0x6
#define RDI_INDEX 0x7
#define R8_INDEX 0x8
#define R9_INDEX 0x9
#define R10_INDEX 0xA
#define R11_INDEX 0xB
#define R12_INDEX 0xC
#define R13_INDEX 0xD
#define R14_INDEX 0xE
#define R15_INDEX 0xF
#define STACK_INDEX 0x10
	
#define test_cc_args(number) private static native intptr_t MACRO_CAT(test_cc_arg_,number)(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15)

	@Code("C3")			test_cc_args(RAX_INDEX);
	@Code("4889C8C3")	test_cc_args(RCX_INDEX);
	@Code("4889D0C3")	test_cc_args(RDX_INDEX);
	@Code("4889D8C3")	test_cc_args(RBX_INDEX);
	@Code("4889E0C3")	test_cc_args(RSP_INDEX);
	@Code("4889E8C3")	test_cc_args(RBP_INDEX);
	@Code("4889F0C3")	test_cc_args(RSI_INDEX);
	@Code("4889F8C3")	test_cc_args(RDI_INDEX);
	@Code("4C89C0C3")	test_cc_args(R8_INDEX);
	@Code("4C89C8C3")	test_cc_args(R9_INDEX);
	@Code("4C89D0C3")	test_cc_args(R10_INDEX);
	@Code("4C89D8C3")	test_cc_args(R11_INDEX);
	@Code("4C89E0C3")	test_cc_args(R12_INDEX);
	@Code("4C89E8C3")	test_cc_args(R13_INDEX);
	@Code("4C89F0C3")	test_cc_args(R14_INDEX);
	@Code("4C89F8C3")	test_cc_args(R15_INDEX);
	
	@Code("488B0424C3") test_cc_args(stack_top);
	@Code("488B442408C3") test_cc_args(stack_arg_1);
	
#define ARG_0	0x0000000000000000L
#define ARG_1	0x1111111111111111L
#define ARG_2	0x2222222222222222L
#define ARG_3	0x3333333333333333L
#define ARG_4	0x4444444444444444L
#define ARG_5	0x5555555555555555L
#define ARG_6	0x6666666666666666L
#define ARG_7	0x7777777777777777L
#define ARG_8	0x8888888888888888L
#define ARG_9	0x9999999999999999L
#define ARG_A	0xAAAAAAAAAAAAAAAAL
#define ARG_B	0xBBBBBBBBBBBBBBBBL
#define ARG_C	0xCCCCCCCCCCCCCCCCL
#define ARG_D	0xDDDDDDDDDDDDDDDDL
#define ARG_E	0xEEEEEEEEEEEEEEEEL
#define ARG_F	0xFFFFFFFFFFFFFFFFL
	
#define run_cc_arg_test(number) MACRO_CAT(test_cc_arg_,number)(ARG_0,ARG_1,ARG_2,ARG_3,ARG_4,ARG_5,ARG_6,ARG_7,ARG_8,ARG_9,ARG_A,ARG_B,ARG_C,ARG_D,ARG_E,ARG_F)

#define get_arg_index(output_array, index, ...) \
{ \
	long temp = run_cc_arg_test(MACRO_DEFAULT_ARG_RAW(index, __VA_ARGS__)); \
	if 		(temp == ARG_0)	output_array[index] = 0x0; \
	else if	(temp == ARG_1)	output_array[index] = 0x1; \
	else if	(temp == ARG_2)	output_array[index] = 0x2; \
	else if	(temp == ARG_3)	output_array[index] = 0x3; \
	else if	(temp == ARG_4)	output_array[index] = 0x4; \
	else if	(temp == ARG_5)	output_array[index] = 0x5; \
	else if	(temp == ARG_6)	output_array[index] = 0x6; \
	else if	(temp == ARG_7)	output_array[index] = 0x7; \
	else if	(temp == ARG_8)	output_array[index] = 0x8; \
	else if	(temp == ARG_9)	output_array[index] = 0x9; \
	else if	(temp == ARG_A)	output_array[index] = 0xA; \
	else if	(temp == ARG_B)	output_array[index] = 0xB; \
	else if	(temp == ARG_C)	output_array[index] = 0xC; \
	else if	(temp == ARG_D)	output_array[index] = 0xD; \
	else if	(temp == ARG_E)	output_array[index] = 0xE; \
	else if	(temp == ARG_F)	output_array[index] = 0xF; \
	else 					output_array[index] = 0x10; \
}
/* switch (run_cc_arg_test(index)) { \
	case ARG_0 -> 0x0; case ARG_1 -> 0x1; case ARG_2 -> 0x2; case ARG_3 -> 0x3; \
	case ARG_4 -> 0x4; case ARG_5 -> 0x5; case ARG_6 -> 0x6; case ARG_7 -> 0x7; \
	case ARG_8 -> 0x8; case ARG_9 -> 0x9; case ARG_A -> 0xA; case ARG_B -> 0xB; \
	case ARG_C -> 0xC; case ARG_D -> 0xD; case ARG_E -> 0xE; case ARG_F -> 0xF; \
	default -> 0x10; \
} */
	
	private static final String[] reg_names = {
		"RAX", "RCX", "RDX", "RBX", "RSP", "RBP", "RSI", "RDI", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "StackArg1", "invalid"
	};
	private static final String[] arg_names = {
		"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "invalid"
	};
	
	private static final void validate_cc_args() {
		int reg_args[] = new int[]{0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10};
		get_arg_index(reg_args, RAX_INDEX);
		get_arg_index(reg_args, RCX_INDEX);
		get_arg_index(reg_args, RDX_INDEX);
		get_arg_index(reg_args, RBX_INDEX);
		get_arg_index(reg_args, RSP_INDEX);
		get_arg_index(reg_args, RBP_INDEX);
		get_arg_index(reg_args, RSI_INDEX);
		get_arg_index(reg_args, RDI_INDEX);
		get_arg_index(reg_args, R8_INDEX);
		get_arg_index(reg_args, R9_INDEX);
		get_arg_index(reg_args, R10_INDEX);
		get_arg_index(reg_args, R11_INDEX);
		get_arg_index(reg_args, R12_INDEX);
		get_arg_index(reg_args, R13_INDEX);
		get_arg_index(reg_args, R14_INDEX);
		get_arg_index(reg_args, R15_INDEX);
		get_arg_index(reg_args, STACK_INDEX, stack_arg_1);
		for (int i = 0; i <= STACK_INDEX; ++i) {
			System.out.println(reg_names[i] + " = " + arg_names[reg_args[i]]);
		}
	}
	
	//
	// Java Calling Convention
	//
	// Scratch Regs:
	// RAX, RCX, RDX, RBX, RSI, RDI, R8, R9, R10, R11, R13, R14
	// 
	// Argument Passing Regs:
	// RDI, RSI, RDX, RCX, R8, R9, R12
	// XMM0-XMM7
	// 
	// Callee-Save Regs:
	// RBP
	// 
	// Caller-Save Regs:
	// XMM Regs
	// 
	// Callee-And-Caller-Save Regs: (Why tho?)
	// 
	// 
	// Special Regs:
	// R12: Heap Base
	// R15: JavaThread
	//
	// Windows Conventions:
	// JVM Int Arguments:		RDX, R8,  R9,  RDI, RSI, RCX, Stack
	// Native Int Arguments:	RCX, RDX, R8,  R9,  Stack
	// JVM Float Arguments:		
	// Native Float Arguments:	XMM0, XMM1, XMM2, XMM3
	//
	// Linux Conventions:
	// JVM Int Arguments:		RSI, RDX, RCX, R8,  R9,  RDI, Stack
	// Native Int Arguments:	RDI, RSI, RDX, RCX, R8,  R9,  Stack
	// JVM Float Arguments:		
	// Native Float Arguments:	XMM0, XMM1, XMM2, XMM3, XMM4, XMM5, XMM6, XMM7
	//
	
	private static final array_ptr(int[4]) cpuid_output_buf;
	
#define use_push_pop 0
	
	@Code(
#if use_push_pop
		"53"+			//	PUSH RBX
#else
		"4989D9"+		//	MOV R9, RBX
#endif
		"89D0"+			//	MOV EAX, EDX
		"0FA2"+			//	CPUID
		"418900"+		//	MOV DWORD PTR [R8], EAX
		"41895804"+		//	MOV DWORD PTR [R8+0x4], EBX
		"41894808"+ 	//	MOV DWORD PTR [R8+0x8], ECX
		"4189500C"+ 	//	MOV DWORD PTR [R8+0xC], EDX
#if use_push_pop
		"5B"+			//	POP RBX
#else
		"4C89CB"+		//	MOV RBX, R9
#endif
		"C3"			//	RETN
	)
	private static native void cpuid_raw_win(int page_num, ptr(int) output);
	
	@Code(
#if use_push_pop
		"53"+			//	PUSH RBX
#endif
		"4889D7"+		//	MOV RDI, RDX
		"89F0"+			//	MOV EAX, ESI
#if !use_push_pop
		"4889DE"+		//	MOV RSI, RBX
#endif
		"0FA2"+			//	CPUID
		"8907"+			//	MOV DWORD PTR [RDI], EAX
		"895F04"+		//	MOV DWORD PTR [RDI+0x4], EBX
		"894F08"+		//	MOV DWORD PTR [RDI+0x8], ECX
		"89570C"+		//	MOV DWORD PTR [RDI+0xC], EDX
#if use_push_pop
		"5B"+			//	POP RBX
#else
		"4889F3"+		//	MOV RBX, RSI
#endif
		"C3"			//	RETN
	)
	private static native void cpuid_raw_lin(int page_num, array_ptr(int[4]) output);
	
	private static void cpuid_raw(int page_num, array_ptr(int[4]) output) {
		if (is_windows) {
			cpuid_raw_win(page_num, output);
		} else {
			cpuid_raw_lin(page_num, output);
		}
	}
	
	/* @CodeString(
		windows =
		"4989D9"+		//	MOV R9, RBX
		"89D0"+			//	MOV EAX, EDX
		"0FA2"+			//	CPUID
		"418900"+		//	MOV DWORD PTR [R8], EAX
		"41895804"+		//	MOV DWORD PTR [R8+0x4], EBX
		"41894808"+ 	//	MOV DWORD PTR [R8+0x8], ECX
		"4189500C"+ 	//	MOV DWORD PTR [R8+0xC], EDX
		"4C89CB"+		//	MOV RBX, R9
		"C3",			//	RETN
		linux =
		""
	)
	@CodeStringRaw(
		windows = {
			(byte)0x49,(byte)0x89,(byte)0xD9,
			(byte)0x89,(byte)0xD0,
			(byte)0x0F,(byte)0xA2,
			(byte)0x41,(byte)0x89,(byte)0x00,
			(byte)0x41,(byte)0x89,(byte)0x58,(byte)0x04,
			(byte)0x41,(byte)0x89,(byte)0x48,(byte)0x08,
			(byte)0x41,(byte)0x89,(byte)0x50,(byte)0x0C,
			(byte)0x4C,(byte)0x89,(byte)0xCB,
			(byte)0xC3
		}
	)
	public static native void cpuid_raw(int page_num, int output[]); */
	
	public static int[] cpuid(int page_num, int output[]) {
		cpuid_raw(page_num, cpuid_output_buf);
		output[0] = read_array_ptr(cpuid_output_buf,int,0);
		output[1] = read_array_ptr(cpuid_output_buf,int,1);
		output[2] = read_array_ptr(cpuid_output_buf,int,2);
		output[3] = read_array_ptr(cpuid_output_buf,int,3);
		return output;
	}
	
	public static int[] cpuid(int page_num) {
		return cpuid(page_num, new int[4]);
	}
	
	@Code(
#if use_push_pop
		"53"+			//	PUSH RBX
#endif
		"4489C1"+		//	MOV ECX, R8D
#if !use_push_pop
		"4989D8"+		//	MOV R8, RBX
#endif
		"89D0"+			//	MOV EAX, EDX
		"0FA2"+			//	CPUID
		"418901"+		//	MOV DWORD PTR [R9], EAX
		"41895904"+		//	MOV DWORD PTR [R9+0x4], EBX
		"41894908"+		//	MOV DWORD PTR [R9+0x8], ECX
		"4189510C"+		//	MOV DWORD PTR [R9+0xC], EDX
#if use_push_pop
		"5B"+			//	POP RBX
#else
		"4C89C3"+		//	MOV RBX, R8
#endif
		"C3"			//	RETN
	)
	private static native void cpuid_ex_raw_win(int page_num, int subpage_num, array_ptr(int[4]) output);
	
	@Code(
#if use_push_pop
		"53"+			//	PUSH RBX
#endif
		"4889CF"+		//	MOV RDI, RCX
		"89D1"+			//	MOV ECX, EDX
		"89F0"+			//	MOV EAX, ESI
#if !use_push_pop
		"4889DE"+		//	MOV RSI, RBX
#endif
		"0FA2"+			//	CPUID
		"8907"+			//	MOV DWORD PTR [RDI], EAX
		"895F04"+		//	MOV DWORD PTR [RDI+0x4], EBX
		"894F08"+		//	MOV DWORD PTR [RDI+0x8], ECX
		"89570C"+		//	MOV DWORD PTR [RDI+0xC], EDX
#if use_push_pop
		"5B"+			//	POP RBX
#else
		"4889F3"+		//	MOV RBX, RSI
#endif
		"C3"			//	RETN
	)
	private static native void cpuid_ex_raw_lin(int page_num, int subpage_num, array_ptr(int[4]) output);
	
	private static void cpuid_ex_raw(int page_num, int subpage_num, array_ptr(int[4]) output) {
		if (is_windows) {
			cpuid_ex_raw_win(page_num, subpage_num, output);
		} else {
			cpuid_ex_raw_lin(page_num, subpage_num, output);
		}
	}
	
	/* @CodeString(
		windows =
		"4489C1"+		//	MOV ECX, R8D
		"4989D8"+		//	MOV R8, RBX
		"89D0"+			//	MOV EAX, EDX
		"0FA2"+			//	CPUID
		"418901"+		//	MOV DWORD PTR [R9], EAX
		"41895904"+		//	MOV DWORD PTR [R9+0x4], EBX
		"41894908"+		//	MOV DWORD PTR [R9+0x8], ECX
		"4189510C"+		//	MOV DWORD PTR [R9+0xC], EDX
		"4C89C3"+		//	MOV RBX, R8
		"C3",			//	RETN
		linux =
		""
	)
	@CodeStringRaw(
		windows = {
			(byte)0x44,(byte)0x89,(byte)0xC1,
			(byte)0x44,(byte)0x89,(byte)0xD8,
			(byte)0x89,(byte)0xD0,
			(byte)0x0F,(byte)0xA2,
			(byte)0x41,(byte)0x89,(byte)0x01,
			(byte)0x41,(byte)0x89,(byte)0x59,(byte)0x04,
			(byte)0x41,(byte)0x89,(byte)0x49,(byte)0x08,
			(byte)0x41,(byte)0x89,(byte)0x51,(byte)0x0C,
			(byte)0x4C,(byte)0x89,(byte)0xC3,
			(byte)0xC3
		}
	)
	public static native void cpuid_ex_raw(int page_num, int subpage_num, int output[]); */
	
	public static int[] cpuid_ex(int page_num, int subpage_num, int output[]) {
		cpuid_ex_raw(page_num, subpage_num, cpuid_output_buf);
		output[0] = read_array_ptr(cpuid_output_buf,int,0);
		output[1] = read_array_ptr(cpuid_output_buf,int,1);
		output[2] = read_array_ptr(cpuid_output_buf,int,2);
		output[3] = read_array_ptr(cpuid_output_buf,int,3);
		return output;
	}
	
	public static int[] cpuid_ex(int page_num, int subpage_num) {
		int[] ret = new int[4];
		cpuid_ex(page_num, subpage_num, ret);
		return ret;
	}
	
	@Code(
		"4901D0"+		//	ADD R8, RDX
		"4901F9"+		//	ADD R9, RDI
		"4531D2"+		//	XOR R10D, R10D
						// loop:
		"430FB63C02"+	//	MOVZX EDI, BYTE PTR [R8+R10]
		"8D4FBF"+		//	LEA ECX, [RDI-0x41]
		"8D4720"+		//	LEA EAX, [RDI+0x20]
		"83F91A"+		//	CMP ECX, 0x1A
		"0F43C7"+		//	CMOVAE EAX, EDI
		"430FB60C0A"+	//	MOVZX ECX, BYTE PTR [R9+R10]
		"8D51BF"+		//	LEA EDX, [RCX-0x41]
		"8D7920"+		//	LEA EDI, [RCX+0x20]
		"83FA1A"+		//	CMP EDX, 0x1A
		"0F43F9"+		//	CMOVAE EDI, ECX
		"2BC7"+			//	SUB EAX, EDI
		"7508"+			//	JNE match_fail
		"49FFC2"+		//	INC R10
		"493BF2"+		//	CMP RSI, R10
		"75D2"+			//	JNE loop
						// match_fail:
		"C3"			//	RETN
	)
	public static native int strnicmp_win(long offsetA, byte strA[], byte strB[], long offsetB, long count);
	
	@Code(
		"4901C8"+		//	ADD R8, RCX
		"4801F2"+		//	ADD RDX, RSI
		"4531D2"+		//	XOR R10D, R10D
						// loop:
		"410FB63C12"+	//	MOVZX EDI, BYTE PTR [RDX+R10]
		"8D4FBF"+		//	LEA ECX, [RDI-0x41]
		"8D4720"+		//	LEA EAX, [RDI+0x20]
		"83F91A"+		//	CMP ECX, 0x1A
		"0F43C7"+		//	CMOVAE EAX, EDI
		"430FB60C02"+	//	MOVZX ECX, BYTE PTR [R8+R10]
		"8D71BF"+		//	LEA ESI, [RCX-0x41]
		"8D7920"+		//	LEA EDI, [RCX+0x20]
		"83FE1A"+		//	CMP ESI, 0x1A
		"0F43F9"+		//	CMOVAE EDI, ECX
		"29F8"+			//	SUB EAX, EDI
		"7508"+			//	JNE match_fail
		"49FFC2"+		//	INC R10
		"4D39D1"+		//	CMP R9, R10
		"75D2"+			//	JNE loop
						// match_fail:
		"C3"			//	RETN
	)
	public static native int strnicmp_lin(long offsetA, byte strA[], byte strB[], long offsetB, long count);
	
	/* @Code(
		"4C8D0C32"+		//	LEA R9, [RSI+RDX]
		"4531D2"+		//	XOR R10D, R10D
						// loop:
		"430FB63C0A"+	//	MOVZX EDI, BYTE PTR [R9+R10]
		"8D5741"+		//	LEA EDX, [RDI-0x41]
		"8D4720"+		//	LEA EAX, [RDI+0x20]
		"83FA1A"+		//	CMP EDX, 0x1A
		"0F43C7"+		//	CMOVAE EAX, EDI
		"410FB6140A"+	//	MOVZX EDX, BYTE PTR [RCX+R10]
		"8D72BF"+		//	LEA ESI, [RDX-0x41]
		"8D7A20"+		//	LEA EDI, [RDX+0x20]
		"83FE1A"+		//	CMP ESI, 0x1A
		"0F43FA"+		//	CMOVAE EDI, EDX
		"29F8"+			//	SUB EAX, EDI
		"7508"+			//	JNE match_fail
		"49FFC2"+		//	INC R10
		"4D39D0"+		//	CMP R8, R10
		"75D2"+			//	JNE loop
						// match_fail:
		"C3"			//	RETN
	)
	public static native int strnicmp_lin(long offsetA, ptr(byte) strA, ptr(byte) strB, long count); */
	
	public static int strnicmp(long offsetA, byte strA[], byte strB[], long offsetB, long count) {
		offsetA += UnsafeUtil.UNSAFE.ARRAY_BYTE_BASE_OFFSET;
		offsetB += UnsafeUtil.UNSAFE.ARRAY_BYTE_BASE_OFFSET;
		return is_windows ? strnicmp_win(offsetA, strA, strB, offsetB, count) : strnicmp_lin(offsetA, strA, strB, offsetB, count);
	}
	
	/* @CodeString(
		windows =
		"49:01D0"+		//	ADD R8, RDX
		"45:31D2"+		//	XOR R10D, R10D
						// loop:
		"43:0FB63402"+	//	MOVZX ESI, BYTE PTR [R8+R10]
		"8D4E BF"+		//	LEA ECX, [RSI-0x41]
		"8D46 20"+		//	LEA EAX, [RSI+0x20]
		"83F9 1A"+		//	CMP ECX, 0x1A
		"0F43C6"+		//	CMOVAE EAX, ESI
		"43:0FB60C0A"+	//	MOVZX ECX, BYTE PTR [R9+R10]
		"8D51 BF"+		//	LEA EDX, [RCX-0x41]
		"8D71 20"+		//	LEA ESI, [RCX+0x20]
		"83FA 1A"+		//	CMP EDX, 0x1A
		"0F43F1"+		//	CMOVAE ESI, ECX
		"29F0"+			//	SUB EAX, ESI
		"75 08"+		//	JNE match_fail
		"49:FFC2"+		//	INC R10
		"4C:39D7"+		//	CMP RDI, R10
		"75 D2"+		//	JNE loop
						// match_fail:
		"C3",			//	RETN
		linux =
		"4C:8D0C32"+	//	LEA R9, [RSI+RDX]
		"45:31D2"+		//	XOR R10D, R10D
						// loop:
		"43:0FB63C0A"+	//	MOVZX EDI, BYTE PTR [R9+R10]
		"8D57 41"+		//	LEA EDX, [RDI-0x41]
		"8D47 20"+		//	LEA EAX, [RDI+0x20]
		"83FA 1A"+		//	CMP EDX, 0x1A
		"0F43C7"+		//	CMOVAE EAX, EDI
		"41:0FB6140A"+	//	MOVZX EDX, BYTE PTR [RCX+R10]
		"8D72 BF"+		//	LEA ESI, [RDX-0x41]
		"8D7A 20"+		//	LEA EDI, [RDX+0x20]
		"83FE 1A"+		//	CMP ESI, 0x1A
		"0F43FA"+		//	CMOVAE EDI, EDX
		"29F8"+			//	SUB EAX, EDI
		"75 08"+		//	JNE match_fail
		"49:FFC2"+		//	INC R10
		"4D:39D0"+		//	CMP R8, R10
		"75 D2"+		//	JNE loop
						// match_fail:
		"C3"			//	RETN
	)
	@CodeStringRaw(
		windows = {
			(byte)0x49,(byte)0x01,(byte)0xD0,
			(byte)0x45,(byte)0x31,(byte)0xD2,
			(byte)0x43,(byte)0x0F,(byte)0xB6,(byte)0x34,(byte)0x02,
			(byte)0x8D,(byte)0x4E,(byte)0xBF,
			(byte)0x8D,(byte)0x46,(byte)0x20,
			(byte)0x83,(byte)0xF9,(byte)0x1A,
			(byte)0x0F,(byte)0x43,(byte)0xC6,
			(byte)0x43,(byte)0x0F,(byte)0xB6,(byte)0x0C,(byte)0x0A,
			(byte)0x8D,(byte)0x51,(byte)0xBF,
			(byte)0x8D,(byte)0x71,(byte)0x20,
			(byte)0x83,(byte)0xFA,(byte)0x1A,
			(byte)0x0F,(byte)0x43,(byte)0xF1,
			(byte)0x29,(byte)0xF0,
			(byte)0x75,(byte)0x08,
			(byte)0x49,(byte)0xFF,(byte)0xC2,
			(byte)0x4C,(byte)0x39,(byte)0xD7,
			(byte)0x75,(byte)0xD2,
			(byte)0xC3
		}
	)
	public static native int strnicmp(long offsetA, byte strA[], byte strB[], long count); */
	
	//@SuppressWarnings("try")
	
	public static boolean is_initialized = false;
	
	private static final boolean is_windows = System.getProperty("os.name").toLowerCase().contains("windows");
	
	public static final void init_funcs() {
		if (!is_initialized) {
			is_initialized = true;
			//try {
				Deencapsulator.open("jdk.vm.ci.runtime.JVMCI");
				YeetLinker.linkClass(JankyHackMate.class);
				Expression.init_cpu_features();
				
				/*
				{
					String code_string_raw = 
					CodeStringVar code_string = new CodeStringVar(code_string_raw);
					System.out.printf("CodeString: " + code_string_raw + "%nCodeString Length: %d%n", code_string.calc_size());
				}
				*/
				///*
				{
					String code_string_raw = "<offsetof:yeet.TestStruct.field_A>";
					CodeStringVar code_string = new CodeStringVar(code_string_raw);
					long field_A_offset = Expression.get_patch_value(code_string, '>');
					System.out.printf("TestStruct.field_A Offset: %d\n", field_A_offset);
					TestStruct test = new TestStruct();
					test.field_A = 42069;
					System.out.printf("TestStruct.field_A Value: %d\n", UNSAFE_BASE.getInt(test, field_A_offset));
				}
				//*/
			//}
			//catch (Exception e) {
				//System.out.println("AVX: ded");
			//}
			
			//validate_cc_args();
			/* Linker.linkClass(JankyHackMate.x86_64.class);
			if (System.getProperty("os.name").toLowerCase().contains("windows")) {
				Linker.linkClass(JankyHackMate.x86_64.Win.class);
			} else {
				Linker.linkClass(JankyHackMate.x86_64.Linux.class);
			} */
		}
	}
	static {
		cpuid_output_buf = malloc_array(int, 4);
		init_funcs();
	}
}