package org.oddjob.beancmpr.composite;

import junit.framework.TestCase;

public class ComparersExamplesTest extends TestCase {

	public static class A {
		
		private double someNum;
		
		private B b;

		private C c;
		
		public double getSomeNum() {
			return someNum;
		}

		public void setSomeNum(double someNum) {
			this.someNum = someNum;
		}

		public B getB() {
			return b;
		}

		public void setB(B b) {
			this.b = b;
		}

		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}
	}
	
	public static class B {
		
		private double anotherNm;
		
		private C c;

		public double getAnotherNm() {
			return anotherNm;
		}

		public void setAnotherNm(double anotherNm) {
			this.anotherNm = anotherNm;
		}

		public C getC() {
			return c;
		}

		public void setC(C c) {
			this.c = c;
		}
	}
	
	public static class C {
		
		private double oneMoreNum;

		public double getOneMoreNum() {
			return oneMoreNum;
		}

		public void setOneMoreNum(double oneMoreNum) {
			this.oneMoreNum = oneMoreNum;
		}
	}
	
	public void testComplexExample() {
		
	}
}
