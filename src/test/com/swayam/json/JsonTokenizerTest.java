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
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testTokenizeWithRecursiveObjects() {
		String text = "{\"level\":\"advanced\",\"name\":{\"last\":{\"name\":\"raina\"}}}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testTokenizeWithRecursiveArray() {
		String text = "{\"roomamtes\":[[\"swayam\",\"gokul\"],[\"jayesh\"],[\"rahul\"]]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testFloatPrimitiveInJson() {
		String text = "{\"cost\":100.06}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testIntegerPrimitiveInJson() {
		String text = "{\"age\":23}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testNegativeIntegerPrimitiveInJson() {
		String text = "{\"loss percent\":-23}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test(expected = JsonException.class)
	public void testIntegerPrimitiveInJsonNegative() {
		String text = "{\"age\":23f}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test(expected = JsonException.class)
	public void testNegativeIntegerPrimitiveInJsonNegative() {
		String text = "{\"age\":-23f}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testTruePrimitiveInJson() {
		String text = "{\"eligible\":true}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test(expected = JsonException.class)
	public void testIncorrectTruePrimitiveInJson() {
		String text = "{\"eligible\":-true}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test(expected = JsonException.class)
	public void testInvalidStringPrimitiveInJsonNegative() {
		String text = "{\"eligible\":truea}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
	}
	
	@Test
	public void testFalsePrimitiveInJson() {
		String text = "{\"eligible\":false}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testNullPrimitiveInJson() {
		String text = "{\"class\":null}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testObjectsInArray() {
		String text = "{\"total\":2,\"class\":[{\"roll1\":{\"fname\":\"swayam\",\"lname\":\"raina\"}},{\"roll2\":{\"fname\":\"ujjwal\",\"lname\":\"raina\"}}]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
		assertEquals(text, json.serialize());
	}
	
	@Test
	public void testJsonArrayWithPrimitives() {
		String text = "{\"numbers\":[1,2,3,4,5,6,7,8,9]}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify());
		System.out.println();
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

