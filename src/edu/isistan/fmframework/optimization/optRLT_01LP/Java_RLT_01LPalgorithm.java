package edu.isistan.fmframework.optimization.optRLT_01LP;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

import org.apache.commons.lang3.tuple.Pair;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.ClauseBasedConstraint;
import edu.isistan.fmframework.core.constraints.globalConstraints.InequalityRestriction;
import edu.isistan.fmframework.core.constraints.treeConstraints.TreeConstraint;
import edu.isistan.fmframework.optimization.Algorithm;
import edu.isistan.fmframework.optimization.BasicAlgorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.objectiveFunctions.MultiLinearPolynomialObjective;
import net.sf.javailp.Linear;
import net.sf.javailp.Operator;
import net.sf.javailp.OptType;
import net.sf.javailp.Problem;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryGLPK;
import net.sf.javailp.SolverFactorySAT4J;

public class Java_RLT_01LPalgorithm implements Algorithm<edu.isistan.fmframework.optimization.Problem<?, ?>>  {

	SolverFactory factory;
	ILPSolver ilpsolver;
	OptType optType;

	// Usar SAT4J, porque GLPK usa librerias nativas, lo que no es justo contra
	// nuestro algoritmo Java
	public static enum ILPSolver {
		SAT4J, GLPK
	}

	public Java_RLT_01LPalgorithm() {
		this(OptType.MIN, ILPSolver.SAT4J);
	}

	public Java_RLT_01LPalgorithm(OptType optType) {
		this(optType, ILPSolver.SAT4J);
	}

	public Java_RLT_01LPalgorithm(ILPSolver ilpsolver) {
		this(OptType.MIN, ilpsolver);
	}

	public Java_RLT_01LPalgorithm(OptType optType, ILPSolver ilpsolver) {
		this.ilpsolver = ilpsolver;
		switch (ilpsolver) {
		case SAT4J:
			factory = new SolverFactorySAT4J();
		case GLPK:
			factory = new SolverFactoryGLPK();
		}
		this.factory.setParameter(Solver.VERBOSE, 0);
		this.optType = optType;
	}

	Problem problem;
	Solver solver;

	@Override
	public void preprocessInstance(edu.isistan.fmframework.optimization.Problem<?, ?> instance) {
		problem = buildProblem(instance);
		solver = factory.get(); // you should use this solver only once
		// for one problem
	}

	@Override
	public Configuration selectConfiguration(edu.isistan.fmframework.optimization.Problem<?, ?> instance) {

		Result result = solver.solve(problem);

		// System.out.println(result);

		if (result != null) {
			Configuration conf = new Configuration(instance.model, FeatureState.DESELECTED);
			for (int i = 0; i < instance.model.getNumFeatures(); i++) {
				if (result.getBoolean(i))
					conf.setFeatureState(i, true);
			}
			return conf;
		}
		return null;
	}

