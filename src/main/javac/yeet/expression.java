package yeet;

#include "util.h"

import yeet.CodeStringVar;
import yeet.YeetUtil;
import yeet.BitArray;
import yeet.JankyHackMate;
import yeet.UnsafeUtil;

import java.lang.Class;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

#define op_t byte

#define AsOp(...)					((byte)(__VA_ARGS__))
#define PreventOverlap(val)			((val)+(val))

#define NullOp						AsOp(0)
#define StartNoOp					AsOp(1)
#define EndGroupOp					AsOp(2)
#define StandaloneTernaryEnd		AsOp(':')
#define BadBrackets					AsOp(4)
#define Power						AsOp('*' + '*')
#define Multiply					AsOp('*')
#define Divide						AsOp('/')
#define Modulo						AsOp('%')
#define Add							AsOp('+')
#define Subtract					AsOp('-')
#define ArithmeticLeftShift			AsOp('<' + '<')
#define ArithmeticRightShift		AsOp('>' + '>')
#define LogicalLeftShift			AsOp('<' + '<' + '<')
#define LogicalRightShift			AsOp('>' + '>' + '>')
#define CircularLeftShift			AsOp(PreventOverlap('r') + '<' + '<')
#define CircularRightShift			AsOp(PreventOverlap('r') + '>' + '>')
#define ThreeWay					AsOp('<' + '=' + '>')
#define Less						AsOp('<')
#define LessEqual					AsOp('<' + '=')
#define Greater						AsOp('>')
#define GreaterEqual				AsOp('>' + '=')
#define Equal						AsOp('=' + '=')
#define NotEqual					AsOp(PreventOverlap('!') + '=')
#define BitwiseAnd					AsOp(PreventOverlap('&'))
#define BitwiseNand					AsOp('~' + PreventOverlap('&'))
#define BitwiseXor					AsOp('^')
#define BitwiseXnor					AsOp('~' + '^')
#define BitwiseOr					AsOp(PreventOverlap('|'))
#define BitwiseNor					AsOp('~' + PreventOverlap('|'))
#define LogicalAnd					AsOp(PreventOverlap('&') + PreventOverlap('&'))
#define LogicalNand					AsOp(PreventOverlap('!') + PreventOverlap('&') + PreventOverlap('&'))
#define LogicalXor					AsOp('^' + '^')
#define LogicalXnor					AsOp(PreventOverlap('!') + '^' + '^')
#define LogicalOr					AsOp(PreventOverlap('|') + PreventOverlap('|'))
#define LogicalNor					AsOp(PreventOverlap('!') + PreventOverlap('|') + PreventOverlap('|'))
#define TernaryConditional			AsOp(PreventOverlap('?'))
#define Assign						AsOp('=')
#define AddAssign					AsOp('+' + '=')
#define SubtractAssign				AsOp('-' + '=')
#define MultiplyAssign				AsOp('*' + '=')
#define DivideAssign				AsOp('/' + '=')
#define ModuloAssign				AsOp('%' + '=')
#define ArithmeticLeftShiftAssign	AsOp('<' + '<' + '=')
#define ArithmeticRightShiftAssign	AsOp('>' + '>' + '=')
#define LogicalLeftShiftAssign		AsOp('<' + '<' + '<' + '=')
#define LogicalRightShiftAssign		AsOp('>' + '>' + '>' + '=')
#define CircularLeftShiftAssign		AsOp(PreventOverlap('r') + '<' + '<' + '=')
#define CircularRightShiftAssign	AsOp(PreventOverlap('r') + '>' + '>' + '=')
#define AndAssign					AsOp(PreventOverlap('&') + '=')
#define NandAssign					AsOp('~' + PreventOverlap('&') + '=')
#define XorAssign					AsOp('^' + '=')
#define XnorAssign					AsOp('~' + '^' + '=')
#define OrAssign					AsOp(PreventOverlap('|') + '=')
#define NorAssign					AsOp('~' + PreventOverlap('|') + '=')
#define Comma						AsOp(',')
#define Gomma						AsOp(';')

public class Expression {
	
	private static final byte precedence[];
	private static final BitArray cpu_features;
	private static int cache_line_size = 64;
	private static long xsave_mask = 0;
	private static long xsave_max_size = 0;
	
private static final byte	cache_line_size_str_raw[] = {'c','a','c','h','e','l','i','n','e','s','i','z','e'};
#define cache_line_size_str	cache_line_size_str_raw, 0

	//private static final HashMap<String, long> options;
	
