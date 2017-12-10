package edu.isistan.fmframework.optimization.optCSA.containers;

public class Queue<Element> implements Container<Element> {

	private java.util.Queue<Element> queue;

	public Queue() {
		this.queue = new java.util.LinkedList<Element>();
	}

	@Override
	public void push(Element content) {
		this.queue.add(content);
	}

	@Override
	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	@Override
	public Element pop() {
		return this.queue.poll();
	}

	@Override
	public Element peek() {
		return this.queue.peek();
	}

	@Override
	public void clear() {
		this.queue.clear();
	}

}
