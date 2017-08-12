package main.com.swayam.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class JsonObject implements UniversalConstants {
	
	private Map<String,Object> Json;
	private int JsonTextStringLength;
	private String prettyJson;
	private String JsonText;
	
	public JsonObject() {
		this.Json = new HashMap<>();
		this.JsonTextStringLength = 0;
		this.prettyJson = null;
		this.JsonText = null;
	}
	
	public void add(String key, Object value) {
		if(Json.get(key) == null) {
			Json.put(key, value);
		}
		else {
			throw new JsonException("Cannot have multiple keys with same name");
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
		this.JsonTextStringLength = length;
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
	
	public String prettify(final String JsonText) {
		if(this.prettyJson != null) {
			return this.prettyJson;
		}
		this.serialize();
		
		StringBuilder prettyJson = new StringBuilder();
		int currentIndex = 0;
		int tabs = 0;
		char currentChar; 
		char nextCharAfterQuote;
		char nextCharAfterColon;
		
		while(currentIndex != JsonTextStringLength) {
			currentChar = JsonText.charAt(currentIndex);
			switch(currentChar) {
				case OPEN_CURLY_BRACE:
				case OPEN_SQUARE_BRACE:
					prettyJson.append(currentChar);
					prettyJson.append(NEWLINE);
					tabs++;
					this.appendTabs(prettyJson, tabs);
					break;
				case CLOSED_CURLY_BRACE:
				case CLOSED_SQUARE_BRACE:
					prettyJson.append(currentChar);
					prettyJson.append(NEWLINE);
					tabs--;
					this.appendTabs(prettyJson, tabs);
					break;
				case COMMA:
					prettyJson.append(SPACE);
					prettyJson.append(currentChar);
					prettyJson.append(NEWLINE);
					this.appendTabs(prettyJson, tabs);
					break;
				case COLON:
					prettyJson.append(SPACE);
					prettyJson.append(currentChar);
					prettyJson.append(SPACE);
					nextCharAfterColon = JsonText.charAt(currentIndex+1);
					if(nextCharAfterColon == OPEN_CURLY_BRACE || nextCharAfterColon == OPEN_SQUARE_BRACE) {
						prettyJson.append(NEWLINE);
						tabs++;
						this.appendTabs(prettyJson, tabs);
					}
					break;
				case QUOTE:
					prettyJson.append(currentChar);
					nextCharAfterQuote = JsonText.charAt(currentIndex+1);
					if(nextCharAfterQuote == CLOSED_CURLY_BRACE || nextCharAfterQuote == CLOSED_SQUARE_BRACE) {
						prettyJson.append(NEWLINE);
						tabs--;
						this.appendTabs(prettyJson, tabs);
					}
					break;
				default:
					prettyJson.append(currentChar);
			}
			currentIndex++;
		}
		this.setPrettyJson(prettyJson.toString());
		
		return this.prettyJson;
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


