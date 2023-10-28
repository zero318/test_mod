package yeet;

import java.util.Arrays;
import java.nio.charset.StandardCharsets;
public class CodeStringVar {
 byte code[];
 int position;
 int value_type;
 long value;
 CodeStringVar(CodeStringVar code_string) {
  this.code = code_string.code;
  this.position = code_string.position;
  this.value = code_string.value;
 }
 public CodeStringVar(String code) {
  this.position = 0;
  this.value = 0;
  byte[] temp = code.getBytes(StandardCharsets.UTF_8);
  this.code = Arrays.copyOf(temp, temp.length + 1);
 }
 private void skip_whitespace() {
  for (;;) {
   switch (this.code[this.position]) {
    case '\t': case '\f': case '\n': case '\r': case ' ':
     ++this.position;
     continue;
    default:
     return;
   }
  }
 }
 private boolean parse_comments() {
  byte prev_char = this.code[++this.position];
  if (prev_char == '*') {
   for (;;) {
    byte cur_char = this.code[++this.position];
    if (cur_char == '\0') {
     return false;
    }
    if (cur_char == '/' && prev_char == '*') {
     break;
    }
    prev_char = cur_char;
   }
   ++this.position;
  }
  return true;
 }
 private byte parse_character_literal() {
  int previous_position = this.position;
  byte c = this.code[++this.position];
  if (c == '\\') {
   switch (c = this.code[++this.position]) {
    default:
     throw new IllegalArgumentException();
    case '\'': case '"': case '?': case '\\':
     break;
    case '0':
     c = '\0';
     break;
    case 'a':
     c = 0x07;
     break;
    case 'b':
     c = '\b';
     break;
    case 'f':
     c = '\f';
     break;
    case 'n':
     c = '\n';
     break;
    case 'r':
     c = '\r';
     break;
    case 't':
     c = '\t';
     break;
    case 'v':
     c = 0x0B;
     break;
   }
  }
  if (this.code[++this.position] != '\'') {
   throw new IllegalArgumentException();
  }
  return c;
 }
 private void parse_brackets(byte c) {
  --this.position;
  int paren_count = 0;
  int square_count = 0;
  int curly_count = 0;
  do {
   paren_count += ((c == '(') ? 1 : 0) - ((c == ')') ? 1 : 0);
   square_count += ((c == '[') ? 1 : 0) - ((c == ']') ? 1 : 0);
   curly_count += ((c == '{') ? 1 : 0) - ((c == '}') ? 1 : 0);
   int temp = paren_count | square_count | curly_count;
   if (temp == 0) {
    return;
   }
   if (temp < 0) {
    break;
   }
  } while ((c = this.code[++this.position]) != '\0');
  throw new IllegalArgumentException();
 }
 int check_for_cast(int end_cast_char) {
  int c, c2, c3;
  switch (c = ((this.code[this.position])|(0x20))) {
   case 'i': case 'u': case 'f':
    if ((c2 = this.code[this.position + 1]) > '\0') {
     if ((c3 = this.code[this.position + 2]) == end_cast_char &&
      c != 'f' && c2 == '8'
     ) {
      this.position += 3;
      return c == 'i' ? (2) : (1);
     }
     else if (c3 > '\0' && end_cast_char == this.code[this.position + 3]) {
      switch (c = ((c) | (c2) << 8 | (c3) << 16)) {
       case (((int)('f') & 0xFF) | ((int)('3') & 0xFF) << 8 | ((int)('2') & 0xFF) << 16):
        this.position += 4;
        return (9);
       case (((int)('f') & 0xFF) | ((int)('6') & 0xFF) << 8 | ((int)('4') & 0xFF) << 16):
        this.position += 4;
        return (10);
       case (((int)('u') & 0xFF) | ((int)('1') & 0xFF) << 8 | ((int)('6') & 0xFF) << 16):
        this.position += 4;
        return (3);
       case (((int)('i') & 0xFF) | ((int)('1') & 0xFF) << 8 | ((int)('6') & 0xFF) << 16):
        this.position += 4;
        return (4);
       case (((int)('u') & 0xFF) | ((int)('3') & 0xFF) << 8 | ((int)('2') & 0xFF) << 16):
        this.position += 4;
        return (5);
       case (((int)('i') & 0xFF) | ((int)('3') & 0xFF) << 8 | ((int)('2') & 0xFF) << 16):
        this.position += 4;
        return (6);
       case (((int)('u') & 0xFF) | ((int)('6') & 0xFF) << 8 | ((int)('4') & 0xFF) << 16):
        this.position += 4;
        return (7);
       case (((int)('i') & 0xFF) | ((int)('6') & 0xFF) << 8 | ((int)('4') & 0xFF) << 16):
        this.position += 4;
        return (8);
      }
     }
    }
   default:
    return (7);
  }
 }
 public int calc_size() {
  this.position = 0;
  int size = 0;
  this.value_type = (0);
  for (;;) {
   byte c;
   switch (c = this.code[this.position]) {
    case '/':
     if (this.parse_comments()) {
      continue;
     }
    case '\0':
     return size;
    case '(': case '{': case '[':
     ++this.position;
     this.value_type = c == '[' ? (5) : this.check_for_cast(':');
     this.parse_brackets(c);
     break;
    case '<':
     Expression.get_patch_value(this, '>');
     break;
    case '?':
     ++this.position;
     if (this.code[this.position] != '?') {
      continue;
     }
     this.value_type = (1);
     ++this.position;
     break;
    case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
    case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
    case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
     c = this.code[++this.position];
     if (!((((((int)((c)-'0'))&(0xFF))<(((int)(10))&(0xFF)))) || (((((int)(((c)|(0x20))-'a'))&(0xFF))<(((int)(6))&(0xFF)))))) {
      continue;
     }
     this.value_type = (1);
     ++this.position;
     break;
    case '\'':
     this.parse_character_literal();
     this.value_type = (1);
    default:
     ++this.position;
     continue;
   }
   switch (this.value_type) {
    case (1): case (2):
     size += (1);
     break;
    case (3): case (4):
     size += (2);
     break;
    case (5): case (6):
     size += (4);
     break;
    case (7): case (8):
     size += (8);
     break;
    case (9):
     size += (4);
     break;
    case (10):
     size += (8);
     break;
   }
  }
 }
 public void render() {
  this.position = 0;
  char_loop: for(;;) {
   byte c;
   switch (c = this.code[this.position]) {
    case '/':
     if (this.parse_comments()) {
      continue;
     }
    case '\0':
     break char_loop;
    case '(': case '{':
     ++this.position;
     this.check_for_cast(':');
     if (c == '(') {
      Expression.evaluate(this, ')');
     }
     else {
      Expression.evaluate(this, '}');
     }
    case '\'':
     this.value = this.parse_character_literal();
     this.value_type = (1);
    default:
     ++this.position;
     break;
   }
  }
 }
}
