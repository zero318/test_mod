package yeet;

#include "util.h"

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;

public class YeetUtil {
    public static final byte bool_to_byte(boolean value) {
        return value ? (byte)1 : 0;
    }
    public static final boolean byte_to_bool(byte value) {
        return value != 0;
    }
    public static final short bool_to_short(boolean value) {
        return value ? (short)1 : 0;
    }
    public static final boolean short_to_bool(short value) {
        return value != 0;
    }
    public static final int bool_to_int(boolean value) {
        return value ? 1 : 0;
    }
    public static final boolean int_to_bool(int value) {
        return value != 0;
    }
    public static final long bool_to_long(boolean value) {
        return value ? 1 : 0;
    }
    public static final boolean long_to_bool(long value) {
        return value != 0;
    }
	
	/* public static final boolean is_valid_decimal(char c) {
        return Integer.compareUnsigned(c - '0', 10) < 0;
    } */
    public static final boolean is_valid_hex(char c) {
        return is_valid_decimal(c) || ULSS((c | 0x20) - 'a', 6);
    }
	
	public static final byte rotate_left_byte(byte value, int amount) {
		amount &= 0x7;
		return (byte)((int)value << amount | (int)value >>> BYTE_WIDTH - amount);
	}
	public static final short rotate_left_short(short value, int amount) {
		amount &= 0xF;
		return (short)((int)value << amount | (int)value >>> SHORT_WIDTH - amount);
	}
	public static final int rotate_left_int(int value, int amount) {
		return value << amount | value >>> INT_WIDTH - amount;
	}
	public static final long rotate_left_long(long value, int amount) {
		return value << amount | value >>> LONG_WIDTH - amount;
	}
	
	public static final long upow64(long value, long arg) {
		if (arg == 0L) return 1L;
		long result = 1L;
		switch (BSR64(value)) {
			default:
				return INT64_MAX;
			case 5:
				result = l2b(arg & 1L) ? value : 1L;
				arg >>>= 1;
				value *= value;
			case 4:
				result *= l2b(arg & 1L) ? value : 1L;
				arg >>>= 1;
				value *= value;
			case 3:
				result *= l2b(arg & 1L) ? value : 1L;
				arg >>>= 1;
				value *= value;
			case 2:
				result *= l2b(arg & 1L) ? value : 1L;
				arg >>>= 1;
				value *= value;
			case 1:
				result *= l2b(arg & 1L) ? value : 1L;
				arg >>>= 1;
				value *= value;
			case 0:
				return result * (l2b(arg & 1L) ? value : 1L);
		}
	}
	
	public static final int upow32(int value, int arg) {
		if (arg == 0) return 1;
		int result = 1;
		switch (BSR32(value)) {
			default:
				return INT32_MAX;
			case 4:
				result = i2b(arg & 1) ? value : 1;
				arg >>>= 1;
				value *= value;
			case 3:
				result *= i2b(arg & 1) ? value : 1;
				arg >>>= 1;
				value *= value;
			case 2:
				result *= i2b(arg & 1) ? value : 1;
				arg >>>= 1;
				value *= value;
			case 1:
				result *= i2b(arg & 1) ? value : 1;
				arg >>>= 1;
				value *= value;
			case 0:
				return result * (i2b(arg & 1) ? value : 1);
		}
	}
	
	public static final byte to_lower(byte c) {
		return (byte)(c | (ULSS8(c - 'A', 26) ? 0x20 : 0));
	}
	public static final byte to_upper(byte c) {
		return (byte)(ULSS8(c - 'a', 26) ? c & 0xDF : c);
	}
	
	public static final boolean strnicmp(int offset, byte strA[], byte strB[], int count) {
		for (int i = 0; i < count; ++i) {
			byte c1 = strA[offset + i];
			if (ULSS8(c1 - 'A', 26)) c1 |= 0x20;
			byte c2 = strB[i];
			if (ULSS8(c2 - 'A', 26)) c2 |= 0x20;
			if (c1 != c2) return false;
		}
		return true;
	}
	
	public static final Vec3i DOWN_OFFSET_VEC = Direction.DOWN.getNormal();
	public static final Vec3i UP_OFFSET_VEC = Direction.UP.getNormal();
	public static final Vec3i NORTH_OFFSET_VEC = Direction.NORTH.getNormal();
	public static final Vec3i SOUTH_OFFSET_VEC = Direction.SOUTH.getNormal();
	public static final Vec3i WEST_OFFSET_VEC = Direction.WEST.getNormal();
	public static final Vec3i EAST_OFFSET_VEC = Direction.EAST.getNormal();
	
