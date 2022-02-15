package engine;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dataload.RawFileLineLoader;
import datamodel.buildingblocks.Document;
import datamodel.buildingblocks.LineBlock;
import datamodel.buildingblocks.DocumentFactory;
import datamodel.ruleset.RuleSet;
import datamodel.ruleset.RuleSetCreator;
import exporters.MarkdownExporter;
import exporters.PdfExporter;

public class Engine implements IPlainTextDocumentEngine {
	private String alias;  //currently never used
	private String filePath = null;
	private String simpleInputFileName = null; //currently never used
	private List<LineBlock> lineblocks;
	private RuleSet inputRuleSet;
	private List<List<String>> inputSpec = null;
	private List<String> prefixes = null;
	private Document document;
	
	/**
	 * Constructs a new engine object to serve a specific input file
	 * 
	 * The document, lineblocks, alias, filePath and simpleInputFileName attributes are all populated.
	 * @param pFilePath the String with the path of the input file
	 * @param pInputType the String characterizing the document as raw, or annotated. Depending on which, a Document factory creates a new DocumentRaw or DocumentAnnotated.
	 * @param pAlias a String with a short name, i.e., an alias for the file
	 */
	public Engine(String pFilePath, String pInputType, String pAlias) {
	
		DocumentFactory docFactory = new DocumentFactory();
		if (pInputType.equalsIgnoreCase("ANNOTATED"))
			this.document = docFactory.createDocument(pFilePath, "ANNOTATED");
		else if (pInputType.equalsIgnoreCase("RAW")){
			this.document = docFactory.createDocument(pFilePath, "RAW");
		}
		this.lineblocks = this.document.getLineblocks();
		this.alias = pAlias;
		this.filePath = pFilePath;
		Path p = Paths.get(this.filePath);
		this.simpleInputFileName = p.getFileName().toString();
		
	}
	
	/**
	 * Registers a global rule set for a plain file at the main engine
	 * 
	 * Returns a RuleSet object as the result of the parsing and internal representation of the rules expressed as strings.
	 * The inputSpec parameter representing the specification of the rules is a List of (List of String)
	 * Each category of rule (OMIT, H1, H2, <B>, <I>) has a dedicated (List of String). 
	 * Every category absent is setup to undefined status and eventually mapped to the resp. RuleUndefined object that always returns false for isValid()
	 * The 0th element is the aforementioned category representative string
	 * The 1st element is STARTS_WITH, POSITIONS, ALL_CAPS 
	 * The 3rd element is a prefix string for STARTS_WITH or a comma-separated string of positions (starting from 0) for POSITIONS
	 * 
	 * @param inputSpec a List of (List of String) with the specification of the rules on how to handle paragraphs
	 * @return a RuleSet object as the result of the parsing and internal representation of the rules expressed as strings.
	 */
	@Override
	public RuleSet registerInputRuleSetForPlainFiles(List<List<String>> inputSpec) {
		this.inputSpec = inputSpec;
		RuleSetCreator ruleSetCreator = new RuleSetCreator(lineblocks, inputSpec, "inputRuleSet");
		this.inputRuleSet =  ruleSetCreator.createRuleSet();
		
		return this.inputRuleSet;
	}

	/**
	 * Registers a global rule set for an annotated  file at the main engine
	 * 
	 * Returns a RuleSet object as the result of the parsing and internal representation of the rules expressed as strings.
	 * The inputSpec parameter representing the specification of the rules is a List of (List of String)
	 * Each category of rule (OMIT, H1, H2, <B>, <I>) has a dedicated (List of String). 
	 * Every category absent is setup to undefined status and eventually mapped to the resp. RuleUndefined object that always returns false for isValid()
	 * The 0th element is the aforementioned category representative string
	 * The 1st element _must_ always be STARTS_WITH  
	 * The 3rd element is a prefix string for STARTS_WITH or a comma-separated string of positions (starting from 0) for POSITIONS
	 * 
	 * The prefixes parameter represents the List<String> to report on the marks at the beginning of each paragraph.
	 * 
	 *  
	 * @param inputSpec a List of (List of String) with the specification of the rules on how to handle paragraphs
	 * @param prefixes a List of Strings as the prefixes for the annotated paragraphs of the file
	 * @return a RuleSet object as the result of the parsing and internal representation of the rules expressed as strings.
	 */
	@Override
	public RuleSet registerInputRuleSetForAnnotatedFiles(List<List<String>> inputSpec, List<String> prefixes) {
		this.inputSpec = inputSpec;
		this.prefixes = prefixes;
		
		if (this.inputSpec == null)
			return null;
		
		RuleSetCreator ruleSetCreator = new RuleSetCreator(lineblocks, inputSpec, "inputRuleSet");
		this.inputRuleSet =  ruleSetCreator.createRuleSet();
		
		return this.inputRuleSet;
	}
		
