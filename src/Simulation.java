import java.util.ArrayList;

import org.jfree.ui.RefineryUtilities;


public class Simulation {
	
	
	private ArrayList<Double> instantiateList(int listSize, ArrayList<Double> list)
	{
		for (int i=0; i< listSize; i++)
		{
			list.add(0.0);
		}
		return list;
	}
	
	private void addToList(ArrayList<Double> list, ArrayList<Double> valuesToAdd)
	{
		for (int i=0; i< list.size(); i++)
		{
			list.set(i,list.get(i) + valuesToAdd.get(i));
		}
	}
		
	private void calculateAverage(ArrayList<Double> list, int num)
	{
		for (int i=0; i< list.size(); i++)
		{
			list.set(i,list.get(i)/(double)num);
		}
	}
	
	public void compareAll(int numOfRuns, int visibilityLevel,int numOfProvidersPerCapability, int numOfInteractions, int changeInd, int attrInd, int policyChangeInd)
	{   
		
		ArrayList<Double> ewResults=instantiateList(numOfInteractions, new ArrayList<Double>());
		ArrayList<Double> tResults=instantiateList(numOfInteractions, new ArrayList<Double>());
		ArrayList<Double> cResults=instantiateList(numOfInteractions, new ArrayList<Double>());
		ArrayList<Double> ctResults=instantiateList(numOfInteractions, new ArrayList<Double>());
				
		
		for (int r=0; r<numOfRuns; r++)
		{
		
		CapabilityHierarchy capabilityHierarchy=new CapabilityHierarchy(numOfProvidersPerCapability);
		capabilityHierarchy.setRoles(visibilityLevel);		
		ArrayList<ArrayList<Provider>> adoptedCombinations=new ArrayList<ArrayList<Provider>>();
		adoptedCombinations.add(capabilityHierarchy.getProviderCombination(0));
		adoptedCombinations.add(capabilityHierarchy.getProviderCombination(1));
		adoptedCombinations.add(capabilityHierarchy.getProviderCombination(0));
		
		int adoptedCombinationInd=0;
		
		ArrayList<Interaction> interactions=new ArrayList<Interaction>();
		int currChange=1;
		int currPolicy=1;
		
		for (int i=0; i<numOfInteractions; i++)
		{
			if (i==(changeInd*currChange))
			{	
				adoptedCombinationInd=currChange;
				currChange+=1;
			}
			if (i==(currPolicy*policyChangeInd))
			{
				capabilityHierarchy.resetProviderPolicies();
				currPolicy+=1;
			}
			
			Interaction currInteraction=new Interaction(i+1,new DelegContext(adoptedCombinations.get(adoptedCombinationInd)));
			interactions.add(currInteraction);			
		}
		
		ReputationModelEqualWeight rmeq=new ReputationModelEqualWeight();
		rmeq.calculateReputationScores(interactions);
		addToList(ewResults,rmeq.getAccuracyResults(attrInd));
		
		
		ReputationModelTime rmt=new ReputationModelTime();
		rmt.calculateReputationScores(interactions);
		addToList(tResults,rmt.getAccuracyResults(attrInd));
		
		ReputationModelContext rmc=new ReputationModelContext(visibilityLevel);
		rmc.calculateReputationScores(interactions);
		addToList(cResults,rmc.getAccuracyResults(attrInd));
		
		ReputationModelContextTime rmct=new ReputationModelContextTime(visibilityLevel);
		rmct.calculateReputationScores(interactions);
		addToList(ctResults,rmct.getAccuracyResults(attrInd));
		
		}
		
		calculateAverage(ewResults,numOfRuns);
		calculateAverage(tResults,numOfRuns);
		calculateAverage(cResults,numOfRuns);
		calculateAverage(ctResults,numOfRuns);
		
	    writeToFile(ewResults, "Equal Weights");
		writeToFile(tResults, "Time Only");
		writeToFile(cResults, "Context Only");
		writeToFile(ctResults, "Context and Time");
		
		
		showResults("One Change Graph",ewResults,"Equal Weights",tResults,"Time Only",cResults, "Context Only", ctResults, "Context and Time");
		
	}
	
