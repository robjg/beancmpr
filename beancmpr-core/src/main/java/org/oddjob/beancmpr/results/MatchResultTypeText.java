package org.oddjob.beancmpr.results;

/**
 * The type of the result. Used as a property on a match result bean.
 * 
 * @author rob
 *
 */
public interface MatchResultTypeText {

	MatchResultType getType();
	
	String getText();
	
	abstract class MatchResultTypeBase
	implements MatchResultTypeText {
		
		@Override
		public String toString() {
			return getClass().getSimpleName() + ", text=" + getText();
		}
	}
	
	abstract class XMissing extends MatchResultTypeBase {

		@Override
		public final MatchResultType getType() {
			return MatchResultType.X_MISSING;
		}
	}
	
	abstract class YMissing  extends MatchResultTypeBase {

		@Override
		public final MatchResultType getType() {
			return MatchResultType.Y_MISSING;
		}
	}
	
	abstract class Equal  extends MatchResultTypeBase {

		@Override
		public final MatchResultType getType() {
			return MatchResultType.EQUAL;
		}
	}
	
	abstract class NotEqual  extends MatchResultTypeBase {

		@Override
		public final MatchResultType getType() {
			return MatchResultType.NOT_EQUAL;
		}
	}
}