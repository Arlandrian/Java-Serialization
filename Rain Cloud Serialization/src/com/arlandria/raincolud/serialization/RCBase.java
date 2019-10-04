package com.arlandria.raincolud.serialization;

public class RCBase {
	protected short nameLength;
	protected byte[] name;
	
	protected int size = 2 + 4;
	
	public void setName(String name) {
		assert(name.length() < Short.MAX_VALUE);
		
		if(this.name != null)
			size -= this.name.length;
		
		nameLength = (short)name.length();
		this.name = name.getBytes();
		size += this.name.length;
	}
	
	public String getName() {
		return new String(name);
	}
}
