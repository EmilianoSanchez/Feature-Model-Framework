package edu.isistan.fmframework.optimization.optCSA.variableSelectors;

public class VariableSelectors {
	public static TopDown topDownVariableSelector=new TopDown();
	public static BottomUp bottomUpVariableSelector=new BottomUp();
	public static MaxValuePerWeight maxValuePerWeightVariableSelector=new MaxValuePerWeight();
	public static MaxHeuristicValue maxHeuristicValueVariableSelector=new MaxHeuristicValue();
	public static MostConstrainedFeature mostConstrainedFeatureVariableSelector=new MostConstrainedFeature();
}
