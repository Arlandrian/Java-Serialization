import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Timer;

import com.arlandria.raincolud.serialization.RCArray;
import com.arlandria.raincolud.serialization.RCDatabase;
import com.arlandria.raincolud.serialization.RCField;
import com.arlandria.raincolud.serialization.RCObject;
import com.arlandria.raincolud.serialization.RCString;

import static com.arlandria.raincolud.serialization.SerializationUtils.*;

public class Main {
	
	static Random random = new Random();
	
	static void printBytes(byte[]data) {
		for(int i = 0; i < data.length; i++) {
			System.out.printf("0x%x ", data[i]);
		}
	}
	
	static void saveToFile(String path, byte[] data) {
		try {
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(path));
			stream.write(data);
			stream.close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void serializationTest() {
		int[] elements = new int[50] ;
		for(int i=0; i < elements.length ; i++ ) {
			elements[i] = random.nextInt();
		} 
		RCDatabase database = new RCDatabase("Database");
		
		RCObject object = new RCObject("Entity");
		RCObject object2 = new RCObject("Entity2");
		
		RCField field = RCField.Integer("Integer",88);
		RCField field2 = RCField.Short("xx",(short)4);
		RCField field3 = RCField.Short("yy",(short)8);
		
		RCArray array = RCArray.Integer("RandomNumbers", elements);
		
		object.addField(field);
		object.addField(field2);
		object.addField(field3);
		
		object.addString(RCString.Create("name_string","string it self!"));
		
		//object.addArray(array);
		
		database.addObject(object);
		database.addObject(object2);
		
		database.SerializeToFile("test.rcd");
	}
	
	public static void deserializationTest() {
		RCDatabase database = RCDatabase.DeserializeFromFile("test.rcd");
		printDatabase(database);
		
	}
	
	public static void printDatabase(RCDatabase database) {
		System.out.printf("[%x %x]Database: %s\n" ,database.VERSION>>8 ,database.VERSION&0xff  , database.getName() );
		System.out.println("Objects: ");
		for(RCObject object : database.objects) {
			System.out.println("--------------");
			System.out.println(object.getName()+":");
			System.out.println("\tFields: ");
			
			for(RCField field :object.fields) {
				System.out.println("\tname:" + field.getName());
			}
			System.out.println("\tStrings: ");
			for(RCString string :object.strings) {
				System.out.println("\tname:" + string.getName());
			}
			System.out.println("\tArrays: ");
			for(RCArray array :object.arrays) {
				System.out.println("\tname:" + array.getName());
			}
			
		}
	}
	
	public static void main(String[] args) {
		long timestamp = System.currentTimeMillis();
		
		//serializationTest();
		//deserializationTest();
		
		Sandbox sandbox = new Sandbox();
		sandbox.play();
		
		System.out.printf("%.3f second elapsed.",((float)(System.currentTimeMillis()-timestamp)/1000));
		//Field field
	}


}
