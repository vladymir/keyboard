package br.ufc.quimica.keyboard.parser;

import java.util.HashMap;

/**
 * Look up table. Stores information about the atoms.
 * @author vladymirbezerra
 *
 */
public class LookUpTable {
	private HashMap<String, Integer> table = new HashMap<>();	
	private static LookUpTable instance;
	
	private LookUpTable() {
		initValues();
	}
	

	public static LookUpTable getTable() {
		if (null == instance) {
			instance = new LookUpTable();
		}
		return instance;
	}
	
	/**
	 * @TODO
	 * Feed the table with data, read from external source 
	 */
	private void initValues() {
		table.put("H", 1);
		table.put("Cl", 3);
		table.put("N", 4);
		
	}
	
	/**
	 * Obs.: Throw null exception
	 */
	public Integer lookAtom(String name) {
		return table.get(name);
	}
}
