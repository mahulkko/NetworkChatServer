package Util.String.impl;

import org.apache.log4j.Logger;

import Util.String.ISplitString;

/**
 * Class - Split String
 * Class to split a Sting by a Char
 * @author Martin Hulkkonen
 * 
 */
public class SplitString implements ISplitString {
	
	/**
	 * String Array for the split string
	 */
	private String[] parts;
	
	/**
	 * Count variable for the string array
	 */
	private int count;
	
	/**
	 * Place of the last found split char
	 */
	private int place;
	
	/**
	 * Logger for log4j SplitString
	 */
	static Logger log = Logger.getLogger("Util.SplitString");

	@Override
	public String[] splitStringByChar(String string, char split) {
		// Initialize the values with zero
		init();
		// Start splitting the string
		log.info("Start to split the array into parts");
		for (int i = 0; i < string.length(); i++) {
			// Found a split char
			if (string.charAt(i) == split) {
				log.info("Found a new split char - so split the string");
				ensureCapacity(this.parts.length + 1);
				parts[this.count] = string.subSequence(this.place, i).toString();
				this.place = i;
				this.count++;
			}
		}
		parts[this.count] = string.subSequence(this.place, string.length()).toString();
		log.info("Return the split string");
		return parts;
	}
	
	/**
	 * Set all values to zero
	 */
	private void init() {
		log.info("Inizialize all values with zero - make new string array");
		this.parts = new String[1];
		this.count = 0;
		this.place = 0;
	}
	
	/**
	 * Makes the string array bigger
	 * @param newCapacity - Set the Array to the new capacity
	 */
	private void ensureCapacity(int newCapacity) {
		// The new capacity must bigger then the old capacity
		log.info("Make the String Array bigger");
		if(newCapacity < this.parts.length) {
			log.warn("New capacity is not bigger then the old");
			return;
		}
		// Make the thread array bigger
		String[] old = this.parts;
		this.parts= new String[newCapacity];
		log.info("Copy all strings to the new array");
		System.arraycopy(old, 0, this.parts, 0, old.length);
	} 
}