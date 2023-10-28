package yeet;

#include "util.h"

import java.util.Arrays;
import java.nio.charset.StandardCharsets;

public class CodeStringVar {
	byte code[];
	int position;
	//int length;
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
		//this.length = code.length();
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
			paren_count += b2i(c == '(') - b2i(c == ')');
			square_count += b2i(c == '[') - b2i(c == ']');
			curly_count += b2i(c == '{') - b2i(c == '}');
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
		switch (c = lowercase(this.code[this.position])) {
			case 'i': case 'u': case 'f': 
				if ((c2 = this.code[this.position + 1]) > '\0') {
					if ((c3 = this.code[this.position + 2]) == end_cast_char &&
						c != 'f' && c2 == '8'
					) {
						this.position += 3;
						return c == 'i' ? PVT_SBYTE : PVT_BYTE;
					}
					else if (c3 > '\0' && end_cast_char == this.code[this.position + 3]) {
						switch (c = TextSInt(c, c2, c3)) {
							case TextInt('f', '3', '2'):
								this.position += 4;
								return PVT_FLOAT;
							case TextInt('f', '6', '4'):
								this.position += 4;
								return PVT_DOUBLE;
							//case TextInt('f', '8', '0'):
								//this.position += 4;
								//return PVT_LONGDOUBLE;
							case TextInt('u', '1', '6'):
								this.position += 4;
								return PVT_WORD;
							case TextInt('i', '1', '6'):
								this.position += 4;
								return PVT_SWORD;
							case TextInt('u', '3', '2'):
								this.position += 4;
								return PVT_DWORD;
							case TextInt('i', '3', '2'):
								this.position += 4;
								return PVT_SDWORD;
							case TextInt('u', '6', '4'):
								this.position += 4;
								return PVT_QWORD;
							case TextInt('i', '6', '4'):
								this.position += 4;
								return PVT_SQWORD;
						}
					}
				}
			default:
				return PVT_DEFAULT;
		}
	}
	
	public int calc_size() {
		this.position = 0;
		int size = 0;
		this.value_type = PVT_NONE;
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
					this.value_type = c == '[' ? PVT_DWORD : this.check_for_cast(':');
					this.parse_brackets(c); // Can throw
					break;
				case '<':
					Expression.get_patch_value(this, '>'); // Can throw
					break;
				case '?':
					++this.position;
					if (this.code[this.position] != '?') {
						continue;
					}
					this.value_type = PVT_BYTE;
					++this.position;
					break;
				case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
				case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
				case 'A': case 'B': case 'C': case 'D': case 'E': case 'F':
					c = this.code[++this.position];
					if (!(is_valid_decimal(c) || is_valid_hex_letter(c))) {
						continue;
					}
					this.value_type = PVT_BYTE;
					++this.position;
					break;
				//case '+': case '-':
					//this.value_size = this.consume_float_value(); // Can throw
					//break;
				case '\'':
					this.parse_character_literal();
					this.value_type = PVT_BYTE;
				default:
					++this.position;
					continue;
			}
			switch (this.value_type) {
				case PVT_BYTE: case PVT_SBYTE:
					size += sizeof_type(byte);
					break;
				case PVT_WORD: case PVT_SWORD:
					size += sizeof_type(short);
					break;
				case PVT_DWORD: case PVT_SDWORD:
					size += sizeof_type(int);
					break;
				case PVT_QWORD: case PVT_SQWORD:
					size += sizeof_type(long);
					break;
				case PVT_FLOAT:
					size += sizeof_type(float);
					break;
				case PVT_DOUBLE:
					size += sizeof_type(double);
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
					this.value_type = PVT_BYTE;
				default:
					++this.position;
					break;
			}
		}
	}
}