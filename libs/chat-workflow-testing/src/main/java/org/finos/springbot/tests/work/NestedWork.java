package org.finos.springbot.tests.work;

import org.finos.springbot.workflow.annotations.Work;

@Work
public class NestedWork {

	public static class Inner {
		
		String s;

		public String getS() {
			return s;
		}

		public void setS(String s) {
			this.s = s;
		}
		
	}

	Inner a;
	Inner b;

	public Inner getA() {
		return a;
	}

	public void setA(Inner a) {
		this.a = a;
	}

	public Inner getB() {
		return b;
	}

	public void setB(Inner b) {
		this.b = b;
	}
	
	
}
