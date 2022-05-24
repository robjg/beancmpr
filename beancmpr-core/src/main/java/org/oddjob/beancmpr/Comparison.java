package org.oddjob.beancmpr;

/**
 * The result of a {@link Comparer} is a comparison between two things.
 * 
 * @author Rob
 *
 */
public interface Comparison<T> {

	/**
	 * Get the x of the comparison.
	 * 
	 * @return The x. Will not be null.
	 */
	public T getX();
	
	/**
	 * Get the y of the comparison.
	 * 
	 * @return The y. Will not be null.
	 */
	public T getY();
	
	/**
	 * The result of the comparison. 0 they are equal. -1 when x < y and
	 * 1 when x > y
	 * 
	 * @return true/false.
	 */
	public int getResult();

	/**
	 * Provide a brief summary of the comparison.
	 * <p>
	 * If the comparison is equal then this should be the text representation
	 * of either of the original values. If the comparison is not equal then
	 * this should be a short description of the difference, 
	 * e.g. 'Fred <> Jane'.
	 * <p>
	 * As a rule of thumb summary should be suitable 
	 * for displaying in the column of a report or the cell of a
	 * spreadsheet.
	 * 
	 * @return A short text description of the comparison.
	 */
	public String getSummaryText();
}
