package yeet;

import yeet.Deencapsulator;
import yeet.UnsafeUtil;
import yeet.YeetLinker;
import yeet.Expression;
import yeet.TestStruct;
import one.nalim.Code;
import one.nalim.Link;
import one.nalim.Linker;
public class JankyHackMate {
 @Code(
  "CC"+
  "C3"
 )
 public static native void breakpoint();
 @Code(
  "EBFE"+
  "C3"
 )
 public static native void infinite_spin_loop();
 public static void infinite_spin_loop_log() {
  System.out.println("Entering infinite spin loop...");
  infinite_spin_loop();
 }
 @Code("C3") private static native long test_cc_arg_0x0(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889C8C3") private static native long test_cc_arg_0x1(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889D0C3") private static native long test_cc_arg_0x2(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889D8C3") private static native long test_cc_arg_0x3(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889E0C3") private static native long test_cc_arg_0x4(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889E8C3") private static native long test_cc_arg_0x5(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889F0C3") private static native long test_cc_arg_0x6(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4889F8C3") private static native long test_cc_arg_0x7(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89C0C3") private static native long test_cc_arg_0x8(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89C8C3") private static native long test_cc_arg_0x9(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89D0C3") private static native long test_cc_arg_0xA(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89D8C3") private static native long test_cc_arg_0xB(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89E0C3") private static native long test_cc_arg_0xC(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89E8C3") private static native long test_cc_arg_0xD(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89F0C3") private static native long test_cc_arg_0xE(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("4C89F8C3") private static native long test_cc_arg_0xF(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("488B0424C3") private static native long test_cc_arg_stack_top(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 @Code("488B442408C3") private static native long test_cc_arg_stack_arg_1(long arg0, long arg1, long arg2, long arg3, long arg4, long arg5, long arg6, long arg7, long arg8, long arg9, long arg10, long arg11, long arg12, long arg13, long arg14, long arg15);
 private static final String[] reg_names = {
  "RAX", "RCX", "RDX", "RBX", "RSP", "RBP", "RSI", "RDI", "R8", "R9", "R10", "R11", "R12", "R13", "R14", "R15", "StackArg1", "invalid"
 };
 private static final String[] arg_names = {
  "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "invalid"
 };
 private static final void validate_cc_args() {
  int reg_args[] = new int[]{0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10,0x10};
  { long temp = test_cc_arg_0x0(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x0] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x0] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x0] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x0] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x0] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x0] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x0] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x0] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x0] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x0] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x0] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x0] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x0] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x0] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x0] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x0] = 0xF; else reg_args[0x0] = 0x10; };
  { long temp = test_cc_arg_0x1(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x1] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x1] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x1] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x1] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x1] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x1] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x1] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x1] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x1] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x1] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x1] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x1] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x1] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x1] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x1] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x1] = 0xF; else reg_args[0x1] = 0x10; };
  { long temp = test_cc_arg_0x2(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x2] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x2] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x2] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x2] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x2] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x2] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x2] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x2] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x2] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x2] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x2] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x2] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x2] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x2] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x2] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x2] = 0xF; else reg_args[0x2] = 0x10; };
  { long temp = test_cc_arg_0x3(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x3] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x3] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x3] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x3] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x3] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x3] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x3] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x3] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x3] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x3] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x3] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x3] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x3] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x3] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x3] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x3] = 0xF; else reg_args[0x3] = 0x10; };
  { long temp = test_cc_arg_0x4(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x4] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x4] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x4] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x4] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x4] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x4] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x4] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x4] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x4] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x4] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x4] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x4] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x4] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x4] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x4] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x4] = 0xF; else reg_args[0x4] = 0x10; };
  { long temp = test_cc_arg_0x5(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x5] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x5] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x5] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x5] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x5] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x5] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x5] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x5] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x5] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x5] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x5] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x5] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x5] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x5] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x5] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x5] = 0xF; else reg_args[0x5] = 0x10; };
  { long temp = test_cc_arg_0x6(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x6] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x6] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x6] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x6] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x6] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x6] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x6] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x6] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x6] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x6] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x6] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x6] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x6] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x6] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x6] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x6] = 0xF; else reg_args[0x6] = 0x10; };
  { long temp = test_cc_arg_0x7(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x7] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x7] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x7] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x7] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x7] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x7] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x7] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x7] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x7] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x7] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x7] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x7] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x7] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x7] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x7] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x7] = 0xF; else reg_args[0x7] = 0x10; };
  { long temp = test_cc_arg_0x8(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x8] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x8] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x8] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x8] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x8] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x8] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x8] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x8] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x8] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x8] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x8] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x8] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x8] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x8] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x8] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x8] = 0xF; else reg_args[0x8] = 0x10; };
  { long temp = test_cc_arg_0x9(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x9] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x9] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x9] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x9] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x9] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x9] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x9] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x9] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x9] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x9] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x9] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x9] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x9] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x9] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x9] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x9] = 0xF; else reg_args[0x9] = 0x10; };
  { long temp = test_cc_arg_0xA(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0xA] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0xA] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0xA] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0xA] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0xA] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0xA] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0xA] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0xA] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0xA] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0xA] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0xA] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0xA] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0xA] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0xA] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0xA] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0xA] = 0xF; else reg_args[0xA] = 0x10; };
  { long temp = test_cc_arg_0xB(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0xB] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0xB] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0xB] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0xB] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0xB] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0xB] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0xB] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0xB] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0xB] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0xB] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0xB] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0xB] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0xB] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0xB] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0xB] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0xB] = 0xF; else reg_args[0xB] = 0x10; };
  { long temp = test_cc_arg_0xC(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0xC] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0xC] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0xC] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0xC] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0xC] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0xC] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0xC] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0xC] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0xC] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0xC] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0xC] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0xC] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0xC] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0xC] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0xC] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0xC] = 0xF; else reg_args[0xC] = 0x10; };
  { long temp = test_cc_arg_0xD(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0xD] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0xD] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0xD] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0xD] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0xD] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0xD] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0xD] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0xD] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0xD] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0xD] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0xD] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0xD] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0xD] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0xD] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0xD] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0xD] = 0xF; else reg_args[0xD] = 0x10; };
  { long temp = test_cc_arg_0xE(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0xE] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0xE] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0xE] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0xE] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0xE] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0xE] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0xE] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0xE] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0xE] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0xE] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0xE] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0xE] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0xE] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0xE] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0xE] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0xE] = 0xF; else reg_args[0xE] = 0x10; };
  { long temp = test_cc_arg_0xF(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0xF] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0xF] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0xF] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0xF] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0xF] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0xF] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0xF] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0xF] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0xF] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0xF] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0xF] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0xF] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0xF] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0xF] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0xF] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0xF] = 0xF; else reg_args[0xF] = 0x10; };
  { long temp = test_cc_arg_stack_arg_1(0x0000000000000000L,0x1111111111111111L,0x2222222222222222L,0x3333333333333333L,0x4444444444444444L,0x5555555555555555L,0x6666666666666666L,0x7777777777777777L,0x8888888888888888L,0x9999999999999999L,0xAAAAAAAAAAAAAAAAL,0xBBBBBBBBBBBBBBBBL,0xCCCCCCCCCCCCCCCCL,0xDDDDDDDDDDDDDDDDL,0xEEEEEEEEEEEEEEEEL,0xFFFFFFFFFFFFFFFFL); if (temp == 0x0000000000000000L) reg_args[0x10] = 0x0; else if (temp == 0x1111111111111111L) reg_args[0x10] = 0x1; else if (temp == 0x2222222222222222L) reg_args[0x10] = 0x2; else if (temp == 0x3333333333333333L) reg_args[0x10] = 0x3; else if (temp == 0x4444444444444444L) reg_args[0x10] = 0x4; else if (temp == 0x5555555555555555L) reg_args[0x10] = 0x5; else if (temp == 0x6666666666666666L) reg_args[0x10] = 0x6; else if (temp == 0x7777777777777777L) reg_args[0x10] = 0x7; else if (temp == 0x8888888888888888L) reg_args[0x10] = 0x8; else if (temp == 0x9999999999999999L) reg_args[0x10] = 0x9; else if (temp == 0xAAAAAAAAAAAAAAAAL) reg_args[0x10] = 0xA; else if (temp == 0xBBBBBBBBBBBBBBBBL) reg_args[0x10] = 0xB; else if (temp == 0xCCCCCCCCCCCCCCCCL) reg_args[0x10] = 0xC; else if (temp == 0xDDDDDDDDDDDDDDDDL) reg_args[0x10] = 0xD; else if (temp == 0xEEEEEEEEEEEEEEEEL) reg_args[0x10] = 0xE; else if (temp == 0xFFFFFFFFFFFFFFFFL) reg_args[0x10] = 0xF; else reg_args[0x10] = 0x10; };
  for (int i = 0; i <= 0x10; ++i) {
   System.out.println(reg_names[i] + " = " + arg_names[reg_args[i]]);
  }
 }
 private static final long cpuid_output_buf;
 @Code(
  "4989D9"+
  "89D0"+
  "0FA2"+
  "418900"+
  "41895804"+
  "41894808"+
  "4189500C"+
  "4C89CB"+
  "C3"
 )
 private static native void cpuid_raw_win(int page_num, long output);
 @Code(
  "4889D7"+
  "89F0"+
  "4889DE"+
  "0FA2"+
  "8907"+
  "895F04"+
  "894F08"+
  "89570C"+
  "4889F3"+
  "C3"
 )
 private static native void cpuid_raw_lin(int page_num, long output);
 private static void cpuid_raw(int page_num, long output) {
  if (is_windows) {
   cpuid_raw_win(page_num, output);
  } else {
   cpuid_raw_lin(page_num, output);
  }
 }
 public static int[] cpuid(int page_num, int output[]) {
  cpuid_raw(page_num, cpuid_output_buf);
  output[0] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((0)<<2))));
  output[1] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((1)<<2))));
  output[2] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((2)<<2))));
  output[3] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((3)<<2))));
  return output;
 }
 public static int[] cpuid(int page_num) {
  return cpuid(page_num, new int[4]);
 }
 @Code(
  "4489C1"+
  "4989D8"+
  "89D0"+
  "0FA2"+
  "418901"+
  "41895904"+
  "41894908"+
  "4189510C"+
  "4C89C3"+
  "C3"
 )
 private static native void cpuid_ex_raw_win(int page_num, int subpage_num, long output);
 @Code(
  "4889CF"+
  "89D1"+
  "89F0"+
  "4889DE"+
  "0FA2"+
  "8907"+
  "895F04"+
  "894F08"+
  "89570C"+
  "4889F3"+
  "C3"
 )
 private static native void cpuid_ex_raw_lin(int page_num, int subpage_num, long output);
 private static void cpuid_ex_raw(int page_num, int subpage_num, long output) {
  if (is_windows) {
   cpuid_ex_raw_win(page_num, subpage_num, output);
  } else {
   cpuid_ex_raw_lin(page_num, subpage_num, output);
  }
 }
 public static int[] cpuid_ex(int page_num, int subpage_num, int output[]) {
  cpuid_ex_raw(page_num, subpage_num, cpuid_output_buf);
  output[0] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((0)<<2))));
  output[1] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((1)<<2))));
  output[2] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((2)<<2))));
  output[3] = UnsafeUtil.UNSAFE.getInt(null,(((cpuid_output_buf))+(((3)<<2))));
  return output;
 }
 public static int[] cpuid_ex(int page_num, int subpage_num) {
  int[] ret = new int[4];
  cpuid_ex(page_num, subpage_num, ret);
  return ret;
 }
 @Code(
  "4901D0"+
  "4901F9"+
  "4531D2"+
  "430FB63C02"+
  "8D4FBF"+
  "8D4720"+
  "83F91A"+
  "0F43C7"+
  "430FB60C0A"+
  "8D51BF"+
  "8D7920"+
  "83FA1A"+
  "0F43F9"+
  "2BC7"+
  "7508"+
  "49FFC2"+
  "493BF2"+
  "75D2"+
  "C3"
 )
 public static native int strnicmp_win(long offsetA, byte strA[], byte strB[], long offsetB, long count);
 @Code(
  "4901C8"+
  "4801F2"+
  "4531D2"+
  "410FB63C12"+
  "8D4FBF"+
  "8D4720"+
  "83F91A"+
  "0F43C7"+
  "430FB60C02"+
  "8D71BF"+
  "8D7920"+
  "83FE1A"+
  "0F43F9"+
  "29F8"+
  "7508"+
  "49FFC2"+
  "4D39D1"+
  "75D2"+
  "C3"
 )
 public static native int strnicmp_lin(long offsetA, byte strA[], byte strB[], long offsetB, long count);
 public static int strnicmp(long offsetA, byte strA[], byte strB[], long offsetB, long count) {
  offsetA += UnsafeUtil.UNSAFE.ARRAY_BYTE_BASE_OFFSET;
  offsetB += UnsafeUtil.UNSAFE.ARRAY_BYTE_BASE_OFFSET;
  return is_windows ? strnicmp_win(offsetA, strA, strB, offsetB, count) : strnicmp_lin(offsetA, strA, strB, offsetB, count);
 }
 public static boolean is_initialized = false;
 private static final boolean is_windows = System.getProperty("os.name").toLowerCase().contains("windows");
 public static final void init_funcs() {
  if (!is_initialized) {
   is_initialized = true;
    Deencapsulator.open("jdk.vm.ci.runtime.JVMCI");
    YeetLinker.linkClass(JankyHackMate.class);
    Expression.init_cpu_features();
    {
     String code_string_raw = "<offsetof:yeet.TestStruct.field_A>";
     CodeStringVar code_string = new CodeStringVar(code_string_raw);
     long field_A_offset = Expression.get_patch_value(code_string, '>');
     System.out.printf("TestStruct.field_A Offset: %d\n", field_A_offset);
     TestStruct test = new TestStruct();
     test.field_A = 42069;
     System.out.printf("TestStruct.field_A Value: %d\n", UnsafeUtil.UNSAFE.getInt(test, field_A_offset));
    }
  }
 }
 static {
  cpuid_output_buf = (UnsafeUtil.UNSAFE.allocateMemory(((4)<<2)));
  init_funcs();
 }
}
