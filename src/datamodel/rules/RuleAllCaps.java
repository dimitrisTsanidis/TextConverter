package datamodel.rules;

import datamodel.buildingblocks.LineBlock;

public class RuleAllCaps extends AbstractRule {
	final int lowercaseAascii = 97;
	final int lowercaseZascii = 122;
	
	public RuleAllCaps() {}

	/**
	 * This method takes input Lineblocks (paragraphs) and checks if all charactes are capitalized.
	 * Returns true or false.
	 */
	@Override
	public boolean isValid(LineBlock paragraph) {
		String linesText = paragraph.toString();
		
		for (int i = 0; i < linesText.length(); i++){
			char currentCharacter = linesText.charAt(i);        
			if((currentCharacter >=lowercaseAascii) && (currentCharacter <=lowercaseZascii)) {
				return false;
			}
		}
		return true;
	}
	

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "enabled";
	}

}