	static {
		
		//options = new HashMap<>();
		
		precedence = new byte[256];
#define ERROR_PRECEDENCE	((byte)(255))
		precedence[MOVZXBL(BadBrackets)] = ERROR_PRECEDENCE;
#define POWER_PRECEDENCE	(19)
		precedence[MOVZXBL(Power)] = POWER_PRECEDENCE;
#define MULTIPLY_PRECEDENCE	(17)
		precedence[MOVZXBL(Multiply)] = precedence[MOVZXBL(Divide)] = precedence[MOVZXBL(Modulo)] = MULTIPLY_PRECEDENCE;
#define ADD_PRECEDENCE		(16)
		precedence[MOVZXBL(Add)] = precedence[MOVZXBL(Subtract)] = ADD_PRECEDENCE;
#define SHIFT_PRECEDENCE	(15)
		precedence[MOVZXBL(LogicalLeftShift)] = precedence[MOVZXBL(LogicalRightShift)] =
		precedence[MOVZXBL(ArithmeticLeftShift)] = precedence[MOVZXBL(ArithmeticRightShift)] =
		precedence[MOVZXBL(CircularLeftShift)] = precedence[MOVZXBL(CircularRightShift)] = SHIFT_PRECEDENCE;
#define COMPARE_PRECEDENCE	(14)
		precedence[MOVZXBL(Less)] = precedence[MOVZXBL(LessEqual)] =
		precedence[MOVZXBL(Greater)] = precedence[MOVZXBL(GreaterEqual)] = COMPARE_PRECEDENCE;
#define EQUALITY_PRECEDENCE	(13)
		precedence[MOVZXBL(Equal)] = precedence[MOVZXBL(NotEqual)] = EQUALITY_PRECEDENCE;
#define THREEWAY_PRECEDENCE	(12)
		precedence[MOVZXBL(ThreeWay)] = THREEWAY_PRECEDENCE;
#define BITAND_PRECEDENCE	(11)
		precedence[MOVZXBL(BitwiseAnd)] = precedence[MOVZXBL(BitwiseNand)] = BITAND_PRECEDENCE;
#define BITXOR_PRECEDENCE	(10)
		precedence[MOVZXBL(BitwiseXor)] = precedence[MOVZXBL(BitwiseXnor)] = BITXOR_PRECEDENCE;
#define BITOR_PRECEDENCE	(9)
		precedence[MOVZXBL(BitwiseOr)] = precedence[MOVZXBL(BitwiseNor)] = BITOR_PRECEDENCE;
#define AND_PRECEDENCE		(8)
		precedence[MOVZXBL(LogicalAnd)] = precedence[MOVZXBL(LogicalNand)] = AND_PRECEDENCE;
#define XOR_PRECEDENCE		(7)
		precedence[MOVZXBL(LogicalXor)] = precedence[MOVZXBL(LogicalXnor)] = XOR_PRECEDENCE;
#define OR_PRECEDENCE		(6)
		precedence[MOVZXBL(LogicalOr)] = precedence[MOVZXBL(LogicalNor)] = OR_PRECEDENCE;
#define TERNARY_PRECEDENCE	(5)
		precedence[MOVZXBL(TernaryConditional)] = TERNARY_PRECEDENCE;
#define ASSIGN_PRECEDENCE	(4)
		precedence[MOVZXBL(Assign)] =
		precedence[MOVZXBL(AddAssign)] = precedence[MOVZXBL(SubtractAssign)] =
		precedence[MOVZXBL(MultiplyAssign)] = precedence[MOVZXBL(DivideAssign)] = precedence[MOVZXBL(ModuloAssign)] =
		precedence[MOVZXBL(LogicalLeftShiftAssign)] = precedence[MOVZXBL(LogicalRightShiftAssign)] =
		precedence[MOVZXBL(ArithmeticLeftShiftAssign)] = precedence[MOVZXBL(ArithmeticRightShiftAssign)] =
		precedence[MOVZXBL(CircularLeftShiftAssign)] = precedence[MOVZXBL(CircularRightShiftAssign)] =
		precedence[MOVZXBL(AndAssign)] = precedence[MOVZXBL(NandAssign)] =
		precedence[MOVZXBL(XorAssign)] = precedence[MOVZXBL(XnorAssign)] =
		precedence[MOVZXBL(OrAssign)] = precedence[MOVZXBL(NorAssign)] = ASSIGN_PRECEDENCE;
#define COMMA_PRECEDENCE	(3)
		precedence[MOVZXBL(Comma)] = COMMA_PRECEDENCE;
#define GOMMA_PRECEDENCE	(2)
		precedence[MOVZXBL(Gomma)] = GOMMA_PRECEDENCE;
#define STARTOP_PRECEDENCE	(1)
		precedence[MOVZXBL(StartNoOp)] = STARTOP_PRECEDENCE;
#define NOOP_PRECEDENCE		(0)
		precedence[MOVZXBL(NullOp)] = NOOP_PRECEDENCE;
		
		cpu_features = new BitArray(104);
	}
	
private static final byte	fxsaveopt_str_raw[] = {'f','x','s','a','v','e','o','p','t'};
#define fxsaveopt_str		fxsaveopt_str_raw, 0
#define INTEL_BIT			(0)
private static final byte	intel_str_raw[] = {'i','n','t','e','l'};
#define intel_str 			intel_str_raw, 0
#define AMD_BIT				(1)
private static final byte	amd_str_raw[] = {'a','m','d'};
#define amd_str				amd_str_raw, 0
#define TSC_BIT				(2)
//private static final byte	tsc_str_raw[] = {'t','s','c'};
#define tsc_str				rdtscp_str_raw, 2
#define CMPXCHG8_BIT		(3)
private static final byte	cmpxchg8_str_raw[] = {'c','m','p','x','c','h','g','8'};
#define cmpxchg8_str		cmpxchg8_str_raw, 0
#define SYSENTER_BIT		(4)
private static final byte	sysenter_str_raw[] = {'s','y','s','e','n','t','e','r'};
#define sysenter_str		sysenter_str_raw, 0
#define CMOV_BIT			(5)
private static final byte	cmov_str_raw[] = {'c','m','o','v'};
#define cmov_str			cmov_str_raw, 0
#define CLFLUSH_BIT			(6)
//private static final byte	clflush_str_raw[] = {'c','l','f','l','u','s','h'};
#define clflush_str 		clflushopt_str
#define MMX_BIT				(7)
//private static final byte	mmx_str_raw[] = {'m','m','x'};
#define mmx_str 			mmxext_str_raw, 0
#define FXSAVE_BIT			(8)
//private static final byte	fxsave_str_raw[] = {'f','x','s','a','v','e'};
#define fxsave_str			fxsaveopt_str
#define SSE_BIT				(9)
//private static final byte	sse_str_raw[] = {'s','s','e'};
#define sse_str 			ssse3_str_raw, 1
private static final byte	sse1_str_raw[] = {'s','s','e','1'};
#define sse1_str			sse1_str_raw, 0
#define SSE2_BIT			(10)
private static final byte	sse2_str_raw[] = {'s','s','e','2'};
#define sse2_str			sse2_str_raw, 0
#define SSE3_BIT			(11)
//private static final byte	sse3_str_raw[] = {'s','s','e','3'};
#define sse3_str			ssse3_str_raw, 1
#define PCLMULQDQ_BIT		(12)
//private static final byte	pclmulqdq_str_raw[] = {'p','c','l','m','u','l','q','d','q'};
#define pclmulqdq_str		vpclmulqdq_str_raw, 1
#define SSSE3_BIT			(13)
private static final byte	ssse3_str_raw[] = {'s','s','s','e','3'};
#define ssse3_str			ssse3_str_raw, 0
#define FMA_BIT				(14)
//private static final byte	fma_str_raw[] = {'f','m','a'};
#define fma_str				fma3_str
private static final byte	fma3_str_raw[] = {'f','m','a','3'};
#define fma3_str			fma3_str_raw, 0
#define CMPXCHG16B_BIT		(15)
private static final byte	cmpxchg16b_str_raw[] = {'c','m','p','x','c','h','g','1','6','b'};
#define cmpxchg16b_str		cmpxchg16b_str_raw, 0
#define SSE41_BIT			(16)
private static final byte	sse41_str_raw[] = {'s','s','e','4','1'};
#define sse41_str			sse41_str_raw, 0
#define SSE42_BIT			(17)
private static final byte	sse42_str_raw[] = {'s','s','e','4','2'};
#define sse42_str			sse42_str_raw, 0
#define MOVBE_BIT			(18)
private static final byte	movbe_str_raw[] = {'m','o','v','b','e'};
#define movbe_str			movbe_str_raw, 0
#define POPCNT_BIT			(19)
//private static final byte	popcnt_str_raw[] = {'p','o','p','c','n','t'};
#define popcnt_str			avx512vpopcntdq_str_raw, 7
#define AES_BIT				(20)
//private static final byte	aes_str_raw[] = {'a','e','s'};
#define aes_str				vaes_str_raw, 1
#define XSAVE_BIT			(21)
//private static final byte	xsave_str_raw[] = {'x','s','a','v','e'};
#define xsave_str			xsaveopt_str
#define AVX_BIT				(22)
//private static final byte	avx_str_raw[] = {'a','v','x'};
#define avx_str				avx2_str
private static final byte	avx1_str_raw[] = {'a','v','x','1'};
#define avx1_str			avx1_str_raw, 0
#define F16C_BIT			(23)
private static final byte	f16c_str_raw[] = {'f','1','6','c'};
#define f16c_str			f16c_str_raw, 0
#define RDRAND_BIT			(24)
private static final byte	rdrand_str_raw[] = {'r','d','r','a','n','d'};
#define rdrand_str			rdrand_str_raw, 0
#define FSGSBASE_BIT		(25)
private static final byte	fsgsbase_str_raw[] = {'f','s','g','s','b','a','s','e'};
#define fsgsbase_str		fsgsbase_str_raw, 0
#define BMI1_BIT			(26)
//private static final byte	bmi_str_raw[] = {'b','m','i'};
#define bmi_str 			bmi2_str
//private static final byte	bmi1_str_raw[] = {'b','m','i','1'};
#define bmi1_str			avx512vbmi1_str_raw, 7
#define TSXHLE_BIT			(27)
private static final byte	tsxhle_str_raw[] = {'t','s','x','h','l','e'};
#define tsxhle_str			tsxhle_str_raw, 0
#define AVX2_BIT			(28)
private static final byte	avx2_str_raw[] = {'a','v','x','2'};
#define avx2_str			avx2_str_raw, 0
#define FDP_EXCPTN_ONLY_BIT	(29)
#define BMI2_BIT			(30)
//private static final byte	bmi2_str_raw[] = {'b','m','i','2'};
#define bmi2_str			avx512vbmi2_str_raw, 7
#define ERMS_BIT			(31)
private static final byte	erms_str_raw[] = {'e','r','m','s'};
#define erms_str			erms_str_raw, 0
#define TSXRTM_BIT			(32)
private static final byte	tsxrtm_str_raw[] = {'t','s','x','r','t','m'};
#define tsxrtm_str			tsxrtm_str_raw, 0
#define FCS_FDS_DEP_BIT		(33)
#define MPX_BIT				(34)
//private static final byte	mpx_str_raw[] = {'m','p','x'};
#define mpx_str				cmpxchg16b_str_raw, 1
#define AVX512F_BIT			(35)
//private static final byte	avx512f_str_raw[] = {'a','v','x','5','1','2','f'};
#define avx512f_str			avx512fp16_str
#define AVX512DQ_BIT		(36)
private static final byte	avx512dq_str_raw[] = {'a','v','x','5','1','2','d','q'};
#define avx512dq_str		avx512dq_str_raw, 0
#define RDSEED_BIT			(37)
private static final byte	rdseed_str_raw[] = {'r','d','s','e','e','d'};
#define rdseed_str			rdseed_str_raw, 0
#define ADX_BIT				(38)
private static final byte	adx_str_raw[] = {'a','d','x'};
#define adx_str				adx_str_raw, 0
#define AVX512IFMA_BIT		(39)
private static final byte	avx512ifma_str_raw[] = {'a','v','x','5','1','2','i','f','m','a'};
#define avx512ifma_str		avx512ifma_str_raw, 0
#define CLFLUSHOPT_BIT		(40)
private static final byte	clflushopt_str_raw[] = {'c','l','f','l','u','s','h','o','p','t'};
#define clflushopt_str		clflushopt_str_raw, 0
#define CLWB_BIT			(41)
private static final byte	clwb_str_raw[] = {'c','l','w','b'};
#define clwb_str			clwb_str_raw, 0
#define AVX512PF_BIT		(42)
private static final byte	avx512pf_str_raw[] = {'a','v','x','5','1','2','p','f'};
#define avx512pf_str		avx512pf_str_raw, 0
#define AVX512ER_BIT		(43)
private static final byte	avx512er_str_raw[] = {'a','v','x','5','1','2','e','r'};
#define avx512er_str		avx512er_str_raw, 0
#define AVX512CD_BIT		(44)
private static final byte	avx512cd_str_raw[] = {'a','v','x','5','1','2','c','d'};
#define avx512cd_str		avx512cd_str_raw, 0
#define SHA_BIT				(45)
private static final byte	sha_str_raw[] = {'s','h','a'};
#define sha_str				sha_str_raw, 0
#define AVX512BW_BIT		(46)
private static final byte	avx512bw_str_raw[] = {'a','v','x','5','1','2','b','w'};
#define avx512bw_str		avx512bw_str_raw, 0
#define AVX512VL_BIT		(47)
private static final byte	avx512vl_str_raw[] = {'a','v','x','5','1','2','v','l'};
#define avx512vl_str		avx512vl_str_raw, 0
#define PREFETCHWT1_BIT		(48)
private static final byte	prefetchwt1_str_raw[] = {'p','r','e','f','e','t','c','h','w','t','1'};
#define prefetchwt1_str		prefetchwt1_str_raw, 0
#define AVX512VBMI_BIT		(49)
//private static final byte	avx512vbmi_str_raw[] = {'a','v','x','5','1','2','v','b','m','i'};
#define avx512vbmi_str		avx512vbmi1_str
private static final byte	avx512vbmi1_str_raw[] = {'a','v','x','5','1','2','v','b','m','i','1'};
#define avx512vbmi1_str		avx512vbmi1_str_raw, 0
#define PKU_BIT				(50)
private static final byte	pku_str_raw[] = {'p','k','u'};
#define pku_str				pku_str_raw, 0
#define WAITPKG_BIT			(51)
private static final byte	waitpkg_str_raw[] = {'w','a','i','t','p','k','g'};
#define waitpkg_str			waitpkg_str_raw, 0
#define AVX512VBMI2_BIT		(52)
private static final byte	avx512vbmi2_str_raw[] = {'a','v','x','5','1','2','v','b','m','i','2'};
#define avx512vbmi2_str		avx512vbmi2_str_raw, 0
#define GFNI_BIT			(53)
private static final byte	gfni_str_raw[] = {'g','f','n','i'};
#define gfni_str			gfni_str_raw, 0
#define VAES_BIT			(54)
private static final byte	vaes_str_raw[] = {'v','a','e','s'};
#define vaes_str			vaes_str_raw, 0
#define VPCLMULQDQ_BIT		(55)
private static final byte	vpclmulqdq_str_raw[] = {'v','p','c','l','m','u','l','q','d','q'};
#define vpclmulqdq_str		vpclmulqdq_str_raw, 0
#define AVX512VNNI_BIT		(56)
private static final byte	avx512vnni_str_raw[] = {'a','v','x','5','1','2','v','n','n','i'};
#define avx512vnni_str		avx512vnni_str_raw, 0
#define AVX512BITALG_BIT	(57)
private static final byte	avx512bitalg_str_raw[] = {'a','v','x','5','1','2','b','i','t','a','l','g'};
#define avx512bitalg_str	avx512bitalg_str_raw, 0
#define AVX512VPOPCNTDQ_BIT	(58)
private static final byte	avx512vpopcntdq_str_raw[] = {'a','v','x','5','1','2','v','p','o','p','c','n','t','d','q'};
#define avx512vpopcntdq_str	avx512vpopcntdq_str_raw, 0
#define RDPID_BIT			(59)
private static final byte	rdpid_str_raw[] = {'r','d','p','i','d'};
#define rdpid_str			rdpid_str_raw, 0
#define CLDEMOTE_BIT		(60)
private static final byte	cldemote_str_raw[] = {'c','l','d','e','m','o','t','e'};
#define cldemote_str		cldemote_str_raw, 0
#define MOVDIRI_BIT			(61)
private static final byte	movdiri_str_raw[] = {'m','o','v','d','i','r','i'};
#define movdiri_str			movdiri_str_raw, 0
#define MOVDIR64B_BIT		(62)
private static final byte	movdir64b_str_raw[] = {'m','o','v','d','i','r','6','4','b'};
#define movdir64b_str		movdir64b_str_raw, 0
#define AVX5124VNNIW_BIT	(63)
private static final byte	avx5124vnniw_str_raw[] = {'a','v','x','5','1','2','4','v','n','n','i','w'};
#define avx5124vnniw_str	avx5124vnniw_str_raw, 0
#define AVX5124FMAPS_BIT	(64)
private static final byte	avx5124fmaps_str_raw[] = {'a','v','x','5','1','2','4','f','m','a','p','s'};
#define avx5124fmaps_str	avx5124fmaps_str_raw, 0
#define FSRM_BIT			(65)
private static final byte	fsrm_str_raw[] = {'f','s','r','m'};
#define fsrm_str			fsrm_str_raw, 0
#define UINTR_BIT			(66)
private static final byte	uintr_str_raw[] = {'u','i','n','t','r'};
#define uintr_str			uintr_str_raw, 0
#define AVX512VP2I_BIT		(67)
private static final byte	avx512vp2i_str_raw[] = {'a','v','x','5','1','2','v','p','2','i'};
#define avx512vp2i_str		avx512vp2i_str_raw, 0
#define SERIALIZE_BIT		(68)
private static final byte	serialize_str_raw[] = {'s','e','r','i','a','l','i','z','e'};
#define serialize_str		serialize_str_raw, 0
#define CET_BIT				(69)
private static final byte	cet_str_raw[] = {'c','e','t'};
#define cet_str				cet_str_raw, 0
#define AMXBF16_BIT			(70)
private static final byte	amxbf16_str_raw[] = {'a','m','x','b','f','1','6'};
#define amxbf16_str			amxbf16_str_raw, 0
#define AVX512FP16_BIT		(71)
private static final byte	avx512fp16_str_raw[] = {'a','v','x','5','1','2','f','p','1','6'};
#define avx512fp16_str		avx512fp16_str_raw, 0
#define AMXTILE_BIT			(72)
private static final byte	amxtile_str_raw[] = {'a','m','x','t','i','l','e'};
#define amxtile_str			amxtile_str_raw, 0
#define AMXINT8_BIT			(73)
private static final byte	amxint8_str_raw[] = {'a','m','x','i','n','t','8'};
#define amxint8_str			amxint8_str_raw, 0
#define AVXVNNI_BIT			(74)
private static final byte	avxvnni_str_raw[] = {'a','v','x','v','n','n','i'};
#define avxvnni_str			avxvnni_str_raw, 0
#define AVX512BF16_BIT		(75)
private static final byte	avx512bf16_str_raw[] = {'a','v','x','5','1','2','b','f','1','6'};
#define avx512bf16_str		avx512bf16_str_raw, 0
#define FRMB0_BIT			(76)
private static final byte	frmb0_str_raw[] = {'f','r','m','b','0'};
#define frmb0_str			frmb0_str_raw, 0
#define FRSB_BIT			(77)
private static final byte	frsb_str_raw[] = {'f','r','s','b'};
#define frsb_str			frsb_str_raw, 0
#define FRCSB_BIT			(78)
private static final byte	frcsb_str_raw[] = {'f','r','c','s','b'};
#define frcsb_str			frcsb_str_raw, 0
#define XSAVEOPT_BIT		(79)
//private static final byte	xsaveopt_str_raw[] = {'x','s','a','v','e','o','p','t'};
#define xsaveopt_str		fxsaveopt_str_raw, 1
#define XSAVEC_BIT			(80)
private static final byte	xsavec_str_raw[] = {'x','s','a','v','e','c'};
#define xsavec_str			xsavec_str_raw, 0
#define SYSCALL_BIT			(81)
private static final byte	syscall_str_raw[] = {'s','y','s','c','a','l','l'};
#define syscall_str			syscall_str_raw, 0
#define MMXEXT_BIT			(82)
private static final byte	mmxext_str_raw[] = {'m','m','x','e','x','t'};
#define mmxext_str			mmxext_str_raw, 0
#define RDTSCP_BIT			(83)
private static final byte	rdtscp_str_raw[] = {'r','d','t','s','c','p'};
#define rdtscp_str			rdtscp_str_raw, 0
#define _3DNOWEXT_BIT		(84)
private static final byte	_3dnowext_str_raw[] = {'3','d','n','o','w','e','x','t'};
#define _3dnowext_str		_3dnowext_str_raw, 0
#define _3DNOW_BIT			(85)
//private static final byte	_3dnow_str_raw[] = {'3','d','n','o','w'};
#define _3dnow_str			_3dnowext_str
#define LMLSAHF_BIT			(86)
private static final byte	lmlsahf_str_raw[] = {'l','m','l','s','a','h','f'};
#define lmlsahf_str			lmlsahf_str_raw, 0
#define ABM_BIT				(87)
private static final byte	abm_str_raw[] = {'a','b','m'};
#define abm_str				abm_str_raw, 0
#define SSE4A_BIT			(88)
private static final byte	sse4a_str_raw[] = {'s','s','e','4','a'};
#define sse4a_str			sse4a_str_raw, 0
#define MXCSRMM_BIT			(89)
private static final byte	mxcsrmm_str_raw[] = {'m','x','c','s','r','m','m'};
#define mxcsrmm_str			mxcsrmm_str_raw, 0
#define PREFETCHW_BIT		(90)
//private static final byte	prefetchw_str_raw[] = {'p','r','e','f','e','t','c','h','w'};
#define prefetchw_str		prefetchwt1_str
#define XOP_BIT				(91)
private static final byte	xop_str_raw[] = {'x','o','p'};
#define xop_str				xop_str_raw, 0
#define LWP_BIT				(92)
//private static final byte	lwp_str_raw[] = {'l','w','p'};
#define lwp_str				lwpval_str
#define FMA4_BIT			(93)
private static final byte	fma4_str_raw[] = {'f','m','a','4'};
#define fma4_str			fma4_str_raw, 0
#define TBM_BIT				(94)
private static final byte	tbm_str_raw[] = {'t','b','m'};
#define tbm_str				tbm_str_raw, 0
#define MONITORX_BIT		(95)
private static final byte	monitorx_str_raw[] = {'m','o','n','i','t','o','r','x'};
#define monitorx_str		monitorx_str_raw, 0
#define CLZERO_BIT			(96)
private static final byte	clzero_str_raw[] = {'c','l','z','e','r','o'};
#define clzero_str			clzero_str_raw, 0
#define RDPRU_BIT			(97)
private static final byte	rdpru_str_raw[] = {'r','d','p','r','u'};
#define rdpru_str			rdpru_str_raw, 0
#define MCOMMIT_BIT			(98)
private static final byte	mcommit_str_raw[] = {'m','c','o','m','m','i','t'};
#define mcommit_str			mcommit_str_raw, 0
#define LWPVAL_BIT			(99)
private static final byte	lwpval_str_raw[] = {'l','w','p','v','a','l'};
#define lwpval_str			lwpval_str_raw, 0
#define MVEX_BIT			(100)
private static final byte	mvex_str_raw[] = {'m','v','e','x'};
#define mvex_str			mvex_str_raw, 0
#define HYBRID_BIT			(101)
private static final byte	hybrid_str_raw[] = {'h','y','b','r','i','d'};
#define hybrid_str			hybrid_str_raw, 0
#define XSAVE_ERRORS_BIT	(102)

#define use_raw_data 1

#if use_raw_data
#define get_cpuid(func) JankyHackMate.cpuid((func),data)
#define get_cpuid_ex(func, page) JankyHackMate.cpuid_ex((func),(page),data)
#else
#define get_cpuid(func) data = JankyHackMate.cpuid(func)
#define get_cpuid_ex(func, page) data = JankyHackMate.cpuid_ex((func),(page))
#endif

#define GET_BIT(value,index) ((value)MACRO_SECOND_EVAL(MACROV_##index(),>>>(index))&1)
#define INIT_BIT(indexA,indexB) cpu_features.write_bit_raw((indexA),GET_BIT(temp,(indexB)))

