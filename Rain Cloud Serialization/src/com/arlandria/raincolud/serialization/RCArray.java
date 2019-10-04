package com.arlandria.raincolud.serialization;

import static com.arlandria.raincolud.serialization.SerializationUtils.*;


public class RCArray extends RCBase{
	public static final byte CONTAINER_TYPE = ContainerType.ARRAY; //DataStorageType (field, array, object)
	
	public byte type; 
	public int count;
	public byte[] data;
	
	private short[]		shortData;
	private char[]		charData;
	private int[]		intData;
	private long[]		longData;
	private float[]		floatData;
	private double[]	doubleData;
	private boolean[]	booleanData;
	
	private RCArray() {
		size += 1 + 1 + 4 ;
	};
	
	public int getBytes(byte[] dest,int pointer) {
		dest[pointer++] = CONTAINER_TYPE;
		pointer = writeBytes(dest,pointer,nameLength);
		pointer = writeBytes(dest,pointer,name);
		pointer = writeBytes(dest,pointer,type);
		pointer = writeBytes(dest,pointer,count);
		
		switch(type) {
			case Type.BYTE: writeBytes(dest,pointer,data);break;
			case Type.SHORT: writeBytes(dest,pointer,shortData);break;
			case Type.CHAR: writeBytes(dest,pointer,charData);break;
			case Type.INT: writeBytes(dest,pointer,intData);break;
			case Type.FLOAT: writeBytes(dest,pointer,floatData);break;
			case Type.LONG: writeBytes(dest,pointer,longData);break;
			case Type.DOUBLE: writeBytes(dest,pointer,doubleData);break;
			case Type.BOOLEAN: writeBytes(dest,pointer,booleanData);break;
		}
		return pointer;
	}
	
	private void updateSize() {
		size += getDataSize();
	}
	
	
	public int getSize() {
		return size;// Type.getSize(Type.BYTE) + Type.getSize(Type.SHORT) + 
					// name.length + Type.getSize(Type.BYTE)+ Type.getSize(Type.INT) + getDataSize();
	}
	
	public int getDataSize() {
		switch(type) {
		case Type.BYTE:		return count * Type.getSize(Type.BYTE);
		case Type.SHORT:	return count * Type.getSize(Type.SHORT);
		case Type.CHAR:		return count * Type.getSize(Type.CHAR);
		case Type.INT:		return count * Type.getSize(Type.INT);
		case Type.FLOAT:	return count * Type.getSize(Type.FLOAT);
		case Type.LONG:		return count * Type.getSize(Type.LONG);
		case Type.DOUBLE:	return count * Type.getSize(Type.DOUBLE);
		case Type.BOOLEAN:	return count * Type.getSize(Type.BOOLEAN);
		}
		return 0;
	}
	
	public static RCArray Byte(String name, byte[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.BYTE;
		array.count = data.length;
		array.data = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Short(String name, short[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.SHORT;
		array.count = data.length;
		array.shortData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Char(String name, char[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.CHAR;
		array.count = data.length;
		array.charData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Integer(String name, int[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.INT;
		array.count = data.length;
		array.intData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Long(String name, long[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.LONG;
		array.count = data.length;
		array.longData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Float(String name, float[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.FLOAT;
		array.count = data.length;
		array.floatData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Double(String name, double[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.DOUBLE;
		array.count = data.length;
		array.doubleData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Boolean(String name, boolean[] data) {
		RCArray array = new RCArray();
		array.setName(name);
		array.type = Type.BOOLEAN;
		array.count = data.length;
		array.booleanData = data;
		array.updateSize();
		return array;
	}
	
	public static RCArray Deserialize(byte []data,int pointer) {
		
		byte containerType = data[pointer++];
		assert(data[pointer++] == CONTAINER_TYPE);
		
		RCArray result = new RCArray();
		result.nameLength = readShort(data,pointer);
		pointer+= 2;
		result.name = readString(data,pointer,result.nameLength).getBytes();
		pointer += result.nameLength ;
		result.type= readByte(data,pointer);
		pointer++;
		result.count = readInt(data,pointer);
		pointer+= 4;
		
		int typeSize = Type.getSize(result.type);

		
		switch(result.type) {
		case Type.BYTE:
			result.data = new byte[result.count];
			readBytes(data,pointer,result.data);
			break;
		case Type.SHORT:
			result.shortData = new short[result.count];
			readShorts(data,pointer,result.shortData);
			break;
		case Type.CHAR:
			result.charData = new char[result.count];
			readChars(data,pointer,result.charData);
			break;
		case Type.INT:
			result.intData = new int[result.count];
			readInts(data,pointer,result.intData);
			break;
		case Type.FLOAT:
			result.floatData = new float[result.count];
			readFloats(data,pointer,result.floatData);
			break;
		case Type.LONG:
			result.longData = new long[result.count];
			readLongs(data,pointer,result.longData);
			break;
		case Type.DOUBLE:
			result.doubleData = new double[result.count];
			readDoubles(data,pointer,result.doubleData);
			break;
		case Type.BOOLEAN:
			result.booleanData = new boolean[result.count];
			readBooleans(data,pointer,result.booleanData);
			break;
		}
		pointer += result.count * Type.getSize(result.type);
		return result;
	}
}
