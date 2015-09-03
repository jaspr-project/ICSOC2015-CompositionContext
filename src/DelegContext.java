import java.util.ArrayList;


public class DelegContext {
	
	// The provider at index 0 instantiates capability c with c.indInCapabilityList=0, and so on
	// When the provider is undefined, a null value is used
	ArrayList<Provider> providerCombination=new ArrayList<Provider>();
	
	public DelegContext(ArrayList<Provider> providerCombination)
	{
		this.providerCombination=providerCombination;
	}
	
	ArrayList<Double> getattributeValues()
	{
		ArrayList<Double> attrValues=new ArrayList<Double>();
		
		attrValues.add(this.getExecutionTimeValue());
		attrValues.add(this.getPackageQuality());
		attrValues.add(this.getPortionSize());
		
		return attrValues;		
	}
	
	private double getExecutionTimeValue()
	{
	  double overallExecutionTime=0.0;
	  for (int i=0; i<this.providerCombination.size(); i++)
	  {
		  Provider currProvider=this.providerCombination.get(i);
		  Capability currCapability=currProvider.capability;
		  if ((currCapability.childCapabilities.size()==0) && (currCapability.coveredAttributes.get(0)==1) && (currCapability.onCriticalPath.get(0)==1))
		  {
			  overallExecutionTime= overallExecutionTime + currProvider.getAttrValue(0);
		  }
	  }
	  return overallExecutionTime; 		  
	}
	
	private double getPackageQuality()
	{
	  double overallPackageQuality=Parameters.maxAttrValue;
	  for (int i=0; i<this.providerCombination.size(); i++)
	  {
		  Provider currProvider=this.providerCombination.get(i);
		  Capability currCapability=currProvider.capability;
		  if ((currCapability.childCapabilities.size()==0) && (currCapability.coveredAttributes.get(1)==1))
		  {
			  double currProviderPackageQuality=currProvider.getAttrValue(1);
			  if (currProviderPackageQuality < overallPackageQuality)
				  overallPackageQuality=currProviderPackageQuality;
		  }
	  }
	  return overallPackageQuality;
	}
	
	private double getPortionSize()
	{
	  double overallPortionSize=Parameters.maxAttrValue;
	  for (int i=0; i<this.providerCombination.size(); i++)
	  {
		  Provider currProvider=this.providerCombination.get(i);
		  Capability currCapability=currProvider.capability;
		  if ((currCapability.childCapabilities.size()==0) && (currCapability.coveredAttributes.get(2)==1))
		  {
			  double currProviderPortionSize=currProvider.getAttrValue(2);
			  if (currProviderPortionSize < overallPortionSize)
				  overallPortionSize=currProviderPortionSize;
		  }
	  }
	  return overallPortionSize;
	}
	
	public ArrayList<Double> estimateRelevance(ArrayList<Provider> futureAvailableInstantiation)
	{
		ArrayList<Double> relevance=new ArrayList<Double>();
		for (int i=0; i< Parameters.numOfAttrs; i++)
			relevance.add(0.0);
		
		for (int i=0; i<futureAvailableInstantiation.size(); i++)
		{
			double currProviderRelevance=0.0001;
			Provider futureProvider=futureAvailableInstantiation.get(i);
			Provider ctxProvider=this.providerCombination.get(i);
			if (futureProvider!=null)
			{
				Capability currCapability=futureProvider.capability;
				if (ctxProvider==null)
					currProviderRelevance=0.5;
				else
					if (ctxProvider.ID==futureProvider.ID)
					{
						currProviderRelevance=1;
					}
				for (int k=0; k< Parameters.numOfAttrs; k++)
					relevance.set(k, relevance.get(k) + (currCapability.role.get(k)*currProviderRelevance));
			}		    			
		}
		//printArray(relevance);
		return relevance;
	}
			
}
