package edu.isistan.fmframework.optimization.optSPLConfigOriginal.model;

import java.util.LinkedList;
public class Feature {
	
	private String name;
	private boolean mandatory;
	private double cost;
	private double benefit; // 0=None, 1=Very Low, 2=Low, 3=Medium, 4=High, 5=Very High
	private String father;
	private double costMin;
	private int alt; // 0=None, 1=OR and 2=XOR
	LinkedList<String> group = new LinkedList<String>();
	
	public Feature(){
	
	}

	public Feature(String name, boolean mandatory, double cost, double benefit,
			String father, double costMin, int alt, LinkedList<String> group) {
		super();
		this.name = name;
		this.mandatory = mandatory;
		this.cost = cost;
		this.benefit = benefit;
		this.father = father;
		this.costMin = costMin;
		this.alt = alt;
		this.group = group;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public double getBenefit() {
		return benefit;
	}

	public double getBenefitAlg() {
		double aux = benefit * 0.2;
		return aux;
	}

	public void setBenefit(double benefit) {
		this.benefit = benefit;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public double getCostMin() {
		return costMin;
	}

	public void setCostMin(double costMin) {
		this.costMin = costMin;
	}

	public int getAlt() {
		return alt;
	}

	public void setAlt(int alt) {
		this.alt = alt;
	}

	public LinkedList<String> getGroup() {
		return group;
	}

	public void setGroup(LinkedList<String> group) {
		this.group = group;
	}
	
}