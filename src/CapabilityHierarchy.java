import java.util.ArrayList;

public class CapabilityHierarchy {
	
	ArrayList<Capability> capabilityList=new ArrayList<Capability>();
	int  numOfProvidersPerCapability=0;
	
	public CapabilityHierarchy(int numOfProvidersPerCapability)
	{
		this.numOfProvidersPerCapability=numOfProvidersPerCapability;
		Capability c0 = new Capability(1,0,0,"FHD",numOfProvidersPerCapability);
		this.capabilityList.add(c0);
		Capability c1 = new Capability(2,1,1,"FPP",numOfProvidersPerCapability);
		this.capabilityList.add(c1);
		Capability c2 = new Capability(3,1,2,"FPD",numOfProvidersPerCapability);
		this.capabilityList.add(c2);
		Capability c3 = new Capability(4,2,3,"FPR",numOfProvidersPerCapability);
		this.capabilityList.add(c3);
		Capability c4 = new Capability(5,2,4,"PPR",numOfProvidersPerCapability);
		this.capabilityList.add(c4);
		Capability c5 = new Capability(6,2,5,"FPK",numOfProvidersPerCapability);
		this.capabilityList.add(c5);
		
		c0.childCapabilities.add(c1);
		c0.childCapabilities.add(c2);		
		c1.childCapabilities.add(c3);
		c1.childCapabilities.add(c4);
		c1.childCapabilities.add(c5);
		
		//not utilised in the current implementation
		c1.neighbourCapabilities.add(c2);
		c3.neighbourCapabilities.add(c5);
		c4.neighbourCapabilities.add(c5);
		
		c1.parentCapability=c0;
		c2.parentCapability=c0;
		c3.parentCapability=c1;
		c4.parentCapability=c1;
		c5.parentCapability=c1;
		
		c0.setCoveredAttributes(1, 1, 1);
		c1.setCoveredAttributes(1, 1, 1);
		c2.setCoveredAttributes(1, 1, 0);
		c3.setCoveredAttributes(1, 0, 1);
		c4.setCoveredAttributes(1, 1, 0);
		c5.setCoveredAttributes(1, 1, 0);
		
		c0.setOnCriticalPath(1, 1, 1);
		c1.setOnCriticalPath(1, 1, 1);
		c2.setOnCriticalPath(1, 1, 1);
		c3.setOnCriticalPath(1, 1, 1);
		c4.setOnCriticalPath(0, 1, 1);
		c5.setOnCriticalPath(1, 1, 1);
		
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			this.capabilityList.get(i).setSWeight();
		}				
	}
	
	public void resetProviderPolicies()
	{
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			Capability currCapability=this.capabilityList.get(i);
			currCapability.resetProviderPolicies();
		}
	}
	
	public void setRoles(int visibilityLevel)
	{
		ArrayList<Double> roleScalingFactor=new ArrayList<Double>();
		for (int k=0; k<Parameters.numOfAttrs; k++)
		{
			roleScalingFactor.add(0.0);
		}
		
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			Capability currCapability=this.capabilityList.get(i);
			currCapability.setHWeight(visibilityLevel);
			for (int k=0; k<Parameters.numOfAttrs; k++)
			{
				roleScalingFactor.set(k, roleScalingFactor.get(k)+ (currCapability.hWeight * currCapability.sWeight.get(k) * currCapability.coveredAttributes.get(k)));
			}			
		}
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			this.capabilityList.get(i).setRole(roleScalingFactor);
		}				
	}
	
	public ArrayList<Provider> getProviderCombination(int indicator)
	{
		ArrayList<Provider> providerCombination=new ArrayList<Provider>();
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			Capability currCapability=this.capabilityList.get(i);			
			int providerInd=indicator;
			providerCombination.add(currCapability.candidateProviders.get(providerInd));
		}
		return providerCombination;		
	}
	
	public ArrayList<Provider> getProviderCombination(int newIndicator, int oldIndicator)
	{
		ArrayList<Provider> providerCombination=new ArrayList<Provider>();
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			Capability currCapability=this.capabilityList.get(i);			
			int providerInd=-1;
			if (currCapability.childCapabilities.size()==0)
				providerInd=newIndicator;
			else
				providerInd=oldIndicator;
			providerCombination.add(currCapability.candidateProviders.get(providerInd));
		}
		return providerCombination;		
	}
	
	public ArrayList<Provider> getProviderCombination()
	{
		ArrayList<Provider> providerCombination=new ArrayList<Provider>();
		for (int i=0; i<this.capabilityList.size(); i++)
		{
			Capability currCapability=this.capabilityList.get(i);
			int providerInd=Parameters.capabilityProviderSelector.nextInt(this.numOfProvidersPerCapability);
			providerCombination.add(currCapability.candidateProviders.get(providerInd));
		}
		return providerCombination;		
	}

}
