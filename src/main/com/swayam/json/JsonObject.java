package main.com.swayam.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonObject implements UniversalConstants {
	
	private Map<String,Object> Json;
	private int jsonTextStringLength;
	private String prettyJson;
	private String JsonText;
	
	public JsonObject() {
		this.Json = new HashMap<>();
		this.jsonTextStringLength = 0;
		this.prettyJson = null;
		this.JsonText = null;
	}
	
	public void add(String key, Object value) {
		if(Json.get(key) == null) {
			Json.put(key, value);
		}
		else {
			throw new JsonException("Cannot have multiple keys with same name. Key:: \"" + key + "\" already present in JSON.");
		}
	}
	
	public String serialize() {
		if(JsonText != null) {
			return this.JsonText;
		}
		
		StringBuilder JsonText = new StringBuilder();
		int lastCommaIndex = 0;
		
		JsonText.append(OPEN_CURLY_BRACE);
		lastCommaIndex += 1;
		for(Entry<String, Object> entry : Json.entrySet()) {
			JsonText.append(QUOTE);
			JsonText.append(entry.getKey());
			lastCommaIndex += entry.getKey().length();
			JsonText.append(QUOTE);
			JsonText.append(COLON);
			
			Object value = entry.getValue();
			List<String> stringAndLength;
			if(value instanceof JsonArray) {
				stringAndLength = ((JsonArray) value).computeStringAndLength();
				JsonText.append(stringAndLength.get(0));
				lastCommaIndex += Integer.parseInt(stringAndLength.get(1));
			}
			else {
				if(value instanceof JsonObject) {
					stringAndLength = ((JsonObject) value).computeStringAndLength();
					JsonText.append(stringAndLength.get(0));
					lastCommaIndex += Integer.parseInt(stringAndLength.get(1));
				}
				else {
					JsonText.append(QUOTE);
					JsonText.append(value.toString());
					lastCommaIndex += value.toString().length();
					JsonText.append(QUOTE);
					lastCommaIndex += 2;
				}
			}
			JsonText.append(COMMA);
			lastCommaIndex += 4;
		}
		JsonText.append(CLOSED_CURLY_BRACE);
		JsonText.deleteCharAt(lastCommaIndex-1);
		this.setJsonTextStringLength(lastCommaIndex);
		this.setJsonText(JsonText.toString());
		
		return this.JsonText;
	}
	
	private void setJsonTextStringLength(int length) {
		this.jsonTextStringLength = length;
	}

	private void setJsonText(String JsonText) {
		this.JsonText = JsonText;
	}

	public List<String> computeStringAndLength() {
		ArrayList<String> stringAndLength = new ArrayList<>();
		String string = this.serialize();
		String length = String.valueOf(string.length());
		stringAndLength.add(string);
		stringAndLength.add(length);
		return stringAndLength;
	}
	
	public String prettify(final String jsonText) {
		// if JSON object already pretty formatted
		// return pretty version
		if(prettyJson != null) return prettyJson;
		
		// create JSON object for incoming request
		serialize();
		
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
				
				case QUOTE:
					prettyJsonBuilder.append(currentChar);
					nextChar = jsonText.charAt(currentIndex+1);
					appendMode = false;
					// check if this quote is ending value in JSON
					// and toggle append mode to "ON"
					if((nextChar == CLOSED_CURLY_BRACE || nextChar == CLOSED_SQUARE_BRACE)) {
						prettyJsonBuilder.append(NEWLINE);
						appendMode = true;
						tabs--;
					}
					break;
					
				default:
					prettyJsonBuilder.append(currentChar);
					appendMode = false;
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
		return this.Json.get(key);
	}
}


