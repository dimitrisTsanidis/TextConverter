package exporters;


import java.io.FileOutputStream;
import java.io.OutputStream;


import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import datamodel.buildingblocks.Document;
import datamodel.buildingblocks.LineBlock;

public class PdfExporter {
	private Document document;
	private String outputPath;
	
	
	public PdfExporter (Document document, String str) {
		this.document = document;
		this.outputPath = str;
	}

	/**
	 * Creates a new file and writes the text of each lineblock within.
	 * For each paragraph (already marked with Markdown symbols), removes the Markdown symbols and sets the exported text to the suitable style
	 * Makes use of the iText5 library. 
	 *  
	 * @return the number of paragraphs written into the file.
	 */
	public int export(){
		int numParagraphs = 0;
		try {
			com.itextpdf.text.Document doc = new com.itextpdf.text.Document();
			
			OutputStream os = new FileOutputStream(outputPath);//+".pdf"); //throws FileNotFoundException and SecurityException
		
			PdfWriter writer = PdfWriter.getInstance(doc, os);
			
			doc.open();
			Paragraph p;
			for(LineBlock lineblock : document.getLineblocks()) {
				String lineBlockText = lineblock.toString();
				
				Font fontDefault = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
				
				if(lineBlockText.isEmpty()||lineBlockText.isBlank()) {
					continue;
				}
				else if(lineBlockText.startsWith("<!--")) {
					continue;
				}
				else if(lineBlockText.startsWith("##")) { //This condition should be checked before # symbol condition. Otherwise this condition will never be met.
					lineBlockText = lineBlockText.replaceFirst("##","");
					Font fontSubheading = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLD);
					p = new Paragraph(lineBlockText+"\n\n", fontSubheading);
					numParagraphs++;
				}
				else if(lineBlockText.startsWith("#")) {
					lineBlockText = lineBlockText.replaceFirst("#","");
					Font fontHeading = new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD);
					p = new Paragraph(lineBlockText+"\n\n", fontHeading);
					numParagraphs++;
				}
				else if((lineBlockText.charAt(0)== '*')&& (lineBlockText.charAt(1) == '*')) {
					lineBlockText = lineBlockText.replaceFirst("\\*","");
					lineBlockText = lineBlockText.replaceFirst("\\*","");
					lineBlockText = lineBlockText.substring(0, lineBlockText.length()-3);
					Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
					p = new Paragraph(lineBlockText+"\n\n", fontBold);
					numParagraphs++;
				}
				else if(lineBlockText.startsWith("_")) {
					lineBlockText = lineBlockText.replaceFirst("_","");
					lineBlockText = lineBlockText.substring(0, lineBlockText.length()-2);
					Font fontItalics = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.ITALIC);
					p = new Paragraph(lineBlockText+"\n\n", fontItalics);
					numParagraphs++;
				}
				else {
					p = new Paragraph(lineBlockText+"\n\n", fontDefault);
					numParagraphs++;
				}
				
				
				doc.add(p);
			}
			doc.close();
			writer.close();
		}
		catch (Exception e){
			System.out.println("MarkdownExporter: could not create the file");
			e.printStackTrace();
			return -1;
		}
		
		return numParagraphs;
	}
}
