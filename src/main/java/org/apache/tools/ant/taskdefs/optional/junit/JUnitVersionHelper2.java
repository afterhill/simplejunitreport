package org.apache.tools.ant.taskdefs.optional.junit;

import junit.framework.Test;

public final class JUnitVersionHelper2 {

	/**
	 * Obtains the name of the test method from the given test.
	 * 
	 * @param test
	 *            the give test.
	 * @return the name of the test method obtained fromt the givent test.
	 */
	public static String testMethodName(Test test) {
		return JUnitVersionHelper.getTestCaseName(test);
	}

	/**
	 * Obtains the name of the test class from the given test.
	 * 
	 * @param test
	 *            the give test.
	 * @return the name of the test class obtained fromt the givent test.
	 */
	public static String testClassName(Test test) {
		return JUnitVersionHelper.getTestCaseClassName(test);
	}

	private JUnitVersionHelper2() {
	}
}
