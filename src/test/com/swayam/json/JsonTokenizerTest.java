package test.com.swayam.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import main.com.swayam.json.JsonException;
import main.com.swayam.json.JsonObject;
import main.com.swayam.json.JsonTokenizer;

public class JsonTokenizerTest {

	@Test
	public void testTokenizeWithNormalJson() {
		String text = "{\"name\":\"swayam\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testTokenizeWithArrayJson() {
		String text = "{\"name\":[\"swayam\",\"jayesh\",\"gokul\",\"rahul\"]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testTokenizeWithObject() {
		String text = "{\"id\":\"12345\",\"name\":{\"first\":\"swayam\",\"last\":\"raina\"},\"level\":\"advanced\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testTokenizeWithRecursiveObjects() {
		String text = "{\"name\":{\"last\":{\"name\":\"raina\"}},\"level\":\"advanced\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
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
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testFloatPrimitiveInJson() {
		String text = "{\"cost\": 100.00}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testIntegerPrimitiveInJson() {
		String text = "{\"age\" : 23}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testNegativeIntegerPrimitiveInJson() {
		String text = "{\"loss percent\" : -23}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test(expected = JsonException.class)
	public void testIntegerPrimitiveInJsonNegative() {
		String text = "{\"age\" : 23f}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test(expected = JsonException.class)
	public void testNegativeIntegerPrimitiveInJsonNegative() {
		String text = "{\"age\" : -23f}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testTruePrimitiveInJson() {
		String text = "{\"eligible\": true}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test(expected = JsonException.class)
	public void testIncorrectTruePrimitiveInJson() {
		String text = "{\"eligible\": -true}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test(expected = JsonException.class)
	public void testInvalidStringPrimitiveInJsonNegative() {
		String text = "{\"eligible\": truea}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testFalsePrimitiveInJson() {
		String text = "{\"eligible\": false }";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testNullPrimitiveInJson() {
		String text = "{\"class\":null}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testObjectsInArray() {
		String text = "{\"class\":[{\"roll1\":{\"fname\":\"swayam\",\"lname\":\"raina\"}},{\"roll2\":{\"fname\":\"ujjwal\",\"lname\":\"raina\"}}],\"total\":2}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
}

