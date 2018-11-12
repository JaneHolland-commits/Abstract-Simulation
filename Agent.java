import java.text.DecimalFormat;

///Agent moves random if NNeighbours is = 0;


//Agent has a prograss bar. Genome is 0-1; when one is reached agent moves

/// 8bit Genome. 

public class Agent {

	private static int NAgents = 0;
	private int AgentNumber;

	//private long Start = System.currentTimeMillis();
	//private long Now;

	private int XLocation;
	private int YLocation;
	private int Orientation;

	private int NewXLocation;
	private int NewYLocation;
	private int NewOrientation;

	private int NNeighbours;
	private int Action; /// 0 or 1;
	int CardDir;
	int Index;
	double AverageDist;

	private static double[] GenomeBinsLower =new double[0];
	//private static double[] GenomeBinsUpper =new double[0];
	double Progress =0;

	//private double Fitness;
	private double RunningFitness = 0;

	private int ClusterNumber;

	static String pattern = "#.###";
	static DecimalFormat decFormat = new DecimalFormat(pattern);

	private double[] Genome; //[0,1,2,3,4,5+]

	public Agent(){/////When Agent is created it should be placed in world.
		AgentNumber = NAgents;
		NAgents++;

		if(GenomeBinsLower.length==0){		
			setGenomeBins();
		}

		do{
			XLocation = (int)((Map.getXLength())*(Math.random()));
			YLocation = (int)(Map.getYLength()*(Math.random()));
		}
		while(Map.getSpotStatus(XLocation, YLocation)!=0);
		
		Map.onSpot(XLocation, YLocation);
		Orientation = (int)( (4)*(Math.random())  );

		Genome = new double[Parameters.NGenomes];
		for(int i = 0; i<Parameters.NGenomes;i++){
			Genome[i] =  Math.random();
		}

	}

	private void setGenomeBins(){
		GenomeBinsLower = new double[10];
		//GenomeBinsUpper = new double[10];

		//double end = Parameters.SpacingDistance;
		// double intra = 1; //((double)(Parameters.Viewcm-Parameters.SpacingDistance))/((double)9);
		//double Next = end + intra;

		GenomeBinsLower[0] = 10.89;
		GenomeBinsLower[1] = 10.00;
		GenomeBinsLower[2] = 9.11;
		GenomeBinsLower[3] = 8.22;
		GenomeBinsLower[4] = 7.34;
		GenomeBinsLower[5] = 6.45;
		GenomeBinsLower[6] = 5.56;
		GenomeBinsLower[7] = 4.67;
		GenomeBinsLower[8] = 3.78;
		GenomeBinsLower[9] = 2.89;


	}

	public static void testingCheckBinBounds(){
		for(int i=0; i<10;i++){
			System.out.println(i+":\t" + GenomeBinsLower[i]);
		}
	}

	public double[] getGenome(){
		return Genome;
	}

	public void setGenome(double[] NewGenome){
		Genome = new double[NewGenome.length];
		for(int i =0; i<NewGenome.length;i++){
			Genome[i] = NewGenome[i];
		}
	}



	public void takeTurn(){//////Called by main. Agent Takes its turn.

		///Reset Variables to be Reset.
		//Start = System.currentTimeMillis();
		reset();


		/////ObserveWorld.
		observe();
		//Now = System.currentTimeMillis();
		//Now = Now - Start;
		//System.out.println("Observe:"+Now);

		////Decide Action.
		decide();


		////Make Action.
		action();

		//Now = System.currentTimeMillis();
		//Now = Now - Start;
		//System.out.println("End:"+Now);

	}


	private void reset(){
		////Set NewLocation/NewOrientation, NNeighbours...... to -42
		NNeighbours = -42;
		Action = -42;

		NewXLocation = -42;
		NewYLocation = -42;
		NewOrientation = -42;

		CardDir = -42;

	}

	public void observe(){
		Index = -42;
		NNeighbours = Map.getNNeighbours(XLocation, YLocation);
		AverageDist = Map.getAvgDis(XLocation, YLocation);

		if (NNeighbours==0){
			Index = 1;
		}

		else{
			for(int i=0; i<GenomeBinsLower.length; i++){
				if(AverageDist < GenomeBinsLower[i]){
					Index = i;
				}
			}
		}
		
		//System.out.println("AvgDist:" + AverageDist + " Index: " + Index);
		//System.out.println("Dist: "+AverageDist +"Bin:" + Index);

		if(Index == -42){
			System.out.println("No Index Found. Dist: "+AverageDist );
		}
	}

	private void decide(){/*
		if(NNeighbours ==0){////No Neighbours.
			Action = Genome[0];////Walk.
		}
		else if(NNeighbours > 0){///Neighbours
			Action = Genome[1];////Stay
		}else{
			System.out.println("Error: NNeighbours is "+NNeighbours);
		}
	 */
		Progress += Genome[Index];

		if(Progress<1){
			Action = 0;////Nothing
		}else{
			Progress = Progress-1;
			Action = 1;
		}

		//System.out.println("AgentNumber:"+AgentNumber+". Progress: "+Progress+". Action: "+Action);
	}


	private void action(){
		if (Action ==0){//Stay
			/////Stay
			NewXLocation = XLocation;
			NewYLocation = YLocation;
			NewOrientation = Orientation;

		}else if(Action == 1){///Random Walk
			////Random
			randomDir();
		}

		Map.offSpot(XLocation, YLocation);
		Map.onSpot(NewXLocation, NewYLocation);

		XLocation = NewXLocation;
		YLocation = NewYLocation;
		Orientation = NewOrientation;

	}///End Action


