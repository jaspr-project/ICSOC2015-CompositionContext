import java.util.ArrayList;


public class Capability {
	int ID=-1;
	int level=0;
	int indInCapabilityList=-1;
	String name= "";
	ArrayList<Provider> candidateProviders=new ArrayList<Provider>();
	ArrayList<Capability> neighbourCapabilities = new ArrayList<Capability>();
	ArrayList<Capability> childCapabilities=new ArrayList<Capability>();
	ArrayList <Integer> coveredAttributes=new ArrayList<Integer>(); 
	Capability parentCapability=null;
	Double hWeight=0.0;
	ArrayList <Double> sWeight=new ArrayList<Double>();
	//ArrayList <Double> aWeight=new ArrayList<Double>();
	ArrayList <Double> role=new ArrayList<Double>();
	ArrayList<Integer> onCriticalPath=new ArrayList<Integer>();
	
	public Capability(int ID, int level, int indInCapabilityList, String name, int numOfProviders)
	{
		this.ID=ID;
		this.level=level;
		this.indInCapabilityList=indInCapabilityList;
		this.name=name;		
		generateProviders(numOfProviders);
	}
	
	private void generateProviders(int numOfProviders)
	{
		for (int i=0; i<numOfProviders; i++)
		{
			this.candidateProviders.add(new Provider(Parameters.nextProviderID,this));
			Parameters.nextProviderID+=1;
		}
	}
	
	public void resetProviderPolicies()
	{
		for (int i=0; i<this.candidateProviders.size(); i++)
		{
			Provider currProvider=this.candidateProviders.get(i);
			currProvider.setAttrPolicies();
		}
	}
	
	public void setCoveredAttributes(int attr1, int attr2, int attr3)
	{
		this.coveredAttributes.add(attr1);
		this.coveredAttributes.add(attr2);
		this.coveredAttributes.add(attr3);		
	}
	
	public void setOnCriticalPath(int attr1, int attr2, int attr3)
	{
		this.onCriticalPath.add(attr1);
		this.onCriticalPath.add(attr2);
		this.onCriticalPath.add(attr3);
	}
	
	public void setRole(ArrayList<Double> normalisationFactor)
	{
		this.role.clear();
		for (int i=0; i<Parameters.numOfAttrs; i++)
		{
			double currAttrRole=(this.hWeight * this.sWeight.get(i) * this.coveredAttributes.get(i))/normalisationFactor.get(i);
			this.role.add(currAttrRole);
		}
	}
	
	//Assign a weight of 1 to leaf capabilities of the instantiated hierarchy
	public void setHWeight(ArrayList<Provider> futureAvailableInstantiation) 
	{
		if (futureAvailableInstantiation.get(this.indInCapabilityList)!=null)
		{
			if ((this.childCapabilities.size()==0)||(futureAvailableInstantiation.get(this.childCapabilities.get(0).indInCapabilityList)==null))
			{
				this.hWeight=1.0;
			}
			else
				this.hWeight=0.0;
		}
		else
		{
			this.hWeight=0.0;
		}
	}
	
	public void setHWeight(int visibilityLevel)
	{
		if (this.level > visibilityLevel)
			this.hWeight=0.0;
		else
		{
			if ((this.level== visibilityLevel) || (this.childCapabilities.size()==0))
			     this.hWeight=1.0;
			else
				this.hWeight=0.0;
		}
	}
	
	public void setSWeight()
	{
		for (int i=0; i<Parameters.numOfAttrs; i++)
			this.sWeight.add((double)this.onCriticalPath.get(i));
		
		Capability currCapability=this;
		while (currCapability.parentCapability!=null)
		{
			currCapability=currCapability.parentCapability;
			for (int i=0; i<Parameters.numOfAttrs; i++)
			{
				this.sWeight.set(i, this.sWeight.get(i) * currCapability.sWeight.get(i));
			}			
		}						
	}
		
}
