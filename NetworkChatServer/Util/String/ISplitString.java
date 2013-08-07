package String;

/**
 * Interface - ISplitString
 * Class to split a Sting by a Char
 * @author Martin Hulkkonen
 *
 */
public interface ISplitString {
	
	/**
	 * Splits a String on the given char into peaces
	 * @param string - String that is to split
	 * @param split - Char with the string is compare and split
	 * @return Returns a array with the splitting parts of the string.
	 */
	String[] splitStringByChar(String string, char split);
}