	/**
	 * Takes the input file specified at the constructor, loads it and processes it according to the rule set specified at the constructor
	 *  
	 *  The blocks of the file are represented in a List in main memory, as the this.lineblocks attribute.
	 *   
	 * @return the number of LineBlocks that were identified and represented in-memory from the input file
	 */
	@Override
	public int loadFileAndCharacterizeBlocks() {
		if(this.lineblocks.size() == 0) {
			loadRawDocument();
		}
		
		if((this.lineblocks !=null) && (this.inputRuleSet!= null))
			characterizeLineblocks(this.document, this.inputRuleSet);
		
		return this.lineblocks.size();
	}
	

	
	
	
	/**
	 * Exports the input file of the constructor as the MarkDown file at the path specified by outputFileName
	 * 
	 *  If the input files has not been processed, and the this.lineblocks attribute has a size of 0, the method loads and characterizes the input
	 *  	
	 * @param outputFileName the path where the exported MarkDown file will be written
	 * @return the number of LineBlocks exported in the output file
	 */
	@Override
	public int exportMarkDown(String outputFileName) { //we wanted it to make this template method export() but the test called exportMarkdown and exportPdf and we didn't want the tests to fail during evaluation
		
		if(this.lineblocks.size() == 0) {
			loadFileAndCharacterizeBlocks();
		}
		
		MarkdownExporter exporter = new MarkdownExporter(this.document, outputFileName);
		int outputNumParagraphs = exporter.export();
		//System.out.println("[Engine.loadProcessMarkup] [file: " + simpleInputFileName + "] exported as " + outputFileName +"\n Input #pars: " + this.lineblocks.size() + " Output #pars: " + outputNumParagraphs);
		
		return outputNumParagraphs;
	}//end exportMarkDown

	/**
	 * Exports the input file of the constructor as the pdf file at the path specified by outputFileName
	 * 
	 *  If the input files has not been processed, and the this.lineblocks attribute has a size of 0, the method loads and characterizes the input
	 *  	
	 * @param outputFileName the path where the exported pdf file will be written
	 * @return the number of LineBlocks exported in the output file
	 */
	@Override
	public int exportPdf(String outputFileName) {
		if(this.lineblocks.size() == 0) {
			loadFileAndCharacterizeBlocks();
		}
		
		PdfExporter exporter = new PdfExporter(this.document, outputFileName);
		int outputNumParagraphs = exporter.export();
		//System.out.println("[Engine.loadProcessPdf] [file: " + simpleInputFileName + "] Input #pars: " + this.lineblocks.size() + " Output #pars: " + outputNumParagraphs);
		
		return outputNumParagraphs;
	}//end exportPdf

	/**
	 * Outputs a List<String> to be used as a report on the number of paragraphs and words of a file.
	 * 
	 * If the input file has not been previously loaded and processed, the method does so.
	 * Then, it creates a List<String> with the following elements:
	 * the 0th element reporting on the total number of paragraphs
	 * the 1st element re porting on the total number of words
	 * each subsequent element reporting on the number of words of each paragraph
	 * 
	 * @return the List<String> with the report's elements
	 */
	@Override
	public List<String> reportWithStats(){
		List<String> report = new ArrayList<String>();
		int numWords = 0;
		int numParagraphs = this.lineblocks.size();
		if(numParagraphs==0)                           
			numParagraphs = loadFileAndCharacterizeBlocks();
		report.add("\n"+ "Total number of paragraphs: " + numParagraphs );
		report.add("\n"+ "Total number of words: " + numWords);
		for(LineBlock lineblock: this.lineblocks) {
			report.add("\n"+ lineblock.getStatsAsString());
			numWords += lineblock.getNumWords();

		}
		report.set(1, "\nTotal number of words: " + numWords);
		return report;
	}
	
	
	/**
	 * Creates a RawFileLineLoader object and calls its load() method, which gets the text of the input file and splits it in LibeBlocks.
	 * Sets this Document object's list of LineBlocks to the RawFileLineLoader.load() returned list of LineBlocks
	 * Sets the engine's list of LineBlocks as well.
	 * 
	 * @return the number of LineBlocks in lineblocks list
	 */
	private int loadRawDocument() {
		List<LineBlock> temporaryList = new ArrayList<LineBlock>();
		
		RawFileLineLoader loader = new RawFileLineLoader();
		temporaryList = loader.load(this.filePath, this.lineblocks); 
		this.document.setLineBlocks(temporaryList);
		this.lineblocks = this.document.getLineblocks();
		
		return lineblocks.size();
	}//end loadRawDocument

	private void characterizeLineblocks(Document document, RuleSet ruleSet) {
		List<LineBlock> lineblocks = document.getLineblocks();
		Objects.requireNonNull(document);
		Objects.requireNonNull(lineblocks);
		Objects.requireNonNull(ruleSet);
		
		characterizeFile(lineblocks, ruleSet);


	}//end characterizeLineblocks

	/**
	 * calls this document's removePrefixes() method, which depending if the object is of type DocumentRaw or DocumentAnnotated does nothing or removes the prefixes given as parameter, respectively 
	 * for each LineBlock depending on the return value of ruleset.determineHeadingStatus/determineFormatStatus, sets the LineBlocks style and format.
	 *  
	 * @param lineblocks
	 * @param ruleSet
	 */
	private void characterizeFile(List<LineBlock> lineblocks, RuleSet ruleSet) {
	
		this.lineblocks = this.document.removePrefixes(prefixes);
		for(LineBlock l: lineblocks) {
			
			l.setStyle(ruleSet.determineHeadingStatus(l));
			l.setFormat(ruleSet.determineFormatStatus(l));
		}
	}//end characterizeRawFile
	
	
	
	
	
}//end class
