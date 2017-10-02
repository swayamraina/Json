package main.com.swayam.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonObject implements UniversalConstants {
	
	private Map<String,Object> json;
	private int jsonTextStringLength;
	private String prettyJson;
	private String jsonText;
	
	public JsonObject() {
		this.json = new HashMap<>();
		this.jsonTextStringLength = 0;
		this.prettyJson = null;
		this.jsonText = null;
	}
	
	public void add(String key, Object value) {
		if(json.get(key) == null) {
			json.put(key, value);
		}
		else {
			throw new JsonException("Cannot have multiple keys with same name. Key :: \"" + key + "\" already present in JSON.");
		}
	}
	
	public String serialize() {
		if(jsonText != null) return jsonText;
		
		StringBuilder jsonTextBuilder = new StringBuilder();
		int lastCommaIndex = 0;
		
		jsonTextBuilder.append(OPEN_CURLY_BRACE);
		lastCommaIndex += 1;
		for(Entry<String, Object> entry : json.entrySet()) {
			jsonTextBuilder.append(QUOTE);
			jsonTextBuilder.append(entry.getKey());
			lastCommaIndex += entry.getKey().length();
			jsonTextBuilder.append(QUOTE);
			jsonTextBuilder.append(COLON);
			
			Object value = entry.getValue();
			List<String> stringAndLength;
			if(value instanceof JsonArray) {
				stringAndLength = ((JsonArray) value).computeStringAndLength();
				jsonTextBuilder.append(stringAndLength.get(0));
				lastCommaIndex += Integer.parseInt(stringAndLength.get(1));
			}
			else {
				if(value instanceof JsonObject) {
					stringAndLength = ((JsonObject) value).computeStringAndLength();
					jsonTextBuilder.append(stringAndLength.get(0));
					lastCommaIndex += Integer.parseInt(stringAndLength.get(1));
				}
				else {
					if(value instanceof String) jsonTextBuilder.append(QUOTE);
					jsonTextBuilder.append(value);
					if(value instanceof String) jsonTextBuilder.append(QUOTE);
					lastCommaIndex += computeLengthForPrimitives(value);
				}
			}
			jsonTextBuilder.append(COMMA);
			lastCommaIndex += 4;
		}
		jsonTextBuilder.append(CLOSED_CURLY_BRACE);
		jsonTextBuilder.deleteCharAt(lastCommaIndex-1);
		this.setJsonTextStringLength(lastCommaIndex);
		this.setJsonText(jsonTextBuilder.toString());
		
		return jsonText;
	}
	
	private void setJsonTextStringLength(int length) {
		this.jsonTextStringLength = length;
	}

	private void setJsonText(String jsonText) {
		this.jsonText = jsonText;
	}

	public List<String> computeStringAndLength() {
		ArrayList<String> stringAndLength = new ArrayList<>();
		String string = this.serialize();
		String length = String.valueOf(string.length());
		stringAndLength.add(string);
		stringAndLength.add(length);
		return stringAndLength;
	}
	
	public String prettify() {
		// if JSON object already pretty formatted
		// return pretty version
		if(prettyJson != null) return prettyJson;
		
		// create JSON object for incoming request
		if(jsonText == null) serialize();
		
		StringBuilder prettyJsonBuilder = new StringBuilder();
		int currentIndex = 0;
		int tabs = 0;
		char currentChar = UniversalConstants.SPACE; 
		char nextChar = UniversalConstants.SPACE;
		boolean appendMode = false;
		
		// start processing incoming JSON text
		while(currentIndex != jsonTextStringLength) {
			currentChar = jsonText.charAt(currentIndex);
			if(appendMode) appendTabs(prettyJsonBuilder, tabs);
			switch(currentChar) {
				// whenever open braces token is encountered, append mode is turned "ON"
				case OPEN_CURLY_BRACE:
				case OPEN_SQUARE_BRACE:
					prettyJsonBuilder.append(currentChar);
					prettyJsonBuilder.append(NEWLINE);
					tabs++;
					appendMode = true;
					break;
					
				// on encounter with close brace token, append mode is turned "OFF"
				// though we toggle it, if next token is a complex token i.e. '}' or ']'
				case CLOSED_CURLY_BRACE:
				case CLOSED_SQUARE_BRACE:
					prettyJsonBuilder.append(currentChar);
					appendMode = false;
					if(currentIndex+1 < jsonTextStringLength) {
						nextChar = jsonText.charAt(currentIndex+1);
						// turn append mode "ON"
						if((nextChar == CLOSED_CURLY_BRACE || nextChar == CLOSED_SQUARE_BRACE)) {
							prettyJsonBuilder.append(NEWLINE);
							appendMode = true;
							tabs--;
						}
					}
					break;
					
				case COMMA:
					prettyJsonBuilder.append(SPACE);
					prettyJsonBuilder.append(currentChar);
					prettyJsonBuilder.append(NEWLINE);
					appendMode = true;
					break;
					
				case COLON:
					prettyJsonBuilder.append(SPACE);
					prettyJsonBuilder.append(currentChar);
					prettyJsonBuilder.append(SPACE);
					appendMode = false;
					break;
				
				default:
					prettyJsonBuilder.append(currentChar);
					nextChar = jsonText.charAt(currentIndex+1);
					appendMode = false;
					// check if current character is ending value in JSON
					// if yes, toggle append mode to "ON"
					if((nextChar == CLOSED_CURLY_BRACE || nextChar == CLOSED_SQUARE_BRACE)) {
						prettyJsonBuilder.append(NEWLINE);
						appendMode = true;
						tabs--;
					}
					break;
			}
			
			// move to next token
			currentIndex++;
		}
		
		// save pretty JSON for future use
		setPrettyJson(prettyJsonBuilder.toString());
		
		return prettyJson;
	}
	
	private void appendTabs(StringBuilder prettyJson, int tabs) {
		for(int i=0;i<tabs;i++) {
			prettyJson.append(TAB);
		}
	}

	private void setPrettyJson(String prettyJson) {
		this.prettyJson = prettyJson;
	}
	
	public Object get(String key) {
		return json.get(key);
	}
	
	public Map<String,Object> getJson() {
		return this.json;
	}
	
	public static int computeLengthForPrimitives(Object data) {
		if(data instanceof String) return ((String) data).length()+2;
		return String.valueOf(data).length();
	}
	
	public static String prettify(final String jsonText) {
		StringBuilder prettyJsonBuilder = new StringBuilder();
		int currentIndex = 0;
		int tabs = 0;
		char currentChar = UniversalConstants.SPACE; 
		char nextChar = UniversalConstants.SPACE;
		boolean appendMode = false;
		
		// start processing incoming JSON text
		while(currentIndex != jsonText.length()) {
			currentChar = jsonText.charAt(currentIndex);
			// TODO : update below code, this can be better
			if(appendMode) new JsonObject().appendTabs(prettyJsonBuilder, tabs);
			switch(currentChar) {
				// whenever open braces token is encountered, append mode is turned "ON"
				case OPEN_CURLY_BRACE:
				case OPEN_SQUARE_BRACE:
					prettyJsonBuilder.append(currentChar);
					prettyJsonBuilder.append(NEWLINE);
					tabs++;
					appendMode = true;
					break;
					
				// on encounter with close brace token, append mode is turned "OFF"
				// though we toggle it, if next token is a complex token i.e. '}' or ']'
				case CLOSED_CURLY_BRACE:
				case CLOSED_SQUARE_BRACE:
					prettyJsonBuilder.append(currentChar);
					appendMode = false;
					if(currentIndex+1 < jsonText.length()) {
						nextChar = jsonText.charAt(currentIndex+1);
						// turn append mode "ON"
						if((nextChar == CLOSED_CURLY_BRACE || nextChar == CLOSED_SQUARE_BRACE)) {
							prettyJsonBuilder.append(NEWLINE);
							appendMode = true;
							tabs--;
						}
					}
					break;
					
				case COMMA:
					prettyJsonBuilder.append(SPACE);
					prettyJsonBuilder.append(currentChar);
					prettyJsonBuilder.append(NEWLINE);
					appendMode = true;
					break;
					
				case COLON:
					prettyJsonBuilder.append(SPACE);
					prettyJsonBuilder.append(currentChar);
					prettyJsonBuilder.append(SPACE);
					appendMode = false;
					break;
				
				default:
					prettyJsonBuilder.append(currentChar);
					nextChar = jsonText.charAt(currentIndex+1);
					appendMode = false;
					// check if current character is ending value in JSON
					// if yes, toggle append mode to "ON"
					if((nextChar == CLOSED_CURLY_BRACE || nextChar == CLOSED_SQUARE_BRACE)) {
						prettyJsonBuilder.append(NEWLINE);
						appendMode = true;
						tabs--;
					}
					break;
			}
			
			// move to next token
			currentIndex++;
		}
		
		return prettyJsonBuilder.toString();
	}
	
}


