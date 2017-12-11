package edu.isistan.fmframework.optimization.optSAT;

import edu.isistan.fmframework.core.Configuration;
import edu.isistan.fmframework.core.FeatureState;
import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.optimization.BasicAlgorithm;
import edu.isistan.fmframework.optimization.BasicProblem;
import edu.isistan.fmframework.optimization.Problem;
import edu.isistan.fmframework.optimization.opt01LP.Java01LPalgorithm.ILPSolver;
import net.sf.javailp.Linear;
import net.sf.javailp.Operator;
import net.sf.javailp.OptType;
import net.sf.javailp.Result;
import net.sf.javailp.Solver;
import net.sf.javailp.SolverFactory;
import net.sf.javailp.SolverFactoryGLPK;
import net.sf.javailp.SolverFactorySAT4J;

public class JavaSAT01LPalgorithm implements BasicAlgorithm {

	SolverFactory factory;
	ILPSolver ilpsolver;
	OptType optType;

	// Usar SAT4J, porque GLPK usa librerias nativas, lo que no es justo contra
	// nuestro algoritmo Java
	public static enum ILPSolver {
		SAT4J, GLPK
	}

	public JavaSAT01LPalgorithm() {
		this(OptType.MAX,ILPSolver.SAT4J);
	}
	
	public JavaSAT01LPalgorithm(OptType optType) {
		this(optType,ILPSolver.SAT4J);
	}

	public JavaSAT01LPalgorithm(ILPSolver ilpsolver) {
		this(OptType.MAX,ilpsolver);
	}
	
	public JavaSAT01LPalgorithm(OptType optType,ILPSolver ilpsolver) {
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


	net.sf.javailp.Problem problem;
	Solver solver;

	@Override
	public void preprocessInstance(BasicProblem instance) {
		if (instance.objectiveFunctions.length > 1 || instance.objectiveFunctions.length == 0)
			throw new IllegalArgumentException(
					"JavaSAT01LPalgorithm only supports instances with a single additive objective function");
		problem = buildProblem(instance);
		solver = factory.get(); // you should use this solver only once
		// for one problem
	}

	@Override
	public Configuration selectConfiguration(BasicProblem instance) {

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

	private net.sf.javailp.Problem buildProblem(BasicProblem instance) {
		net.sf.javailp.Problem problem = new net.sf.javailp.Problem();

		Linear linear;
		for (Clause clause : instance.model.getClauses()) {
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

		for (int r = 0; r < instance.globalConstraints.length; r++) {
			linear = new Linear();
			for (int i = 0; i < instance.model.getNumFeatures(); i++) {
				if (instance.globalConstraints[r].attributes[i] != 0.0)
					linear.add((long) (instance.globalConstraints[r].attributes[i] * MULTIPLIER), i);
			}
			problem.add(new net.sf.javailp.Constraint("constraint", linear, Operator.LE,
					(long) ((instance.globalConstraints[r].restrictionLimit) * MULTIPLIER)));
		}

		linear = new Linear();

		for (int i = 0; i < instance.model.getNumFeatures(); i++) {
			if (instance.objectiveFunctions[0].attributes[i] != 0.0)
				linear.add((long) (instance.objectiveFunctions[0].attributes[i] * MULTIPLIER), i);
			problem.setVarType(i, Boolean.class);
		}
		problem.setObjective(linear, optType);
		return problem;
	}

	@Override
	public String getName() {
		return BasicAlgorithm.super.getName() + "-" + ilpsolver.toString();
	}
	
	private static final double MULTIPLIER = 1000000.0;
}
