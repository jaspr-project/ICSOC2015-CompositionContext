import java.util.ArrayList;


public class ReputationModelEqualWeight {

	ArrayList<ArrayList<Double>> scoresOverTime=new ArrayList<ArrayList<Double>>();
	ArrayList<ArrayList<Double>> accuracyOverTime=new ArrayList<ArrayList<Double>>();
	
	public ReputationModelEqualWeight()
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
	
	private ArrayList<Double> calculateReputationScoreForInteraction(ArrayList<Interaction> interactions, int interactionInd)
	{
		ArrayList<Double> attrReputationScore=new ArrayList<Double>();
		for (int k=0; k<Parameters.numOfAttrs; k++)
			attrReputationScore.add(0.0);
		
		for (int i=0; i<interactionInd; i++)
		{
			ArrayList<Double> currAttrRating=interactions.get(i).attrRating;
			for (int k=0; k<Parameters.numOfAttrs; k++)
			{
				attrReputationScore.set(k, attrReputationScore.get(k)+currAttrRating.get(k));
			}
		}
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			attrReputationScore.set(k,attrReputationScore.get(k)/(double)interactionInd);
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