	static final void init_cpu_features() {
#if use_raw_data
		int[] data = new int[4];
#else
		int[] data;
#endif
		get_cpuid(0);
		boolean is_amd = false;
		if (data[1] == TextInt('G', 'e', 'n', 'u') &&
			data[3] == TextInt('i', 'n', 'e', 'I') &&
			data[2] == TextInt('n', 't', 'e', 'l')
		){
			cpu_features.set_bit(INTEL_BIT);
		}
		else if (data[1] == TextInt('A', 'u', 't', 'h') &&
				 data[3] == TextInt('e', 'n', 't', 'i') &&
				 data[2] == TextInt('c', 'A', 'M', 'D')
		) {
			cpu_features.set_bit(AMD_BIT);
			is_amd = true;
		}
		int temp;
		switch (data[0]) {
			default:
				get_cpuid_ex(13, 0);
				xsave_mask = pack_long(data[3],data[0]);
				get_cpuid_ex(13, 1);
				temp = data[0];
				INIT_BIT(XSAVEOPT_BIT,0);
				INIT_BIT(XSAVEC_BIT,1);
			case 12: case 11: case 10: case 9: case 8: case 7:
				get_cpuid_ex(7, 0);
				temp = data[1];
				INIT_BIT(FSGSBASE_BIT,0);
				INIT_BIT(BMI1_BIT,3);
				INIT_BIT(TSXHLE_BIT,4);
				INIT_BIT(AVX2_BIT,5);
				INIT_BIT(FDP_EXCPTN_ONLY_BIT,6);
				INIT_BIT(BMI2_BIT,8);
				INIT_BIT(ERMS_BIT,9);
				INIT_BIT(TSXRTM_BIT,11);
				INIT_BIT(FCS_FDS_DEP_BIT,13);
				INIT_BIT(MPX_BIT,14);
				INIT_BIT(AVX512F_BIT,16);
				INIT_BIT(AVX512DQ_BIT,17);
				INIT_BIT(RDSEED_BIT,18);
				INIT_BIT(ADX_BIT,19);
				INIT_BIT(AVX512IFMA_BIT,21);
				INIT_BIT(CLFLUSHOPT_BIT,23);
				INIT_BIT(CLWB_BIT,24);
				INIT_BIT(AVX512PF_BIT,26);
				INIT_BIT(AVX512ER_BIT,27);
				INIT_BIT(AVX512CD_BIT,28);
				INIT_BIT(SHA_BIT,29);
				INIT_BIT(AVX512BW_BIT,30);
				INIT_BIT(AVX512VL_BIT,31);
				temp = data[2];
				INIT_BIT(PREFETCHWT1_BIT,0);
				INIT_BIT(AVX512VBMI_BIT,1);
				cpu_features.write_bit_raw(PKU_BIT,GET_BIT(temp,3)&GET_BIT(temp,4));
				INIT_BIT(WAITPKG_BIT,5);
				INIT_BIT(AVX512VBMI2_BIT,6);
				INIT_BIT(GFNI_BIT,8);
				INIT_BIT(VAES_BIT,9);
				INIT_BIT(VPCLMULQDQ_BIT,10);
				INIT_BIT(AVX512VNNI_BIT,11);
				INIT_BIT(AVX512BITALG_BIT,12);
				INIT_BIT(AVX512VPOPCNTDQ_BIT,14);
				INIT_BIT(RDPID_BIT,22);
				INIT_BIT(CLDEMOTE_BIT,25);
				INIT_BIT(MOVDIRI_BIT,27);
				INIT_BIT(MOVDIR64B_BIT,28);
				temp = data[3];
				INIT_BIT(AVX5124VNNIW_BIT,2);
				INIT_BIT(AVX5124FMAPS_BIT,3);
				INIT_BIT(FSRM_BIT,4);
				INIT_BIT(UINTR_BIT,5);
				INIT_BIT(AVX512VP2I_BIT,8);
				INIT_BIT(SERIALIZE_BIT,14);
				INIT_BIT(HYBRID_BIT,15);
				INIT_BIT(CET_BIT,20);
				INIT_BIT(AMXBF16_BIT,22);
				INIT_BIT(AVX512FP16_BIT,23);
				INIT_BIT(AMXTILE_BIT,24);
				INIT_BIT(AMXINT8_BIT,25);
				if (data[0] > 0) {
					get_cpuid_ex(7, 1);
					temp = data[0];
					INIT_BIT(AVXVNNI_BIT,4);
					INIT_BIT(AVX512BF16_BIT,5);
					INIT_BIT(FRMB0_BIT,10);
					INIT_BIT(FRSB_BIT,11);
					INIT_BIT(FRCSB_BIT,12);
				}
			case 6: case 5: case 4: case 3: case 2:
				get_cpuid(0x20000000);
				if (data[0] > 0) {
					get_cpuid(0x20000001);
					cpu_features.write_bit_raw(MVEX_BIT,GET_BIT(data[3],4));
				}
				get_cpuid(0x80000000);
				temp = data[0];
				if (UGTR32(temp,0x80000000)) {
					switch (temp) {
						default:
							get_cpuid(0x8000001C);
							cpu_features.write_bit_raw(LWPVAL_BIT,GET_BIT(data[0],1));
						case 0x8000001B: case 0x8000001A: case 0x80000019:
						case 0x80000018: case 0x80000017: case 0x80000016:
						case 0x80000015: case 0x80000014: case 0x80000013:
						case 0x80000012: case 0x80000011: case 0x80000010:
						case 0x8000000F: case 0x8000000E: case 0x8000000D:
						case 0x8000000C: case 0x8000000B: case 0x8000000A:
						case 0x80000009: case 0x80000008:
							get_cpuid(0x80000008);
							temp = data[1];
							INIT_BIT(CLZERO_BIT,0);
							INIT_BIT(XSAVE_ERRORS_BIT,2);
							INIT_BIT(RDPRU_BIT,4);
							INIT_BIT(MCOMMIT_BIT,8);
						case 0x80000007: case 0x80000006: case 0x80000005:
						case 0x80000004: case 0x80000003: case 0x80000002:
						case 0x80000001:
							get_cpuid(0x80000001);
							temp = data[3];
							INIT_BIT(SYSCALL_BIT,11);
							INIT_BIT(MMXEXT_BIT,22);
							INIT_BIT(_3DNOWEXT_BIT,30);
							INIT_BIT(_3DNOW_BIT,31);
							temp = data[2];
							INIT_BIT(LMLSAHF_BIT,0);
							INIT_BIT(ABM_BIT,5);
							INIT_BIT(SSE4A_BIT,6);
							INIT_BIT(MXCSRMM_BIT,7);
							INIT_BIT(PREFETCHW_BIT,8);
							INIT_BIT(XOP_BIT,11);
							INIT_BIT(FMA4_BIT,16);
							INIT_BIT(TBM_BIT,21);
							INIT_BIT(MONITORX_BIT,29);
					}
				}
			case 1:
				get_cpuid(1);
				get_cpuid(1);
				temp = data[3];
				INIT_BIT(TSC_BIT,4);
				INIT_BIT(CMPXCHG8_BIT,8);
				if (!is_amd) {
					INIT_BIT(SYSENTER_BIT,11);
				}
				INIT_BIT(CMOV_BIT,15);
				if ((temp & 1 << 19)!=0) {
					cpu_features.set_bit(CLFLUSH_BIT);
					cache_line_size = (data[1] & 0xFF00) >> 5;
				}
				INIT_BIT(MMX_BIT,23);
				INIT_BIT(FXSAVE_BIT,24);
				INIT_BIT(SSE_BIT,25);
				INIT_BIT(SSE2_BIT,26);
				temp = data[2];
				INIT_BIT(SSE3_BIT,0);
				INIT_BIT(PCLMULQDQ_BIT,1);
				INIT_BIT(SSSE3_BIT,9);
				INIT_BIT(FMA_BIT,12);
				INIT_BIT(CMPXCHG16B_BIT,13);
				INIT_BIT(SSE41_BIT,19);
				INIT_BIT(SSE42_BIT,20);
				INIT_BIT(MOVBE_BIT,22);
				INIT_BIT(POPCNT_BIT,23);
				INIT_BIT(AES_BIT,25);
				cpu_features.write_bit_raw(XSAVE_BIT,GET_BIT(temp,26)&GET_BIT(temp,27));
				INIT_BIT(AVX_BIT,28);
				INIT_BIT(F16C_BIT,29);
				INIT_BIT(RDRAND_BIT,30);
		}
	}
	
