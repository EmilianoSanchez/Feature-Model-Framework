package com.emiliano.fmframework.optimization.csa.strategies;

public class Stack<Element> implements Container<Element> {

	private java.util.Stack<Element> stack;

	public Stack() {
		this.stack = new java.util.Stack<Element>();
	}

	@Override
	public void push(Element content) {
		this.stack.push(content);
	}

	@Override
	public boolean isEmpty() {
		return this.stack.isEmpty();
	}

	@Override
	public Element pop() {
		return this.stack.pop();
	}

	@Override
	public Element peek() {
		return this.stack.peek();
	}

}
