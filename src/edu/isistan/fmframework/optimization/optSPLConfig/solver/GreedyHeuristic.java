package edu.isistan.fmframework.optimization.optSPLConfig.solver;


import java.util.LinkedList;

import edu.isistan.fmframework.optimization.optSPLConfig.model.FM;
import edu.isistan.fmframework.optimization.optSPLConfig.model.Feature;


public class GreedyHeuristic {
	
	private static double budget; //Sum costs of the features in root
	private static double c_root;
	private static double b_root;
	private static double c_min_root;
	private long start_time;
	
	private static LinkedList<Feature> featureList = new LinkedList<Feature>(); //List of features in tree (except root)
	private static LinkedList<Feature> featureListAux = new LinkedList<Feature>(); 
	private static LinkedList<Integer> root = new LinkedList<Integer>(); //List of features in root
	
	
	public GreedyHeuristic(FM fm, String budgetText){
		
		start_time = System.currentTimeMillis();
		
		boolean solution = true;
		root.clear();
		featureList.clear();
		startFeatureModel(true, fm, budgetText);
		//printRoot();
		//printFeatureList();
		
		do{
			merge ();
			
			//set cost minimum
			for (int i=featureList.size()-1; i>=0; i--){
				featureList.get(i).setCostMin(featureList.get(i).getCost() + calculateCostMin(featureList.get(i).getName()));
			}
	        c_min_root = c_root + calculateCostMin(root.get(0));
	        
	        if (c_min_root <= budget){
	            
	            //delete features that cut the budget
	            deleteFeature(); 
	            if (featureList.isEmpty()){
	                break;
	            }
	            
	            //set cost minimum
				for (int i=featureList.size()-1; i>=0; i--){
					featureList.get(i).setCostMin(featureList.get(i).getCost() + calculateCostMin(featureList.get(i).getName()));
				}
		        c_min_root = c_root + calculateCostMin(root.get(0));
	    
	            impGreedyHeuristic();
	            
	        }else{
	        	solution = false;
	            //System.out.println("Nao ha orcamento disponivel!");
	            root.clear();
	            break;
	        }
        
		} while(!featureList.isEmpty());
		
		if (solution){
	        //features selected
			startFeatureModel(false, fm, budgetText);
			//printFeatureList();
	        FindDependents();
	        //printRoot();
	        outputFile(start_time);
	    }
	}
	
	public static void startFeatureModel(boolean r, FM fm, String budgetText){
		
		budget = Double.parseDouble(budgetText);
		
		if(r){
			//Root
			root.add(fm.getFeatures().get(fm.getRootIndex()).getName());
			c_root = fm.getFeatures().get(fm.getRootIndex()).getCost();
			c_min_root = c_root;
			b_root = 5;
			
			featureList = fm.getFeatures();
			// Remove root feature
			featureList.remove(fm.getRootIndex());
			// Mandatory features have "very high" benefits
			for(Feature f : featureList)
				if (f.isMandatory())
					f.setBenefit(5);
			// "none" benefit condition
			for(Feature f : featureList)
				if (f.getBenefit() == 0 && !f.isMandatory())
					f.setCost(budget+1);
			
			criateListAux();
		}else{
			featureList = featureListAux;
		}
	}
	
	public static void criateListAux(){
		
		java.util.LinkedList<Integer> g = new java.util.LinkedList<Integer>();
		
		//featureListAux = featureList;
		for (int i=0; i<featureList.size(); i++){
			int n = featureList.get(i).getName();
			boolean t = featureList.get(i).isMandatory();
			double c = featureList.get(i).getCost();
			double b = featureList.get(i).getBenefit();
			int f = featureList.get(i).getFather();
			int a = featureList.get(i).getAlt();
			
			if (a != 0){
				g = featureList.get(i).getGroup();
			}
			
			double c_m = featureList.get(i).getCostMin();
			
			featureListAux.add(new Feature(n, t, c, b, f, c_m, a, g));
		}
	}
	
	public static void merge(){

		for (int i=0; i<featureList.size(); i++){
			
			if (featureList.get(i).isMandatory()){//Is it mandatory?
				
				int n = featureList.get(i).getName();
				double c = featureList.get(i).getCost();
				double b = featureList.get(i).getBenefit();
				int f = featureList.get(i).getFather();
				
				if (f == root.get(0)){//If the father is the root...
					//Insert feature in root
				    root.add(n);
				    //sum feature cost with the root cost 
	                c_root = c_root + c;
	                b_root = b_root + b;
				}else{
					//Change father cost and benefit
					for (int j=0; j<featureList.size(); j++){
						if (f == featureList.get(j).getName()){
							featureList.get(j).setCost((featureList.get(j).getCost()) + c);
							featureList.get(j).setBenefit((featureList.get(j).getBenefit()) + b);
	                    }
					}
				}
				
				//Change father
				for (int j=0; j<featureList.size(); j++){
	                if (n == featureList.get(j).getFather()){
	                	featureList.get(j).setFather(f);
	                }
	            }
				
				// Delete mandatory feature in featureList
				featureList.remove(featureList.get(i));
				i--;
			}
		}
	}
	
