
import java.io.PrintWriter;

public class Parameters {

	///OutPut

	static boolean PrintWorld = true;


	static String DefaultFileName = "TaskFourSpeedProgressBarDistFit";

	////Setable Parameters

	static double NAgents = 50;
	static double Duration = 500;
	static int NGenerations = 75;

	static double[] SecsPerItArray = {2};//{2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2};
	static int NRuns = 1;

	static int NGenomes = 10; ////////Needs to be Changed in NAgents


	static public double XLengthcm = 100; ///XArena Length cm 
	static public double YLengthcm = 100;
	static public double SpeedcmPs = 1; //cm/s
	static public double Viewcm = 10; //cm 

	////Genetic Parameters
	static double MutProb = 0.02;
	static double CrossProb = 0.30;


	//////Calculated
	static double SecsPerIt = SecsPerItArray[0];
	static double NIterations = Duration / SecsPerIt;
	static public double SpacingDistance = SecsPerIt * SpeedcmPs;	
	static public double XGridLength = XLengthcm/SpacingDistance;
	static public double YGridLength = YLengthcm/SpacingDistance;



	//////Just A Random Number

	static public double RandomNumber = (1000)*Math.random();

	public static void recalculateParameters(){
		NIterations = Duration / SecsPerIt;
		SpacingDistance = SecsPerIt * SpeedcmPs;
		XGridLength = XLengthcm/SpacingDistance;
		YGridLength = YLengthcm/SpacingDistance;
	}

	public static void displayParameters(){
		System.out.println("Incoded Parameters: Iteration Length: "+Parameters.SecsPerIt+"s. GridMeasurments: "+Parameters.XLengthcm+"cmX"+Parameters.XLengthcm+"cm. View: "+Parameters.Viewcm+"cm."+ " Nagents:" +NAgents +" Agent Density: "+(NAgents/(XGridLength*YGridLength))*100 +"%." );
		System.out.println("Calculated Parameters: DiscreteGrid: "+Map.getXLength()+"X"+Map.getYLength()+". Distance Between Points: "+ SpacingDistance + "NIterations: " + NIterations);
		Map.maxViewAblePoints();
	}

	public static void verifyParameters(){
		boolean ERROR = false;
		if ( (Math.round(Parameters.XGridLength) != Parameters.XGridLength) || ((Math.round(Parameters.YGridLength) != Parameters.YGridLength)) ){
			System.out.println("Grid: "+Parameters.XGridLength+"X"+Parameters.YGridLength+". Warning This will be rounded!!!");
			ERROR = true;
		}

		if( NAgents > (XGridLength*YGridLength) ){
			System.out.println("ERROR: More Agents Than Spaces.");
			ERROR = true;
		}

		if(  (NIterations)!=(double)Math.round(NIterations)  ){
			System.out.print("NIterations is "+NIterations+" will be rounded to "+(double)Math.round(NIterations) );
			NIterations = (double)Math.round(NIterations);
			System.out.println(" for t = " + (NIterations*SecsPerIt) );
			ERROR = true;
		}

		displayParameters();
		if(!ERROR){
			System.out.println("Parameters seem OK.");;
		}

	}

	public static void outputParametersToFile(PrintWriter out){
		out.println("Incoded Parameters: Iteration Length: "+Parameters.SecsPerIt+"s. GridMeasurments: "+Parameters.XLengthcm+"cmX"+Parameters.XLengthcm+"cm. View: "+Parameters.Viewcm+"cm."+ " Nagents:" +NAgents +" Agent Density: "+(NAgents/(XGridLength*YGridLength))*100 +"%." );
		out.println("Calculated Parameters: DiscreteGrid: "+Map.getXLength()+"X"+Map.getYLength()+". Distance Between Points: "+ SpacingDistance + "NIterations: " + NIterations);
		out.println("GA. MutProb: "+Parameters.MutProb+". Crossover: "+Parameters.CrossProb);
		out.println("Random Number: (To MatchOutputs NOTASEED!!)"+RandomNumber);
	}

	public static void worldOutHeader(PrintWriter out){
		out.println(XGridLength+"\t"+YGridLength+"\t"+NAgents+"\t"+0+"\t"+NGenerations+"\t"+NIterations);
		out.println();
	}


}
