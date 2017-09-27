package com.emiliano.fmframework;

import org.junit.Assert;
import org.junit.Test;

import com.emiliano.fmframework.building.FMGenerator;
import com.emiliano.fmframework.core.Configuration;
import com.emiliano.fmframework.optimization.ConfigurationSelectionInstance;
import com.emiliano.fmframework.optimization.csa.BacktrackingCSA;
import com.emiliano.fmframework.optimization.csa.BranchAndBoundCSA;
import com.emiliano.fmframework.optimization.csa.heuristicFunctions.Heuristics;
import com.emiliano.fmframework.optimization.csa.strategies.SearchStrategy;

public class TestOptimization {

	@Test
	public void testAlgorithms() {
		BacktrackingCSA bfsstar=new BacktrackingCSA(SearchStrategy.BestFirstSearchStar);
		BranchAndBoundCSA bandb=new BranchAndBoundCSA();
		BacktrackingCSA greedy=new BacktrackingCSA(SearchStrategy.GreedyBestFirstSearch,Heuristics.heuristicB);
		
		for(int i=0;i<100;i++){
			ConfigurationSelectionInstance csi=new FMGenerator().setNumFeatures(10).setNumTreeConstraints(3)
					.setNumCrossTreeConstraints(0)
					.setNumObjectives(1)
					.setNumResourceRestrictions(1).generateConfigurationSelectionInstance();
			
			Configuration confbfsstar=bfsstar.selectConfiguration(csi);
			Configuration confbandb=bandb.selectConfiguration(csi);
			Configuration confgreedy=greedy.selectConfiguration(csi);
			
			double valueconfbfsstar=csi.objective.evaluate(confbfsstar);
			double valueconfbandb=csi.objective.evaluate(confbandb);
			double valueconfgreedy=csi.objective.evaluate(confgreedy);
			System.out.println(valueconfbfsstar+" "+valueconfbandb +" "+valueconfgreedy);
			
			Assert.assertTrue(valueconfbfsstar==valueconfbandb);
			Assert.assertTrue(valueconfbfsstar<= valueconfgreedy);
		}
		
	}
}