	public static final Vec3i NORTH_DOWN_OFFSETS_VEC = new Vec3i(NORTH_DOWN_OFFSETS);
	public static final Vec3i NORTH_UP_OFFSETS_VEC = new Vec3i(NORTH_UP_OFFSETS);
	public static final Vec3i NORTH_WEST_OFFSETS_VEC = new Vec3i(NORTH_WEST_OFFSETS);
	public static final Vec3i NORTH_EAST_OFFSETS_VEC = new Vec3i(NORTH_EAST_OFFSETS);

	public static final Vec3i SOUTH_DOWN_OFFSETS_VEC = new Vec3i(SOUTH_DOWN_OFFSETS);
	public static final Vec3i SOUTH_UP_OFFSETS_VEC = new Vec3i(SOUTH_UP_OFFSETS);
	public static final Vec3i SOUTH_WEST_OFFSETS_VEC = new Vec3i(SOUTH_WEST_OFFSETS);
	public static final Vec3i SOUTH_EAST_OFFSETS_VEC = new Vec3i(SOUTH_EAST_OFFSETS);

	public static final Vec3i WEST_DOWN_OFFSETS_VEC = new Vec3i(WEST_DOWN_OFFSETS);
	public static final Vec3i WEST_UP_OFFSETS_VEC = new Vec3i(WEST_UP_OFFSETS);

	public static final Vec3i EAST_DOWN_OFFSETS_VEC = new Vec3i(EAST_DOWN_OFFSETS);
	public static final Vec3i EAST_UP_OFFSETS_VEC = new Vec3i(EAST_UP_OFFSETS);
	
	public static final Vec3i DOWN_TO_UP_DELTA_VEC = new Vec3i(DOWN_TO_UP_DELTA);
	// public static final Vec3i DOWN_TO_NORTH_DELTA_VEC = NORTH_UP_OFFSETS_VEC;
	// public static final Vec3i DOWN_TO_SOUTH_DELTA_VEC = SOUTH_UP_OFFSETS_VEC;
	// public static final Vec3i DOWN_TO_WEST_DELTA_VEC = WEST_UP_OFFSETS_VEC;
	// public static final Vec3i DOWN_TO_EAST_DELTA_VEC = EAST_UP_OFFSETS_VEC;


	public static final Vec3i UP_TO_DOWN_DELTA_VEC = new Vec3i(UP_TO_DOWN_DELTA);
	// public static final Vec3i UP_TO_NORTH_DELTA_VEC = NORTH_DOWN_OFFSETS_VEC;
	// public static final Vec3i UP_TO_SOUTH_DELTA_VEC = SOUTH_DOWN_OFFSETS_VEC;
	// public static final Vec3i UP_TO_WEST_DELTA_VEC = WEST_DOWN_OFFSETS_VEC;
	// public static final Vec3i UP_TO_EAST_DELTA_VEC = EAST_DOWN_OFFSETS_VEC;


	// public static final Vec3i NORTH_TO_DOWN_DELTA_VEC = SOUTH_DOWN_OFFSETS_VEC;
	// public static final Vec3i NORTH_TO_UP_DELTA_VEC = SOUTH_UP_OFFSETS_VEC;
	public static final Vec3i NORTH_TO_SOUTH_DELTA_VEC = new Vec3i(NORTH_TO_SOUTH_DELTA);
	// public static final Vec3i NORTH_TO_WEST_DELTA_VEC = SOUTH_WEST_OFFSETS_VEC;
	// public static final Vec3i NORTH_TO_EAST_DELTA_VEC = SOUTH_EAST_OFFSETS_VEC;

	// public static final Vec3i SOUTH_TO_DOWN_DELTA_VEC = NORTH_DOWN_OFFSETS_VEC;
	// public static final Vec3i SOUTH_TO_UP_DELTA_VEC = NORTH_UP_OFFSETS_VEC;
	public static final Vec3i SOUTH_TO_NORTH_DELTA_VEC = new Vec3i(SOUTH_TO_NORTH_DELTA);
	// public static final Vec3i SOUTH_TO_WEST_DELTA_VEC = NORTH_WEST_OFFSETS_VEC;
	// public static final Vec3i SOUTH_TO_EAST_DELTA_VEC = NORTH_EAST_OFFSETS_VEC;