	private int realitiveDir(int Dir){
		int CardDir = -41;
		int[] XModifers = {0,1,0,-1};/// Basically a Sin Wave. Unit Circle
		int[] YModifers = {1,0,-1,0};

		///////Find CardDir from Dir Possible Dirs 0,1,2 nedded 3,0,1

		if(Dir==2){
			Dir =3;
		}

		CardDir = MyFunctions.mod(Dir+Orientation, 4);


		if(Map.getSpotStatus(XLocation+XModifers[CardDir], YLocation+YModifers[CardDir])==0){
			NewXLocation = XLocation + XModifers[CardDir];
			NewYLocation = YLocation + YModifers[CardDir];
			NewOrientation = CardDir;
			return 0;
		}else{
			return -1;
		}



	}


	private void randomDir(){

		/////Finds new X/Y Or.		
		int[] Locations = new int[4];///////The Availability of Locations. Takes Cardinal Dir. 0-North, 1-East.

		int[] XModifers = {0,1,0,-1};/// Basically a Sin Wave. Unit Circle
		int[] YModifers = {1,0,-1,0};/// Cos.
		///-1 if unavialble.
		int AvailableSpaces = 0;
		int NthPlace;


		for(int i =0; i<Locations.length;i++){///Check if each spot is Available.
			if ( Map.getSpotStatus(XLocation + XModifers[i], YLocation + YModifers[i]) !=0 ){
				Locations[i] = -1;///Not Available can't move there.
			}
		}

		///Can't Move Back. if Or is 1(Right). Cant move (3)West.
		///Can't Move (Or+2) mod 4

		Locations[MyFunctions.mod(Orientation + 2, 4)]=-1;

		///Loop through Array to count Available Spaces.
		for(int i : Locations){
			if (i==0){
				AvailableSpaces++;
			}
		}


		NthPlace = (int)( (AvailableSpaces)*(Math.random()) );

		if (AvailableSpaces == 0){
			NewXLocation = XLocation;
			NewYLocation = YLocation;
			NewOrientation = Orientation;
			return;
		}

		///Take NTHFree Place
		int FreeSpaces=0;

		for(int i =0;i<Locations.length;i++){
			if(Locations[i] ==0){
				if(FreeSpaces == NthPlace){
					CardDir = i;
				}
				FreeSpaces++;
			}
		}

		if (CardDir == -42){
			System.out.println("ERROR in random(). CardDir Nor Assigned");
		}else{
			NewXLocation = XLocation + XModifers[CardDir];
			NewYLocation = YLocation + YModifers[CardDir];
			NewOrientation = CardDir;

		}


	}////End Random


	public void testMotion(){
		System.out.println("Agent " + AgentNumber +" Test.");
		System.out.println("Start: XLocation: "+XLocation+" YLocation: "+YLocation + " Orientation: "+Orientation);

		takeTurn();
		System.out.println("Choices: NNEighbours"+NNeighbours+" Action: " + Action+" CardDir:"+CardDir);
		System.out.println("End: XLocation: "+XLocation+" YLocation: "+YLocation + " Orientation: "+Orientation+"\n");
	}



	public int getXLocation() {
		return XLocation;
	}

	public int getYLocation() {
		return YLocation;
	}

	public static void resetAgentCount(){
		NAgents = 0;
	}

	public int getNNeighbour(){
		observe();
		return NNeighbours;
	}

	/*public void calcFitness(){
		observe();
		//Fitness = NNeighbours;
		Fitness = Map.getFitness(XLocation, YLocation);
		//System.out.println("Agent "+AgentNumber+" Fitness:"+Fitness);
	}*/

	public void updateFitness(){
		observe();
		/*
		//Fitness = NNeighbours;
		//RunningFitness += Map.getFitness(XLocation, YLocation);
		//System.out.println("Agent "+AgentNumber+" Fitness:"+Fitness);


		double DNNeighbours = NNeighbours+1;
		double DIndex = Index+1;
		DIndex = Math.pow(DIndex, -1);						
		RunningFitness += Math.pow(DNNeighbours, DIndex);

		 */

		//double AverageDist = Map.getAvgDis(XLocation, YLocation);
		//System.out.print("\nAgentNumber"+AgentNumber+"AverageDist: "+AverageDist);
		//if(AverageDist>=2){
		//	AverageDist = 1/AverageDist;
		//System.out.print(" Inverted "+AverageDist+" OldFitness:"+RunningFitness);
		double DNNeighbours = NNeighbours+1;
		
		//System.out.println("No. of neighbours: " + DNNeighbours + " Index:" + Index);
		
		
		RunningFitness += Index*DNNeighbours;
		//System.out.println("New Fitness:"+RunningFitness);

		//System.out.println("NEigh:" +NNeighbours+". Index: "+Index +"Fitness:" +( (double) NNeighbours+1)*((double) Math.pow((double)(Index+1), -1))+" Dist: "+AverageDist);

	}

	public double getFitness(){
		//calcFitness();
		return RunningFitness;
	}

	public int getClusterNumber(){
		return ClusterNumber;
	}

	public void setClusterNumber(int Number){
		ClusterNumber = Number;
	}

	public void markAgentMap(){
		Map.AgentMap[XLocation][YLocation] = AgentNumber+1; ///So Agent 0 can't be invisible. The Bastard.
	}

	public int getAgentNumber(){
		return AgentNumber;
	}

}