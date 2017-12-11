package edu.isistan.fmframework.evaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import constraints.CNFClause;
import constraints.CNFLiteral;
import constraints.PropositionalFormula;
import edu.isistan.fmframework.core.Feature;
import edu.isistan.fmframework.core.FeatureModel;
import edu.isistan.fmframework.core.constraints.Clause;
import edu.isistan.fmframework.core.constraints.treeConstraints.AlternativeGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.CardinalityGroup;
import edu.isistan.fmframework.core.constraints.treeConstraints.MandatoryFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.OptionalFeature;
import edu.isistan.fmframework.core.constraints.treeConstraints.OrGroup;
import fm.FeatureGroup;
import fm.FeatureModelException;
import fm.FeatureTreeNode;
import fm.PlacemarkFeatureTreeNode;
import fm.RootNode;
import fm.SolitaireFeature;
import fm.XMLFeatureModel;

public class SXFMtoFM {
	
	public static FeatureModel parse(String featureModelFile) throws FeatureModelException{
		return SXFMtoFM.parse(featureModelFile,true);
	}
	
	public static FeatureModel parse(String featureModelFile,boolean parseModelWithClauses) throws FeatureModelException{
		fm.FeatureModel sxfm = new XMLFeatureModel(featureModelFile, XMLFeatureModel.USE_VARIABLE_NAME_AS_ID);
		sxfm.loadModel();
		return parse(sxfm,parseModelWithClauses);
	}
	
	private static Map<String,Integer> nodeMap;
	
	public static FeatureModel parse(fm.FeatureModel sxfm,boolean parseModelWithClauses){
		FeatureModel fm=new FeatureModel(sxfm.getName());
		
		FeatureTreeNode sxroot = sxfm.getRoot();
//		System.out.println(sxroot.getID());
		Feature root=new Feature(sxroot.getID());
		nodeMap=new HashMap<>();
		nodeMap.put(sxroot.getID(), 0);
		fm.addFeature(0, root);
		if(parseModelWithClauses)
			traverseTreeClauseBased(fm,sxroot,0,0);
		else
			traverseTreeCardinalityBased(fm,sxroot,0,0);
		traverseConstraints(fm,sxfm);
		return fm;
	}

	private static void traverseConstraints(FeatureModel fm, fm.FeatureModel sxfm) {
		for( PropositionalFormula formula : sxfm.getConstraints() ) {
//			System.out.println(formula);
			
			Collection<CNFClause> cnfClauses = formula.toCNFClauses();
			for(CNFClause cnfclause: cnfClauses){
				Clause clause=new Clause();
				for(CNFLiteral cnfliteral:cnfclause.getLiterals()){
					String literalId = cnfliteral.getVariable().getID();
//					System.out.println(literalId);
					clause.add(nodeMap.get(literalId), cnfliteral.isPositive());
				}
				
				fm.addCrossTreeConstraint(clause);
			}
		}
	}
	
	//return the new offset
	private static int traverseTreeClauseBased(FeatureModel fm, FeatureTreeNode sxnode, int nodeindex,int offset) {

		int childindex=offset;
		for( int i = 0 ; i < sxnode.getChildCount() ; i++ ) {
			FeatureTreeNode sxchildnode=(FeatureTreeNode) sxnode.getChildAt(i);

			if ( sxchildnode instanceof FeatureGroup ) {
//				boolean featureGroup=true;
				int minCardinality = ((FeatureGroup)sxchildnode).getMin();
				int maxCardinality = ((FeatureGroup)sxchildnode).getMax();
//				System.out.println("Feature Group[" + minCardinality + "," + maxCardinality + "]");
				
				int[] children=new int[sxchildnode.getChildCount()];
				for( int j = 0 ; j < sxchildnode.getChildCount() ; j++ ) {
					childindex++;
					children[j]=childindex;
					FeatureTreeNode sxchildgroupnode=(FeatureTreeNode) sxchildnode.getChildAt(j);
					nodeMap.put(sxchildgroupnode.getID(), childindex);
					Feature childgroupnode=new Feature(sxchildgroupnode.getID());
					fm.addFeature(childindex, childgroupnode);
				}
				
				if(minCardinality==1 && maxCardinality==1){
						fm.addTreeConstraint(new AlternativeGroup(nodeindex, children));
					}else{
						if(minCardinality==1 && maxCardinality==-1){
							fm.addTreeConstraint(new OrGroup(nodeindex, children));
						}else{
							System.out.println("ERROR");
							fm.addTreeConstraint(new CardinalityGroup(minCardinality,maxCardinality,nodeindex, children));
						}
				}
				
//				if (minCardinality == 1 && maxCardinality == -1) {
//					fm.addTreeConstraint(new OrGroup(nodeindex, children));
//				} else {
//					fm.addTreeConstraint(new CardinalityGroup(minCardinality, maxCardinality, nodeindex, children));
//				}
				
				
//				fm.addTreeConstraint(new CardinalityGroup(minCardinality,maxCardinality,nodeindex, children));
				
			} else {
				childindex++;
				nodeMap.put(sxchildnode.getID(), childindex);
				Feature childnode=new Feature(sxchildnode.getID());
				fm.addFeature(childindex, childnode);
				
				if ( sxchildnode instanceof RootNode ) {
//					System.out.println("Root");
				} else {
					if ( sxchildnode instanceof SolitaireFeature ) {
						if ( ((SolitaireFeature)sxchildnode).isOptional()){
//							System.out.println("Optional");
							fm.addTreeConstraint(new OptionalFeature(nodeindex, childindex));
						}else{
//							System.out.println("Mandatory");
							fm.addTreeConstraint(new MandatoryFeature(nodeindex, childindex));
						}
					} else {
						if ( sxchildnode instanceof PlacemarkFeatureTreeNode ) {
						} else {
						}
					}
				}
//				else {
//					System.out.println("Grouped");
//					groupedchildren.add(childindex);
//				}
			}
		}
		
		int newoffset=childindex;
		childindex=offset;
		for( int i = 0 ; i < sxnode.getChildCount() ; i++ ) {
			FeatureTreeNode sxchildnode=(FeatureTreeNode) sxnode.getChildAt(i);
			if ( sxchildnode instanceof FeatureGroup ) {
				for( int j = 0 ; j < sxchildnode.getChildCount() ; j++ ) {
					FeatureTreeNode sxchildgroupnode=(FeatureTreeNode) sxchildnode.getChildAt(j);
					childindex++;
					newoffset=traverseTreeClauseBased(fm,sxchildgroupnode, childindex,newoffset);
				}
			}else{
				childindex++;
				newoffset=traverseTreeClauseBased(fm,sxchildnode, childindex,newoffset);
			}
		}
		
		return newoffset;
	}