	private static final op_t find_next_op_impl(CodeStringVar code_string, int end_char) {
		byte c;
		goto_block(CTimes2PlusEqualRetPlus3) {
		goto_block(CPlusEqualRetPlus2) {
		goto_block(CTimes2RetPlus2) {
		goto_block(CRetPlus1) {
			int starting_position = code_string.position--;
			for (;;) {
				switch (c = code_string.code[++code_string.position]) {
					case '\0':
						code_string.position = starting_position;
					case ':':
						return AsOp(c);
					case '(': case '[':
						return BadBrackets;
					case ')': case ']':
						return EndGroupOp;
					case '~': {
						byte temp = code_string.code[code_string.position + 1];
						switch (temp) {
							case '&': case '|':
								temp += temp;
							case '^':
								if (code_string.code[code_string.position + 2] == '=') {
									code_string.position += 3;
									return AsOp(c + temp + '=');
								}
								code_string.position += 2;
								return AsOp(c + temp);
						}
						continue;
					}
					case '!': {
						c += c;
						byte temp = code_string.code[code_string.position + 1];
						if (temp == '=') {
							goto(CPlusEqualRetPlus2);
						}
						switch (temp) {
							case '&': case '|':
								temp += temp;
							case '^':
								if (code_string.code[code_string.position + 2] == code_string.code[code_string.position + 1]) {
									code_string.position += 3;
									return AsOp(c + temp + temp);
								}
						}
						continue;
					}
					case '>': // Make sure the end of a patch value isn't confused with an operator
						if (end_char == '>') {
							return EndGroupOp;
						}
					case '<': {
						int temp = code_string.code[code_string.position + 1];
						if (temp != '\0') {
							temp = (c & 0xFD) | (temp & 0xFD) << 8;
							int c3 = code_string.code[code_string.position + 2];
							if (c3 != '\0') {
								temp |= (c3 & 0xFD) << 16 | (code_string.code[code_string.position + 3] & 0xFF) << 24;
								if (temp == TextInt('<', '<', '<', '=')) {
									code_string.position += 4;
									return AsOp(c + c + c + '=');
								}
								switch (temp &= TextInt(0xFF, 0xFF, 0xFF, '\0')) {
									case TextInt('<', '=', '<'):
										code_string.position += 3;
										return ThreeWay;
									case TextInt('<', '<', '='):
										goto(CTimes2PlusEqualRetPlus3);
									case TextInt('<', '<', '<'):
										code_string.position += 3;
										return AsOp(c + c + c);
								}
								temp &= TextInt(0xFF, 0xFF, '\0');
							}
							switch (temp) {
								case TextInt('<', '='):	goto(CPlusEqualRetPlus2);
								case TextInt('<', '<'):	goto(CTimes2RetPlus2);
							}
						}
						goto(CRetPlus1);
					}
					case 'r': case 'R': {
						int temp = code_string.code[code_string.position + 1];
						if (temp != '\0') {
							int c3 = code_string.code[code_string.position + 2];
							if (temp == c3) {
								c |= lowercase_mask;
								temp = (c & 0xFF) | (temp & 0xFD) << 8 | (c3 & 0xFD) << 16 | (code_string.code[code_string.position + 2] & 0xFF) << 24;
								c += c;
								if (temp == TextInt('r', '<', '<', '=')) {
									code_string.position += 4;
									return AsOp(c + c3 + c3 + '=');
								}
								temp &= TextSInt(0xFF, 0xFF, 0xFF, '\0');
								if (temp == TextInt('r', '<', '<')) {
									code_string.position += 3;
									return AsOp(c + c3 + c3);
								}
							}
						}
						continue;
					}
					case '&':
						c += c;
						switch (code_string.code[code_string.position + 1]) {
							case '&':	goto(CTimes2RetPlus2);
							case '=':	goto(CPlusEqualRetPlus2);
							default:	goto(CRetPlus1);
						}
					case '|':
						c += c;
						switch (code_string.code[code_string.position + 1]) {
							case '|':	goto(CTimes2RetPlus2);
							case '=':	goto(CPlusEqualRetPlus2);
							default:	goto(CRetPlus1);
						}
					case '*':
						switch (code_string.code[code_string.position + 1]) {
							case '*':	goto(CTimes2RetPlus2);
							case '=':	goto(CPlusEqualRetPlus2);
							default:	goto(CRetPlus1);
						}
					case '^':
						switch (code_string.code[code_string.position + 1]) {
							case '^':	goto(CTimes2RetPlus2);
							case '=':	goto(CPlusEqualRetPlus2);
							default:	goto(CRetPlus1);
						}
					case '+': case '-': case '/': case '%': case '=':
						switch (code_string.code[code_string.position + 1]) {
							case '=':	goto(CPlusEqualRetPlus2);
							default:	goto(CRetPlus1);
						}
					case '?':
						c += c;
					case ',': case ';': //case ':':
						goto(CRetPlus1);
				}
			}
		} goto_target(CRetPlus1);
			++code_string.position;
			return AsOp(c);
		} goto_target(CTimes2RetPlus2);
			code_string.position += 2;
			return AsOp(c + c);
		} goto_target(CPlusEqualRetPlus2);
			code_string.position += 2;
			return AsOp(c + '=');
		} goto_target(CTimes2PlusEqualRetPlus3);
			code_string.position += 3;
			return AsOp(c + c + '=');
	}
	
