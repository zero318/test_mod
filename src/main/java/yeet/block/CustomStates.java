package yeet.block;

import yeet.block.SparseIntegerProperty;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
public class CustomStates {
 public static int[] WIRE_STATES = { 0, 5, 6, 9, 10, 17, 18, 20, 21, 22, 24, 25, 26, 33, 34, 36, 37, 38, 40, 41, 42, 65, 66, 68, 69, 70, 72, 73, 74, 80, 81, 82, 84, 85, 86, 88, 89, 90, 96, 97, 98, 100, 101, 102, 104, 105, 106, 129, 130, 132, 133, 134, 136, 137, 138, 144, 145, 146, 148, 149, 150, 152, 153, 154, 160, 161, 162, 164, 165, 166, 168, 169, 170 };
 public static final IntegerProperty RESISTANCE = IntegerProperty.create("resistance", 1, 15);
 public static final EnumProperty<InspectorMode> MODE_INSPECTOR = EnumProperty.create("mode", InspectorMode.class);
 public static final SparseIntegerProperty CONNECTIONS = SparseIntegerProperty.create("connections", 0, 5, 6, 9, 10, 17, 18, 20, 21, 22, 24, 25, 26, 33, 34, 36, 37, 38, 40, 41, 42, 65, 66, 68, 69, 70, 72, 73, 74, 80, 81, 82, 84, 85, 86, 88, 89, 90, 96, 97, 98, 100, 101, 102, 104, 105, 106, 129, 130, 132, 133, 134, 136, 137, 138, 144, 145, 146, 148, 149, 150, 152, 153, 154, 160, 161, 162, 164, 165, 166, 168, 169, 170);
 public enum InspectorMode implements StringRepresentable {
  DIGITAL("digital"),
  ANALOG("analog");
  private final String name;
  InspectorMode(String name) {
   this.name = name;
  }
  @Override
  public String getSerializedName() {
   return this.name;
  }
 }
}
