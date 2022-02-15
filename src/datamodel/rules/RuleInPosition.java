package datamodel.rules;

import java.util.List;

import datamodel.buildingblocks.LineBlock;

public class RuleInPosition extends AbstractRule {
	private List<LineBlock> lineblocks;
	private List<Integer> positions;
	
	// Stores the List of all Lineblocks and the list of positions in which the rule applies. 
	public RuleInPosition(List<LineBlock> pLineblocks, List<Integer> pPositions) {
		this.lineblocks = pLineblocks;
		this.positions = pPositions;
	}
	
	/**
	 * This method takes input a Lineblock (paragraph) object and checks if its index in the Lineblocks list is equal with one of the values in the positions list.
	 * Returns true or false.
	 */
	@Override
	public boolean isValid(LineBlock paragraph) {
	
		
		int counter = 0;
		for(LineBlock l: lineblocks) {
			
			if(l == paragraph) { 
				for(int p: positions) {
					if( counter == p) {
						return true;
					}
				}		
			}
			counter ++;
		}
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return positions.toString();
	}

}