	// public static final Vec3i WEST_TO_DOWN_DELTA_VEC = EAST_DOWN_OFFSETS_VEC;
	// public static final Vec3i WEST_TO_UP_DELTA_VEC = EAST_UP_OFFSETS_VEC;
	// public static final Vec3i WEST_TO_NORTH_DELTA_VEC = NORTH_EAST_OFFSETS_VEC;
	// public static final Vec3i WEST_TO_SOUTH_DELTA_VEC = SOUTH_EAST_OFFSETS_VEC;
	public static final Vec3i WEST_TO_EAST_DELTA_VEC = new Vec3i(WEST_TO_EAST_DELTA);

	// public static final Vec3i EAST_TO_DOWN_DELTA_VEC = WEST_DOWN_OFFSETS_VEC;
	// public static final Vec3i EAST_TO_UP_DELTA_VEC = WEST_UP_OFFSETS_VEC;
	// public static final Vec3i EAST_TO_NORTH_DELTA_VEC = NORTH_WEST_OFFSETS_VEC;
	// public static final Vec3i EAST_TO_SOUTH_DELTA_VEC = SOUTH_WEST_OFFSETS_VEC;
	public static final Vec3i EAST_TO_WEST_DELTA_VEC = new Vec3i(EAST_TO_WEST_DELTA);
	
	//
	// All Directions Order:
	// Down, Up, North, South, West, East
	//
	// Horizontal Plane Order:
	// North, East, South, West
	//
	// Vertical Plane Order:
	// Up, Down
	//
	// Shape Update Order:
	// West, East, North, South, Down, Up
	//
	// Update Neighbors At Order:
	// West, East, Down, Up, North, South
	//
	
	public static final Direction[] ALL_DIRECTIONS = {
		Direction.DOWN,
		Direction.UP,
		Direction.NORTH,
		Direction.SOUTH,
		Direction.WEST,
		Direction.EAST
	};
	
	public static final Vec3i[] horizontal_direction_offsets = {
		NORTH_OFFSET_VEC,
		EAST_OFFSET_VEC,
		SOUTH_OFFSET_VEC,
		WEST_OFFSET_VEC
	};
	
	public static final Vec3i[] vertical_direction_offsets = {
		UP_OFFSET_VEC,
		DOWN_OFFSET_VEC
	};
	
	public static final Vec3i[] all_direction_offsets = {
		DOWN_OFFSET_VEC,
		UP_OFFSET_VEC,
		NORTH_OFFSET_VEC,
		SOUTH_OFFSET_VEC,
		WEST_OFFSET_VEC,
		EAST_OFFSET_VEC
	};
	
	public static final Vec3i[] shape_update_offsets = {
		WEST_OFFSET_VEC,
		EAST_OFFSET_VEC,
		NORTH_OFFSET_VEC,
		SOUTH_OFFSET_VEC,
		DOWN_OFFSET_VEC,
		UP_OFFSET_VEC
	};
	
	public static final Vec3i[] update_neighbors_offsets = {
		WEST_OFFSET_VEC,
		EAST_OFFSET_VEC,
		DOWN_OFFSET_VEC,
		UP_OFFSET_VEC,
		NORTH_OFFSET_VEC,
		SOUTH_OFFSET_VEC
	};
	
	public static final Vec3i[] all_direction_iter_offsets = {
		DOWN_OFFSET_VEC,
		DOWN_TO_UP_DELTA_VEC,
		UP_TO_NORTH_DELTA_VEC,
		NORTH_TO_SOUTH_DELTA_VEC,
		SOUTH_TO_WEST_DELTA_VEC,
		WEST_TO_EAST_DELTA_VEC
	};
	
	public static final Vec3i[] horizontal_direction_iter_offsets = {
		NORTH_OFFSET_VEC,
		NORTH_TO_EAST_DELTA_VEC,
		EAST_TO_SOUTH_DELTA_VEC,
		SOUTH_TO_WEST_DELTA_VEC
	};
	
	public static final Vec3i[] vertical_direction_iter_offsets = {
		UP_OFFSET_VEC,
		UP_TO_DOWN_DELTA_VEC
	};
	
