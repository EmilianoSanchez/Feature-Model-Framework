package com.emiliano.fmframework.optimization.objectiveFunctions;

import com.emiliano.fmframework.optimization.aggregateFunctions.ProductFunction;

public class ProductObjective  extends AggregateObjective{

	public ProductObjective(String attributeSelected) {
		super(attributeSelected,new ProductFunction());
	}
	public ProductObjective(String attributeSelected, String attributeDeselected) {
		super(attributeSelected,attributeDeselected,new ProductFunction());
	}

}