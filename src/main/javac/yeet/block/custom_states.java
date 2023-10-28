package yeet.block;

#include "custom_state_util.h"

import yeet.block.SparseIntegerProperty;

import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CustomStates {
	
	public static int[] WIRE_STATES = { VALID_SOUL_DUST_STATES };
	
	public static final IntegerProperty RESISTANCE = IntegerProperty.create("resistance", 1, 15);
	public static final EnumProperty<InspectorMode> MODE_INSPECTOR = EnumProperty.create("mode", InspectorMode.class);
	//public static final IntegerProperty CONNECTIONS = IntegerProperty.create("connections", 0, 255);
	public static final SparseIntegerProperty CONNECTIONS = SparseIntegerProperty.create("connections", VALID_SOUL_DUST_STATES);
	
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