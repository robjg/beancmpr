package org.oddjob.beancmpr.beans;

/**
 * The type of the result. Used as a property on a match result bean.
 * 
 * @author rob
 *
 */
public interface MatchResultType {

	public enum Type {
		
		/** No X data was found matching the Y key. */
		X_MISSING,
		
		/** No Y data was found matching the X key. */
		Y_MISSING,
		
		/** Two things matched by key and their values were equal. */
		EQUAL,
		
		/** Two things matched by key but one or more of there values
		 * were not equal.
		 */
		NOT_EQUAL,		
	}
	
	public Type getType();
	
	
	public String getText();	
	
	public static abstract class XMissing implements MatchResultType {

		@Override
		public final Type getType() {
			return Type.X_MISSING;
		}
	}
	
	public static abstract class YMissing implements MatchResultType {

		@Override
		public final Type getType() {
			return Type.Y_MISSING;
		}
	}
	
	public static abstract class Equal implements MatchResultType {

		@Override
		public final Type getType() {
			return Type.EQUAL;
		}
	}
	
	public static abstract class NotEqual implements MatchResultType {

		@Override
		public final Type getType() {
			return Type.NOT_EQUAL;
		}
	}
}