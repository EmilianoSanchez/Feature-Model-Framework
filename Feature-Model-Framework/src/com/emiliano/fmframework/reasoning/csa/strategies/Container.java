package com.emiliano.fmframework.reasoning.csa.strategies;

public interface Container<Element> {

	public void push(Element content);

	public boolean isEmpty();

	public Element pop();

	public Element peek();

	// public default void clear(){
	// while(!isEmpty())
	// pop();
	// };
}
