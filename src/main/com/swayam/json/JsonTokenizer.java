package main.com.swayam.json;

public class JsonTokenizer implements UniversalConstants {
	
	private String JsonText;
	private int currentIndex;
	private int length;
	private char currentCharacter;
	
	public JsonTokenizer() {
		this.currentIndex = 0;
		this.length = 0;
		this.currentCharacter = ' ';
	}
	
	public JsonObject tokenize(final String JsonText) {
		this.setJsonText(JsonText);
		if(!validJson()) {
			throw new JsonException("Parsing Error : Invalid Json format.");
		}
		return this.extractObject();
	}
	
	public String extractKey() {
		boolean startExpression = true;
		StringBuilder keyExtractor = new StringBuilder();
		
		while(true) {
			if(currentCharacter == QUOTE && startExpression) {
				startExpression = false;
			}
			else {
				if(currentCharacter == QUOTE && !startExpression) {
					break;
				}
				else {
					keyExtractor.append(currentCharacter);
				}
			}
			currentCharacter = this.JsonText.charAt(++this.currentIndex);
		}
		// escape colon
		this.currentIndex++;
		// escape comma
		this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		
		return keyExtractor.toString();
	}
	
	public boolean validJson() {
		StringBuilder stack = new StringBuilder();
		int stackSize = 0;
		char currentChar;
		this.length = this.JsonText.length();
		
		for(int i=0;i<this.length;i++) {
			currentChar = this.JsonText.charAt(i);
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
		
		this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		while(this.currentCharacter != QUOTE) {
			text.append(this.currentCharacter);
			this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		}
		this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		return text.toString();
	}
	
	public JsonArray extractArray() {
		JsonArray array = new JsonArray();
		int squareBraceCount = 1;
		
		this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		while(squareBraceCount != 0) {
			switch(this.currentCharacter) {
				case CLOSED_SQUARE_BRACE:
					squareBraceCount--;
					this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
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
					this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
			}
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
			}
		}
		return array;
	}
	
	public JsonObject extractObject() {
		JsonObject Json = new JsonObject();
		boolean curlyBraceCount = true;
		String key = null;
		
		this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		while(curlyBraceCount) {
			if(this.currentCharacter == QUOTE) {
				key = this.extractKey();
			}
			switch(this.currentCharacter) {
			case CLOSED_CURLY_BRACE:
				curlyBraceCount = false;
				if(this.currentIndex + 1 < this.length) {
					this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
				}
				break;
			case OPEN_CURLY_BRACE:
				Json.add(key, this.extractObject());
				break;
			case QUOTE:
				Json.add(key, this.extractString());
				break;
			case OPEN_SQUARE_BRACE:
				Json.add(key, this.extractArray());
				break;
			case CLOSED_SQUARE_BRACE:
				this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
			}
			if(this.currentCharacter == COMMA || this.currentCharacter == COLON) {
				this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
			}
		}
		return Json;
	}
	
	public void setJsonText(String JsonText) {
		this.JsonText = JsonText;
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
				return ' ';
		}
	}
	
	private void skipSpaces() {
		while(this.currentCharacter == UniversalConstants.SPACE) {
			this.currentCharacter = this.JsonText.charAt(++this.currentIndex);
		}
	}
}