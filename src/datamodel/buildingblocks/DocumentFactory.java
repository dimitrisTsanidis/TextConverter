package datamodel.buildingblocks;

public class DocumentFactory {

	public Document createDocument(String pFilePath, String argument) {
		if(argument == null) {
			return null;
		}
		else if(argument.equalsIgnoreCase("RAW")) {
			return new DocumentRaw(pFilePath);
		}
		else {
			return new DocumentAnnotated(pFilePath);
		}	
	}
}