	private static final long apply_operator(long value, long arg, op_t op) {
		switch (MOVZXBL(op)) {
			case Power:
				return YeetUtil.upow64(value, arg);
			case MultiplyAssign:
			case Multiply:
				return value * arg;
			case DivideAssign:
			case Divide:
				return UDIV64(value, arg);
			case ModuloAssign:
			case Modulo:
				return UMOD64(value, arg);
			case AddAssign:
			case Add:
				return value + arg;
			case SubtractAssign:
			case Subtract:
				return value - arg;
			case LogicalLeftShiftAssign: case ArithmeticLeftShiftAssign:
			case LogicalLeftShift: case ArithmeticLeftShift:
				return value << arg;
			case LogicalRightShiftAssign:
			case LogicalRightShift:
				return value >>> arg;
			case ArithmeticRightShiftAssign:
			case ArithmeticRightShift:
				return value >> arg;
			case CircularLeftShiftAssign:
			case CircularLeftShift:
				return ROL64(value, arg);
			case CircularRightShiftAssign:
			case CircularRightShift:
				return ROR64(value, arg);
			case Less:
				return b2l(value < arg);
			case LessEqual:
				return b2l(value <= arg);
			case Greater:
				return b2l(value > arg);
			case GreaterEqual:
				return b2l(value >= arg);
			case Equal:
				return b2l(value == arg);
			case NotEqual:
				return b2l(value != arg);
			case ThreeWay:
				return threeway_compare(value, arg);
			case AndAssign:
			case BitwiseAnd:
				return value & arg;
			case NandAssign:
			case BitwiseNand:
				return ~(value & arg);
			case XorAssign:
			case BitwiseXor:
				return value ^ arg;
			case XnorAssign:
			case BitwiseXnor:
				return ~(value ^ arg);
			case OrAssign:
			case BitwiseOr:
				return value | arg;
			case NorAssign:
			case BitwiseNor:
				return ~(value | arg);
			case LogicalAnd:
				return b2l(l2b(value) && l2b(arg));
			case LogicalNand:
				return b2l(!(l2b(value) && l2b(arg)));
			case LogicalXor:
				return b2l(l2b(value) ^ l2b(arg));
			case LogicalXnor:
				return b2l(!(l2b(value) ^ l2b(arg)));
			case LogicalOr:
				return b2l(l2b(value) || l2b(arg));
			case LogicalNor:
				return b2l(!(l2b(value) || l2b(arg)));
			default:
				return arg;
		}
	}
	
	private static final void skip_value(CodeStringVar code_string, int end_char) {
		int starting_position = code_string.position--;
		int depth = 0;
		outer_loop: for (;;) {
			int c = code_string.code[++code_string.position];
			switch (c) {
				case '\0':
					if (end_char == '\0' && depth == 0) {
						return;
					}
					break outer_loop;
				default:
					if (c == end_char && depth == 0) {
						return;
					}
					continue;
				case '(': case '[':
					++depth;
					continue;
				case ')': case ']':
					if (c == end_char && depth == 0) {
						return;
					}
					if (--depth < 0) {
						break outer_loop;
					}
					continue;
			}
		}
		code_string.position = starting_position;
		throw new IllegalArgumentException();
	}
	