	public static final Vec3i[] shape_update_iter_offsets = {
		WEST_OFFSET_VEC,
		WEST_TO_EAST_DELTA_VEC,
		EAST_TO_NORTH_DELTA_VEC,
		NORTH_TO_SOUTH_DELTA_VEC,
		SOUTH_TO_DOWN_DELTA_VEC,
		DOWN_TO_UP_DELTA_VEC
	};
	
	public static final Vec3i[] update_neighbors_iter_offsets = {
		WEST_OFFSET_VEC,
		WEST_TO_EAST_DELTA_VEC,
		EAST_TO_DOWN_DELTA_VEC,
		DOWN_TO_UP_DELTA_VEC,
		UP_TO_NORTH_DELTA_VEC,
		NORTH_TO_SOUTH_DELTA_VEC
	};
	
	public static final int calculate_neighbor_direction(BlockPos self_pos, BlockPos neighbor_pos) {
		int temp;
		if (i2b(temp = self_pos.getX() - neighbor_pos.getX())) return WEST_ORDINAL + sign_bit(temp);
		if (i2b(temp = self_pos.getZ() - neighbor_pos.getZ())) return NORTH_ORDINAL + sign_bit(temp);
		return sign_bit(self_pos.getY() - neighbor_pos.getY());
	}
	
	public static final int calculate_neighbor_multi_direction(BlockPos self_pos, BlockPos neighbor_pos) {
		return (self_pos.getX() - neighbor_pos.getX() & 0x7) +
		((self_pos.getY() - neighbor_pos.getY() & 0x7) << 3) +
		((self_pos.getZ() - neighbor_pos.getZ() & 0x7) << 6);
	}
	