	//Cost minimum
	public static double calculateCostMin(int f){
	     
	     double aux_cost = 0;
	     double c_min = 0;
	     double min = 0;   
	     
	     for(int i1=0; i1<featureList.size(); i1++){
	    	 
	    	 if ((f==featureList.get(i1).getFather()) && (featureList.get(i1).getAlt() != 0)){ //Is f father and it is alternative?
	    		 
	    		 aux_cost = featureList.get(i1).getCost();
	    		 
	    		 for (int i2=0; i2<featureList.get(i1).getGroup().size(); i2++){
	    			 
	    			 for(int i3=0; i3<featureList.size(); i3++){
	    				 
	    				 int nameGroup = featureList.get(i1).getGroup().get(i2);
	    				 int nameList = featureList.get(i3).getName();
	    				 if (nameGroup == nameList){
	                          if (aux_cost <  featureList.get(i3).getCostMin()){
	                              min = aux_cost;
	                          } 
	                      }
	    			 }
	    			 
	    		 }
	    		 
	    		 if (featureList.get(i1).getGroup().isEmpty()){
	                  min = aux_cost;
	             }
	              
	             c_min = c_min + min;
	             return c_min;
	    	 } 
	     }
	     return c_min; 
	}
	
	public static void deleteFeature(){    
		
		int i = 0;
		while (i != featureList.size()){
			
			if ((featureList.get(i).getCostMin() + c_root) > budget){
				
				LinkedList<Integer> listdelete = new LinkedList<Integer>();
				listdelete.add(featureList.get(i).getName());
				
				for(int i1=0; i1<listdelete.size(); i1++){
					for(int i2=0; i2<featureList.size(); i2++){ 
						
						/*if (!listdelete.isEmpty()){
							if (listdelete.get(0).equals("C106")){
								System.out.println("DELETE: " + listdelete.get(i1));
							}
						}*/
						
						if ((featureList.get(i2).getName()) == (listdelete.get(i1))){
							
							int a = featureList.get(i2).getAlt();
							
							//if alternative feature remove in group too
							if (a != 0){
								
								for(int i3=0; i3<featureList.get(i2).getGroup().size(); i3++){
									for(int i4=0; i4<featureList.size(); i4++){
										
										if ((featureList.get(i4).getName()) == (featureList.get(i2).getGroup().get(i3))){
											featureList.get(i4).getGroup().remove(featureList.get(i2).getName());
											break;          
										}     
									}          
								} 
							}
							
							// Delete optional feature in featureList
							featureList.remove(featureList.get(i2));
							i = i2;
							break;
						}
					}
					
					//delete listdelete and create a new
					if (i1 == (listdelete.size()-1)){
						LinkedList<Integer> children = new LinkedList<Integer>();
						children = filhos(listdelete);
						listdelete.clear();
						listdelete = children;
						//System.out.println("LISTA DELETE: \t" + listdelete);
						//if listdelete does not empty
						if (!listdelete.isEmpty()){   
						   i1 = -1;   
						}else{
						   break; 
						}
					}
				}
				
			}else{
				i++;
			}
		}
	} 

	public static LinkedList<Integer> filhos(LinkedList<Integer> listdelete){

		//children.clear();
		LinkedList<Integer> auxChildren = new LinkedList<Integer>();

		for(int it1=0; it1<listdelete.size(); it1++){  
			for(int it2=0; it2<featureList.size(); it2++){  
				if ((featureList.get(it2).getFather()) == (listdelete.get(it1))){
					auxChildren.add(featureList.get(it2).getName());
				}           
			}
		}
		
		return auxChildren;
	}
	
