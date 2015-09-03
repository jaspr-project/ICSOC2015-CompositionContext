import java.util.ArrayList;


public class ReputationModelContextTime {

	ArrayList<ArrayList<Double>> scoresOverTime=new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> accuracyOverTime=new ArrayList<ArrayList<Double>>();
	// 0: no delegation is visible; 1: only the first delegation level is visible; and so on
	int visibilityLevel=0;
	double lambda=(-5/Math.log(0.5));
	
	public ReputationModelContextTime(int visibilityLevel)
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
	}
	
	private ArrayList<Double> getInteractionWeight(Interaction interaction, ArrayList<Provider> futureAvailableInstantiation, int currTimeStep)
	{
		int timeStepDifference=(currTimeStep-interaction.timeStep)+1;
		double timeWeight= Math.exp(-timeStepDifference/this.lambda);
		ArrayList<Double> combinedWeight=interaction.delegContext.estimateRelevance(futureAvailableInstantiation);
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			combinedWeight.set(k, combinedWeight.get(k) * timeWeight);
		}		
		return combinedWeight;
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
		for (int i=0; i<interactionInd; i++)
		{
			ArrayList<Double> currAttrRating=interactions.get(i).attrRating;
			ArrayList<Double> currWeightPerAttr=this.getInteractionWeight(interactions.get(i), futureAvailableInstantiation, interactionInd+1);
			for (int k=0; k<Parameters.numOfAttrs; k++)
			{
				attrReputationScore.set(k, attrReputationScore.get(k) + (currWeightPerAttr.get(k) * currAttrRating.get(k)));
				attrScalingFactor.set(k, attrScalingFactor.get(k)+currWeightPerAttr.get(k));
			}
		}
		
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			attrReputationScore.set(k,attrReputationScore.get(k)/attrScalingFactor.get(k));
		}
		
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
}
