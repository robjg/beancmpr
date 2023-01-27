package org.oddjob.beancmpr;

import org.junit.jupiter.api.Test;

import java.util.Iterator;


class SimpleMatchDefinitionTest extends TestCase {

	@Test
	void testSimple() {
		MatchDefinition test = new SimpleMatchDefinition(
				new String[] { "id", "fruit"},
				new String[] { "colour", "quantity", "price"},
				new String[] { "country", "organic" } );
		
		Iterator<String> iter;
		
		iter = test.getKeyProperties().iterator();
		
		assertEquals("id", iter.next());
		assertEquals("fruit", iter.next());
		
		iter = test.getValueProperties().iterator();
		
		assertEquals("colour", iter.next());
		assertEquals("quantity", iter.next());
		assertEquals("price", iter.next());
		
		iter = test.getOtherProperties().iterator();
		
		assertEquals("country", iter.next());
		assertEquals("organic", iter.next());						
	}		
}
