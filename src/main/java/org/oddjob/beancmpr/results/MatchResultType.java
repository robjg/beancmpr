package org.oddjob.beancmpr.results;

/**
 * The type of the result. Used as a property on a match result bean.
 * 
 * @author rob
 *
 */
public interface MatchResultType {

	public enum Type {
		
		/** Two things matched by key and their values were equal. */
		EQUAL,
		
		/** No X data was found matching the Y key. */
		X_MISSING,
		
		/** No Y data was found matching the X key. */
		Y_MISSING,
		
		/** Two things matched by key but one or more of there values
		 * were not equal.
		 */
		NOT_EQUAL,		
	}
	
	public Type getType();
	
	public String getText();	
	
	public static abstract class MatchResultTypeBase 
	implements MatchResultType {
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + ", text=" + getText();
		}
	}
	
	public static abstract class XMissing extends MatchResultTypeBase {

		@Override
		public final Type getType() {
			return Type.X_MISSING;
		}
	}
	
	public static abstract class YMissing  extends MatchResultTypeBase {

		@Override
		public final Type getType() {
			return Type.Y_MISSING;
		}
	}
	
	public static abstract class Equal  extends MatchResultTypeBase {

		@Override
		public final Type getType() {
			return Type.EQUAL;
		}
	}
	
	public static abstract class NotEqual  extends MatchResultTypeBase {

		@Override
		public final Type getType() {
			return Type.NOT_EQUAL;
		}
	}
}