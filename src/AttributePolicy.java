import org.apache.commons.math3.distribution.NormalDistribution;


public class AttributePolicy {
	
	double policyMean=-1;
	double policyStandardDeviation=-1;
	NormalDistribution nd=null;
	
	public AttributePolicy(double policyMean, double policyStandardDeviation)
	{
	 this.policyMean=policyMean;
	 this.policyStandardDeviation=policyStandardDeviation;
	 this.nd=new NormalDistribution(this.policyMean,this.policyStandardDeviation); 
	}
	
	public double getValue()
	{
		double randomVal= nd.sample();
		if (randomVal> Parameters.maxAttrValue)
			return Parameters.maxAttrValue;
		if (randomVal < Parameters.minAttrValue)
			return Parameters.minAttrValue;
		return randomVal;
	}
	
	public void changeAttributePolicy(double policyMean)
	{
		this.policyMean=policyMean;
		this.nd= new NormalDistribution(this.policyMean,this.policyStandardDeviation); 
	}

}
