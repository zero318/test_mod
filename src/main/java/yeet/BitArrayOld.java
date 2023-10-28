package yeet;
public class BitArrayOld {
	private long values[];
	public BitArrayOld(int bits) {
		this.values = new long[(bits + 63) >>> 6];
	}
	public final int read_bit_raw(int bit_index) {
		return (int)(this.values[bit_index >>> 6] >>> bit_index) & 1;
	}
	public final boolean read_bit(int bit_index) {
		return this.read_bit_raw(bit_index) != 0;
	}
	public final void write_bit_raw(int bit_index, int value) {
		int array_index = bit_index >>> 6;
		this.values[array_index] = (this.values[array_index] & ~(1L << bit_index)) | (value << bit_index);
	}
	public final void write_bit(int bit_index, boolean value) {
		this.write_bit_raw(bit_index, value ? 1 : 0);
	}
	public final void clear_bit(int bit_index) {
		this.values[bit_index >>> 6] &= ~(1L << bit_index);
	}
	public final void set_bit(int bit_index) {
		this.values[bit_index >>> 6] |= (1L << bit_index);
	}
}
