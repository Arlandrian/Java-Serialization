package com.arlandria.raincolud.serialization;

import static com.arlandria.raincolud.serialization.SerializationUtils.*;

import java.util.ArrayList;
import java.util.List;
public class RCObject extends RCBase {
	public static final byte CONTAINER_TYPE = ContainerType.OBJECT; //DataStorageType (field, array, object)

	private int fieldCount=0;
	private int arrayCount=0;
	private int stringCount = 0;
	public List<RCField> fields = new ArrayList<RCField>();
	public List<RCArray> arrays = new ArrayList<RCArray>();
	public List<RCString> strings = new ArrayList<RCString>();
	
	
	public RCObject(String name) {
		size += 1 + 4 + 4 + 4;
		setName(name);
	}
	private RCObject() {
		//size = 1 + 4 + 4 + 4;
	}
	
	public void addField(RCField field) {
		fields.add(field);
		size += field.getSize();
		fieldCount = fields.size();
	}
	
	public void addArray(RCArray array) {
		arrays.add(array);
		size += array.getSize();
		arrayCount = arrays.size();
	}
	
	public void addString(RCString string) {
		strings.add(string);
		size += string.getSize();
		stringCount = strings.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public int getBytes(byte[] dest,int pointer) {
		dest[pointer++] = CONTAINER_TYPE;
		pointer = writeBytes(dest,pointer,nameLength);
		pointer = writeBytes(dest,pointer,name);
		pointer = writeBytes(dest,pointer,size);
		
		pointer = writeBytes(dest,pointer,fieldCount);	
		for(RCField field : fields) {
			pointer = field.getBytes(dest, pointer);
		}
		
		pointer = writeBytes(dest,pointer,stringCount);	
		for(RCString string : strings) {
			pointer = string.getBytes(dest, pointer);
		}
		
		pointer = writeBytes(dest,pointer,arrayCount);	
		for(RCArray array : arrays) {
			pointer = array.getBytes(dest, pointer);
		}
		
		return pointer;
	}
	
	public static RCObject Deserialize(byte[] data, int pointer) {
		byte containerType = data[pointer++];
		assert(data[pointer++] == CONTAINER_TYPE);
		
		RCObject result = new RCObject();
		result.nameLength = readShort(data,pointer);
		pointer+= 2;
		result.name = readString(data,pointer,result.nameLength).getBytes();
		pointer += result.nameLength ;
		
		result.size = readInt(data,pointer);
		pointer +=4;
		
		result.fieldCount = readInt(data,pointer);
		pointer +=4;
		
		for(int i = 0 ; i < result.fieldCount ; i++) {
			RCField field = RCField.Deserialize(data,pointer);
			result.fields.add(field);
			pointer += field.getSize();
		}
		
		result.stringCount = readInt(data,pointer);
		pointer +=4;
		for(int i = 0 ; i < result.stringCount ; i++) {
			RCString string = RCString.Deserialize(data,pointer);
			result.strings.add(string);
			pointer += string.getSize();
		}
		
		result.arrayCount = readInt(data,pointer);
		pointer +=4;
		for(int i = 0 ; i < result.arrayCount ; i++) {
			RCArray array = RCArray.Deserialize(data,pointer);
			result.arrays.add(array);
			pointer += array.getSize();
		}

		return result;
	}
	
	public RCField findField(String name) {
		for(RCField field : fields) {
			if(new String(field.name).equals(name)) {
				return field;
			}
		}
		return null;
	}
	
	public RCString findString(String name) {
		for(RCString string : strings) {
			if(new String(string.name).equals(name)) {
				return string;
			}
		}
		return null;
	}
	
	public RCArray findArray(String name) {
		for(RCArray array : arrays) {
			if(new String(array.name).equals(name)) {
				return array;
			}
		}
		return null;
	}
	
	
	
}
