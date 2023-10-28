package yeet;

import yeet.CodeStringVar;
import yeet.YeetUtil;
import yeet.BitArray;
import yeet.JankyHackMate;
import yeet.UnsafeUtil;
import java.lang.Class;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
public class Expression {
 private static final byte precedence[];
 private static final BitArray cpu_features;
 private static int cache_line_size = 64;
 private static long xsave_mask = 0;
 private static long xsave_max_size = 0;
private static final byte cache_line_size_str_raw[] = {'c','a','c','h','e','l','i','n','e','s','i','z','e'};
 static {
  precedence = new byte[256];
  precedence[(((int)(((byte)(4))))&(0xFF))] = ((byte)(255));
  precedence[(((int)(((byte)('*' + '*'))))&(0xFF))] = (19);
  precedence[(((int)(((byte)('*'))))&(0xFF))] = precedence[(((int)(((byte)('/'))))&(0xFF))] = precedence[(((int)(((byte)('%'))))&(0xFF))] = (17);
  precedence[(((int)(((byte)('+'))))&(0xFF))] = precedence[(((int)(((byte)('-'))))&(0xFF))] = (16);
  precedence[(((int)(((byte)('<' + '<' + '<'))))&(0xFF))] = precedence[(((int)(((byte)('>' + '>' + '>'))))&(0xFF))] =
  precedence[(((int)(((byte)('<' + '<'))))&(0xFF))] = precedence[(((int)(((byte)('>' + '>'))))&(0xFF))] =
  precedence[(((int)(((byte)((('r')+('r')) + '<' + '<'))))&(0xFF))] = precedence[(((int)(((byte)((('r')+('r')) + '>' + '>'))))&(0xFF))] = (15);
  precedence[(((int)(((byte)('<'))))&(0xFF))] = precedence[(((int)(((byte)('<' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)('>'))))&(0xFF))] = precedence[(((int)(((byte)('>' + '='))))&(0xFF))] = (14);
  precedence[(((int)(((byte)('=' + '='))))&(0xFF))] = precedence[(((int)(((byte)((('!')+('!')) + '='))))&(0xFF))] = (13);
  precedence[(((int)(((byte)('<' + '=' + '>'))))&(0xFF))] = (12);
  precedence[(((int)(((byte)((('&')+('&'))))))&(0xFF))] = precedence[(((int)(((byte)('~' + (('&')+('&'))))))&(0xFF))] = (11);
  precedence[(((int)(((byte)('^'))))&(0xFF))] = precedence[(((int)(((byte)('~' + '^'))))&(0xFF))] = (10);
  precedence[(((int)(((byte)((('|')+('|'))))))&(0xFF))] = precedence[(((int)(((byte)('~' + (('|')+('|'))))))&(0xFF))] = (9);
  precedence[(((int)(((byte)((('&')+('&')) + (('&')+('&'))))))&(0xFF))] = precedence[(((int)(((byte)((('!')+('!')) + (('&')+('&')) + (('&')+('&'))))))&(0xFF))] = (8);
  precedence[(((int)(((byte)('^' + '^'))))&(0xFF))] = precedence[(((int)(((byte)((('!')+('!')) + '^' + '^'))))&(0xFF))] = (7);
  precedence[(((int)(((byte)((('|')+('|')) + (('|')+('|'))))))&(0xFF))] = precedence[(((int)(((byte)((('!')+('!')) + (('|')+('|')) + (('|')+('|'))))))&(0xFF))] = (6);
  precedence[(((int)(((byte)((('?')+('?'))))))&(0xFF))] = (5);
  precedence[(((int)(((byte)('='))))&(0xFF))] =
  precedence[(((int)(((byte)('+' + '='))))&(0xFF))] = precedence[(((int)(((byte)('-' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)('*' + '='))))&(0xFF))] = precedence[(((int)(((byte)('/' + '='))))&(0xFF))] = precedence[(((int)(((byte)('%' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)('<' + '<' + '<' + '='))))&(0xFF))] = precedence[(((int)(((byte)('>' + '>' + '>' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)('<' + '<' + '='))))&(0xFF))] = precedence[(((int)(((byte)('>' + '>' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)((('r')+('r')) + '<' + '<' + '='))))&(0xFF))] = precedence[(((int)(((byte)((('r')+('r')) + '>' + '>' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)((('&')+('&')) + '='))))&(0xFF))] = precedence[(((int)(((byte)('~' + (('&')+('&')) + '='))))&(0xFF))] =
  precedence[(((int)(((byte)('^' + '='))))&(0xFF))] = precedence[(((int)(((byte)('~' + '^' + '='))))&(0xFF))] =
  precedence[(((int)(((byte)((('|')+('|')) + '='))))&(0xFF))] = precedence[(((int)(((byte)('~' + (('|')+('|')) + '='))))&(0xFF))] = (4);
  precedence[(((int)(((byte)(','))))&(0xFF))] = (3);
  precedence[(((int)(((byte)(';'))))&(0xFF))] = (2);
  precedence[(((int)(((byte)(1))))&(0xFF))] = (1);
  precedence[(((int)(((byte)(0))))&(0xFF))] = (0);
  cpu_features = new BitArray(104);
 }
private static final byte fxsaveopt_str_raw[] = {'f','x','s','a','v','e','o','p','t'};
private static final byte intel_str_raw[] = {'i','n','t','e','l'};
private static final byte amd_str_raw[] = {'a','m','d'};
private static final byte cmpxchg8_str_raw[] = {'c','m','p','x','c','h','g','8'};
private static final byte sysenter_str_raw[] = {'s','y','s','e','n','t','e','r'};
private static final byte cmov_str_raw[] = {'c','m','o','v'};
private static final byte sse1_str_raw[] = {'s','s','e','1'};
private static final byte sse2_str_raw[] = {'s','s','e','2'};
private static final byte ssse3_str_raw[] = {'s','s','s','e','3'};
private static final byte fma3_str_raw[] = {'f','m','a','3'};
private static final byte cmpxchg16b_str_raw[] = {'c','m','p','x','c','h','g','1','6','b'};
private static final byte sse41_str_raw[] = {'s','s','e','4','1'};
private static final byte sse42_str_raw[] = {'s','s','e','4','2'};
private static final byte movbe_str_raw[] = {'m','o','v','b','e'};
private static final byte avx1_str_raw[] = {'a','v','x','1'};
private static final byte f16c_str_raw[] = {'f','1','6','c'};
private static final byte rdrand_str_raw[] = {'r','d','r','a','n','d'};
private static final byte fsgsbase_str_raw[] = {'f','s','g','s','b','a','s','e'};
private static final byte tsxhle_str_raw[] = {'t','s','x','h','l','e'};
private static final byte avx2_str_raw[] = {'a','v','x','2'};
private static final byte erms_str_raw[] = {'e','r','m','s'};
private static final byte tsxrtm_str_raw[] = {'t','s','x','r','t','m'};
private static final byte avx512dq_str_raw[] = {'a','v','x','5','1','2','d','q'};
private static final byte rdseed_str_raw[] = {'r','d','s','e','e','d'};
private static final byte adx_str_raw[] = {'a','d','x'};
private static final byte avx512ifma_str_raw[] = {'a','v','x','5','1','2','i','f','m','a'};
private static final byte clflushopt_str_raw[] = {'c','l','f','l','u','s','h','o','p','t'};
private static final byte clwb_str_raw[] = {'c','l','w','b'};
private static final byte avx512pf_str_raw[] = {'a','v','x','5','1','2','p','f'};
private static final byte avx512er_str_raw[] = {'a','v','x','5','1','2','e','r'};
private static final byte avx512cd_str_raw[] = {'a','v','x','5','1','2','c','d'};
private static final byte sha_str_raw[] = {'s','h','a'};
private static final byte avx512bw_str_raw[] = {'a','v','x','5','1','2','b','w'};
private static final byte avx512vl_str_raw[] = {'a','v','x','5','1','2','v','l'};
private static final byte prefetchwt1_str_raw[] = {'p','r','e','f','e','t','c','h','w','t','1'};
private static final byte avx512vbmi1_str_raw[] = {'a','v','x','5','1','2','v','b','m','i','1'};
private static final byte pku_str_raw[] = {'p','k','u'};
private static final byte waitpkg_str_raw[] = {'w','a','i','t','p','k','g'};
private static final byte avx512vbmi2_str_raw[] = {'a','v','x','5','1','2','v','b','m','i','2'};
private static final byte gfni_str_raw[] = {'g','f','n','i'};
private static final byte vaes_str_raw[] = {'v','a','e','s'};
private static final byte vpclmulqdq_str_raw[] = {'v','p','c','l','m','u','l','q','d','q'};
private static final byte avx512vnni_str_raw[] = {'a','v','x','5','1','2','v','n','n','i'};
private static final byte avx512bitalg_str_raw[] = {'a','v','x','5','1','2','b','i','t','a','l','g'};
private static final byte avx512vpopcntdq_str_raw[] = {'a','v','x','5','1','2','v','p','o','p','c','n','t','d','q'};
private static final byte rdpid_str_raw[] = {'r','d','p','i','d'};
private static final byte cldemote_str_raw[] = {'c','l','d','e','m','o','t','e'};
private static final byte movdiri_str_raw[] = {'m','o','v','d','i','r','i'};
private static final byte movdir64b_str_raw[] = {'m','o','v','d','i','r','6','4','b'};
private static final byte avx5124vnniw_str_raw[] = {'a','v','x','5','1','2','4','v','n','n','i','w'};
private static final byte avx5124fmaps_str_raw[] = {'a','v','x','5','1','2','4','f','m','a','p','s'};
private static final byte fsrm_str_raw[] = {'f','s','r','m'};
private static final byte uintr_str_raw[] = {'u','i','n','t','r'};
private static final byte avx512vp2i_str_raw[] = {'a','v','x','5','1','2','v','p','2','i'};
private static final byte serialize_str_raw[] = {'s','e','r','i','a','l','i','z','e'};
private static final byte cet_str_raw[] = {'c','e','t'};
private static final byte amxbf16_str_raw[] = {'a','m','x','b','f','1','6'};
private static final byte avx512fp16_str_raw[] = {'a','v','x','5','1','2','f','p','1','6'};
private static final byte amxtile_str_raw[] = {'a','m','x','t','i','l','e'};
private static final byte amxint8_str_raw[] = {'a','m','x','i','n','t','8'};
private static final byte avxvnni_str_raw[] = {'a','v','x','v','n','n','i'};
private static final byte avx512bf16_str_raw[] = {'a','v','x','5','1','2','b','f','1','6'};
private static final byte frmb0_str_raw[] = {'f','r','m','b','0'};
private static final byte frsb_str_raw[] = {'f','r','s','b'};
private static final byte frcsb_str_raw[] = {'f','r','c','s','b'};
private static final byte xsavec_str_raw[] = {'x','s','a','v','e','c'};
private static final byte syscall_str_raw[] = {'s','y','s','c','a','l','l'};
private static final byte mmxext_str_raw[] = {'m','m','x','e','x','t'};
private static final byte rdtscp_str_raw[] = {'r','d','t','s','c','p'};
private static final byte _3dnowext_str_raw[] = {'3','d','n','o','w','e','x','t'};
private static final byte lmlsahf_str_raw[] = {'l','m','l','s','a','h','f'};
private static final byte abm_str_raw[] = {'a','b','m'};
private static final byte sse4a_str_raw[] = {'s','s','e','4','a'};
private static final byte mxcsrmm_str_raw[] = {'m','x','c','s','r','m','m'};
private static final byte xop_str_raw[] = {'x','o','p'};
private static final byte fma4_str_raw[] = {'f','m','a','4'};
private static final byte tbm_str_raw[] = {'t','b','m'};
private static final byte monitorx_str_raw[] = {'m','o','n','i','t','o','r','x'};
private static final byte clzero_str_raw[] = {'c','l','z','e','r','o'};
private static final byte rdpru_str_raw[] = {'r','d','p','r','u'};
private static final byte mcommit_str_raw[] = {'m','c','o','m','m','i','t'};
private static final byte lwpval_str_raw[] = {'l','w','p','v','a','l'};
private static final byte mvex_str_raw[] = {'m','v','e','x'};
private static final byte hybrid_str_raw[] = {'h','y','b','r','i','d'};
 static final void init_cpu_features() {
  int[] data = new int[4];
  JankyHackMate.cpuid((0),data);
  boolean is_amd = false;
  if (data[1] == (((int)('G') & 0xFF) | ((int)('e') & 0xFF) << 8 | ((int)('n') & 0xFF) << 16 | ('u') << 24) &&
   data[3] == (((int)('i') & 0xFF) | ((int)('n') & 0xFF) << 8 | ((int)('e') & 0xFF) << 16 | ('I') << 24) &&
   data[2] == (((int)('n') & 0xFF) | ((int)('t') & 0xFF) << 8 | ((int)('e') & 0xFF) << 16 | ('l') << 24)
  ){
   cpu_features.set_bit((0));
  }
  else if (data[1] == (((int)('A') & 0xFF) | ((int)('u') & 0xFF) << 8 | ((int)('t') & 0xFF) << 16 | ('h') << 24) &&
     data[3] == (((int)('e') & 0xFF) | ((int)('n') & 0xFF) << 8 | ((int)('t') & 0xFF) << 16 | ('i') << 24) &&
     data[2] == (((int)('c') & 0xFF) | ((int)('A') & 0xFF) << 8 | ((int)('M') & 0xFF) << 16 | ('D') << 24)
  ) {
   cpu_features.set_bit((1));
   is_amd = true;
  }
  int temp;
  switch (data[0]) {
   default:
    JankyHackMate.cpuid_ex((13),(0),data);
    xsave_mask = (((long)(int)(data[3]))<<32| ((long)(data[0])&0xFFFFFFFFL));
    JankyHackMate.cpuid_ex((13),(1),data);
    temp = data[0];
    cpu_features.write_bit_raw(((79)),((temp)>>>((0))&1));
    cpu_features.write_bit_raw(((80)),((temp)>>>((1))&1));
   case 12: case 11: case 10: case 9: case 8: case 7:
    JankyHackMate.cpuid_ex((7),(0),data);
    temp = data[1];
    cpu_features.write_bit_raw(((25)),((temp)>>>((0))&1));
    cpu_features.write_bit_raw(((26)),((temp)>>>((3))&1));
    cpu_features.write_bit_raw(((27)),((temp)>>>((4))&1));
    cpu_features.write_bit_raw(((28)),((temp)>>>((5))&1));
    cpu_features.write_bit_raw(((29)),((temp)>>>((6))&1));
    cpu_features.write_bit_raw(((30)),((temp)>>>((8))&1));
    cpu_features.write_bit_raw(((31)),((temp)>>>((9))&1));
    cpu_features.write_bit_raw(((32)),((temp)>>>((11))&1));
    cpu_features.write_bit_raw(((33)),((temp)>>>((13))&1));
    cpu_features.write_bit_raw(((34)),((temp)>>>((14))&1));
    cpu_features.write_bit_raw(((35)),((temp)>>>((16))&1));
    cpu_features.write_bit_raw(((36)),((temp)>>>((17))&1));
    cpu_features.write_bit_raw(((37)),((temp)>>>((18))&1));
    cpu_features.write_bit_raw(((38)),((temp)>>>((19))&1));
    cpu_features.write_bit_raw(((39)),((temp)>>>((21))&1));
    cpu_features.write_bit_raw(((40)),((temp)>>>((23))&1));
    cpu_features.write_bit_raw(((41)),((temp)>>>((24))&1));
    cpu_features.write_bit_raw(((42)),((temp)>>>((26))&1));
    cpu_features.write_bit_raw(((43)),((temp)>>>((27))&1));
    cpu_features.write_bit_raw(((44)),((temp)>>>((28))&1));
    cpu_features.write_bit_raw(((45)),((temp)>>>((29))&1));
    cpu_features.write_bit_raw(((46)),((temp)>>>((30))&1));
    cpu_features.write_bit_raw(((47)),((temp)>>>((31))&1));
    temp = data[2];
    cpu_features.write_bit_raw(((48)),((temp)>>>((0))&1));
    cpu_features.write_bit_raw(((49)),((temp)>>>((1))&1));
    cpu_features.write_bit_raw((50),((temp)>>>(3)&1)&((temp)>>>(4)&1));
    cpu_features.write_bit_raw(((51)),((temp)>>>((5))&1));
    cpu_features.write_bit_raw(((52)),((temp)>>>((6))&1));
    cpu_features.write_bit_raw(((53)),((temp)>>>((8))&1));
    cpu_features.write_bit_raw(((54)),((temp)>>>((9))&1));
    cpu_features.write_bit_raw(((55)),((temp)>>>((10))&1));
    cpu_features.write_bit_raw(((56)),((temp)>>>((11))&1));
    cpu_features.write_bit_raw(((57)),((temp)>>>((12))&1));
    cpu_features.write_bit_raw(((58)),((temp)>>>((14))&1));
    cpu_features.write_bit_raw(((59)),((temp)>>>((22))&1));
    cpu_features.write_bit_raw(((60)),((temp)>>>((25))&1));
    cpu_features.write_bit_raw(((61)),((temp)>>>((27))&1));
    cpu_features.write_bit_raw(((62)),((temp)>>>((28))&1));
    temp = data[3];
    cpu_features.write_bit_raw(((63)),((temp)>>>((2))&1));
    cpu_features.write_bit_raw(((64)),((temp)>>>((3))&1));
    cpu_features.write_bit_raw(((65)),((temp)>>>((4))&1));
    cpu_features.write_bit_raw(((66)),((temp)>>>((5))&1));
    cpu_features.write_bit_raw(((67)),((temp)>>>((8))&1));
    cpu_features.write_bit_raw(((68)),((temp)>>>((14))&1));
    cpu_features.write_bit_raw(((101)),((temp)>>>((15))&1));
    cpu_features.write_bit_raw(((69)),((temp)>>>((20))&1));
    cpu_features.write_bit_raw(((70)),((temp)>>>((22))&1));
    cpu_features.write_bit_raw(((71)),((temp)>>>((23))&1));
    cpu_features.write_bit_raw(((72)),((temp)>>>((24))&1));
    cpu_features.write_bit_raw(((73)),((temp)>>>((25))&1));
    if (data[0] > 0) {
     JankyHackMate.cpuid_ex((7),(1),data);
     temp = data[0];
     cpu_features.write_bit_raw(((74)),((temp)>>>((4))&1));
     cpu_features.write_bit_raw(((75)),((temp)>>>((5))&1));
     cpu_features.write_bit_raw(((76)),((temp)>>>((10))&1));
     cpu_features.write_bit_raw(((77)),((temp)>>>((11))&1));
     cpu_features.write_bit_raw(((78)),((temp)>>>((12))&1));
    }
   case 6: case 5: case 4: case 3: case 2:
    JankyHackMate.cpuid((0x20000000),data);
    if (data[0] > 0) {
     JankyHackMate.cpuid((0x20000001),data);
     cpu_features.write_bit_raw((100),((data[3])>>>(4)&1));
    }
    JankyHackMate.cpuid((0x80000000),data);
    temp = data[0];
    if (((Integer.compareUnsigned(((int)(temp)),((int)(0x80000000))))>0)) {
     switch (temp) {
      default:
       JankyHackMate.cpuid((0x8000001C),data);
       cpu_features.write_bit_raw((99),((data[0])>>>(1)&1));
      case 0x8000001B: case 0x8000001A: case 0x80000019:
      case 0x80000018: case 0x80000017: case 0x80000016:
      case 0x80000015: case 0x80000014: case 0x80000013:
      case 0x80000012: case 0x80000011: case 0x80000010:
      case 0x8000000F: case 0x8000000E: case 0x8000000D:
      case 0x8000000C: case 0x8000000B: case 0x8000000A:
      case 0x80000009: case 0x80000008:
       JankyHackMate.cpuid((0x80000008),data);
       temp = data[1];
       cpu_features.write_bit_raw(((96)),((temp)>>>((0))&1));
       cpu_features.write_bit_raw(((102)),((temp)>>>((2))&1));
       cpu_features.write_bit_raw(((97)),((temp)>>>((4))&1));
       cpu_features.write_bit_raw(((98)),((temp)>>>((8))&1));
      case 0x80000007: case 0x80000006: case 0x80000005:
      case 0x80000004: case 0x80000003: case 0x80000002:
      case 0x80000001:
       JankyHackMate.cpuid((0x80000001),data);
       temp = data[3];
       cpu_features.write_bit_raw(((81)),((temp)>>>((11))&1));
       cpu_features.write_bit_raw(((82)),((temp)>>>((22))&1));
       cpu_features.write_bit_raw(((84)),((temp)>>>((30))&1));
       cpu_features.write_bit_raw(((85)),((temp)>>>((31))&1));
       temp = data[2];
       cpu_features.write_bit_raw(((86)),((temp)>>>((0))&1));
       cpu_features.write_bit_raw(((87)),((temp)>>>((5))&1));
       cpu_features.write_bit_raw(((88)),((temp)>>>((6))&1));
       cpu_features.write_bit_raw(((89)),((temp)>>>((7))&1));
       cpu_features.write_bit_raw(((90)),((temp)>>>((8))&1));
       cpu_features.write_bit_raw(((91)),((temp)>>>((11))&1));
       cpu_features.write_bit_raw(((93)),((temp)>>>((16))&1));
       cpu_features.write_bit_raw(((94)),((temp)>>>((21))&1));
       cpu_features.write_bit_raw(((95)),((temp)>>>((29))&1));
     }
    }
   case 1:
    JankyHackMate.cpuid((1),data);
    JankyHackMate.cpuid((1),data);
    temp = data[3];
    cpu_features.write_bit_raw(((2)),((temp)>>>((4))&1));
    cpu_features.write_bit_raw(((3)),((temp)>>>((8))&1));
    if (!is_amd) {
     cpu_features.write_bit_raw(((4)),((temp)>>>((11))&1));
    }
    cpu_features.write_bit_raw(((5)),((temp)>>>((15))&1));
    if ((temp & 1 << 19)!=0) {
     cpu_features.set_bit((6));
     cache_line_size = (data[1] & 0xFF00) >> 5;
    }
    cpu_features.write_bit_raw(((7)),((temp)>>>((23))&1));
    cpu_features.write_bit_raw(((8)),((temp)>>>((24))&1));
    cpu_features.write_bit_raw(((9)),((temp)>>>((25))&1));
    cpu_features.write_bit_raw(((10)),((temp)>>>((26))&1));
    temp = data[2];
    cpu_features.write_bit_raw(((11)),((temp)>>>((0))&1));
    cpu_features.write_bit_raw(((12)),((temp)>>>((1))&1));
    cpu_features.write_bit_raw(((13)),((temp)>>>((9))&1));
    cpu_features.write_bit_raw(((14)),((temp)>>>((12))&1));
    cpu_features.write_bit_raw(((15)),((temp)>>>((13))&1));
    cpu_features.write_bit_raw(((16)),((temp)>>>((19))&1));
    cpu_features.write_bit_raw(((17)),((temp)>>>((20))&1));
    cpu_features.write_bit_raw(((18)),((temp)>>>((22))&1));
    cpu_features.write_bit_raw(((19)),((temp)>>>((23))&1));
    cpu_features.write_bit_raw(((20)),((temp)>>>((25))&1));
    cpu_features.write_bit_raw((21),((temp)>>>(26)&1)&((temp)>>>(27)&1));
    cpu_features.write_bit_raw(((22)),((temp)>>>((28))&1));
    cpu_features.write_bit_raw(((23)),((temp)>>>((29))&1));
    cpu_features.write_bit_raw(((24)),((temp)>>>((30))&1));
  }
 }
 private static final byte find_next_op_impl(CodeStringVar code_string, int end_char) {
  byte c;
  CTimes2PlusEqualRetPlus3: do {
  CPlusEqualRetPlus2: do {
  CTimes2RetPlus2: do {
  CRetPlus1: do {
   int starting_position = code_string.position--;
   for (;;) {
    switch (c = code_string.code[++code_string.position]) {
     case '\0':
      code_string.position = starting_position;
     case ':':
      return ((byte)(c));
     case '(': case '[':
      return ((byte)(4));
     case ')': case ']':
      return ((byte)(2));
     case '~': {
      byte temp = code_string.code[code_string.position + 1];
      switch (temp) {
       case '&': case '|':
        temp += temp;
       case '^':
        if (code_string.code[code_string.position + 2] == '=') {
         code_string.position += 3;
         return ((byte)(c + temp + '='));
        }
        code_string.position += 2;
        return ((byte)(c + temp));
      }
      continue;
     }
     case '!': {
      c += c;
      byte temp = code_string.code[code_string.position + 1];
      if (temp == '=') {
       break CPlusEqualRetPlus2;
      }
      switch (temp) {
       case '&': case '|':
        temp += temp;
       case '^':
        if (code_string.code[code_string.position + 2] == code_string.code[code_string.position + 1]) {
         code_string.position += 3;
         return ((byte)(c + temp + temp));
        }
      }
      continue;
     }
     case '>':
      if (end_char == '>') {
       return ((byte)(2));
      }
     case '<': {
      int temp = code_string.code[code_string.position + 1];
      if (temp != '\0') {
       temp = (c & 0xFD) | (temp & 0xFD) << 8;
       int c3 = code_string.code[code_string.position + 2];
       if (c3 != '\0') {
        temp |= (c3 & 0xFD) << 16 | (code_string.code[code_string.position + 3] & 0xFF) << 24;
        if (temp == (((int)('<') & 0xFF) | ((int)('<') & 0xFF) << 8 | ((int)('<') & 0xFF) << 16 | ('=') << 24)) {
         code_string.position += 4;
         return ((byte)(c + c + c + '='));
        }
        switch (temp &= (((int)(0xFF) & 0xFF) | ((int)(0xFF) & 0xFF) << 8 | ((int)(0xFF) & 0xFF) << 16 | ('\0') << 24)) {
         case (((int)('<') & 0xFF) | ((int)('=') & 0xFF) << 8 | ((int)('<') & 0xFF) << 16):
          code_string.position += 3;
          return ((byte)('<' + '=' + '>'));
         case (((int)('<') & 0xFF) | ((int)('<') & 0xFF) << 8 | ((int)('=') & 0xFF) << 16):
          break CTimes2PlusEqualRetPlus3;
         case (((int)('<') & 0xFF) | ((int)('<') & 0xFF) << 8 | ((int)('<') & 0xFF) << 16):
          code_string.position += 3;
          return ((byte)(c + c + c));
        }
        temp &= (((int)(0xFF) & 0xFF) | ((int)(0xFF) & 0xFF) << 8 | ((int)('\0') & 0xFF) << 16);
       }
       switch (temp) {
        case (((int)('<') & 0xFF) | ((int)('=') & 0xFF) << 8): break CPlusEqualRetPlus2;
        case (((int)('<') & 0xFF) | ((int)('<') & 0xFF) << 8): break CTimes2RetPlus2;
       }
      }
      break CRetPlus1;
     }
     case 'r': case 'R': {
      int temp = code_string.code[code_string.position + 1];
      if (temp != '\0') {
       int c3 = code_string.code[code_string.position + 2];
       if (temp == c3) {
        c |= (0x20);
        temp = (c & 0xFF) | (temp & 0xFD) << 8 | (c3 & 0xFD) << 16 | (code_string.code[code_string.position + 2] & 0xFF) << 24;
        c += c;
        if (temp == (((int)('r') & 0xFF) | ((int)('<') & 0xFF) << 8 | ((int)('<') & 0xFF) << 16 | ('=') << 24)) {
         code_string.position += 4;
         return ((byte)(c + c3 + c3 + '='));
        }
        temp &= ((0xFF) | (0xFF) << 8 | (0xFF) << 16 | ('\0') << 24);
        if (temp == (((int)('r') & 0xFF) | ((int)('<') & 0xFF) << 8 | ((int)('<') & 0xFF) << 16)) {
         code_string.position += 3;
         return ((byte)(c + c3 + c3));
        }
       }
      }
      continue;
     }
     case '&':
      c += c;
      switch (code_string.code[code_string.position + 1]) {
       case '&': break CTimes2RetPlus2;
       case '=': break CPlusEqualRetPlus2;
       default: break CRetPlus1;
      }
     case '|':
      c += c;
      switch (code_string.code[code_string.position + 1]) {
       case '|': break CTimes2RetPlus2;
       case '=': break CPlusEqualRetPlus2;
       default: break CRetPlus1;
      }
     case '*':
      switch (code_string.code[code_string.position + 1]) {
       case '*': break CTimes2RetPlus2;
       case '=': break CPlusEqualRetPlus2;
       default: break CRetPlus1;
      }
     case '^':
      switch (code_string.code[code_string.position + 1]) {
       case '^': break CTimes2RetPlus2;
       case '=': break CPlusEqualRetPlus2;
       default: break CRetPlus1;
      }
     case '+': case '-': case '/': case '%': case '=':
      switch (code_string.code[code_string.position + 1]) {
       case '=': break CPlusEqualRetPlus2;
       default: break CRetPlus1;
      }
     case '?':
      c += c;
     case ',': case ';':
      break CRetPlus1;
    }
   }
  } while(false);
   ++code_string.position;
   return ((byte)(c));
  } while(false);
   code_string.position += 2;
   return ((byte)(c + c));
  } while(false);
   code_string.position += 2;
   return ((byte)(c + '='));
  } while(false);
   code_string.position += 3;
   return ((byte)(c + c + '='));
 }
 private static final long apply_operator(long value, long arg, byte op) {
  switch ((((int)(op))&(0xFF))) {
   case ((byte)('*' + '*')):
    return YeetUtil.upow64(value, arg);
   case ((byte)('*' + '=')):
   case ((byte)('*')):
    return value * arg;
   case ((byte)('/' + '=')):
   case ((byte)('/')):
    return (Long.divideUnsigned(((long)(value)),((long)(arg))));
   case ((byte)('%' + '=')):
   case ((byte)('%')):
    return (Long.remainderUnsigned(((long)(value)),((long)(arg))));
   case ((byte)('+' + '=')):
   case ((byte)('+')):
    return value + arg;
   case ((byte)('-' + '=')):
   case ((byte)('-')):
    return value - arg;
   case ((byte)('<' + '<' + '<' + '=')): case ((byte)('<' + '<' + '=')):
   case ((byte)('<' + '<' + '<')): case ((byte)('<' + '<')):
    return value << arg;
   case ((byte)('>' + '>' + '>' + '=')):
   case ((byte)('>' + '>' + '>')):
    return value >>> arg;
   case ((byte)('>' + '>' + '=')):
   case ((byte)('>' + '>')):
    return value >> arg;
   case ((byte)((('r')+('r')) + '<' + '<' + '=')):
   case ((byte)((('r')+('r')) + '<' + '<')):
    return ((((long)(value))<<(((int)(arg))))|((long)(value))>>>(-(((int)(arg)))));
   case ((byte)((('r')+('r')) + '>' + '>' + '=')):
   case ((byte)((('r')+('r')) + '>' + '>')):
    return (((long)(value))<<(-(((int)(arg))))|(((long)(value))>>>(((int)(arg)))));
   case ((byte)('<')):
    return ((value < arg) ? 1L : 0L);
   case ((byte)('<' + '=')):
    return ((value <= arg) ? 1L : 0L);
   case ((byte)('>')):
    return ((value > arg) ? 1L : 0L);
   case ((byte)('>' + '=')):
    return ((value >= arg) ? 1L : 0L);
   case ((byte)('=' + '=')):
    return ((value == arg) ? 1L : 0L);
   case ((byte)((('!')+('!')) + '=')):
    return ((value != arg) ? 1L : 0L);
   case ((byte)('<' + '=' + '>')):
    return ((((value)>(arg)) ? 1 : 0)-(((value)<(arg)) ? 1 : 0));
   case ((byte)((('&')+('&')) + '=')):
   case ((byte)((('&')+('&')))):
    return value & arg;
   case ((byte)('~' + (('&')+('&')) + '=')):
   case ((byte)('~' + (('&')+('&')))):
    return ~(value & arg);
   case ((byte)('^' + '=')):
   case ((byte)('^')):
    return value ^ arg;
   case ((byte)('~' + '^' + '=')):
   case ((byte)('~' + '^')):
    return ~(value ^ arg);
   case ((byte)((('|')+('|')) + '=')):
   case ((byte)((('|')+('|')))):
    return value | arg;
   case ((byte)('~' + (('|')+('|')) + '=')):
   case ((byte)('~' + (('|')+('|')))):
    return ~(value | arg);
   case ((byte)((('&')+('&')) + (('&')+('&')))):
    return ((((value) != 0L) && ((arg) != 0L)) ? 1L : 0L);
   case ((byte)((('!')+('!')) + (('&')+('&')) + (('&')+('&')))):
    return ((!(((value) != 0L) && ((arg) != 0L))) ? 1L : 0L);
   case ((byte)('^' + '^')):
    return ((((value) != 0L) ^ ((arg) != 0L)) ? 1L : 0L);
   case ((byte)((('!')+('!')) + '^' + '^')):
    return ((!(((value) != 0L) ^ ((arg) != 0L))) ? 1L : 0L);
   case ((byte)((('|')+('|')) + (('|')+('|')))):
    return ((((value) != 0L) || ((arg) != 0L)) ? 1L : 0L);
   case ((byte)((('!')+('!')) + (('|')+('|')) + (('|')+('|')))):
    return ((!(((value) != 0L) || ((arg) != 0L))) ? 1L : 0L);
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
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vpopcntdq_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((58));
    break;
   case 13:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, cache_line_size_str_raw, 0, length) == 0) return cache_line_size;
    break;
   case 12:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512bitalg_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((57));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx5124fmaps_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((64));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx5124vnniw_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((63));
    break;
   case 11:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi1_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((49));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi2_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((52));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, prefetchwt1_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((48));
    break;
   case 10:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, vpclmulqdq_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((55));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, cmpxchg16b_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((15));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512ifma_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((39));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi1_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((49));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vp2i_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((67));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512bf16_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((75));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, clflushopt_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((40));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vnni_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((56));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512fp16_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((71));
    break;
   case 9:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, vpclmulqdq_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((12));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, movdir64b_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((62));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, prefetchwt1_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((90));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, serialize_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((68));
    break;
   case 8:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, fxsaveopt_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((79));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, fsgsbase_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((25));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, cmpxchg8_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((3));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vl_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((47));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512dq_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((36));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512bw_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((46));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512cd_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((44));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, cldemote_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((60));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, sysenter_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((4));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512er_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((43));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512pf_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((42));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, _3dnowext_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((84));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, monitorx_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((95));
   case 7:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, waitpkg_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((51));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, movdiri_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((61));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512fp16_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((35));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, mxcsrmm_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((89));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, lmlsahf_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((86));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, clflushopt_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((6));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, amxint8_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((73));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, amxtile_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((72));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, amxbf16_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((70));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avxvnni_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((74));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, mcommit_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((98));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, syscall_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((81));
    break;
   case 6:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vpopcntdq_str_raw, 7, length) == 0) return cpu_features.read_bit_raw((19));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, rdtscp_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((83));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, xsavec_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((80));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, fxsaveopt_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((8));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, mmxext_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((82));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, rdrand_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((24));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, rdseed_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((37));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, clzero_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((96));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, lwpval_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((99));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, tsxhle_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((27));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, tsxrtm_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((32));
    break;
   case 5:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, intel_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((0));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, ssse3_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((13));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, sse41_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((16));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, sse42_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((17));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, sse4a_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((88));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, movbe_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((18));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, fxsaveopt_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((21));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, _3dnowext_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((85));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, frmb0_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((76));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, frcsb_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((78));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, rdpid_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((59));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, uintr_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((66));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, rdpru_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((97));
    break;
   case 4:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, ssse3_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((11));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx2_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((28));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi1_str_raw, 7, length) == 0) return cpu_features.read_bit_raw((26));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi2_str_raw, 7, length) == 0) return cpu_features.read_bit_raw((30));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, fma4_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((93));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, vaes_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((54));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, cmov_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((5));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, sse2_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((10));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, f16c_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((23));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, gfni_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((53));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, erms_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((31));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, fsrm_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((65));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, frsb_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((77));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, clwb_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((41));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, mvex_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((100));
    break;
   case 3:
    if (JankyHackMate.strnicmp(code_string.position, code_string.code, amd_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((1));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx2_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((22));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, fma3_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((14));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, avx512vbmi2_str_raw, 7, length) == 0) return cpu_features.read_bit_raw((26));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, adx_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((38));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, sha_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((45));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, vaes_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((20));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, abm_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((87));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, xop_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((91));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, tbm_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((94));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, ssse3_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((9));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, mmxext_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((7));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, lwpval_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((92));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, cet_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((69));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, cmpxchg16b_str_raw, 1, length) == 0) return cpu_features.read_bit_raw((34));
    else if (JankyHackMate.strnicmp(code_string.position, code_string.code, pku_str_raw, 0, length) == 0) return cpu_features.read_bit_raw((50));
    break;
  }
  return 0;
 }
 private static final long get_field_offset(CodeStringVar code_string, int length) {
  ThrowError: do {
  int start_position = code_string.position;
  int current_position = start_position - 1;
  length += start_position;
  while (code_string.code[++current_position] != '.') {
   if (current_position == length) {
    break ThrowError;
   }
  }
  int class_name_length = ((current_position) - (start_position));
  if (class_name_length == 0) {
   break ThrowError;
  }
  start_position = current_position + 1;
  while (++current_position != length) {
   if (code_string.code[current_position] == '.') {
    break ThrowError;
   }
  }
  int field_name_length = ((current_position) - (start_position));
  if (field_name_length == 0) {
   break ThrowError;
  }
  try {
   return UnsafeUtil.UNSAFE.objectFieldOffset(
    Class.forName(new String(code_string.code, code_string.position, class_name_length, StandardCharsets.UTF_8))
     .getDeclaredField(
      new String(code_string.code, start_position, field_name_length, StandardCharsets.UTF_8)
     )
   );
  } catch (Exception e) {
   break ThrowError;
  }
  } while(false);
  throw new IllegalArgumentException();
 }
 private static final long get_field_offset2(CodeStringVar code_string, int length) {
  int start_position;
  int current_position = (length += (start_position = code_string.position));
  while (current_position-- > start_position) {
   if (code_string.code[current_position] == '.') {
    int class_name_length = ((current_position) - (start_position));
    if (class_name_length == 0) {
     break;
    }
    length -= ++current_position;
    if (length == 0) {
     break;
    }
    try {
     return UnsafeUtil.UNSAFE.objectFieldOffset(
      Class.forName(new String(code_string.code, start_position, class_name_length, StandardCharsets.UTF_8))
       .getDeclaredField(
        new String(code_string.code, current_position, length, StandardCharsets.UTF_8)
       )
     );
    } catch (Exception e) {
     break;
    }
   }
  }
  throw new IllegalArgumentException();
 }
 private static final long get_array_offset(CodeStringVar code_string, int length) {
  try {
   return UnsafeUtil.UNSAFE.arrayBaseOffset(Class.forName(new String(code_string.code, code_string.position, length, StandardCharsets.UTF_8)));
  } catch (Exception e) {
   throw new IllegalArgumentException();
  }
 }
 private static final byte codecave_prefix[] = { 'c', 'o', 'd', 'e', 'c', 'a', 'v', 'e', ':' };
 private static final byte cpuid_prefix[] = { 'c', 'p', 'u', 'i', 'd', ':' };
 private static final byte offsetof_prefix[] = { 'o', 'f', 'f', 's', 'e', 't', 'o', 'f', ':' };
 private static final byte option_prefix[] = { 'o', 'p', 't', 'i', 'o', 'n', ':' };
 private static final byte baseof_prefix[] = { 'b', 'a', 's', 'e', 'o', 'f', ':' };
 private static final long patch_value_impl(CodeStringVar code_string, int length, int end_char) {
  long ret = 0;
  switch (((code_string.code[code_string.position])|(0x20))) {
   case 'b':
    if (
     length > (7) + (3) &&
     JankyHackMate.strnicmp(code_string.position, code_string.code, baseof_prefix, 0, (7)) == 0
    ) {
     code_string.position += (7);
     length -= (7);
     ret = get_array_offset(code_string, length);
     code_string.value_type = (5);
     code_string.position += length + 1;
     return ret;
    }
    break;
   case 'c':
    if (
     length > (6) + (3) &&
     JankyHackMate.strnicmp(code_string.position, code_string.code, cpuid_prefix, 0, (6)) == 0
    ) {
     code_string.position += (6);
     length -= (6);
     ret = get_cpu_feature_test(code_string, length);
     code_string.position += length + 1;
     return ret;
    }
    break;
   case 'o':
    if (
     length > (9) + (3) &&
     JankyHackMate.strnicmp(code_string.position, code_string.code, offsetof_prefix, 0, (9)) == 0
    ) {
     code_string.position += (9);
     length -= (9);
     ret = get_field_offset2(code_string, length);
     code_string.value_type = (5);
     code_string.position += length + 1;
     return ret;
    }
    break;
  }
  ret = evaluate_impl(code_string, end_char, ((byte)(1)), 0);
  code_string.value_type = (7);
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
   depth += ((c == start_char) ? 1 : 0) - ((c == end_char) ? 1 : 0);
  } while (depth > 0);
  return patch_value_impl(code_string, ((current_position) - (code_string.position)), end_char);
 }
 private static final long consume_value_impl(CodeStringVar code_string) {
  int tempA;
  int tempB;
  long ret = 0;
  code_string.value_type = (7);
  --code_string.position;
  tempB = 10;
  tempA = 0;
  byte c;
  PostfixCheck: do {
  for (;;) {
   switch (c = code_string.code[++code_string.position]) {
    default:
     throw new IllegalArgumentException();
    case '0':
     switch (code_string.code[code_string.position + 1]) {
      case 'x': case 'X':
       tempB = 16;
       c = code_string.code[code_string.position += 2];
       break;
      case 'b': case 'B':
       tempB = 2;
       c = code_string.code[code_string.position += 2];
       break;
     }
    case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9': {
     long max_pre_mul = (Long.divideUnsigned(((long)((0xFFFFFFFFFFFFFFFFL))),((long)(tempB))));
     for (;;) {
      tempA = c - '0';
      if (((Integer.compareUnsigned(tempA,10))>=0)) {
       tempA = ((c)|(0x20)) - 'a';
       if (((Integer.compareUnsigned(tempA,6))>=0)) {
        break;
       }
       tempA += 10;
      }
      if (tempA > tempB) {
       break;
      }
      c = code_string.code[++code_string.position];
      if (ret == (0xFFFFFFFFFFFFFFFFL)) {
       continue;
      }
      if (((Long.compareUnsigned(((long)(ret)),((long)(max_pre_mul))))<=0)) {
       ret *= tempB;
      } else {
       ret = (0xFFFFFFFFFFFFFFFFL);
      }
      if (((Long.compareUnsigned(((long)(ret)),((long)((0xFFFFFFFFFFFFFFFFL) - tempA))))<=0)) {
       ret += tempA;
      } else {
       ret = (0xFFFFFFFFFFFFFFFFL);
      }
     }
     break PostfixCheck;
    }
    case '\t': case '\f': case '\n': case '\r': case ' ':
     continue;
    case '!': case '~': case '+': case '-':
     if (c == code_string.code[++code_string.position]) {
      ++code_string.position;
      tempA = 1;
     }
     ret = consume_value_impl(code_string);
     switch (c << tempA) {
      case '~': ret = ~ret; break;
      case '!': ret = ((!((ret) != 0L)) ? 1L : 0L); break;
      case '-': ret = -ret; break;
      case '!' << 1: ret = ((((ret) != 0L)) ? 1L : 0L); break;
      case '-' << 1: --ret; break;
      case '+' << 1: ++ret; break;
     }
     break PostfixCheck;
    case '*': {
     ret = consume_value_impl(code_string);
     tempA = -1;
     switch (code_string.value_type) {
      case (1):
       tempA = (0xFF);
      case (2):
       ret = (long)(tempA & UnsafeUtil.UNSAFE.getByte(null,(ret)));
       break;
      case (3):
       tempA = (0xFFFF);
      case (4):
       ret = (long)(tempA & UnsafeUtil.UNSAFE.getShort(null,(ret)));
       break;
      case (5):
       tempA = (0xFFFFFFFF);
      case (6):
       ret = (long)(tempA & UnsafeUtil.UNSAFE.getInt(null,(ret)));
       break;
      default:
       ret = UnsafeUtil.UNSAFE.getLong(null,(ret));
       break;
      case (9):
       ret = (long)UnsafeUtil.UNSAFE.getFloat(null,(ret));
       break;
      case (10):
       ret = (long)UnsafeUtil.UNSAFE.getDouble(null,(ret));
       break;
     }
     break PostfixCheck;
    }
    case '{': {
     ++code_string.position;
     tempA = code_string.check_for_cast('}');
     ret = consume_value_impl(code_string);
     code_string.value_type = tempA;
     break PostfixCheck;
    }
    case '(': {
     ++code_string.position;
     tempA = code_string.check_for_cast(')');
     if (tempA != (7)) {
      ret = consume_value_impl(code_string);
      ++code_string.position;
      switch (tempA) {
       case (1): ret &= (0xFF); break;
       case (2): ret = ((long)(byte)(ret)); break;
       case (3): ret &= (0xFFFF); break;
       case (4): ret = ((long)(short)(ret)); break;
       case (5): ret &= (0xFFFFFFFF); break;
       case (6): ret = ((long)(int)(ret)); break;
      }
     }
     else {
      ret = evaluate_impl(code_string, ')', ((byte)(1)), 0);
      ++code_string.position;
     }
     break PostfixCheck;
    }
    case '[':{
     tempA = 1;
     tempB = code_string.position++;
     do {
      c = code_string.code[++tempB];
      if (c == '\0') {
       throw new IllegalArgumentException();
      }
      tempA += ((c == '[') ? 1 : 0) - ((c == ']') ? 1 : 0);
     } while (tempA > 0);
     ret = patch_value_impl(code_string, ((tempB) - (code_string.position)), ']');
     break PostfixCheck;
    }
    case '<': {
     tempA = 1;
     tempB = code_string.position++;
     do {
      c = code_string.code[++tempB];
      if (c == '\0') {
       throw new IllegalArgumentException();
      }
      tempA += ((c == '<') ? 1 : 0) - ((c == '>') ? 1 : 0);
     } while (tempA > 0);
     ret = patch_value_impl(code_string, ((tempB) - (code_string.position)), '>');
     break PostfixCheck;
    }
   }
  }
  } while(false);
  c = code_string.code[code_string.position];
  if ((c == '+' || c == '-') && c == code_string.code[code_string.position + 1]) {
   code_string.position += 2;
  }
  return ret;
 }
 private static final long evaluate_impl(CodeStringVar code_string, int end_char, byte ops_cur, long value) {
  long cur_value = 0;
  do {
   if (ops_cur != ((byte)(0))) {
    cur_value = consume_value_impl(code_string);
   }
   int pre_op_position = code_string.position;
   byte ops_next = find_next_op_impl(code_string, end_char);
   byte cur_precedence = precedence[ops_cur];
   byte next_precedence = precedence[ops_next];
   switch (((((cur_precedence)>(next_precedence)) ? 1 : 0)-(((cur_precedence)<(next_precedence)) ? 1 : 0))) {
    default:
     if (ops_next != ((byte)((('?')+('?'))))) {
      cur_value = evaluate_impl(code_string, end_char, ops_next, cur_value);
      byte temp = code_string.code[code_string.position];
      if (temp == '?') {
       ++code_string.position;
      }
      else if (temp == end_char) {
       ops_next = ((byte)(0));
       break;
      }
     }
     if (cur_value != 0) {
      byte temp = code_string.code[code_string.position];
      if (temp != ':') {
       cur_value = evaluate_impl(code_string, ':', ((byte)(1)), 0);
      }
      skip_value(code_string, end_char);
     }
     else {
      evaluate_impl(code_string, ':', ((byte)(1)), 0);
      while (code_string.code[code_string.position++] != ':');
      continue;
     }
     break;
    case (0):
     if ((((((int)((byte)((cur_precedence)-((4)))))&(0xFF))-(((int)((byte)(((5))-((4)))))&(0xFF))) <= 0)) {
      cur_value = evaluate_impl(code_string, end_char, ops_next, cur_value);
     }
     break;
    case (1):
     code_string.position = pre_op_position;
     end_char = code_string.code[code_string.position];
   }
   value = apply_operator(value, cur_value, ops_cur);
   ops_cur = ops_next;
  } while (code_string.code[code_string.position] != end_char);
  return value;
 }
 public static final long evaluate(CodeStringVar code_string, int end_char) {
  return evaluate_impl(code_string, end_char, ((byte)(1)), 0);
 }
}