	private static final int get_cpu_feature_test(CodeStringVar code_string, int length) {
		switch (length) {
			case 15:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vpopcntdq_str, length) == 0) return cpu_features.read_bit_raw(AVX512VPOPCNTDQ_BIT);
				break;
			case 13:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, cache_line_size_str, length) == 0) return cache_line_size;
				break;
			case 12:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512bitalg_str, length) == 0) return cpu_features.read_bit_raw(AVX512BITALG_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx5124fmaps_str, length) == 0) return cpu_features.read_bit_raw(AVX5124FMAPS_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx5124vnniw_str, length) == 0) return cpu_features.read_bit_raw(AVX5124VNNIW_BIT);
				break;
			case 11:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi1_str, length) == 0) return cpu_features.read_bit_raw(AVX512VBMI_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi2_str, length) == 0) return cpu_features.read_bit_raw(AVX512VBMI2_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, prefetchwt1_str, length) == 0) return cpu_features.read_bit_raw(PREFETCHWT1_BIT);
				break;
			case 10:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, vpclmulqdq_str, length) == 0) return cpu_features.read_bit_raw(VPCLMULQDQ_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, cmpxchg16b_str, length) == 0) return cpu_features.read_bit_raw(CMPXCHG16B_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512ifma_str, length) == 0) return cpu_features.read_bit_raw(AVX512IFMA_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi_str, length) == 0) return cpu_features.read_bit_raw(AVX512VBMI_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vp2i_str, length) == 0) return cpu_features.read_bit_raw(AVX512VP2I_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512bf16_str, length) == 0) return cpu_features.read_bit_raw(AVX512BF16_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, clflushopt_str, length) == 0) return cpu_features.read_bit_raw(CLFLUSHOPT_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vnni_str, length) == 0) return cpu_features.read_bit_raw(AVX512VNNI_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512fp16_str, length) == 0) return cpu_features.read_bit_raw(AVX512FP16_BIT);
				break;
			case 9:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, pclmulqdq_str, length) == 0) return cpu_features.read_bit_raw(PCLMULQDQ_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, movdir64b_str, length) == 0) return cpu_features.read_bit_raw(MOVDIR64B_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, prefetchw_str, length) == 0) return cpu_features.read_bit_raw(PREFETCHW_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, serialize_str, length) == 0) return cpu_features.read_bit_raw(SERIALIZE_BIT);
				break;
			case 8:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, xsaveopt_str, length) == 0) return cpu_features.read_bit_raw(XSAVEOPT_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, fsgsbase_str, length) == 0) return cpu_features.read_bit_raw(FSGSBASE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, cmpxchg8_str, length) == 0) return cpu_features.read_bit_raw(CMPXCHG8_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vl_str, length) == 0) return cpu_features.read_bit_raw(AVX512VL_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512dq_str, length) == 0) return cpu_features.read_bit_raw(AVX512DQ_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512bw_str, length) == 0) return cpu_features.read_bit_raw(AVX512BW_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512cd_str, length) == 0) return cpu_features.read_bit_raw(AVX512CD_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, cldemote_str, length) == 0) return cpu_features.read_bit_raw(CLDEMOTE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sysenter_str, length) == 0) return cpu_features.read_bit_raw(SYSENTER_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512er_str, length) == 0) return cpu_features.read_bit_raw(AVX512ER_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512pf_str, length) == 0) return cpu_features.read_bit_raw(AVX512PF_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, _3dnowext_str, length) == 0) return cpu_features.read_bit_raw(_3DNOWEXT_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, monitorx_str, length) == 0) return cpu_features.read_bit_raw(MONITORX_BIT);
			case 7:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, waitpkg_str, length) == 0) return cpu_features.read_bit_raw(WAITPKG_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, movdiri_str, length) == 0) return cpu_features.read_bit_raw(MOVDIRI_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx512f_str, length) == 0) return cpu_features.read_bit_raw(AVX512F_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, mxcsrmm_str, length) == 0) return cpu_features.read_bit_raw(MXCSRMM_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, lmlsahf_str, length) == 0) return cpu_features.read_bit_raw(LMLSAHF_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, clflush_str, length) == 0) return cpu_features.read_bit_raw(CLFLUSH_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, amxint8_str, length) == 0) return cpu_features.read_bit_raw(AMXINT8_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, amxtile_str, length) == 0) return cpu_features.read_bit_raw(AMXTILE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, amxbf16_str, length) == 0) return cpu_features.read_bit_raw(AMXBF16_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avxvnni_str, length) == 0) return cpu_features.read_bit_raw(AVXVNNI_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, mcommit_str, length) == 0) return cpu_features.read_bit_raw(MCOMMIT_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, syscall_str, length) == 0) return cpu_features.read_bit_raw(SYSCALL_BIT);
				break;
			case 6:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, popcnt_str, length) == 0) return cpu_features.read_bit_raw(POPCNT_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, rdtscp_str, length) == 0) return cpu_features.read_bit_raw(RDTSCP_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, xsavec_str, length) == 0) return cpu_features.read_bit_raw(XSAVEC_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, fxsave_str, length) == 0) return cpu_features.read_bit_raw(FXSAVE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, mmxext_str, length) == 0) return cpu_features.read_bit_raw(MMXEXT_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, rdrand_str, length) == 0) return cpu_features.read_bit_raw(RDRAND_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, rdseed_str, length) == 0) return cpu_features.read_bit_raw(RDSEED_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, clzero_str, length) == 0) return cpu_features.read_bit_raw(CLZERO_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, lwpval_str, length) == 0) return cpu_features.read_bit_raw(LWPVAL_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, tsxhle_str, length) == 0) return cpu_features.read_bit_raw(TSXHLE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, tsxrtm_str, length) == 0) return cpu_features.read_bit_raw(TSXRTM_BIT);
				break;
			case 5:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, intel_str, length) == 0) return cpu_features.read_bit_raw(INTEL_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, ssse3_str, length) == 0) return cpu_features.read_bit_raw(SSSE3_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sse41_str, length) == 0) return cpu_features.read_bit_raw(SSE41_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sse42_str, length) == 0) return cpu_features.read_bit_raw(SSE42_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sse4a_str, length) == 0) return cpu_features.read_bit_raw(SSE4A_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, movbe_str, length) == 0) return cpu_features.read_bit_raw(MOVBE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, xsave_str, length) == 0) return cpu_features.read_bit_raw(XSAVE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, _3dnow_str, length) == 0) return cpu_features.read_bit_raw(_3DNOW_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, frmb0_str, length) == 0) return cpu_features.read_bit_raw(FRMB0_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, frcsb_str, length) == 0) return cpu_features.read_bit_raw(FRCSB_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, rdpid_str, length) == 0) return cpu_features.read_bit_raw(RDPID_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, uintr_str, length) == 0) return cpu_features.read_bit_raw(UINTR_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, rdpru_str, length) == 0) return cpu_features.read_bit_raw(RDPRU_BIT);
				break;
			case 4:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, sse3_str, length) == 0) return cpu_features.read_bit_raw(SSE3_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx2_str, length) == 0) return cpu_features.read_bit_raw(AVX2_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, bmi1_str, length) == 0) return cpu_features.read_bit_raw(BMI1_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, bmi2_str, length) == 0) return cpu_features.read_bit_raw(BMI2_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, fma4_str, length) == 0) return cpu_features.read_bit_raw(FMA4_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, vaes_str, length) == 0) return cpu_features.read_bit_raw(VAES_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, cmov_str, length) == 0) return cpu_features.read_bit_raw(CMOV_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sse2_str, length) == 0) return cpu_features.read_bit_raw(SSE2_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, f16c_str, length) == 0) return cpu_features.read_bit_raw(F16C_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, gfni_str, length) == 0) return cpu_features.read_bit_raw(GFNI_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, erms_str, length) == 0) return cpu_features.read_bit_raw(ERMS_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, fsrm_str, length) == 0) return cpu_features.read_bit_raw(FSRM_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, frsb_str, length) == 0) return cpu_features.read_bit_raw(FRSB_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, clwb_str, length) == 0) return cpu_features.read_bit_raw(CLWB_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, mvex_str, length) == 0) return cpu_features.read_bit_raw(MVEX_BIT);
				break;
			case 3:
				if		(JankyHackMate.strnicmp(code_string.position, code_string.code, amd_str, length) == 0) return cpu_features.read_bit_raw(AMD_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, avx_str, length) == 0) return cpu_features.read_bit_raw(AVX_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, fma_str, length) == 0) return cpu_features.read_bit_raw(FMA_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, bmi_str, length) == 0) return cpu_features.read_bit_raw(BMI1_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, adx_str, length) == 0) return cpu_features.read_bit_raw(ADX_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sha_str, length) == 0) return cpu_features.read_bit_raw(SHA_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, aes_str, length) == 0) return cpu_features.read_bit_raw(AES_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, abm_str, length) == 0) return cpu_features.read_bit_raw(ABM_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, xop_str, length) == 0) return cpu_features.read_bit_raw(XOP_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, tbm_str, length) == 0) return cpu_features.read_bit_raw(TBM_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, sse_str, length) == 0) return cpu_features.read_bit_raw(SSE_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, mmx_str, length) == 0) return cpu_features.read_bit_raw(MMX_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, lwp_str, length) == 0) return cpu_features.read_bit_raw(LWP_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, cet_str, length) == 0) return cpu_features.read_bit_raw(CET_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, mpx_str, length) == 0) return cpu_features.read_bit_raw(MPX_BIT);
				else if	(JankyHackMate.strnicmp(code_string.position, code_string.code, pku_str, length) == 0) return cpu_features.read_bit_raw(PKU_BIT);
				break;
		}
		return 0;
		//throw new IllegalArgumentException();
	}
	
#define UseGenericObjectOffset 0
	
