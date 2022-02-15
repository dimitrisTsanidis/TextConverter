package exporters;

import java.io.File;

import java.io.PrintWriter;





import datamodel.buildingblocks.Document;
import datamodel.buildingblocks.LineBlock;

public class MarkdownExporter {
	private Document document;
	private String outputPath;
	
	
	public MarkdownExporter (Document document, String str) {
		this.document = document;
		this.outputPath = str;
	}

	/**
	 * Creates a new file and writes the text of each lineblock within.
	 *  
	 * @return the number of paragraphs written into the file.
	 */
	public int export(){
		File fileToSave ;
		PrintWriter writer;
		
		fileToSave = new File(outputPath.toString());// + ".md");
		try {
			fileToSave.createNewFile(); //throws IOException and SecurityException
		}
		catch (Exception e1) {
			System.out.println("MarkdownExporter: could not create the file");
			e1.printStackTrace();
			return -1;
		}
		
		try {
			writer = new PrintWriter(fileToSave); //throws FileNotFoundException and SecurityException
		}
		catch (Exception e){
			System.out.println("MarkdownExporter: could not create the file");
			e.printStackTrace();
			return -1;
		}
		int numParagraphs=0;
		for(LineBlock lineblock: document.getLineblocks()) {
			if(!lineblock.toString().startsWith("<!--")) { //In our implementation the text to be omitted is marked as md comment. Here we completely skip writing the omitted text to the file. 
				writer.println(lineblock.toString());
				writer.println();
				numParagraphs++;
			}
		}
		writer.close();
		
		return numParagraphs;	
	}
}
