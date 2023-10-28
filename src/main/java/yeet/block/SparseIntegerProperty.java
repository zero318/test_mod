package yeet.block;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import net.minecraft.world.level.block.state.properties.Property;
public class SparseIntegerProperty extends Property<Integer> {
 private final ImmutableSet<Integer> values;
 protected SparseIntegerProperty(String string, long[] ranges) {
        super(string, Integer.class);
  HashSet hashSet = Sets.newHashSet();
  int length = ranges.length;
  for (int i = 0; i < length; ++i) {
   int lower, upper;
   long range = ranges[i];
   { (lower) = (int)((range) >> 32); (upper) = (int)(range); };
   for (int j = lower; j < upper; ++j) {
    hashSet.add(j);
   }
  }
        this.values = ImmutableSet.copyOf((Collection)hashSet);
    }
 protected SparseIntegerProperty(String string, int[] values) {
  super(string, Integer.class);
  HashSet hashSet = Sets.newHashSet();
  int length = values.length;
  for (int i = 0; i < length; ++i) {
   hashSet.add(values[i]);
  }
        this.values = ImmutableSet.copyOf((Collection)hashSet);
 }
 @SafeVarargs
 public static SparseIntegerProperty create(String string, long ... ranges) {
        return new SparseIntegerProperty(string, ranges);
    }
 @SafeVarargs
 public static SparseIntegerProperty create(String string, int ... values) {
  return new SparseIntegerProperty(string, values);
 }
 @Override
    public Collection<Integer> getPossibleValues() {
        return this.values;
    }
 @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object instanceof SparseIntegerProperty && super.equals(object)) {
            SparseIntegerProperty integerProperty = (SparseIntegerProperty)object;
            return this.values.equals(integerProperty.values);
        }
        return false;
    }
 @Override
    public int generateHashCode() {
        return 31 * super.generateHashCode() + this.values.hashCode();
    }
 @Override
    public Optional<Integer> getValue(String string) {
        try {
            Integer n = Integer.valueOf(string);
            return this.values.contains((Object)n) ? Optional.of(n) : Optional.empty();
        }
        catch (NumberFormatException numberFormatException) {
            return Optional.empty();
        }
    }
    @Override
    public String getName(Integer n) {
        return n.toString();
    }
}
