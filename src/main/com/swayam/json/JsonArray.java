package main.com.swayam.json;

import java.util.ArrayList;
import java.util.List;

public class JsonArray implements UniversalConstants {
	
	private List<Object> jsonArray; 
	
	public JsonArray() {
		this.jsonArray = new ArrayList<>();
	}
	
	public JsonArray(Object value) {
		this.jsonArray = new ArrayList<>();
		jsonArray.add(String.valueOf(value));
	}

	public List<String> computeStringAndLength() {
		List<String> stringAndLength = new ArrayList<>();
		List<String> innerStringAndLength;
		int length = 2;
		StringBuilder arrayText = new StringBuilder();
		
		arrayText.append(OPEN_SQUARE_BRACE);
		
		if(this.jsonArray.get(0) instanceof JsonObject) {
			for(Object object : this.jsonArray) {
				innerStringAndLength = ((JsonObject) object).computeStringAndLength();
				arrayText.append(innerStringAndLength.get(0));
				arrayText.append(COMMA);
				length += Integer.parseInt(innerStringAndLength.get(1)) + 1;
			}
		}
		else {
			if(this.jsonArray.get(0) instanceof JsonArray) {
				for(Object object : this.jsonArray) {
					innerStringAndLength = ((JsonArray) object).computeStringAndLength();
					arrayText.append(innerStringAndLength.get(0));
					arrayText.append(COMMA);
					length += Integer.parseInt(innerStringAndLength.get(1)) + 1;
				}
			}
			else {
				if(this.jsonArray.get(0) instanceof String) {
					for(Object stringValue : this.jsonArray) {
						arrayText.append(QUOTE);
						arrayText.append(stringValue);
						arrayText.append(QUOTE);
						arrayText.append(COMMA);
						length += ((String) stringValue).length() + 3;
					}
				}
				else {
					for(Object value : this.jsonArray) {
						arrayText.append(value);
						arrayText.append(COMMA);
						length += String.valueOf(value).length() + 1;
					}
				}
			}
		}
		
		arrayText.append(CLOSED_SQUARE_BRACE);
		arrayText.deleteCharAt(length-2);
		stringAndLength.add(arrayText.toString());
		stringAndLength.add(String.valueOf(length-1));
		return stringAndLength;
	}

	public void addValue(Object obj) {
		this.jsonArray.add(obj);
	}
	
	public String serialize() {
		return this.computeStringAndLength().get(0);
	}
}
