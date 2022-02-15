package datamodel.buildingblocks;

import java.util.List;

public class DocumentAnnotated extends Document {

	public DocumentAnnotated(String pPath) {
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
		for(LineBlock l: super.Lineblocks) {
			for(String prefix: prefixes) {
				if(l.toString().startsWith(prefix)) {
					String newString = l.toString().replaceFirst(prefix, "");
					l.setText(newString);
				}
			}
		}
		return super.Lineblocks;
	}

}
