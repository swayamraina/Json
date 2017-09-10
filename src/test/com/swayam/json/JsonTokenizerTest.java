package test.com.swayam.json;

import org.junit.Test;

import main.com.swayam.json.JsonObject;
import main.com.swayam.json.JsonTokenizer;

public class JsonTokenizerTest {

	@Test
	public void testTokenize1() {
		String text = "{\"name\":\"swayam\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
	}
	
	@Test
	public void testTokenize2() {
		String text = "{\"id\":\"12345\",\"name\":{\"first\":\"swayam\",\"last\":\"raina\"},\"level\":\"advanced\"}";
		JsonObject json = new JsonTokenizer().tokenize(text);
		System.out.println(json.prettify(text));
	}
	
}