#define MEMBER_ACCESS_CHAR		'.'
#define MEMBER_ACCESS_LENGTH	(1)
	private static final long get_field_offset(CodeStringVar code_string, int length) {
		goto_block(ThrowError) {
			
		int start_position = code_string.position;
		int current_position = start_position - 1;
		length += start_position;
#define end_position length
		while (code_string.code[++current_position] != MEMBER_ACCESS_CHAR) {
			if (current_position == end_position) {
				goto(ThrowError);
			}
		}
		int class_name_length = OffsetDiffStrlen(current_position, start_position);
		if (class_name_length == 0) {
			goto(ThrowError);
		}
		start_position = current_position + 1;
		while (++current_position != end_position) {
			if (code_string.code[current_position] == MEMBER_ACCESS_CHAR) {
				goto(ThrowError);
			}
		}
		int field_name_length = OffsetDiffStrlen(current_position, start_position);
		if (field_name_length == 0) {
			goto(ThrowError);
		}
#undef end_position
		try {
#if UseGenericObjectOffset
			Class<?> temp = Class.forName(new String(code_string.code, code_string.position, class_name_length, StandardCharsets.UTF_8));
			return UNSAFE_BASE.objectFieldOffset(
				(Object)temp,
				new String(code_string.code, start_position, field_name_length, StandardCharsets.UTF_8)
			);
#else
			return UNSAFE_BASE.objectFieldOffset(
				Class.forName(new String(code_string.code, code_string.position, class_name_length, StandardCharsets.UTF_8))
					.getDeclaredField(
						new String(code_string.code, start_position, field_name_length, StandardCharsets.UTF_8)
					)
			);
#endif
		} catch (Exception e) {
			goto(ThrowError);
		}
			
		} goto_target(ThrowError);
		throw new IllegalArgumentException();
	}
	
	private static final long get_field_offset2(CodeStringVar code_string, int length) {
		int start_position;
		int current_position = (length += (start_position = code_string.position));
#define end_position length
#define field_name_length length
		while (current_position-- > start_position) {
			if (code_string.code[current_position] == MEMBER_ACCESS_CHAR) {
				int class_name_length = OffsetDiffStrlen(current_position, start_position);
				if (class_name_length == 0) {
					break;
				}
				field_name_length -= ++current_position;
				if (field_name_length == 0) {
					break;
				}
				try {
#if UseGenericObjectOffset
					Class<?> temp = Class.forName(new String(code_string.code, start_position, class_name_length, StandardCharsets.UTF_8));
					return UNSAFE_BASE.objectFieldOffset(
						(Object)temp,
						new String(code_string.code, current_position, field_name_length, StandardCharsets.UTF_8)
					);
#else
					return UNSAFE_BASE.objectFieldOffset(
						Class.forName(new String(code_string.code, start_position, class_name_length, StandardCharsets.UTF_8))
							.getDeclaredField(
								new String(code_string.code, current_position, field_name_length, StandardCharsets.UTF_8)
							)
					);
#endif
				} catch (Exception e) {
					break;
				}
			}
		}
#undef end_position
#undef field_name_length
		throw new IllegalArgumentException();
	}
	
#define use_array_base_lookup_table 0

#if use_array_base_lookup_table
	private static final byte byte_type_str[] = {'b','y','t','e'};
	private static final byte short_type_str[] = {'s','h','o','r','t'};
	private static final byte int_type_str[] = {'i','n','t'};
	private static final byte long_type_str[] = {'l','o','n','g'};
	private static final byte float_type_str[] = {'f','l','o','a','t'};
	private static final byte double_type_str[] = {'d','o','u','b','l','e'};
	private static final long get_array_offset(CodeStringVar code_string, int length) {
		switch (length) {
			case 6:
				if (strnicmp(code_string.code, code_string.position, double_type_str, 0, length) == 0) {
					return baseof_array(double);
				}
				break;
			case 5:
				if (strnicmp(code_string.code, code_string.position, float_type_str, 0, length) == 0) {
					return baseof_array(float);
				}
				if (strnicmp(code_string.code, code_string.position, short_type_str, 0, length) == 0) {
					return baseof_array(short);
				}
				break;
			case 4:
				if (strnicmp(code_string.code, code_string.position, byte_type_str, 0, length) == 0) {
					return baseof_array(byte);
				}
				if (strnicmp(code_string.code, code_string.position, long_type_str, 0, length) == 0) {
					return baseof_array(long);
				}
				break;
			case 3:
				if (strnicmp(code_string.code, code_string.position, int_type_str, 0, length) == 0) {
					return baseof_array(int);
				}
				break;
		}
		throw new IllegalArgumentException();
	}
#else
	private static final long get_array_offset(CodeStringVar code_string, int length) {
		try {
			return UNSAFE_BASE.arrayBaseOffset(Class.forName(new String(code_string.code, code_string.position, length, StandardCharsets.UTF_8)));
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
	}
#endif

	private static final byte codecave_prefix[] = { 'c', 'o', 'd', 'e', 'c', 'a', 'v', 'e', ':' };
#define CODECAVE_PREFIX_LENGTH		(9)
#define MIN_CODECAVE_DATA_LENGTH	(1)
	private static final byte cpuid_prefix[] = { 'c', 'p', 'u', 'i', 'd', ':' };
#define CPUID_PREFIX_LENGTH			(6)
#define MIN_CPUID_DATA_LENGTH		(3)
	private static final byte offsetof_prefix[] = { 'o', 'f', 'f', 's', 'e', 't', 'o', 'f', ':' };
#define OFFSETOF_PREFIX_LENGTH		(9)
#define MIN_OFFSETOF_DATA_LENGTH	(3)
	private static final byte option_prefix[] = { 'o', 'p', 't', 'i', 'o', 'n', ':' };
#define OPTION_PREFIX_LENGTH		(7)
#define MIN_OPTION_DATA_LENGTH		(1)
	private static final byte baseof_prefix[] = { 'b', 'a', 's', 'e', 'o', 'f', ':' };
#define BASEOF_PREFIX_LENGTH		(7)
#define MIN_BASEOF_DATA_LENGTH		(3)
	//private static final byte patch_prefix[] = { 'p', 'a', 't', 'c', 'h', ':' };
#define PATCH_PREFIX_LENGTH			(6)
#define MIN_PATCH_DATA_LENGTH		(1)
	//private static final byte nop_prefix[] = { 'n', 'o', 'p', ':' };
#define NOP_PREFIX_LENGTH			(4)
#define MIN_NOP_DATA_LENGTH			(1)
	//private static final byte int3_prefix[] = { 'i', 'n', 't', '3', ':' };
#define INT3_PREFIX_LENGTH			(5)
#define MIN_INT3_DATA_LENGTH		(1)

	private static final long patch_value_impl(CodeStringVar code_string, int length, int end_char) {
		long ret = 0;
		switch (lowercase(code_string.code[code_string.position])) {
			case 'b':
				if (
					length > BASEOF_PREFIX_LENGTH + MIN_BASEOF_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, baseof_prefix, 0, BASEOF_PREFIX_LENGTH) == 0
				) {
					code_string.position += BASEOF_PREFIX_LENGTH;
					length -= BASEOF_PREFIX_LENGTH;
					ret = get_array_offset(code_string, length);
					code_string.value_type = PVT_DWORD;
					code_string.position += length + 1;
					return ret;
				}
				break;
			case 'c':
				/*
				if (
					length > CODECAVE_PREFIX_LENGTH + MIN_CODECAVE_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, codecave_prefix, 0, CODECAVE_PREFIX_LENGTH) == 0
				) {
					code_string.position += CODECAVE_PREFIX_LENGTH;
					code_string.value_type = end_char == ']' ? PVT_DWORD : PVT_POINTER;
					//return get_codecave_address(code_string.position);
					return ret;
				}
				*/
				if (
					length > CPUID_PREFIX_LENGTH + MIN_CPUID_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, cpuid_prefix, 0, CPUID_PREFIX_LENGTH) == 0
				) {
					code_string.position += CPUID_PREFIX_LENGTH;
					length -= CPUID_PREFIX_LENGTH;
					ret = get_cpu_feature_test(code_string, length);
					code_string.position += length + 1;
					return ret;
				}
				break;
			case 'o':
				if (
					length > OFFSETOF_PREFIX_LENGTH + MIN_OFFSETOF_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, offsetof_prefix, 0, OFFSETOF_PREFIX_LENGTH) == 0
				) {
					code_string.position += OFFSETOF_PREFIX_LENGTH;
					length -= OFFSETOF_PREFIX_LENGTH;
					ret = get_field_offset2(code_string, length);
					code_string.value_type = PVT_DWORD;
					code_string.position += length + 1;
					return ret;
				}
				/*
				if (
					length > OPTION_PREFIX_LENGTH + MIN_OPTION_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, option_prefix, 0, OPTION_PREFIX_LENGTH) == 0
				) {
					code_string.position += OPTION_PREFIX_LENGTH;
					length -= OPTION_PREFIX_LENGTH;
					
					code_string.position += length + 1;
					return ret;
				}
				*/
				break;
			/*
			case 'p':
				if (
					length > PATCH_PREFIX_LENGTH + MIN_PATCH_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, patch_prefix, 0, PATCH_PREFIX_LENGTH) == 0
				) {
					code_string.position += PATCH_PREFIX_LENGTH;
					length -= PATCH_PREFIX_LENGTH;
					
					code_string.value_type = PVT_DWORD;
					code_string.position += length + 1;
					return ???
				}
				break;
			*/
			/*
			case 'n':
				if (
					length > NOP_PREFIX_LENGTH + MIN_NOP_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, nop_prefix, 0, NOP_PREFIX_LENGTH) == 0
				) {
					code_string.position += NOP_PREFIX_LENGTH;
					length -= NOP_PREFIX_LENGTH;
					
					code_string.position += length + 1;
				}
				break;
			case 'i':
				if (
					length > INT3_PREFIX_LENGTH + MIN_INT3_DATA_LENGTH &&
					JankyHackMate.strnicmp(code_string.position, code_string.code, int3_prefix, 0, INT3_PREFIX_LENGTH) == 0
				) {
					code_string.position += INT3_PREFIX_LENGTH;
					length -= INT3_PREFIX_LENGTH;
					
					code_string.position += length + 1;
				}
				break;
			*/
		}
		
		ret = evaluate_impl(code_string, end_char, StartNoOp, 0);
		/* if (end_char == ']') {
			ret -= rel_source + 4;
		} */
		code_string.value_type = PVT_DEFAULT;
		return ret;
	}
	
	public static final long get_patch_value(CodeStringVar code_string, int end_char) {
		int depth = 1;
		int current_position = code_string.position++;
		int start_char = end_char == ']' ? '[' : '<';
		do {
			byte c = code_string.code[++current_position];
			if (c == '\0') {
				throw new IllegalArgumentException();
			}
			depth += b2i(c == start_char) - b2i(c == end_char);
		} while (depth > 0);
		return patch_value_impl(code_string, OffsetDiffStrlen(current_position, code_string.position), end_char);
	}
	
