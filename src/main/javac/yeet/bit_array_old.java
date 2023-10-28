package yeet;

#include "util.h"

#define backing_type long

public class BitArrayOld {
	private backing_type values[];
	
#define bit_mask TYPE_SHIFT_MASK(backing_type)
	
#define array_count_for_bits(bit_count) (((bit_count) += bit_mask) >>> lsb_type(backing_type))
#define array_index_for_bit(bit_index) ((bit_index) >>> lsb_type(backing_type))
	
	public BitArrayOld(int bits) {
		this.values = new backing_type[array_count_for_bits(bits)];
	}
	
	public final int read_bit_raw(int bit_index) {
		return (int)SHR_TYPE(backing_type, this.values[array_index_for_bit(bit_index)], bit_index) & 1;
	}
	
	public final boolean read_bit(int bit_index) {
		return i2b(this.read_bit_raw(bit_index));
	}
	
	public final void write_bit_raw(int bit_index, int value) {
		int array_index = array_index_for_bit(bit_index);
		this.values[array_index] = this.values[array_index] & ~SHL_TYPE(backing_type, 1, bit_index) | SHL_TYPE(backing_type, value, bit_index);
	}
	
	public final void write_bit(int bit_index, boolean value) {
		this.write_bit_raw(bit_index, b2i(value));
	}
	
	public final void clear_bit(int bit_index) {
		this.values[array_index_for_bit(bit_index)] &= ~SHL_TYPE(backing_type, 1, bit_index);
	}
	
	public final void set_bit(int bit_index) {
		this.values[array_index_for_bit(bit_index)] |= SHL_TYPE(backing_type, 1, bit_index);
	}
}