	public void compareVisibilityLevels(int numOfRuns, int numOfProvidersPerCapability, int numOfInteractions, int changeInd, int attrInd)
	{   
		
		ArrayList<Double> nvResults=instantiateList(numOfInteractions, new ArrayList<Double>());
		ArrayList<Double> pvResults=instantiateList(numOfInteractions, new ArrayList<Double>());
		ArrayList<Double> fvResults=instantiateList(numOfInteractions, new ArrayList<Double>());
					
		for (int r=0; r<numOfRuns; r++)
		{
		
		CapabilityHierarchy capabilityHierarchy=new CapabilityHierarchy(numOfProvidersPerCapability);
			
		ArrayList<ArrayList<Provider>> adoptedCombinations=new ArrayList<ArrayList<Provider>>();
		adoptedCombinations.add(capabilityHierarchy.getProviderCombination(0,0));
		adoptedCombinations.add(capabilityHierarchy.getProviderCombination(1,0));
		adoptedCombinations.add(capabilityHierarchy.getProviderCombination(2,0));
		
		int adoptedCombinationInd=0;
		
		ArrayList<Interaction> interactions=new ArrayList<Interaction>();
		int currChange=1;
		
		for (int i=0; i<numOfInteractions; i++)
		{
			if (i==(changeInd*currChange))
			{	
				adoptedCombinationInd=currChange;
				currChange+=1;
			}	
				
			Interaction currInteraction=new Interaction(i+1,new DelegContext(adoptedCombinations.get(adoptedCombinationInd)));
			interactions.add(currInteraction);			
		}
		
		capabilityHierarchy.setRoles(0);	
		
		ReputationModelContext nvrm=new ReputationModelContext(0);
		nvrm.calculateReputationScores(interactions);
		addToList(nvResults,nvrm.getAccuracyResults(attrInd));
		
        capabilityHierarchy.setRoles(1);	
		
		ReputationModelContext pvrm=new ReputationModelContext(1);
		pvrm.calculateReputationScores(interactions);
		addToList(pvResults,pvrm.getAccuracyResults(attrInd));
		
		capabilityHierarchy.setRoles(2);	
			
	    ReputationModelContext fvrm=new ReputationModelContext(2);
	    fvrm.calculateReputationScores(interactions);
	    addToList(fvResults,fvrm.getAccuracyResults(attrInd));
		
		}
		
		calculateAverage(nvResults,numOfRuns);
		calculateAverage(pvResults,numOfRuns);
		calculateAverage(fvResults,numOfRuns);
		
	    writeToFile(nvResults, "No Visibility");
		writeToFile(pvResults, "Partial Visibility");
		writeToFile(fvResults, "Full Visibility");
		
		
		showResults("One Change Graph",nvResults,"No Visibility",pvResults,"Partial Visibility",fvResults, "Full Visibility");
		
	}
	
	private void writeToFile(ArrayList<Double> modelAccuracy, String modelName)
	{
		FileResult modelFile = new FileResult(modelName+".txt");
	    
		for (int i=0; i<modelAccuracy.size(); i++)
		{
			modelFile.writeln(i+": "+modelAccuracy.get(i).toString());
		}
	    
		modelFile.close();
	}
	
	private void showResults(String title, ArrayList<Double> firstLine, String firstLineTitle, ArrayList<Double> secondLine, String secondLineTitle, ArrayList<Double> thirdLine, String thirdLineTitle, ArrayList<Double> fourthLine, String fourthLineTitle)
	{
		GraphResult graphResult = new GraphResult(title, firstLine, firstLineTitle, secondLine, secondLineTitle, thirdLine, thirdLineTitle, fourthLine, fourthLineTitle);
	    graphResult.pack();
        RefineryUtilities.centerFrameOnScreen(graphResult);
        graphResult.setVisible(true);
	}
	
	private void showResults(String title, ArrayList<Double> firstLine, String firstLineTitle, ArrayList<Double> secondLine, String secondLineTitle, ArrayList<Double> thirdLine, String thirdLineTitle)
	{
		GraphResult graphResult = new GraphResult(title, firstLine, firstLineTitle, secondLine, secondLineTitle, thirdLine, thirdLineTitle);
	    graphResult.pack();
        RefineryUtilities.centerFrameOnScreen(graphResult);
        graphResult.setVisible(true);
	}
    
}