	public static final void update_neighbors(Level level, BlockPos blockPos, Block block, BlockPos.MutableBlockPos mutable_pos) {
		level.neighborChanged(mutable_pos_move(mutable_pos, WEST_OFFSETS), block, blockPos);
		level.neighborChanged(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), block, blockPos);
		level.neighborChanged(mutable_pos_move(mutable_pos, EAST_TO_DOWN_DELTA), block, blockPos);
		level.neighborChanged(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), block, blockPos);
		level.neighborChanged(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), block, blockPos);
		level.neighborChanged(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), block, blockPos);
	}
	
	public static final void update_neighbors(Level level, BlockPos blockPos, Block block) {
		YeetUtil.update_neighbors(level, blockPos, block, mutable_pos_create(blockPos));
	}
	
	public static final int get_signal(Level level, BlockPos blockPos, Direction direction, BlockPos.MutableBlockPos mutable_pos) {
		BlockState block_state = level.getBlockState(blockPos);
		int ret = block_state.getSignal(level, blockPos, direction);
		if (block_state.isRedstoneConductor(level, blockPos) &&
			(ret = Math.max(ret, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_OFFSETS), Direction.DOWN))) < MAX_REDSTONE_POWER &&
			(ret = Math.max(ret, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), Direction.UP))) < MAX_REDSTONE_POWER &&
			(ret = Math.max(ret, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), Direction.NORTH))) < MAX_REDSTONE_POWER &&
			(ret = Math.max(ret, level.getDirectSignal(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), Direction.SOUTH))) < MAX_REDSTONE_POWER &&
			(ret = Math.max(ret, level.getDirectSignal(mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), Direction.WEST))) < MAX_REDSTONE_POWER
		) {
			ret = Math.max(ret, level.getDirectSignal(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), Direction.EAST));
		}
		return ret;
	}
	
	public static final int get_signal(Level level, BlockPos blockPos, Direction direction) {
		return YeetUtil.get_signal(level, blockPos, direction, mutable_pos_create(blockPos));
	}
	
	/* public static final int get_best_neighbor_signal(Level level, BlockPos blockPos, int from_direction, BlockPos.MutableBlockPos mutable_pos) {
		int power = 0;
		for (int i = 0; i < 6; ++i) {
			if (i != from_direction) {
				BlockState block_state = level.getBlockState(mutable_pos.setWithOffset(blockPos, all_direction_offsets[i]));
				power = Math.max(power, block_state.getSignal(level, mutable_pos, ALL_DIRECTIONS[i]));
				if (power >= MAX_REDSTONE_POWER) break;
				if (block_state.isRedstoneConductor(level, mutable_pos)) {
					
				}
			}
		}
		return power & MAX_REDSTONE_POWER;
	} */
	
	public static final int get_best_neighbor_signal_unrolled(Level level, BlockPos.MutableBlockPos mutable_pos, int from_direction) {
		int power = 0;
		BlockState block_state;
		early_ret: do {
			//mutable_pos_move(mutable_pos, blockPos);
			if (from_direction != DOWN_ORDINAL) {
				block_state = level.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS));
				if ((power = block_state.getSignal(level, mutable_pos, Direction.DOWN)) >= MAX_REDSTONE_POWER) break early_ret;
				if (block_state.isRedstoneConductor(level, mutable_pos)) {
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_OFFSETS), Direction.DOWN))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_TO_NORTH_DELTA), Direction.NORTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), Direction.SOUTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), Direction.WEST))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), Direction.EAST))) >= MAX_REDSTONE_POWER) break early_ret;
					mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
				}
				mutable_pos_move(mutable_pos, DOWN_TO_MID_DELTA);
			}
			if (from_direction != UP_ORDINAL) {
				block_state = level.getBlockState(mutable_pos_move(mutable_pos, UP_OFFSETS));
				if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.UP))) >= MAX_REDSTONE_POWER) break early_ret;
				if (block_state.isRedstoneConductor(level, mutable_pos)) {
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_OFFSETS), Direction.UP))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), Direction.NORTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), Direction.SOUTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), Direction.WEST))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), Direction.EAST))) >= MAX_REDSTONE_POWER) break early_ret;
					mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
				}
				mutable_pos_move(mutable_pos, UP_TO_MID_DELTA);
			}
			if (from_direction != NORTH_ORDINAL) {
				block_state = level.getBlockState(mutable_pos_move(mutable_pos, NORTH_OFFSETS));
				if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.NORTH))) >= MAX_REDSTONE_POWER) break early_ret;
				if (block_state.isRedstoneConductor(level, mutable_pos)) {
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_OFFSETS), Direction.DOWN))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), Direction.UP))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_TO_SOUTH_DELTA), Direction.SOUTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), Direction.WEST))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), Direction.EAST))) >= MAX_REDSTONE_POWER) break early_ret;
					mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
				}
				mutable_pos_move(mutable_pos, NORTH_TO_MID_DELTA);
			}
			if (from_direction != SOUTH_ORDINAL) {
				block_state = level.getBlockState(mutable_pos_move(mutable_pos, SOUTH_OFFSETS));
				if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.SOUTH))) >= MAX_REDSTONE_POWER) break early_ret;
				if (block_state.isRedstoneConductor(level, mutable_pos)) {
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_OFFSETS), Direction.DOWN))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), Direction.UP))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), Direction.NORTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, NORTH_TO_WEST_DELTA), Direction.WEST))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), Direction.EAST))) >= MAX_REDSTONE_POWER) break early_ret;
					mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
				}
				mutable_pos_move(mutable_pos, SOUTH_TO_MID_DELTA);
			}
			if (from_direction != WEST_ORDINAL) {
				block_state = level.getBlockState(mutable_pos_move(mutable_pos, WEST_OFFSETS));
				if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.WEST))) >= MAX_REDSTONE_POWER) break early_ret;
				if (block_state.isRedstoneConductor(level, mutable_pos)) {
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_OFFSETS), Direction.DOWN))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), Direction.UP))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), Direction.NORTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), Direction.SOUTH))) >= MAX_REDSTONE_POWER) break early_ret;
					if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, SOUTH_TO_EAST_DELTA), Direction.EAST))) >= MAX_REDSTONE_POWER) break early_ret;
					mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
				}
				mutable_pos_move(mutable_pos, SOUTH_TO_MID_DELTA);
			}
			block_state = level.getBlockState(mutable_pos_move(mutable_pos, EAST_OFFSETS));
			if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.EAST))) >= MAX_REDSTONE_POWER) break early_ret;
			if (block_state.isRedstoneConductor(level, mutable_pos)) {
				if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_OFFSETS), Direction.DOWN))) >= MAX_REDSTONE_POWER) break early_ret;
				if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), Direction.UP))) >= MAX_REDSTONE_POWER) break early_ret;
				if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), Direction.NORTH))) >= MAX_REDSTONE_POWER) break early_ret;
				if ((power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), Direction.SOUTH))) >= MAX_REDSTONE_POWER) break early_ret;
				power = Math.max(power, level.getDirectSignal(mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), Direction.WEST));
			}
		} while (false);
		return power & MAX_REDSTONE_POWER;
	}
}