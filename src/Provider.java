import java.util.ArrayList;

public class Provider {
	int ID=-1;
	ArrayList<AttributePolicy> attrPolicies=new ArrayList<AttributePolicy>();
	Capability capability=null;
	
	public Provider(int ID, Capability capability)
	{
		this.ID=ID;
		this.capability=capability;
		setAttrPolicies();
	}
	
	// to be invoked for atomic providers
	public void setAttrPolicies()
	{
		this.attrPolicies.clear();
		for (int i=0; i<Parameters.numOfAttrs; i++)
		{
			/*if (this.capability.coveredAttributes.get(i)==1)
			{*/
				
				double currRandomVal= Parameters.attrPolicyGenerator.nextDouble();
				double startMeanVal=(Parameters.minAttrValue + Parameters.attrPolicyStandardDeviation);
				double endMeanVal=(Parameters.maxAttrValue - Parameters.attrPolicyStandardDeviation);		
				double currMean= startMeanVal + (currRandomVal* (endMeanVal-startMeanVal));
				AttributePolicy attrPolicy=new AttributePolicy(currMean, Parameters.attrPolicyStandardDeviation);
				this.attrPolicies.add(attrPolicy);				
			/*}
			else
			{
				this.attrPolicies.add(null);
			}*/
		}
	}
	
	public double getAttrValue(int attrInd)
	{
		return this.attrPolicies.get(attrInd).getValue();
	}

}
