package datamodel.rules;


import datamodel.buildingblocks.LineBlock;

public class RuleStartWith extends AbstractRule {
	private String prefix;
	
	public RuleStartWith(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * This method takes input a Lineblock (paragraph) and checks if the Lineblock has a specific prefix.
	 * Returns true or false.
	 */
	@Override
	public boolean isValid(LineBlock paragraph) {
		String LinesText = paragraph.toString();
		for (int i = 0; i< prefix.length();i++) { 
			if((LinesText.length()<i)||(LinesText.isEmpty())) { //if the lineblocks text size is smaller than the length of the prefix then for sure, doesn't start with the prefix.
																//We check this so we don't get an exception.
				return false;							
			}
			char paragraphCharacter = LinesText.charAt(i);
			char prefixCharacter = prefix.charAt(i);
			if(paragraphCharacter!=prefixCharacter) {
		    	return false;
		    }
		}
		return true;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return prefix;
	}

}