	public static void impGreedyHeuristic(){
	     
	    double valAlt = -1;
	    double valOthers = -1;
	    int nameAlt = 0;
	    int nameOthers = 0;
	    
	    for (int i2=0; i2<featureList.size(); i2++){
	    	if ((featureList.get(i2).getFather()) == (root.get(0))){
	    		if (featureList.get(i2).getAlt() != 0){
	    			if (((featureList.get(i2).getBenefit())/(featureList.get(i2).getCost())) > valAlt){
	    				valAlt = (featureList.get(i2).getBenefit())/(featureList.get(i2).getCost());
	                    nameAlt =  featureList.get(i2).getName();
	    			}
	    		}else{
	    			if (((featureList.get(i2).getBenefit())/(featureList.get(i2).getCost())) > valOthers){
	                    valOthers = (featureList.get(i2).getBenefit())/(featureList.get(i2).getCost()); 
	                    nameOthers =  featureList.get(i2).getName();
	                } 
	    		}
	    	}
	    }
	    
	    //selected features (change mandatory) and delete feature in group if there is (change cost)
	    int i3;
	    if (valAlt != -1){
	    	for (i3=0; i3<featureList.size(); i3++){
	    		if ((featureList.get(i3).getName()) ==(nameAlt)){
	    			featureList.get(i3).setMandatory(true); 
	                if (!featureList.get(i3).getGroup().isEmpty()){
	                	
	                	if (featureList.get(i3).getAlt() == 1){ //OR
	                		for(int i4=0; i4<featureList.get(i3).getGroup().size(); i4++){
	                			for (int i5=0; i5<featureList.size(); i5++){
	                                if ((featureList.get(i5).getName()) ==(featureList.get(i3).getGroup().get(i4))){
	                                    featureList.get(i5).setAlt(0);
	                                    featureList.get(i5).getGroup().clear();        
	                                }     
	                            }
	                        }  
	                	}else{ //XOR
	                		for(int i4=0; i4<featureList.get(i3).getGroup().size(); i4++){
	                			for (int i5=0; i5<featureList.size(); i5++){
	                				if ((featureList.get(i5).getName()) ==(featureList.get(i3).getGroup().get(i4))){
	                					featureList.get(i5).setCost(budget + 1);
	                					featureList.get(i5).setAlt(0);
	                                    featureList.get(i5).getGroup().clear();
	                                }
	                            }
	                        } 
	                	}
	                	 
	                	featureList.get(i3).setAlt(0);
	                	featureList.get(i3).getGroup().clear();  
	                }
	                break;
	    		}
	    	}
	    }else{
	    	for(i3=0; i3<featureList.size(); i3++){ 
	            if ((featureList.get(i3).getName()) ==(nameOthers)){
	            	featureList.get(i3).setMandatory(true); 
	                break; 
	            }     
	        }  
	    }
	}
	
	public static void FindDependents(){
	    
		for (int iR=0; iR<root.size(); iR++){
	    	for (int i1=0; i1<featureList.size(); i1++){
	    		if (((featureList.get(i1).getName()) ==(root.get(iR)))){
	    			for (int i2=0; i2<featureList.size(); i2++){
	    				if ((featureList.get(i2).getFather()) ==(featureList.get(i1).getName()) && (featureList.get(i2).isMandatory())){
	    					root.add(featureList.get(i2).getName());
	    				}
	    			}
	             }
	         }
	     }
	}
	
	public static void outputFile(long start_time){
    
		/*FileWriter arquivo;  
        try {  
            arquivo = new FileWriter(new File("C:\\programs\\runtime-EclipseApplication\\" + clientext.getText() + "\\SPLConfig\\configs\\result.txt"));  
            arquivo.write("Time: " + (System.currentTimeMillis() - start_time) + "\n"); 
            arquivo.write("Cost: " + c_root + "\n"); 
            arquivo.write("Benefit: " + b_root + "\n"); 
            arquivo.close(); 
        } catch (IOException e) {  
            e.printStackTrace();  
        } catch (Exception e) {  
            e.printStackTrace();  
        } */
        
//        System.out.println("Selected features: "); 
//        System.out.println("\t" + root);
   	}
	
	public String getStringResult(){
		String stringResult;
		stringResult = "Time: " + (System.currentTimeMillis() - start_time) + "\n";
		stringResult = stringResult + "Cost: " + c_root + "\n";
		stringResult = stringResult + "Benefit: " + b_root + "\n";
		return stringResult;
	}
	
	public static void printFeatureList(){
		
		System.out.print("Features in list: \n");
		for (int i=0; i<featureList.size(); i++){
			System.out.println("Name: " + featureList.get(i).getName());
			System.out.println("Type: " + featureList.get(i).isMandatory());
			System.out.println("Cost: " + featureList.get(i).getCost());
			System.out.println("Benefit: " + featureList.get(i).getBenefit());
			System.out.println("Father: " + featureList.get(i).getFather());
			System.out.println("Alternative: " + featureList.get(i).getAlt());
			
			if (featureList.get(i).getAlt() != 0){
				System.out.println("Group: ");
				System.out.println("\t" + featureList.get(i).getGroup());
			}
			
			System.out.println("Cost Minimum: " + featureList.get(i).getCostMin());
		}
	}

	public static void printRoot(){
		System.out.println("Features in root: \n");
		System.out.println("\t" + root);
		System.out.println("Cost: " + c_root);
		System.out.println("Benefit: " + b_root);
		System.out.println("Cost Minimum: " + c_min_root);
	}
	
	public LinkedList<Integer> getResult(){
		return root;
	}
	
	public double getBenefit(){
		double aux = c_root;
		/*double aux = b_root / root.size();
		aux *= 20;*/
		return aux;
	}
}

