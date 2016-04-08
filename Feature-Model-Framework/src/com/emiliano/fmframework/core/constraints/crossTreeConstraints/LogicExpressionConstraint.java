package com.emiliano.fmframework.core.constraints.crossTreeConstraints;

import java.util.Set;

import com.emiliano.fmframework.core.constraints.Clause;
import com.emiliano.fmframework.core.constraints.Constraint;

public class LogicExpressionConstraint extends Constraint {

	private String expression;

	public LogicExpressionConstraint(String expression) {
		this.expression = expression;
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		// TODO Auto-generated method stub
	}

	// enum TokenType {
	// OPEN_PARENTESIS, CLOSE_PARENTESIS, NAME, NEGATION, AND, OR, IMPLY,
	// EXCLUDE, MUTUAL_EXCLUSION, MUTUAL_IMPLICATION, END
	// }
	//
	// private static class Token {
	//
	// Token(TokenType type) {
	// this.type = type;
	// }
	//
	// Token(String name) {
	// this.type = TokenType.NAME;
	// this.name = name;
	// }
	//
	// TokenType type;
	// String name;
	// }
	//
	// private static class Tokeniser {
	//
	// private int pos;
	// private String expression;
	//
	// Tokeniser(String expression) {
	// this.expression = expression;
	// this.pos = 0;
	// };
	//
	// Token getNextToken() {
	// if (pos >= this.expression.length())
	// return new Token(TokenType.END);
	// Token token = null;
	// switch (expression.charAt(pos)) {
	// case '(':
	// token = new Token(TokenType.OPEN_PARENTESIS);
	// case ')':
	// token = new Token(TokenType.CLOSE_PARENTESIS);
	// case '"':
	//
	// }
	// pos++;
	// return token;
	// };
	// }
}
