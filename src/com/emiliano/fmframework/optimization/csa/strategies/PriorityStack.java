package com.emiliano.fmframework.optimization.csa.strategies;

public class PriorityStack<Element extends Comparable<Element>> implements Container<Element> {

	private java.util.Stack<Element> stack;
	private java.util.PriorityQueue<Element> pqueue;

	public PriorityStack() {
		this.stack = new java.util.Stack<Element>();
		this.pqueue = new java.util.PriorityQueue<Element>();
	}

	@Override
	public void push(Element content) {
		this.pqueue.add(content);
	}

	@Override
	public boolean isEmpty() {
		if (this.pqueue.isEmpty()) {
			return stack.isEmpty();
		} else {
			java.util.Stack<Element> swap = new java.util.Stack<Element>();
			while (!this.pqueue.isEmpty()) {
				swap.push(this.pqueue.poll());
			}
			while (!swap.isEmpty()) {
				this.stack.push(swap.pop());
			}
			return false;
		}
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