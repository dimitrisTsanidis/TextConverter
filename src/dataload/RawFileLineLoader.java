package dataload;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import datamodel.buildingblocks.LineBlock;

public class RawFileLineLoader {

	/**
	 * Reads the file specified by filePath line by line until it find \n\n\ creates an LineBlock object and stores the text already read.
	 * It then resets the reader and continues until the end of the file. It then stores the last LineBlock, closes the reader and returns.
	 * 
	 * @param filePath
	 * @param lineblocks 
	 * @return A list of all the created LineBlock onjects
	 */
	public List<LineBlock> load(String filePath, List<LineBlock> lineblocks) {
		StringBuilder sb = new StringBuilder();
		
		
		try { //opening a file can throw NullPointerException
			File selectedFile = new File(filePath); 
			Scanner input = new Scanner(selectedFile).useDelimiter("\n");
			String Line;
			while(input.hasNext()) { //hasNext() can throw IllegalStateException
				Line = input.next(); ////hasNext() can throw IllegalStateException, NoSuchElementException
				if(Line.length()>2) {
					Line = Line.substring(0, Line.length()-1)+" ";
				}
				if(Line.isBlank()){          //If the current line is blank
					if(!(input.hasNext())) { //if it isn't the last line of the document
						sb.append(Line);	 //append this line too, as it is the last of this lineBlock.
					}
					if(sb.toString().isBlank()||sb.toString().isEmpty()){}
					else { //Case when the current line is blank but the whole paragraph is not. Stores the current read string as a LineBlock and resets the reader.
						   //Practically ensures that double blank lines aren't converted into LineBlocks.
						LineBlock lineBlock = new LineBlock(sb.toString());
						sb.delete(0, sb.length());
						lineblocks.add(lineBlock);
					}
				}
				else { //Stores the last Paragraph text into a LineBlock.
					sb.append(Line);
					if(!(input.hasNext())) {
						LineBlock lineBlock = new LineBlock(sb.toString());
						sb.delete(0, sb.length());
						lineblocks.add(lineBlock);				
					}
				}	
			}
			input.close();
			sb.delete(0, sb.length());
		}
		catch (FileNotFoundException e1) {
			System.out.println("RawFileLineLoader: The file could not be opened.");
			e1.printStackTrace();	
		}
		
		return lineblocks;	
	}
}
