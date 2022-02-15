package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JFileChooser;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import engine.Engine;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JSeparator;
import javax.swing.JRadioButton;

public class TextProcessorView {
	private String path;
	private File selectedFile;
	private Engine engine;
	private JTextArea textArea;
	private List<List<String>> inputSpec;

	private JFrame frame;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextProcessorView window = new TextProcessorView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TextProcessorView() {
		doEverything();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void doEverything() {
		frame = new JFrame();
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(0, 0, 292, 229);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblDocTypeInputLabel = new JLabel("    Is the document raw or annotated?");
		lblDocTypeInputLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDocTypeInputLabel.setBounds(0, 0, 292, 24);
		panel.add(lblDocTypeInputLabel);
		
		JRadioButton rdbtnRawRadioButton = new JRadioButton("Raw document");
		rdbtnRawRadioButton.setBounds(6, 71, 134, 23);
		panel.add(rdbtnRawRadioButton);
		
		JRadioButton rdbtnAnnotatedRadioButton = new JRadioButton("Annotated document");
		rdbtnAnnotatedRadioButton.setBounds(6, 124, 134, 23);
		panel.add(rdbtnAnnotatedRadioButton);
		
		ButtonGroup group = new ButtonGroup();
		group.add(rdbtnRawRadioButton);
		group.add(rdbtnAnnotatedRadioButton);
		
		
		JButton btnConfirmDocumentTypeButton = new JButton("Confirm");
		btnConfirmDocumentTypeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				panel.setEnabled(false);
				panel.setVisible(false);
				String filename = selectedFile.getName(); 
				if(rdbtnRawRadioButton.isSelected()==true) { //Depending on user selection create an engine with RAW/ANNOTATED argument
					engine = new Engine(path, "RAW", filename);
					engine.registerInputRuleSetForPlainFiles(inputSpec);
				}
				else {
					engine = new Engine(path, "ANNOTATED", filename);
					List<String> prefixes = new ArrayList<String>();
					
					//Fill the prefixes list
					prefixes.add("<H1>");
					prefixes.add("<H2>");
					prefixes.add("<i>");
					prefixes.add("<b>");
					prefixes.add("<p>");
					
					engine.registerInputRuleSetForAnnotatedFiles(inputSpec, prefixes);
				}
			}
		});
		btnConfirmDocumentTypeButton.setBounds(193, 195, 89, 23);
		panel.add(btnConfirmDocumentTypeButton);
		
		panel.setEnabled(false);
		panel.setVisible(false);
		
		JTextArea textAreaRulesetInput = new JTextArea();
		textAreaRulesetInput.setBackground(new Color(248, 248, 255));
		textAreaRulesetInput.setBounds(150, 0, 456, 539);
		frame.getContentPane().add(textAreaRulesetInput);
		textAreaRulesetInput.setVisible(false);
	
		JButton btnRulesetEnteredOK = new JButton("OK");
		btnRulesetEnteredOK.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnRulesetEnteredOK.setVisible(false);
				textAreaRulesetInput.setVisible(false);
				String textAreaRulesetInputText = textAreaRulesetInput.getText(); //from here on we create rules
				
				if(textAreaRulesetInputText.equals("")) { //We don't accept empty rules list
					System.out.println("TextPorcessorView: Empty rules list. Exiting.");
					System.exit(-1);
				}
				
				List<String> IntermidiateList = new ArrayList<String>();
				IntermidiateList.addAll(Arrays.asList(textAreaRulesetInputText.split("\n")));  //splits the text to lines
				inputSpec = new ArrayList<List<String> >(); 
				for(String line: IntermidiateList){
					inputSpec.add(Arrays.asList(line.split(" ", 3))); //maximum of three arguments. Excess arguments are ignored.
				}
				
			}
		});
		btnRulesetEnteredOK.setBounds(643, 490, 89, 23);
		frame.getContentPane().add(btnRulesetEnteredOK);
		btnRulesetEnteredOK.setVisible(false);
		
		
		JLabel lblWelcomePrompt = new JLabel("Welcome! Please enter the ruleset");
		lblWelcomePrompt.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblWelcomePrompt.setForeground(Color.ORANGE);
		lblWelcomePrompt.setBounds(250, 218, 254, 40);
		frame.getContentPane().add(lblWelcomePrompt);
		
		JButton btnProceedRuleset = new JButton("Proceed");
		btnProceedRuleset.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textAreaRulesetInput.setVisible(true);
				lblWelcomePrompt.setVisible(false);
				lblWelcomePrompt.setEnabled(false);
				btnProceedRuleset.setVisible(false);
				btnProceedRuleset.setEnabled(false);
				btnRulesetEnteredOK.setVisible(true);
			}
		});
		
		btnProceedRuleset.setBounds(332, 271, 89, 23);
		frame.getContentPane().add(btnProceedRuleset);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 784, 539);
		frame.getContentPane().add(scrollPane);
		
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setFont(new Font("Calibri", Font.PLAIN, 13));
		
		
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		fileChooser.setForeground(Color.GRAY);
		fileChooser.setBounds(150, 91, 582, 399);
		frame.getContentPane().add(fileChooser);
		fileChooser.setVisible(false);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFileMenu = new JMenu("File");
		menuBar.add(mnFileMenu);
		
		JMenuItem mntmCloseItem = new JMenuItem("Close");
		mntmCloseItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				System.exit(0);
			}
		});
		
		JMenu mnExportMenu = new JMenu("Export As");
		mnExportMenu.setEnabled(false);
		mnFileMenu.add(mnExportMenu);
		JMenuItem mntmShowReportItem = new JMenuItem("Show Report");
		
		//Gets the document's to open path.
		JMenuItem mntmOpenDocument = new JMenuItem("Open Document");
		mntmOpenDocument.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if(inputSpec == null) {
					System.out.println("TextProcessorView: List of rules is empty. Please enter the ruleset before opening a file. Exiting.");
					System.exit(-1);
				}
				
				fileChooser.setVisible(true);
				int returnValue = fileChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					selectedFile = fileChooser.getSelectedFile();
					path = selectedFile.getAbsolutePath();
					
					panel.setEnabled(true);
					panel.setVisible(true);
					mnExportMenu.setEnabled(true);
					mntmShowReportItem.setEnabled(true);
				}
			}
		});
		mnFileMenu.add(mntmOpenDocument);
		
		JSeparator separator_1 = new JSeparator();
		mnFileMenu.add(separator_1);
		
		//Report choice
		mntmShowReportItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				textArea.setText(engine.reportWithStats().toString());
			}
		});
		mnFileMenu.add(mntmShowReportItem);
		mntmShowReportItem.setEnabled(false);		
		
		JFileChooser fileChooserSave = new JFileChooser();
		fileChooserSave.setDialogTitle("Specify a file to save");   
		
		//Pdf exprorter
		JMenuItem mntmExportAsPdfMenuItem = new JMenuItem("Pdf");
		mntmExportAsPdfMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int userSelection = fileChooserSave.showSaveDialog(null);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooserSave.getSelectedFile();
				   
					engine.exportPdf(fileToSave.getAbsolutePath());
				}
			}
		});
		mnExportMenu.add(mntmExportAsPdfMenuItem);
		
		//Markdwon Exporter
		JMenuItem mntmExportAsMarkdownMenuItem = new JMenuItem("Markdown");
		mntmExportAsMarkdownMenuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int userSelection = fileChooserSave.showSaveDialog(null);
				 
				if (userSelection == JFileChooser.APPROVE_OPTION) {
				    File fileToSave = fileChooserSave.getSelectedFile();
				   
						engine.exportMarkDown(fileToSave.getAbsolutePath());
				}
			}
		});
		mnExportMenu.add(mntmExportAsMarkdownMenuItem);
		
		JSeparator separator = new JSeparator();
		mnFileMenu.add(separator);
		mnFileMenu.add(mntmCloseItem);
		frame.getContentPane().setLayout(null);
	}
}
