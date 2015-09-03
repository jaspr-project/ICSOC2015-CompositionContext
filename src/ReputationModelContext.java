import java.util.ArrayList;


public class ReputationModelContext {
	ArrayList<ArrayList<Double>> scoresOverTime=new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> accuracyOverTime=new ArrayList<ArrayList<Double>>();
	// 0: no delegation is visible; 1: only the first delegation level is visible; and so on
	int visibilityLevel=0;
	
	FileResult modelFile = new FileResult("trackContextResults.txt");
	
	public ReputationModelContext(int visibilityLevel)
	{
	  ArrayList<Double> initialScores=new ArrayList<Double>();
	  for (int i=0; i<Parameters.numOfAttrs+1; i++)
		  initialScores.add(0.5);
	  this.scoresOverTime.add(initialScores);
	  this.visibilityLevel=visibilityLevel;
	}
	
	public void calculateReputationScores(ArrayList<Interaction> interactions)
	{
		this.accuracyOverTime.add(calculateAccuracyForInteraction(interactions,0));
		for (int i=1; i<interactions.size(); i++)
		{
			this.scoresOverTime.add(calculateReputationScoreForInteraction(interactions,i));
			this.accuracyOverTime.add(calculateAccuracyForInteraction(interactions,i));
		}
		modelFile.close();
	}
	
	private ArrayList<Double> getInteractionWeight(Interaction interaction, ArrayList<Provider> futureAvailableInstantiation)
	{
		return (interaction.delegContext.estimateRelevance(futureAvailableInstantiation));
	}
	
	private ArrayList<Provider> getFutureAvailableInstantiation(Interaction futureInteraction)
	{
		ArrayList<Provider> futureAvailableInstantiation=new ArrayList<Provider>();
		
		for (int i=0; i<futureInteraction.delegContext.providerCombination.size(); i++)
		{
			Provider currProvider=futureInteraction.delegContext.providerCombination.get(i);
			if (currProvider.capability.level <= this.visibilityLevel)
				futureAvailableInstantiation.add(currProvider);
			else
				futureAvailableInstantiation.add(null);
		}
		
		return futureAvailableInstantiation;
	}
	
	private ArrayList<Double> calculateReputationScoreForInteraction(ArrayList<Interaction> interactions, int interactionInd)
	{
		ArrayList<Double> attrReputationScore=new ArrayList<Double>();
		for (int k=0; k<Parameters.numOfAttrs; k++)
			attrReputationScore.add(0.0);
		
		ArrayList<Double> attrScalingFactor=new ArrayList<Double>();
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			attrScalingFactor.add(0.0);
		}
		
		ArrayList<Provider> futureAvailableInstantiation=getFutureAvailableInstantiation(interactions.get(interactionInd));
		
		modelFile.writeln("Reputation Score at Interaction:"+interactionInd);
		for (int i=0; i<interactionInd; i++)
		{
			modelFile.writeln("Interaction "+interactionInd+" Ratings of Interaction "+i);
			ArrayList<Double> currAttrRating=interactions.get(i).attrRating;
			printArray(currAttrRating);
			modelFile.writeln("Interaction "+interactionInd+" Weight for Interaction "+i);
			ArrayList<Double> currWeightPerAttr=this.getInteractionWeight(interactions.get(i), futureAvailableInstantiation);
			printArray(currWeightPerAttr);
			for (int k=0; k<Parameters.numOfAttrs; k++)
			{
				attrReputationScore.set(k, attrReputationScore.get(k) + (currWeightPerAttr.get(k) * currAttrRating.get(k)));
				attrScalingFactor.set(k, attrScalingFactor.get(k)+currWeightPerAttr.get(k));
			}
		}
		System.out.println("..............");		
		
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			modelFile.writeln("Sum Score for attribute "+k+" = "+attrReputationScore.get(k));
			modelFile.writeln("scaling factor for attribute "+k+" = "+attrScalingFactor.get(k));
			modelFile.writeln("Reputation Score for "+k+" = "+attrReputationScore.get(k)/attrScalingFactor.get(k));
			attrReputationScore.set(k,attrReputationScore.get(k)/attrScalingFactor.get(k));
		}
		modelFile.writeln("Actual Ratings for Interaction "+interactionInd);
		printArray(interactions.get(interactionInd).attrRating);
		modelFile.writeln("End Interaction "+interactionInd);
		modelFile.writeln("..............");
		double overallReputationScore=0.0;
		for (int k=0; k<Parameters.numOfAttrs; k++)
			overallReputationScore+=(Parameters.attrWeight * attrReputationScore.get(k));
		attrReputationScore.add(overallReputationScore);
		return attrReputationScore;
	}
	
	private ArrayList<Double> calculateAccuracyForInteraction(ArrayList<Interaction> interactions, int interactionInd)
	{
		ArrayList<Double> attrAccuracyScore=new ArrayList<Double>();
		ArrayList<Double> currAttrRating=interactions.get(interactionInd).attrRating;
		ArrayList<Double> currReputationScore=this.scoresOverTime.get(interactionInd);
		
		for (int k=0; k<=Parameters.numOfAttrs; k++)
		{
			attrAccuracyScore.add(Math.abs(currAttrRating.get(k)-currReputationScore.get(k)));
		}
		
		return attrAccuracyScore;
	}
	
	public ArrayList<Double> getAccuracyResults(int attrInd)
	{
		ArrayList<Double> accuracyOverTimeForAttr=new ArrayList<Double>();
		
		for (int i=0; i < this.accuracyOverTime.size(); i++)
		{
			accuracyOverTimeForAttr.add(this.accuracyOverTime.get(i).get(attrInd));
		}
		return accuracyOverTimeForAttr;
	}
	
	private void printArray(ArrayList<Double> arr)
	{
		for (int i=0; i<arr.size(); i++)
		{
			modelFile.write(" "+arr.get(i)+"");		
		}
		modelFile.writeln("");
	}
	
}