	private Problem buildProblem(edu.isistan.fmframework.optimization.Problem<?, ?> instance) {
		Problem problem = new Problem();

		Linear linear = new Linear();
		linear.add(1, 0);
		problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.EQ, 1));
		for (int i = 1; i < instance.model.getNumFeatures(); i++) {
			int parent = instance.model.getParent(i);
			linear = new Linear();
			linear.add(-1, parent);
			linear.add(1, i);
			problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.LE, 0));
		}

		for (TreeConstraint treeConstraint : instance.model.getTreeConstraints()) {

			switch (treeConstraint.getType()) {
			case CARDINALITY_GROUP:
				linear = new Linear();
				Linear linear2 = new Linear();
				for (int c = 0; c < treeConstraint.getChildren().length; c++) {
					linear.add(1, treeConstraint.getChildren()[c]);
					linear2.add(1, treeConstraint.getChildren()[c]);
				}
				linear.add(-treeConstraint.getMinCardinality(), treeConstraint.getParent());
				if (treeConstraint.getMinCardinality() == treeConstraint.getMaxCardinality()) {
					problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.EQ, 0));
				} else {
					problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.GE, 0));
					linear2.add(-treeConstraint.getMaxCardinality(), treeConstraint.getParent());
					problem.add(new net.sf.javailp.Constraint("constraint", linear2, Operator.LE, 0));
				}
				break;
			case ALTERNATIVE_GROUP:
				linear = new Linear();
				for (int c = 0; c < treeConstraint.getChildren().length; c++) {
					linear.add(1, treeConstraint.getChildren()[c]);
				}
				linear.add(-1, treeConstraint.getParent());
				problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.EQ, 0));
				break;
			case OR_GROUP:
				linear = new Linear();
				for (int c = 0; c < treeConstraint.getChildren().length; c++) {
					linear.add(1, treeConstraint.getChildren()[c]);
				}
				linear.add(-1, treeConstraint.getParent());
				problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.GE, 0));
				break;
			case MANDATORY:
				int parent = treeConstraint.getParent();
				linear = new Linear();
				linear.add(1, parent);
				linear.add(-1, treeConstraint.getChildren()[0]);
				problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.LE, 0));
				break;

			// case CARDINALITY:
			}

		}

		for (ClauseBasedConstraint constraint : instance.model.getCrossTreeConstraints()) {
			Set<Clause> clauses = new HashSet<>();
			for (Clause clause : constraint.getClauses()) {
				if (!clauses.contains(clause)) {
					clauses.add(clause);
					linear = new Linear();
					int negativeliterals = 0;
					for (int i = 0; i < clause.literalIds.length; i++) {
						if (clause.literalValues[i]) {
							linear.add(1, clause.literalIds[i]);
						} else {
							linear.add(-1, clause.literalIds[i]);
							negativeliterals++;
						}
					}
					problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.GE, 1 - negativeliterals));
				}
			}

		}

		for (int r = 0; r < instance.globalConstraints.length; r++) {
			linear = new Linear();
			for (int i = 0; i < instance.model.getNumFeatures(); i++) {
				linear.add((long) (((InequalityRestriction)instance.globalConstraints[r]).attributes[i] * MULTIPLIER), i);
			}
			problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.LE,
					(long) ((((InequalityRestriction)instance.globalConstraints[r]).restrictionLimit) * MULTIPLIER)));
		}

		if (!(instance.objectiveFunctions[0] instanceof MultiLinearPolynomialObjective)) {
			throw new UnsupportedOperationException("No se soporta la funcion");
		}

		Linear linearObjective = new Linear();
		MultiLinearPolynomialObjective objective = (MultiLinearPolynomialObjective) instance.objectiveFunctions[0];
		List<Pair<Double, SortedSet<Integer>>> terms = objective.getTerms();

		int newVariable = instance.model.getNumFeatures();
		for (Pair<Double, SortedSet<Integer>> term : terms) {
			// System.out.println("Term: "+term);
			switch (term.getRight().size()) {
			case 0:
				linearObjective.add((long) (term.getKey() * MULTIPLIER), newVariable);
				linear = new Linear();
				linear.add(1, newVariable);
				problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.EQ, 1));
				newVariable++;
				break;
			case 1:
				linearObjective.add((long) (term.getKey() * MULTIPLIER), term.getRight().first());
				break;
			default:
				linearObjective.add((long) (term.getKey() * MULTIPLIER), newVariable);
				//
				int negLiterals = 0;
				linear = new Linear();
				for (int variable : term.getRight()) {
					linear.add(-1, variable);
					negLiterals++;
				}
				linear.add(1, newVariable);
				problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.GE, 1 - negLiterals));

				for (int variable : term.getRight()) {
					linear = new Linear();
					linear.add(1, variable);
					linear.add(-1, newVariable);
					problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.GE, 0));
				}
				//
				newVariable++;
			}
		}
		for (int i = 0; i < newVariable; i++)
			problem.setVarType(i, Boolean.class);

		// System.out.println("linearObjective: "+ linearObjective);

		problem.setObjective(linearObjective, optType);
		return problem;
	}

	@Override
	public String getName() {
		return "RLT 01LP-" + ilpsolver.toString();
	}

	private static final double MULTIPLIER = 1000000.0;
}