	//return the new offset
	private static int traverseTreeCardinalityBased(FeatureModel fm, FeatureTreeNode sxnode, int nodeindex,int offset) {

		int childindex=offset;
		for( int i = 0 ; i < sxnode.getChildCount() ; i++ ) {
			FeatureTreeNode sxchildnode=(FeatureTreeNode) sxnode.getChildAt(i);

			if ( sxchildnode instanceof FeatureGroup ) {
//				boolean featureGroup=true;
				int minCardinality = ((FeatureGroup)sxchildnode).getMin();
				int maxCardinality = ((FeatureGroup)sxchildnode).getMax();
//				System.out.println("Feature Group[" + minCardinality + "," + maxCardinality + "]");
				
				int[] children=new int[sxchildnode.getChildCount()];
				for( int j = 0 ; j < sxchildnode.getChildCount() ; j++ ) {
					childindex++;
					children[j]=childindex;
					FeatureTreeNode sxchildgroupnode=(FeatureTreeNode) sxchildnode.getChildAt(j);
					nodeMap.put(sxchildgroupnode.getID(), childindex);
					Feature childgroupnode=new Feature(sxchildgroupnode.getID());
					fm.addFeature(childindex, childgroupnode);
				}
				
//				if(minCardinality==1 && maxCardinality==1){
//						fm.addTreeConstraint(new AlternativeGroup(nodeindex, children));
//					}else{
//						if(minCardinality==1 && maxCardinality==-1){
//							fm.addTreeConstraint(new OrGroup(nodeindex, children));
//						}else{
//							fm.addTreeConstraint(new CardinalityGroup(minCardinality,maxCardinality,nodeindex, children));
//						}
//				}
				
				
//				if (minCardinality == 1 && maxCardinality == -1) {
//					fm.addTreeConstraint(new OrGroup(nodeindex, children));
//				} else {
//					fm.addTreeConstraint(new CardinalityGroup(minCardinality, maxCardinality, nodeindex, children));
//				}
				
				System.out.println("ERROR");
				fm.addTreeConstraint(new CardinalityGroup(minCardinality,maxCardinality,nodeindex, children));
				
			} else {
				childindex++;
				nodeMap.put(sxchildnode.getID(), childindex);
				Feature childnode=new Feature(sxchildnode.getID());
				fm.addFeature(childindex, childnode);
				
				if ( sxchildnode instanceof RootNode ) {
//					System.out.println("Root");
				} else {
					if ( sxchildnode instanceof SolitaireFeature ) {
						if ( ((SolitaireFeature)sxchildnode).isOptional()){
//							System.out.println("Optional");
							fm.addTreeConstraint(new OptionalFeature(nodeindex, childindex));
						}else{
//							System.out.println("Mandatory");
							fm.addTreeConstraint(new MandatoryFeature(nodeindex, childindex));
						}
					} else {
						if ( sxchildnode instanceof PlacemarkFeatureTreeNode ) {
						} else {
						}
					}
				}
//				else {
//					System.out.println("Grouped");
//					groupedchildren.add(childindex);
//				}
			}
		}
		
		int newoffset=childindex;
		childindex=offset;
		for( int i = 0 ; i < sxnode.getChildCount() ; i++ ) {
			FeatureTreeNode sxchildnode=(FeatureTreeNode) sxnode.getChildAt(i);
			if ( sxchildnode instanceof FeatureGroup ) {
				for( int j = 0 ; j < sxchildnode.getChildCount() ; j++ ) {
					FeatureTreeNode sxchildgroupnode=(FeatureTreeNode) sxchildnode.getChildAt(j);
					childindex++;
					newoffset=traverseTreeCardinalityBased(fm,sxchildgroupnode, childindex,newoffset);
				}
			}else{
				childindex++;
				newoffset=traverseTreeCardinalityBased(fm,sxchildnode, childindex,newoffset);
			}
		}
		
		return newoffset;
	}
	
}
