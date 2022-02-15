package datamodel.buildingblocks;


import java.util.ArrayList;
import java.util.List;



public abstract class Document {
	protected List<LineBlock> Lineblocks;

	
	public Document (String pPath) {
		Lineblocks = new ArrayList<LineBlock>();
	
	}
	
	/**
	 * Setter for this document's Lineblocks object
	 * 
	 * @param lb
	 */
	public void setLineBlocks(List<LineBlock> lb) {
		this.Lineblocks = lb;
	}
	
	/**
	 * Getter for this document's Lineblocks object
	 */
	public List<LineBlock> getLineblocks() {
		
		return this.Lineblocks;
	}
	
	/**
	 * removes all unnecessary prefixes from each LineBlock in this document's list of LineBlock.
	 * 
	 * @param prefixes
	 * @return this document's list of LineBlock
	 */
	public abstract List<LineBlock> removePrefixes(List<String> prefixes);
}
