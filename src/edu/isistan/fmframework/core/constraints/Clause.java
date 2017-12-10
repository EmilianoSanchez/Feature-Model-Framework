package edu.isistan.fmframework.core.constraints;

import java.util.Arrays;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;

public class Clause extends ClauseBasedConstraint {

	public int[] literalIds;
	public boolean[] literalValues;

	public Clause() {
		literalIds = new int[]{};
		literalValues = new boolean[]{};
	}

	public Clause(int[] literalIds, boolean[] literalValues) {
		if(literalIds.length==literalValues.length){
			this.literalIds = literalIds;
			this.literalValues = literalValues;
		}else
			throw new RuntimeException("literalIds must have the same length than literalValues");
	}

	public Clause(int literalId, boolean literalValue) {
		this(new int[]{literalId},new boolean[]{literalValue});
	}

	@Override
	public boolean isSatisfied(Configuration conf) {
		for (int i=0;i<this.literalIds.length;i++) {
			FeatureState state = conf.getFeatureState(literalIds[i]);
			if (state == FeatureState.UNSELECTED || state.booleanValue() == literalValues[i])
				return true;
		}
		return false;
	}

	@Override
	protected void generateClauses(Set<Clause> clauses) {
		clauses.add(this);
	}
	
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		// string.append("(");
		for (int i=0;i<literalIds.length;i++) {
			if (literalValues[i] == false)
				string.append('!');
			string.append("\"" + literalIds[i] + "\"|");
		}
		string.deleteCharAt(string.length() - 1);
		// string.append(")");
		return string.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(literalIds);
		result = prime * result + Arrays.hashCode(literalValues);
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
		if (!Arrays.equals(literalIds, other.literalIds))
			return false;
		if (!Arrays.equals(literalValues, other.literalValues))
			return false;
		return true;
	}

	public void add(int literalId, boolean literalValue) {
		this.literalIds=ArrayUtils.addAll(this.literalIds, new int[]{literalId});
		this.literalValues=ArrayUtils.addAll(this.literalValues, new boolean[]{literalValue});
	}
	
	@Override
	public Clause clone() {
		Clause clone = new Clause(this.literalIds.clone(),this.literalValues.clone());
		return clone;
	}
}