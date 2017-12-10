package edu.isistan.fmframework.optimization.optCSA.containers.debug;

import edu.isistan.fmframework.optimization.optCSA.containers.Container;

public class DebugContainer<Element> implements Container<Element>{

	long expandedStates;
	long elements=0;
	long maxFrontier;
	Container<Element> container;
	
	public DebugContainer(Container<Element> container) {
		this.container=container;
		this.expandedStates=0;
		this.maxFrontier=0;
	}
	
	public void push(Element content){
		expandedStates++;
		elements++;
		if(elements>maxFrontier)
			maxFrontier=elements;
		this.container.push(content);
	};

	public Element pop(){
		elements--;
		return this.container.pop();
	};

	public void clear(){
		expandedStates=0;
		maxFrontier=0;
		elements=0;
		this.container.clear();
	}

	@Override
	public boolean isEmpty() {
		return this.container.isEmpty();
	}

	@Override
	public Element peek() {
		return this.container.peek();
	}

	public long getExpandedStates() {
		return expandedStates;
	}

	public long getMaxFrontier() {
		return maxFrontier;
	}

	public Container<Element> getContainer() {
		return container;
	};
	
	
	
}
