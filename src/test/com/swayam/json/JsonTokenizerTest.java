package test.com.swayam.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.com.swayam.json.JsonObject;
import main.com.swayam.json.JsonTokenizer;

public class JsonTokenizerTest {

	@Test
	public void testTokenizeWithNormalJson() {
		String text = "{\"name\":\"swayam\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
	}
	
	@Test
	public void testTokenizeWithArrayJson() {
		String text = "{\"name\":[\"swayam\",\"jayesh\",\"gokul\",\"rahul\"]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
	}
	
	@Test
	public void testTokenizeWithObject() {
		String text = "{\"id\":\"12345\",\"name\":{\"first\":\"swayam\",\"last\":\"raina\"},\"level\":\"advanced\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
	}
	
	@Test
	public void testTokenizeWithRecursiveObjects() {
		String text = "{\"name\":{\"last\":{\"name\":\"raina\"}},\"level\":\"advanced\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
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
	public void testTokenizeWithRecursiveArray() {
		String text = "{\"roomamtes\":[[\"swayam\", \"gokul\"],[\"jayesh\"],[\"rahul\"]]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
	}
	
}

