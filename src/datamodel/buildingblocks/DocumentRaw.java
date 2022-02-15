package datamodel.buildingblocks;

import java.util.List;



public class DocumentRaw extends Document {

	public DocumentRaw(String pPath) {
		super(pPath);
	}

	/**
	 * removes all unnecessary prefixes from each LineBlock in this document's list of LineBlock.
	 * 
	 * @param prefixes
	 * @return this document's list of LineBlock
	 */
	@Override
	public List<LineBlock> removePrefixes(List<String> prefixes) {
		return super.Lineblocks;
		
	}

}
