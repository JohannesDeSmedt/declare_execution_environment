package org.processmining.plugins.declare.visualizing;

public interface IConditionSytaxCheckerListener {
	/**
	 * checkSyntax
	 * 
	 * @param expresion
	 *            String
	 */
	public boolean checkSyntax(String expression);

	/**
	 * checkSyntax
	 * 
	 * @param expression
	 *            String
	 */
	public void checkSyntaxNotify(String expression);
}