#define HigherThanNext	(1)
#define SameAsNext		(0)
#define LowerThanNext	(-1)
#define LowerThanPrev	(1)
#define SameAsPrev		(0)
#define HigherThanPrev	(-1)

	private static final long consume_value_impl(CodeStringVar code_string) {
#define local_value_type tempA
#define ret_mask tempA
#define depth tempA
#define temp_number tempA
#define next_matches tempA
#define length tempA
		int tempA;
#define starting_position tempB
#define number_base tempB
#define current_position tempB
		int tempB;

		long ret = 0;
		code_string.value_type = PVT_DEFAULT;
		--code_string.position;
		number_base = 10;
		next_matches = 0;
		byte c;
		
		goto_block(PostfixCheck) {
		for (;;) {
			switch (c = code_string.code[++code_string.position]) {
				default: // case '\0':
					// invalid character
					throw new IllegalArgumentException();
				case '0':
					switch (code_string.code[code_string.position + 1]) {
						case 'x': case 'X':
							number_base = 16;
							c = code_string.code[code_string.position += 2];
							break;
						case 'b': case 'B':
							number_base = 2;
							c = code_string.code[code_string.position += 2];
							break;
					}
				case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': {
					long max_pre_mul = UDIV64(UINT64_MAX, number_base);
					for (;;) {
						temp_number = c - '0';
						if (UGEQ(temp_number, 10)) {
							temp_number = lowercase(c) - 'a';
							if (UGEQ(temp_number, 6)) {
								break;
							}
							temp_number += 10;
						}
						if (temp_number > number_base) {
							break;
						}
						c = code_string.code[++code_string.position];
						if (ret == UINT64_MAX) {
							continue;
						}
						if (ULEQ64(ret, max_pre_mul)) {
							ret *= number_base;
						} else {
							ret = UINT64_MAX;
							// throw new IllegalArgumentException();
						}
						if (ULEQ64(ret, UINT64_MAX - temp_number)) {
							ret += temp_number;
						} else {
							ret = UINT64_MAX;
							// throw new IllegalArgumentException();
						}
					}
					goto(PostfixCheck);
				}
				case '\t': case '\f': case '\n': case '\r': case ' ':
					continue;
				case '!': case '~': case '+': case '-':
					if (c == code_string.code[++code_string.position]) {
						++code_string.position;
						next_matches = 1;
					}
					ret = consume_value_impl(code_string); // Can throw
					switch (c << next_matches) {
						case '~': ret = ~ret; break;
						case '!': ret = b2l(!l2b(ret)); break;
						case '-': ret = -ret; break;
						//case '+': ret = +ret; break;
						//case '~' << 1: ret = ~~ret; break;
						case '!' << 1: ret = b2l(l2b(ret)); break;
						case '-' << 1: --ret; break;
						case '+' << 1: ++ret; break;
					}
					goto(PostfixCheck);
				case '*': {
					ret = consume_value_impl(code_string); // Can throw
					ret_mask = -1;
					switch (code_string.value_type) {
						case PVT_BYTE:
							ret_mask = UINT8_MAX;
						case PVT_SBYTE:
							ret = (long)(ret_mask & read_ptr(ret, byte));
							break;
						case PVT_WORD:
							ret_mask = UINT16_MAX;
						case PVT_SWORD:
							ret = (long)(ret_mask & read_ptr(ret, short));
							break;
						case PVT_DWORD:
							ret_mask = UINT32_MAX;
						case PVT_SDWORD:
							ret = (long)(ret_mask & read_ptr(ret, int));
							break;
						//case PVT_QWORD: case PVT_SQWORD:
						default:
							ret = read_ptr(ret, long);
							break;
						case PVT_FLOAT:
							ret = (long)read_ptr(ret, float);
							break;
						case PVT_DOUBLE:
							ret = (long)read_ptr(ret, double);
							break;
					}
					goto(PostfixCheck);
				}
				case '{': {
					++code_string.position;
					local_value_type = code_string.check_for_cast('}');
					ret = consume_value_impl(code_string); // Can throw
					code_string.value_type = local_value_type;
					goto(PostfixCheck);
				}
				case '(': {
					++code_string.position;
					local_value_type = code_string.check_for_cast(')');
					if (local_value_type != PVT_DEFAULT) {
						ret = consume_value_impl(code_string); // Can throw
						++code_string.position;
						switch (local_value_type) {
							case PVT_BYTE:		ret &= UINT8_MAX; break;
							case PVT_SBYTE:		ret = MOVSXBQ(ret); break;
							case PVT_WORD:		ret &= UINT16_MAX; break;
							case PVT_SWORD:		ret = MOVSXWQ(ret); break;
							case PVT_DWORD:		ret &= UINT32_MAX; break;
							case PVT_SDWORD:	ret = MOVSXLQ(ret); break;
							// case PVT_QWORD:	ret = ret; break;
							// case PVT_SQWORD:	ret = ret; break;
							//case PVT_FLOAT:	ret = (long)(float)ret; break;
							//case PVT_DOUBLE:	ret = (long)(double)ret; break;
						}
					}
					else {
						ret = evaluate_impl(code_string, ')', StartNoOp, 0); // Can throw
						++code_string.position;
					}
					goto(PostfixCheck);
				}
				case '[':{
					depth = 1;
					current_position = code_string.position++;
					do {
						c = code_string.code[++current_position];
						if (c == '\0') {
							throw new IllegalArgumentException();
						}
						depth += b2i(c == '[') - b2i(c == ']');
					} while (depth > 0);
					ret = patch_value_impl(code_string, OffsetDiffStrlen(current_position, code_string.position), ']'); // Can throw
					goto(PostfixCheck);
				}
				case '<': {
					depth = 1;
					current_position = code_string.position++;
					do {
						c = code_string.code[++current_position];
						if (c == '\0') {
							throw new IllegalArgumentException();
						}
						depth += b2i(c == '<') - b2i(c == '>');
						
					} while (depth > 0);
					ret = patch_value_impl(code_string, OffsetDiffStrlen(current_position, code_string.position), '>'); // Can throw
					goto(PostfixCheck);
				}
			}
		}
		
		} goto_target(PostfixCheck);
		c = code_string.code[code_string.position];
		if ((c == '+' || c == '-') && c == code_string.code[code_string.position + 1]) {
			code_string.position += 2;
		}
		return ret;
	}
	
	private static final long evaluate_impl(CodeStringVar code_string, int end_char, op_t ops_cur, long value) {
		long cur_value = 0;
		do {
			if (ops_cur != NullOp) {
				cur_value = consume_value_impl(code_string); // Can throw
			}
			int pre_op_position = code_string.position;
			op_t ops_next = find_next_op_impl(code_string, end_char);
			byte cur_precedence = precedence[ops_cur];
			byte next_precedence = precedence[ops_next];
			switch (threeway_compare(cur_precedence, next_precedence)) {
				default:
					if (ops_next != TernaryConditional) {
						cur_value = evaluate_impl(code_string, end_char, ops_next, cur_value); // Can throw
						byte temp = code_string.code[code_string.position];
						if (temp == '?') {
							++code_string.position;
						}
						else if (temp == end_char) {
							ops_next = NullOp;
							break;
						}
					}
					if (cur_value != 0) {
						byte temp = code_string.code[code_string.position];
						if (temp != ':') {
							cur_value = evaluate_impl(code_string, ':', StartNoOp, 0); // Can throw
						}
						skip_value(code_string, end_char); // Can throw
					}
					else {
						evaluate_impl(code_string, ':', StartNoOp, 0); // Can throw
						while (code_string.code[code_string.position++] != ':');
						continue;
					}
					break;
				case SameAsNext:
					if (byte_in_range_inclusive(cur_precedence, ASSIGN_PRECEDENCE, TERNARY_PRECEDENCE)) {
						cur_value = evaluate_impl(code_string, end_char, ops_next, cur_value); // Can throw
					}
					break;
				case HigherThanNext:
					code_string.position = pre_op_position;
					end_char = code_string.code[code_string.position];
			}
			value = apply_operator(value, cur_value, ops_cur);
			ops_cur = ops_next;
		} while (code_string.code[code_string.position] != end_char);
		return value;
	}
	
	public static final long evaluate(CodeStringVar code_string, int end_char) {
		return evaluate_impl(code_string, end_char, StartNoOp, 0);
	}
}