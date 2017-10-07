package test.com.swayam.json;

import static org.junit.Assert.*;

import org.junit.Test;

import main.com.swayam.json.JsonObject;
import main.com.swayam.json.JsonTokenizer;

public class JsonObjectTest {
	
	@Test
	public void testStandAlonePrettify() {
		String text = "{\"total\":2,\"class\":[{\"roll1\":{\"fname\":\"swayam\",\"lname\":\"raina\"}},{\"roll2\":{\"fname\":\"ujjwal\",\"lname\":\"raina\"}}]}";
		System.out.println(JsonObject.prettify(text));
		System.out.println();
	}
	
	@Test
	public void compareStandAlonePrettify() {
		String text = "{\"total\":2,\"class\":[{\"roll1\":{\"fname\":\"swayam\",\"lname\":\"raina\"}},{\"roll2\":{\"fname\":\"ujjwal\",\"lname\":\"raina\"}}]}";
		String standAlone = JsonObject.prettify(text);
		String dependent = new JsonTokenizer().tokenize(text).prettify();
		assertEquals(dependent, standAlone);
	}
	
	@Test
	public void testComputeLengthForPrimitives() {
		int t1=100;
		float t2=1.032f;
		boolean t3=true;
		String t4="hello";
		
		Object o = t1;
		assertEquals(3, JsonObject.computeLengthForPrimitives(o));
		o = t2;
		assertEquals(5, JsonObject.computeLengthForPrimitives(o));
		o = t3;
		assertEquals(4, JsonObject.computeLengthForPrimitives(o));
		o = t4;
		assertEquals(7, JsonObject.computeLengthForPrimitives(o));
	}
	
	@Test
	public void testString() {
		String text = "{\"address\":\"347 Everit Street, Cotopaxi, Florida, 215\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
}

