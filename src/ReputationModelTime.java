import java.util.ArrayList;

public class ReputationModelTime {
    
	ArrayList<ArrayList<Double>> scoresOverTime=new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> accuracyOverTime=new ArrayList<ArrayList<Double>>();
	double lambda=(-5/Math.log(0.5));
	FileResult modelFile = new FileResult("trackTimeResults.txt");
	
	public ReputationModelTime()
	{
	  ArrayList<Double> initialScores=new ArrayList<Double>();
	  for (int i=0; i<Parameters.numOfAttrs+1; i++)
		  initialScores.add(0.5);
	  this.scoresOverTime.add(initialScores);
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
	
	private double getInteractionWeight(Interaction interaction, int currTimeStep)
	{
		int timeStepDifference=(currTimeStep-interaction.timeStep)+1;
		return (Math.exp(-timeStepDifference/this.lambda));		
	}
	
	private ArrayList<Double> calculateReputationScoreForInteraction(ArrayList<Interaction> interactions, int interactionInd)
	{
		ArrayList<Double> attrReputationScore=new ArrayList<Double>();
		for (int k=0; k<Parameters.numOfAttrs; k++)
			attrReputationScore.add(0.0);
		
		double scalingFactor=0.0;
		modelFile.writeln("Reputation Score at Interaction:"+interactionInd);
		for (int i=0; i<interactionInd; i++)
		{
			modelFile.writeln("Interaction "+interactionInd+" Ratings of Interaction "+i);
			ArrayList<Double> currAttrRating=interactions.get(i).attrRating;
			printArray(currAttrRating);
			modelFile.writeln("Interaction "+interactionInd+" Weight for Interaction "+i);			
			double currWeight=this.getInteractionWeight(interactions.get(i), interactionInd+1);
			modelFile.writeln(""+currWeight+"");
			for (int k=0; k<Parameters.numOfAttrs; k++)
			{
				attrReputationScore.set(k, attrReputationScore.get(k)+ (currWeight * currAttrRating.get(k)));
			}
		    scalingFactor+=currWeight;
		}
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			modelFile.writeln("Sum Score for attribute "+k+" = "+attrReputationScore.get(k));
			modelFile.writeln("scaling factor for attribute "+k+" = "+scalingFactor);
			modelFile.writeln("Reputation Score for "+k+" = "+attrReputationScore.get(k)/scalingFactor);
	
			attrReputationScore.set(k,attrReputationScore.get(k)/scalingFactor);
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
