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
	
	public JsonObject tokenize(final String JsonText) {
		this.setJsonText(JsonText);
		if(!validJson()) {
			throw new JsonException("Parsing Error : Invalid Json format.");
		}
		return this.extractObject();
	}
	
	public String extractKey() {
		StringBuilder keyExtractor = new StringBuilder();
		// skip opening quote token
		jumpAhead();
		while(currentCharacter!=QUOTE && lookBack()!=BACKSLASH) {
			keyExtractor.append(currentCharacter);
			jumpAhead();
		}
		skipSpaces();
		// escape ':'
		this.currentIndex++;
		// escape ','
		jumpAhead();
		
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
		int squareBraceCount = 1;
		
		jumpAhead();
		while(squareBraceCount != 0) {
			switch(this.currentCharacter) {
				case CLOSED_SQUARE_BRACE:
					squareBraceCount--;
					jumpAhead();
					break;
				case OPEN_SQUARE_BRACE:
					//squareBraceCount++;
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
			}
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				jumpAhead();
			}
		}
		return array;
	}
	
	public JsonObject extractObject() {
		JsonObject json = new JsonObject();
		boolean curlyBraceCount = true;
		String key = null;
		
		jumpAhead();
		while(curlyBraceCount) {
			skipSpaces();
			if(this.currentCharacter == QUOTE) {
				key = this.extractKey();
			}
			
			switch(this.currentCharacter) {
			case CLOSED_CURLY_BRACE:
				curlyBraceCount = false;
				if(this.currentIndex + 1 < this.length) {
					jumpAhead();
				}
				break;
			case OPEN_CURLY_BRACE:
				json.add(key, this.extractObject());
				break;
			case QUOTE:
				json.add(key, this.extractString());
				break;
			case OPEN_SQUARE_BRACE:
				json.add(key, this.extractArray());
				break;
			case CLOSED_SQUARE_BRACE:
				jumpAhead();
			}
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				jumpAhead();
			}
		}
		return json;
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
}