package datamodel.buildingblocks;

import java.util.Scanner;

public class LineBlock {
	private String text; //if we don't use lines
	
	public LineBlock(String text) {
		this.text = text;
	}
	
	public void setText(String newText) {
		this.text = newText;
	}
	
	/**
	 * Computes the number of lines and words (through getNumWords()) of this LineBlock's text
	 *
	 * @return a format string which contains the computed numWords and numLines
	 */
	public String getStatsAsString() {
		int numLines=0;
		int numWords= getNumWords();
		Scanner input = new Scanner(this.text);
		while(input.hasNextLine()) {
			numLines++;
			input.nextLine();	
		}
		input.close();
		
		return "Lines: "+numLines+"                Words: "+numWords+",\n";
	}

	/**
	 * Computes the number of words in this LineBlock's text. Because the text is already marked with Markdown symbols, we temporarily strip them, before counting
	 * 
	 * @return the number of words in this LineBlock's text.
	 */
	public int getNumWords() {
		int numWords = 0;
		String text = this.toString().strip();
		
		if(text.startsWith("_")) {
			text = text.replaceFirst("_","");
			text = text.substring(0, text.length()-2);
		}
		else if((text.charAt(0)== '*')&& (text.charAt(1) == '*')) {
			
			text = text.replaceFirst("\\*","");
			text = text.replaceFirst("\\*","");
			text = text.substring(0, text.length()-3);
		}
		else if(text.startsWith("##")) { //This condition should be checked before # symbol condition. Otherwise, this condition will never be met.
			text = text.replaceFirst("##","");
		}
		else if(text.startsWith("#")) {
			text = text.replaceFirst("#","");
		}
		else if(text.startsWith("<!--")) {
			text = text.replaceFirst("<!--","");
			text = text.substring(0, text.length()-4);
		}
		
		//increases the counter for the last word in each line, since the next for loop doesn't consider the last word. 
		//Unless the last char of the line is ' ' then don't increase because it will be added in the next for loop
		Scanner input = new Scanner(text);
		while(input.hasNextLine()) {
			String text2 = input.nextLine();
			if(text2.charAt(text2.length()-1)== ' ') {
				continue;
			}
			numWords++; 
		}
		//When a character is blank increase the counter for the previous word.
		char currentCharacter;
		for (int i = 1; i < text.length(); i++){ //Also checks the case of multiple blank characters, in which case it doesn't add a word for each of them
		    currentCharacter = text.charAt(i);        
		    if(currentCharacter == 	' ' && text.charAt(i-1)!= ' ') { 
		    	numWords++;
		    }
		}
		input.close();
	
		return numWords;
	}
	
	/**
	 * Depending on the input parameter's value, this method adds Markdown text to this LineBlock's text.
	 * 
	 * @param determineHeadingStatus
	 */
	public void setStyle(StyleEnum determineHeadingStatus) {

		if(determineHeadingStatus == StyleEnum.OMITTED) {	//Instead of deleting the text, we mark the text as comment and during exporting we completely skip 
			this.text = "<!--"+this.text+"-->";				//the paragraph.
		}
		else if(determineHeadingStatus == StyleEnum.H1) {
			this.text = "#"+this.text;
		}
		else if(determineHeadingStatus == StyleEnum.H2) {
			this.text = "##"+this.text;
		}
		else if(determineHeadingStatus == StyleEnum.NORMAL) {
		}
	}

	/**
	 * Depending on the input parameter's value, this method adds Markdown text to this LineBlock's text.
	 * 
	 * @param determineFormatStatus
	 */
	public void setFormat(FormatEnum determineFormatStatus) {
		if(this.text.startsWith("<!--")||this.text.startsWith("#")||this.text.startsWith("##")) { //Checks if the LineBlock isn't normal
			if(determineFormatStatus == FormatEnum.REGULAR) {
				
			}
		}
		else {
			if(determineFormatStatus == FormatEnum.BOLD) {
				this.text = "**"+this.text+"**";
			}
			else if(determineFormatStatus == FormatEnum.ITALICS) {
				this.text = "_"+this.text+"_";
			}
		}
		
	}
	
	public String toString() {
		
		return this.text;
	}
}
