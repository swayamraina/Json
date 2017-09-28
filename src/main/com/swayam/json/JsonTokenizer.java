package main.com.swayam.json;

public class JsonTokenizer implements UniversalConstants {
	
	private String jsonText;
	private int currentIndex;
	private int length;
	private char currentCharacter;
	
	public JsonTokenizer() {
		this.currentIndex = 0;
		this.length = 0;
		this.currentCharacter = SPACE;
	}
	
	public JsonObject tokenize(final String jsonText) {
		this.setJsonText(jsonText);
		if(!validJson()) throw new JsonException("Parsing Error : Invalid Json format.");
		
		// if valid, parse the JSON
		return this.extractObject();
	}
	
	public String extractKey() {
		StringBuilder keyExtractor = new StringBuilder();
		// skip opening quote token
		jumpAhead();
		
		skipSpaces();
		while(currentCharacter!=QUOTE && lookBack()!=BACKSLASH) {
			keyExtractor.append(currentCharacter);
			jumpAhead();
		}
		skipSpaces();
		
		// escape ' " '
		jumpAhead();
		skipSpaces();
		// escape ':'
		jumpAhead();
		skipSpaces();
		
		return keyExtractor.toString();
	}
	
	public boolean validJson() {
		StringBuilder stack = new StringBuilder();
		int stackSize = 0;
		char currentChar;
		this.length = jsonText.length();
		
		for(int i=0;i<this.length;i++) {
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
	
	public String extractString() {
		StringBuilder text = new StringBuilder();
		
		jumpAhead();
		while(this.currentCharacter != QUOTE) {
			text.append(this.currentCharacter);
			jumpAhead();
		}
		jumpAhead();
		return text.toString();
	}
	
	public JsonArray extractArray() {
		JsonArray array = new JsonArray();
		boolean objectNotParsed = true;
		
		// skip '{' token
		jumpAhead();
		
		// start parsing tokens
		while(objectNotParsed) {
			skipSpaces();
			switch(this.currentCharacter) {
				case CLOSED_SQUARE_BRACE:
					objectNotParsed = false;
					jumpAhead();
					break;
					
				case OPEN_SQUARE_BRACE:
					array.addValue(this.extractArray());
					break;
					
				case QUOTE:
					array.addValue(this.extractString());
					break;
					
				case OPEN_CURLY_BRACE:
					//array.addValue(this.extractObject());
					break;
					
				case CLOSED_CURLY_BRACE:
					jumpAhead();
					break;
			}
			
			// skip additional tokens
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				jumpAhead();
			}
		}
		return array;
	}
	
	public JsonObject extractObject() {
		JsonObject internalJson = new JsonObject();
		boolean objectNotParsed = true;
		String key = null;
		
		// skip '{' token
		jumpAhead();
		
		// start parsing tokens
		while(objectNotParsed) {
			skipSpaces();
			if(this.currentCharacter == QUOTE) {
				key = this.extractKey();
			}
			
			switch(this.currentCharacter) {
				case CLOSED_CURLY_BRACE:
					objectNotParsed = false;
					if(this.currentIndex + 1 < this.length) {
						jumpAhead();
					}
					break;
					
				case OPEN_CURLY_BRACE:
					internalJson.add(key, this.extractObject());
					break;
					
				case QUOTE:
					internalJson.add(key, this.extractString());
					break;
					
				case OPEN_SQUARE_BRACE:
					internalJson.add(key, this.extractArray());
					break;
					
				case CLOSED_SQUARE_BRACE:
					jumpAhead();
					break;
					
				default:
					if(!validStartForPrimitive(this.currentCharacter)) throw new JsonException("Parsing Error : Invalid Json format.");
					internalJson.add(key, this.extractPrimitive());
			}
			
			// skip additional tokens
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				jumpAhead();
			}
		}
		return internalJson;
	}
	
	public void setJsonText(String jsonText) {
		this.jsonText = jsonText;
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
	
	private void skipSpaces() {
		while(this.currentCharacter == SPACE) {
			jumpAhead();
		}
	}
	
	private void jumpAhead() {
		currentCharacter = jsonText.charAt(++currentIndex);
	}
	
	private char lookBack() {
		return (currentIndex > 0 && currentIndex < length) ? jsonText.charAt(currentIndex-1) : SPACE;
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
	
	private Object extractPrimitive() {
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
			jumpAhead();
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