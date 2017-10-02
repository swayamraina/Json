package main.com.swayam.json;

public class JsonTokenizer implements UniversalConstants {
	
	private int currentIndex;
	private char currentCharacter;
	
	public JsonTokenizer() {
		this.currentIndex = 0;
		this.currentCharacter = SPACE;
	}
	
	public JsonObject tokenize(final String jsonText) {
		if(!validJson(jsonText)) throw new JsonException("Parsing Error : Invalid Json format.");
		
		// if valid, parse the JSON
		return this.extractObject(jsonText);
	}
	
	public String extractKey(final String jsonText) {
		StringBuilder keyExtractor = new StringBuilder();
		// skip opening quote token
		jumpAhead(jsonText, 1);
		
		skipSpaces(jsonText);
		while(currentCharacter!=QUOTE && lookBack(jsonText)!=BACKSLASH) {
			keyExtractor.append(currentCharacter);
			jumpAhead(jsonText, 1);
		}
		skipSpaces(jsonText);
		
		// escape ' " '
		jumpAhead(jsonText, 1);
		skipSpaces(jsonText);
		// escape ':'
		jumpAhead(jsonText, 1);
		skipSpaces(jsonText);
		
		return keyExtractor.toString();
	}
	
	public boolean validJson(final String jsonText) {
		StringBuilder stack = new StringBuilder();
		int stackSize = 0;
		char currentChar;
		int length = jsonText.length();
		
		for(int i=0; i<length; i++) {
			currentChar = jsonText.charAt(i);
			if(currentChar == OPEN_CURLY_BRACE || currentChar == OPEN_SQUARE_BRACE) {
				stack.append(currentChar);
				stackSize++;
			}
			else {
				if(currentChar == CLOSED_CURLY_BRACE || currentChar == CLOSED_SQUARE_BRACE) {
					if(stackSize == 0 || currentChar != mirrorOf(stack.charAt(stackSize-1))) {
						return false;
					}
					if(currentChar == mirrorOf(stack.charAt(stackSize-1))) {
						stack.deleteCharAt(--stackSize);
					}
				}
			}
		}
		if(stackSize != 0) {
			return false;
		}
		return true;
	}
	
	public String extractString(final String jsonText) {
		StringBuilder text = new StringBuilder();
		
		jumpAhead(jsonText, 1);
		while(this.currentCharacter != QUOTE) {
			text.append(this.currentCharacter);
			jumpAhead(jsonText, 1);
		}
		jumpAhead(jsonText, 1);
		return text.toString();
	}
	
	public JsonArray extractArray(final String jsonText) {
		JsonArray array = new JsonArray();
		boolean objectNotParsed = true;
		
		// skip '{' token
		jumpAhead(jsonText, 1);
		
		// start parsing tokens
		while(objectNotParsed) {
			skipSpaces(jsonText);
			switch(this.currentCharacter) {
				case CLOSED_SQUARE_BRACE:
					objectNotParsed = false;
					jumpAhead(jsonText, 1);
					break;
					
				case OPEN_SQUARE_BRACE:
					array.addValue(this.extractArray(jsonText));
					break;
					
				case QUOTE:
					array.addValue(this.extractString(jsonText));
					break;
					
				case OPEN_CURLY_BRACE:
					//array.addValue(this.extractObject());
					break;
					
				case CLOSED_CURLY_BRACE:
					jumpAhead(jsonText, 1);
					break;
			}
			
			// skip additional tokens
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				jumpAhead(jsonText, 1);
			}
		}
		return array;
	}
	
	public JsonObject extractObject(final String jsonText) {
		JsonObject internalJson = new JsonObject();
		boolean objectNotParsed = true;
		String key = null;
		
		// skip '{' token
		jumpAhead(jsonText, 1);
		
		// start parsing tokens
		while(objectNotParsed) {
			skipSpaces(jsonText);
			if(this.currentCharacter == QUOTE) {
				key = this.extractKey(jsonText);
			}
			
			switch(this.currentCharacter) {
				case CLOSED_CURLY_BRACE:
					objectNotParsed = false;
					if(this.currentIndex + 1 < jsonText.length()) {
						jumpAhead(jsonText, 1);
					}
					break;
					
				case OPEN_CURLY_BRACE:
					internalJson.add(key, this.extractObject(jsonText));
					break;
					
				case QUOTE:
					internalJson.add(key, this.extractString(jsonText));
					break;
					
				case OPEN_SQUARE_BRACE:
					internalJson.add(key, this.extractArray(jsonText));
					break;
					
				case CLOSED_SQUARE_BRACE:
					jumpAhead(jsonText, 1);
					break;
					
				default:
					if(!validStartForPrimitive(this.currentCharacter)) throw new JsonException("Parsing Error : Invalid Json format.");
					internalJson.add(key, this.extractPrimitive(jsonText));
			}
			
			// skip additional tokens
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				jumpAhead(jsonText, 1);
			}
		}
		return internalJson;
	}
	
	public char mirrorOf(char c) {
		switch(c) {
			case OPEN_CURLY_BRACE:
				return CLOSED_CURLY_BRACE;
			case CLOSED_CURLY_BRACE:
				return OPEN_CURLY_BRACE;
			case OPEN_SQUARE_BRACE:
				return CLOSED_SQUARE_BRACE;
			case CLOSED_SQUARE_BRACE:
				return OPEN_SQUARE_BRACE;
			default:
				return SPACE;
		}
	}
	
	private void skipSpaces(final String jsonText) {
		while(this.currentCharacter == SPACE) {
			jumpAhead(jsonText, 1);
		}
	}
	
	private void jumpAhead(final String jsonText, int jumps) {
		for(int i=1; i<=jumps; i++) {
			currentCharacter = jsonText.charAt(++currentIndex);
		}
	}
	
	private char lookBack(final String jsonText) {
		return (currentIndex > 0 && currentIndex < jsonText.length()) ? jsonText.charAt(currentIndex-1) : SPACE;
	}
	
	private boolean validStartForPrimitive(char token) {
		int intToken = (int) token;
		if(intToken==43 || intToken==45 || 
				intToken==110 || intToken==102 || intToken==116 || 
					(intToken>=48 && intToken<=57)) {
			
			return true;
		}
		return false;
	}
	
	private Object extractPrimitive(final String jsonText) {
		boolean isNumeric = false;
		boolean isFloat = false;
		String primitiveToken;
		int intToken;
		StringBuilder primitiveBuilder = new StringBuilder();
		while(this.currentCharacter != SPACE && this.currentCharacter != COMMA && this.currentCharacter != CLOSED_CURLY_BRACE && this.currentCharacter != CLOSED_SQUARE_BRACE) {
			if(this.currentCharacter == DOT) { 
				if(isFloat) throw new JsonException("Parsing Error : Invalid Json format.");
				isFloat = true;
			}
			intToken = (int) this.currentCharacter;
			if(intToken >= 48 && intToken <= 57) isNumeric = true;
			if((intToken==110 || intToken==102 || intToken==116) && isNumeric) throw new JsonException("Parsing Error : Invalid Json format.");
			primitiveBuilder.append(this.currentCharacter);
			jumpAhead(jsonText, 1);
		}
		primitiveToken = primitiveBuilder.toString();
		
		if(isFloat) return new Float(primitiveToken);
		if(isNumeric) return new Integer(primitiveToken);
		if(primitiveToken.equals(TRUE)) return true;
		if(primitiveToken.equals(FALSE)) return false;
		if(primitiveToken.equals(NULL)) return null;
		
		throw new JsonException("Parsing Error : Invalid Json format.");
	}
}