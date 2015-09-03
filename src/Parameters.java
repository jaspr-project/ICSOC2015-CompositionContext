import java.util.Random;

public class Parameters {
	   // The acceptable value for the attribute is considered to be 0
	   public static double minAttrValue = -1;
	   public static double maxAttrValue = 1;
	   
	   // the first is execution time, the second is packaging quality, and the third is portion size  
	   public static int numOfAttrs = 3;
	   public static Random attrPolicyGenerator= new Random();
	   public static Random capabilityProviderSelector= new Random();
	   public static double attrPolicyStandardDeviation=0.15;
	   
	   public static double attrWeight=0.333;
	   public static int nextProviderID=1;	   
}
