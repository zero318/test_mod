package yeet;

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
    public static final boolean is_valid_hex(char c) {
        return (((((int)((c)-'0'))&(0xFF))<(((int)(10))&(0xFF)))) || ((Integer.compareUnsigned((c | 0x20) - 'a',6))<0);
    }
 public static final byte rotate_left_byte(byte value, int amount) {
  amount &= 0x7;
  return (byte)((int)value << amount | (int)value >>> (8) - amount);
 }
 public static final short rotate_left_short(short value, int amount) {
  amount &= 0xF;
  return (short)((int)value << amount | (int)value >>> (16) - amount);
 }
 public static final int rotate_left_int(int value, int amount) {
  return value << amount | value >>> (32) - amount;
 }
 public static final long rotate_left_long(long value, int amount) {
  return value << amount | value >>> (64) - amount;
 }
 public static final long upow64(long value, long arg) {
  if (arg == 0L) return 1L;
  long result = 1L;
  switch ((63-(Long.numberOfLeadingZeros(((long)(value)))))) {
   default:
    return (0x7FFFFFFFFFFFFFFFL);
   case 5:
    result = ((arg & 1L) != 0L) ? value : 1L;
    arg >>>= 1;
    value *= value;
   case 4:
    result *= ((arg & 1L) != 0L) ? value : 1L;
    arg >>>= 1;
    value *= value;
   case 3:
    result *= ((arg & 1L) != 0L) ? value : 1L;
    arg >>>= 1;
    value *= value;
   case 2:
    result *= ((arg & 1L) != 0L) ? value : 1L;
    arg >>>= 1;
    value *= value;
   case 1:
    result *= ((arg & 1L) != 0L) ? value : 1L;
    arg >>>= 1;
    value *= value;
   case 0:
    return result * (((arg & 1L) != 0L) ? value : 1L);
  }
 }
 public static final int upow32(int value, int arg) {
  if (arg == 0) return 1;
  int result = 1;
  switch ((31-(Integer.numberOfLeadingZeros(((int)(value)))))) {
   default:
    return (0x7FFFFFFF);
   case 4:
    result = ((arg & 1) != 0) ? value : 1;
    arg >>>= 1;
    value *= value;
   case 3:
    result *= ((arg & 1) != 0) ? value : 1;
    arg >>>= 1;
    value *= value;
   case 2:
    result *= ((arg & 1) != 0) ? value : 1;
    arg >>>= 1;
    value *= value;
   case 1:
    result *= ((arg & 1) != 0) ? value : 1;
    arg >>>= 1;
    value *= value;
   case 0:
    return result * (((arg & 1) != 0) ? value : 1);
  }
 }
 public static final byte to_lower(byte c) {
  return (byte)(c | (((((int)(c - 'A'))&(0xFF))<(((int)(26))&(0xFF))) ? 0x20 : 0));
 }
 public static final byte to_upper(byte c) {
  return (byte)(((((int)(c - 'a'))&(0xFF))<(((int)(26))&(0xFF))) ? c & 0xDF : c);
 }
 public static final boolean strnicmp(int offset, byte strA[], byte strB[], int count) {
  for (int i = 0; i < count; ++i) {
   byte c1 = strA[offset + i];
   if (((((int)(c1 - 'A'))&(0xFF))<(((int)(26))&(0xFF)))) c1 |= 0x20;
   byte c2 = strB[i];
   if (((((int)(c2 - 'A'))&(0xFF))<(((int)(26))&(0xFF)))) c2 |= 0x20;
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
 public static final Vec3i NORTH_DOWN_OFFSETS_VEC = new Vec3i(0, -1, -1);
 public static final Vec3i NORTH_UP_OFFSETS_VEC = new Vec3i(0, 1, -1);
 public static final Vec3i NORTH_WEST_OFFSETS_VEC = new Vec3i(-1, 0, -1);
 public static final Vec3i NORTH_EAST_OFFSETS_VEC = new Vec3i(1, 0, -1);
 public static final Vec3i SOUTH_DOWN_OFFSETS_VEC = new Vec3i(0, -1, 1);
 public static final Vec3i SOUTH_UP_OFFSETS_VEC = new Vec3i(0, 1, 1);
 public static final Vec3i SOUTH_WEST_OFFSETS_VEC = new Vec3i(-1, 0, 1);
 public static final Vec3i SOUTH_EAST_OFFSETS_VEC = new Vec3i(1, 0, 1);
 public static final Vec3i WEST_DOWN_OFFSETS_VEC = new Vec3i(-1, -1, 0);
 public static final Vec3i WEST_UP_OFFSETS_VEC = new Vec3i(-1, 1, 0);
 public static final Vec3i EAST_DOWN_OFFSETS_VEC = new Vec3i(1, -1, 0);
 public static final Vec3i EAST_UP_OFFSETS_VEC = new Vec3i(1, 1, 0);
 public static final Vec3i DOWN_TO_UP_DELTA_VEC = new Vec3i(0, 2, 0);
 public static final Vec3i UP_TO_DOWN_DELTA_VEC = new Vec3i(0, -2, 0);
 public static final Vec3i NORTH_TO_SOUTH_DELTA_VEC = new Vec3i(0, 0, 2);
 public static final Vec3i SOUTH_TO_NORTH_DELTA_VEC = new Vec3i(0, 0, -2);
 public static final Vec3i WEST_TO_EAST_DELTA_VEC = new Vec3i(2, 0, 0);
 public static final Vec3i EAST_TO_WEST_DELTA_VEC = new Vec3i(-2, 0, 0);
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
  NORTH_DOWN_OFFSETS_VEC,
  NORTH_TO_SOUTH_DELTA_VEC,
  NORTH_WEST_OFFSETS_VEC,
  WEST_TO_EAST_DELTA_VEC
 };
 public static final Vec3i[] horizontal_direction_iter_offsets = {
  NORTH_OFFSET_VEC,
  SOUTH_EAST_OFFSETS_VEC,
  SOUTH_WEST_OFFSETS_VEC,
  NORTH_WEST_OFFSETS_VEC
 };
 public static final Vec3i[] vertical_direction_iter_offsets = {
  UP_OFFSET_VEC,
  UP_TO_DOWN_DELTA_VEC
 };
 public static final Vec3i[] shape_update_iter_offsets = {
  WEST_OFFSET_VEC,
  WEST_TO_EAST_DELTA_VEC,
  NORTH_WEST_OFFSETS_VEC,
  NORTH_TO_SOUTH_DELTA_VEC,
  NORTH_DOWN_OFFSETS_VEC,
  DOWN_TO_UP_DELTA_VEC
 };
 public static final Vec3i[] update_neighbors_iter_offsets = {
  WEST_OFFSET_VEC,
  WEST_TO_EAST_DELTA_VEC,
  WEST_DOWN_OFFSETS_VEC,
  DOWN_TO_UP_DELTA_VEC,
  NORTH_DOWN_OFFSETS_VEC,
  NORTH_TO_SOUTH_DELTA_VEC
 };
 public static final int calculate_neighbor_direction(BlockPos self_pos, BlockPos neighbor_pos) {
  int temp;
  if (((temp = self_pos.getX() - neighbor_pos.getX()) != 0)) return (4) + ((temp) >>> 31);
  if (((temp = self_pos.getZ() - neighbor_pos.getZ()) != 0)) return (2) + ((temp) >>> 31);
  return ((self_pos.getY() - neighbor_pos.getY()) >>> 31);
 }
 public static final int calculate_neighbor_multi_direction(BlockPos self_pos, BlockPos neighbor_pos) {
  return (self_pos.getX() - neighbor_pos.getX() & 0x7) +
  ((self_pos.getY() - neighbor_pos.getY() & 0x7) << 3) +
  ((self_pos.getZ() - neighbor_pos.getZ() & 0x7) << 6);
 }
 public static final void update_neighbors(Level level, BlockPos blockPos, Block block, BlockPos.MutableBlockPos mutable_pos) {
  level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)), block, blockPos);
  level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(2)), block, blockPos);
  level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(-1)), block, blockPos);
  level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(2)), block, blockPos);
  level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), block, blockPos);
  level.neighborChanged((mutable_pos).setZ((mutable_pos).getZ()+(2)), block, blockPos);
 }
 public static final void update_neighbors(Level level, BlockPos blockPos, Block block) {
  YeetUtil.update_neighbors(level, blockPos, block, (new BlockPos.MutableBlockPos((blockPos).getX(),(blockPos).getY(),(blockPos).getZ())));
 }
 public static final int get_signal(Level level, BlockPos blockPos, Direction direction, BlockPos.MutableBlockPos mutable_pos) {
  BlockState block_state = level.getBlockState(blockPos);
  int ret = block_state.getSignal(level, blockPos, direction);
  if (block_state.isRedstoneConductor(level, blockPos) &&
   (ret = Math.max(ret, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)), Direction.DOWN))) < 15 &&
   (ret = Math.max(ret, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(2)), Direction.UP))) < 15 &&
   (ret = Math.max(ret, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH))) < 15 &&
   (ret = Math.max(ret, level.getDirectSignal((mutable_pos).setZ((mutable_pos).getZ()+(2)), Direction.SOUTH))) < 15 &&
   (ret = Math.max(ret, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.WEST))) < 15
  ) {
   ret = Math.max(ret, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(2)), Direction.EAST));
  }
  return ret;
 }
 public static final int get_signal(Level level, BlockPos blockPos, Direction direction) {
  return YeetUtil.get_signal(level, blockPos, direction, (new BlockPos.MutableBlockPos((blockPos).getX(),(blockPos).getY(),(blockPos).getZ())));
 }
 public static final int get_best_neighbor_signal_unrolled(Level level, BlockPos.MutableBlockPos mutable_pos, int from_direction) {
  int power = 0;
  BlockState block_state;
  early_ret: do {
   if (from_direction != (0)) {
    block_state = level.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)));
    if ((power = block_state.getSignal(level, mutable_pos, Direction.DOWN)) >= 15) break early_ret;
    if (block_state.isRedstoneConductor(level, mutable_pos)) {
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)), Direction.DOWN))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(1)).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setZ((mutable_pos).getZ()+(2)), Direction.SOUTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.WEST))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(2)), Direction.EAST))) >= 15) break early_ret;
     (mutable_pos).setX((mutable_pos).getX()+(-1));
    }
    (mutable_pos).setY((mutable_pos).getY()+(1));
   }
   if (from_direction != (1)) {
    block_state = level.getBlockState((mutable_pos).setY((mutable_pos).getY()+(1)));
    if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.UP))) >= 15) break early_ret;
    if (block_state.isRedstoneConductor(level, mutable_pos)) {
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(1)), Direction.UP))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setZ((mutable_pos).getZ()+(2)), Direction.SOUTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.WEST))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(2)), Direction.EAST))) >= 15) break early_ret;
     (mutable_pos).setX((mutable_pos).getX()+(-1));
    }
    (mutable_pos).setY((mutable_pos).getY()+(-1));
   }
   if (from_direction != (2)) {
    block_state = level.getBlockState((mutable_pos).setZ((mutable_pos).getZ()+(-1)));
    if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.NORTH))) >= 15) break early_ret;
    if (block_state.isRedstoneConductor(level, mutable_pos)) {
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)), Direction.DOWN))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(2)), Direction.UP))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(1)), Direction.SOUTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.WEST))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(2)), Direction.EAST))) >= 15) break early_ret;
     (mutable_pos).setX((mutable_pos).getX()+(-1));
    }
    (mutable_pos).setZ((mutable_pos).getZ()+(1));
   }
   if (from_direction != (3)) {
    block_state = level.getBlockState((mutable_pos).setZ((mutable_pos).getZ()+(1)));
    if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.SOUTH))) >= 15) break early_ret;
    if (block_state.isRedstoneConductor(level, mutable_pos)) {
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)), Direction.DOWN))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(2)), Direction.UP))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(1)), Direction.WEST))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(2)), Direction.EAST))) >= 15) break early_ret;
     (mutable_pos).setX((mutable_pos).getX()+(-1));
    }
    (mutable_pos).setZ((mutable_pos).getZ()+(-1));
   }
   if (from_direction != (4)) {
    block_state = level.getBlockState((mutable_pos).setX((mutable_pos).getX()+(-1)));
    if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.WEST))) >= 15) break early_ret;
    if (block_state.isRedstoneConductor(level, mutable_pos)) {
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)), Direction.DOWN))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(2)), Direction.UP))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setZ((mutable_pos).getZ()+(2)), Direction.SOUTH))) >= 15) break early_ret;
     if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(1)).setZ((mutable_pos).getZ()+(-1)), Direction.EAST))) >= 15) break early_ret;
     (mutable_pos).setX((mutable_pos).getX()+(-1));
    }
    (mutable_pos).setZ((mutable_pos).getZ()+(-1));
   }
   block_state = level.getBlockState((mutable_pos).setX((mutable_pos).getX()+(1)));
   if ((power = Math.max(power, block_state.getSignal(level, mutable_pos, Direction.EAST))) >= 15) break early_ret;
   if (block_state.isRedstoneConductor(level, mutable_pos)) {
    if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)), Direction.DOWN))) >= 15) break early_ret;
    if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(2)), Direction.UP))) >= 15) break early_ret;
    if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH))) >= 15) break early_ret;
    if ((power = Math.max(power, level.getDirectSignal((mutable_pos).setZ((mutable_pos).getZ()+(2)), Direction.SOUTH))) >= 15) break early_ret;
    power = Math.max(power, level.getDirectSignal((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.WEST));
   }
  } while (false);
  return power & 15;
 }
}
