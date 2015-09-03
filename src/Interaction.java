import java.util.ArrayList;


public class Interaction {
	
	int timeStep=0;
	DelegContext delegContext=null;
	ArrayList<Double> attrRating=new ArrayList<Double>();
	double overallRating=0.0;
	
	public Interaction(int timeStep, DelegContext delegContext)
	{
		this.timeStep=timeStep;
		this.delegContext=delegContext;
		this.attrRating=delegContext.getattributeValues();
		this.overallRating=getOverallRating();
		this.attrRating.add(this.overallRating);
	}
	
	private double getOverallRating()
	{
		double overallRating=0.0;
		for (int i=0; i<Parameters.numOfAttrs; i++)
		{
			overallRating=overallRating + (Parameters.attrWeight *  this.attrRating.get(i));
		}
		return overallRating;
	}

}
