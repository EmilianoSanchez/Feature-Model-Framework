package com.emiliano.fmframework.reasoning.objectiveFunctions;

import com.emiliano.fmframework.reasoning.aggregateFunctions.ProductFunction;

public class ProductObjective  extends AggregateObjective{

	public ProductObjective(String attributeSelected) {
		super(attributeSelected,new ProductFunction());
	}
	public ProductObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new ProductFunction());
	}

}