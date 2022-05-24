package org.oddjob.beancmpr.matchables;

import org.oddjob.beancmpr.MockMatchDefinition;
import org.oddjob.beancmpr.matchables.MatchableMetaData;

public class MockMatchableMetaData extends MockMatchDefinition
implements MatchableMetaData {

	@Override
	public Class<?> getPropertyType(String name) {
		throw new RuntimeException("Unexpected");
	}	
}
