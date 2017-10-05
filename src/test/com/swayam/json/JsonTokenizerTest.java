package test.com.swayam.json;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

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
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testTokenizeWithArrayJson() {
		String text = "{\"name\":[\"swayam\",\"jayesh\",\"gokul\",\"rahul\"]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testTokenizeWithObject() {
		String text = "{\"level\":\"advanced\",\"name\":{\"last\":\"raina\",\"first\":\"swayam\"},\"id\":\"12345\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testTokenizeWithRecursiveObjects() {
		String text = "{\"level\":\"advanced\",\"name\":{\"last\":{\"name\":\"raina\"}}}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement
		assertEquals(text, json.serialize());
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
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testFloatPrimitiveInJson() {
		String text = "{\"cost\": 100.00}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testIntegerPrimitiveInJson() {
		String text = "{\"age\" : 23}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testNegativeIntegerPrimitiveInJson() {
		String text = "{\"loss percent\" : -23}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
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
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
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
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testNullPrimitiveInJson() {
		String text = "{\"class\":null}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testObjectsInArray() {
		String text = "{\"class\":[{\"roll1\":{\"fname\":\"swayam\",\"lname\":\"raina\"}},{\"roll2\":{\"fname\":\"ujjwal\",\"lname\":\"raina\"}}],\"total\":2}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
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
		// may fail because tokenizer internally changes
		// the ordering of the inner elements
		assertEquals(dependent, standAlone);
	}
	
	@Test
	public void testJsonArrayWithPrimitives() {
		String text = "{\"numbers\": [1,2,3,4,5,6,7,8,9] }";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testString() {
		String text = "{\"address\": \"347 Everit Street, Cotopaxi, Florida, 215\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		// may fail because of internal rearrangement or space trimming
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testJsonCreationFromFile() throws IOException {
		File jsonFile = new File("/home/swayam/Json/src/test/com/swayam/json/files/test1.json");
		JsonObject json = new JsonTokenizer().tokenize(jsonFile);
		System.out.println(json.prettify());
		System.out.println();
	}
	
}

