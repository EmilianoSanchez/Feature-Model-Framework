package com.emiliano.fmframework.core.constraints;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.core.FeatureState;

public class Clause extends Constraint {

	public Map<String, Boolean> literals;

	public Clause() {
		literals = new HashMap<String, Boolean>();
	}

	public Clause(String[] literalNames, boolean[] literalValues) {
		this();
		for (int i = 0; i < literalNames.length; i++)
			literals.put(literalNames[i], literalValues[i]);
	}

	public Clause(Map<String, Boolean> literals) {
		this();
		literals.putAll(literals);
	}

	@Override
	public boolean isSatisfied(Configuration conf) {
		for (Map.Entry<String, Boolean> literal : this.literals.entrySet()) {
			FeatureState state = conf.getFeatureState(literal.getKey());
			if (state == FeatureState.UNSELECTED || state.booleanValue() == literal.getValue())
				return true;
		}
		return false;
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		clauses.add(this);
	}

	// public void put(String name, boolean selected) {
	// this.literals.put(name, selected);
	// }
	//
	// public Set<String> keySet() {
	// return this.literals.keySet();
	// }
	// public Set<Entry<String, Boolean>> entrySet() {
	// return this.literals.entrySet();
	// }
	//
	// public boolean contains(String name){
	// return this.literals.containsKey(name);
	// }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((literals == null) ? 0 : literals.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clause other = (Clause) obj;
		if (literals == null) {
			if (other.literals != null)
				return false;
		} else if (!literals.equals(other.literals))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		// string.append("(");
		for (Map.Entry<String, Boolean> literal : this.literals.entrySet()) {
			if (literal.getValue() == false)
				string.append('!');
			string.append('"' + literal.getKey() + "\"|");
		}
		string.deleteCharAt(string.length() - 1);
		// string.append(")");
		return string.toString();
	}
}