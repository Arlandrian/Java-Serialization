package com.arlandria.raincolud.serialization;

import static com.arlandria.raincolud.serialization.SerializationUtils.*;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RCDatabase extends RCBase {
	public static final byte[] HEADER = "RCDB".getBytes();
	public static final short VERSION = 0x0100;
	public static final byte CONTAINER_TYPE = ContainerType.DATABASE; //DataStorageType (field, array, object)

	private int objectCount;

	public List<RCObject> objects = new ArrayList<RCObject>();
	
	private RCDatabase() {}

	public RCDatabase(String name){
		size += HEADER.length + 2 + 1 + 4; 
		setName(name);
	}
	
	public void addObject(RCObject object) {
		objects.add(object);
		size += object.getSize();
		objectCount = objects.size();
	}
	
	public int getSize() {
		return size;
	}
	
	public int getBytes(byte[] dest,int pointer) {
		pointer = writeBytes(dest,pointer,HEADER);
		pointer = writeBytes(dest,pointer,VERSION);
		pointer = writeBytes(dest,pointer,CONTAINER_TYPE);
		pointer = writeBytes(dest,pointer,nameLength);
		pointer = writeBytes(dest,pointer,name);
		pointer = writeBytes(dest,pointer,size);
				
		pointer = writeBytes(dest,pointer,objectCount);	
		for(RCObject object : objects) {
			pointer = object.getBytes(dest, pointer);
		}
			
		return pointer;
	}
	
	public static RCDatabase Deserialize(byte [] data) {
		int pointer = 0;
		
		String header = readString(data,pointer,4);
		pointer+=HEADER.length;
		if(!header.equals(new String(HEADER))) {
			System.err.println("Invalid file type (Not RCDB).");
			return null;
		}
		short version = readShort(data,pointer);
		pointer+=2;
		
		if(version != VERSION) {
			System.err.println("Invalid RCDB version!.-"+version);
			return null;
		}
		
		byte containerType = readByte(data,pointer);
		assert(containerType == CONTAINER_TYPE);
		pointer++;
		
		RCDatabase result = new RCDatabase();
		
		result.nameLength = readShort(data,pointer);
		pointer += 2;
		result.name = readString(data,pointer,result.nameLength).getBytes();
		pointer += result.nameLength ;
		
		result.size = readInt(data,pointer);
		pointer +=4;

		result.objectCount = readInt(data,pointer);
		pointer +=4;

		for(int i = 0 ; i < result.objectCount ; i++) {
			RCObject object = RCObject.Deserialize(data,pointer);
			result.objects.add(object);
			pointer += object.getSize();
		}
		
		
		
		return result;
	}
	
	public static RCDatabase DeserializeFromFile(String path) {
		byte[] buffer = null;
		try {
			BufferedInputStream stream = new BufferedInputStream(new FileInputStream(path));
			buffer = new byte[stream.available()];
			stream.read(buffer);
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Deserialize(buffer);
	}
	
	public void SerializeToFile(String path) {
		byte[] data = new byte[getSize()];
		getBytes(data,0);
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
			stream.write(data);
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public RCObject findObject(String name) {
		for(RCObject object : objects) {
			if(new String(object.name).equals(name)) {
				return object;
			}
		}
		return null;
	}
	
}
