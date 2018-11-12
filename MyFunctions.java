
public class MyFunctions {
	
	public static double calcDistance(double X1 , double Y1, double X2, double Y2){
		
		double XsSquare = (X2-X1)*(X2-X1);
		double YsSquare = (Y2-Y1)*(Y2-Y1);
		
		return Parameters.SpacingDistance*Math.sqrt(XsSquare+YsSquare);
		
		///Gives Spacing/2 less for length need to think if thats right.
		
		
	}
	
	
	public static int mod(int a,int b){
		if(a >=0){
			return a %b;
		}
		else if(a<0){
			//System.out.println("in negative"+(b+a));
			return mod(b+a,b);
		}
		else{
			System.out.println("ERROR: IN MOD"+a);
			return -1;
		}
	}

}