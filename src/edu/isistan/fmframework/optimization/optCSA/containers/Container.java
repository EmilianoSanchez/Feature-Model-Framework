package edu.isistan.fmframework.optimization.optCSA.containers;

public interface Container<Element> {

	public void push(Element content);

	public boolean isEmpty();

	public Element pop();

	public Element peek();

	public void clear();
}
