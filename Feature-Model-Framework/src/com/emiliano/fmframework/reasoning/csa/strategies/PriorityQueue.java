package com.emiliano.fmframework.reasoning.csa.strategies;

public class PriorityQueue<Element extends Comparable<Element>> implements Container<Element> {

	private java.util.PriorityQueue<Element> pqueue;

	public PriorityQueue() {
		this.pqueue = new java.util.PriorityQueue<Element>();
	}

	@Override
	public void push(Element content) {
		this.pqueue.add(content);
	}

	@Override
	public boolean isEmpty() {
		return this.pqueue.isEmpty();
	}

	@Override
	public Element pop() {
		return this.pqueue.poll();
	}

	@Override
	public Element peek() {
		return this.pqueue.peek();
	}

}