

public class Map {

	static private int[][] Map;
	static public int[][] AgentMap;

	Map(){
		Map = new int[(int) Math.round(Parameters.XGridLength)][(int) Math.round(Parameters.YGridLength)];
		AgentMap = new int[(int) Math.round(Parameters.XGridLength)][(int) Math.round(Parameters.YGridLength)];
	}

	public static void mapReset(){
		Map = new int[(int) Math.round(Parameters.XGridLength)][(int) Math.round(Parameters.YGridLength)];
	}

	public static void resetAgentMap(){
		AgentMap = new int[(int) Math.round(Parameters.XGridLength)][(int) Math.round(Parameters.YGridLength)];
	}

	public static int getXLength(){
		return Map.length;
	}

	public static int getYLength(){
		return Map[0].length;
	}

	public static int getSpotStatus(int X, int Y){//Basicly occupied but outputs two for a wall


		if ( (X >= Map.length) || (Y >= Map[0].length) || (X<0) || (Y<0) ){
			return 2;//wall		
		}else{
			return Map[X][Y];
		}

	}

	public static void onSpot(int X, int Y){
		if(getSpotStatus(X,Y)==0){
			Map[X][Y] = 1;
		}
		else{
			System.out.println("Error: in onSpot. spotStatus is: " + getSpotStatus(X,Y));
		}
	}


	public static void offSpot(int X, int Y){
		if(getSpotStatus(X,Y)==1){
			Map[X][Y] = 0;
		}
		else{
			System.out.println("Error: in offSpot. spotStatus is: " + getSpotStatus(X,Y));
		}
	}

	public static int getNNeighbours(int X, int Y){//////Not Sure if This is the Best Way.
		double Dist;
		int NNeighbours =0;
		for(int XCount =0; XCount < Map.length; XCount++){
			for(int YCount=0; YCount<Map[0].length; YCount++){

				Dist = MyFunctions.calcDistance(X, Y, XCount, YCount);

				if ( (Dist <= Parameters.Viewcm) && (Dist > 0) ){//Point is Viewable
					if(getSpotStatus(XCount,YCount)==1){
						NNeighbours++;
					}
				}

			}
		}///End Fors

		return NNeighbours;

	}///End getNNeighbours

	public static double getFitness(int X, int Y){
		double fitness = 0;
		double Dist;
		for(int XCount =0; XCount < Map.length; XCount++){
			for(int YCount=0; YCount<Map[0].length; YCount++){

				Dist = MyFunctions.calcDistance(X, Y, XCount, YCount);

				if ( (Dist <= Parameters.Viewcm) && (Dist > 0) ){//Point is Viewable
					if(getSpotStatus(XCount,YCount)==1){
						////////Neighbour to an Agent with d(i,j) = dist;
						fitness = fitness + (1/Dist);
						//System.out.println("Dist"+Dist+". Added"+(1/Dist));
					}
				}

			}
		}///End Fors

		return fitness;

	}


	public static void maxViewAblePoints(){//////Not Sure if This is the Best Way.

		double X = Math.round(Map.length/2);
		double Y = Math.round(Map[0].length/2);
		double Dist;
		int Viewable =0;
		for(int XCount =0; XCount < Map.length; XCount++){
			for(int YCount=0; YCount<Map[0].length; YCount++){

				Dist = MyFunctions.calcDistance(X, Y, XCount, YCount);

				if ( (Dist <= Parameters.Viewcm) && (Dist > 0) ){
					Viewable++;
				}

			}
		}///End Fors

		double ViewablePercent = Viewable/(Parameters.XGridLength*Parameters.YGridLength)*100;
		System.out.println("Paremeters Give " + Viewable + " Spots. Or "+ViewablePercent + "% Viewable Grid");

	}///End max ViewablePoints


	public static int[] getAgentsViewable(int X, int Y){
		int[] AgentsViewable = new int[(int) Parameters.NAgents];


		double Dist;
		int Seen=0;
		for(int XCount =0; XCount < Map.length; XCount++){
			for(int YCount=0; YCount<Map[0].length; YCount++){

				Dist = MyFunctions.calcDistance(X, Y, XCount, YCount);

				if ( (Dist <= Parameters.Viewcm) && (Dist > 0) ){//Point is Viewable
					if(getSpotStatus(XCount,YCount)==1){//////Agent at (X,Y) can see (XCount,YCount)
						AgentsViewable[Seen] = AgentMap[XCount][YCount];
						Seen++;
					}
				}

			}
		}///End Fors

		return AgentsViewable;

	}


	public static double getAvgDis(int X, int Y){
		//int[] AgentsViewable = new int[(int) Parameters.NAgents];


		double Dist;
		double AverageDist = 0;
		double Seen=0;
		for(int XCount =0; XCount < Map.length; XCount++){
			for(int YCount=0; YCount<Map[0].length; YCount++){

				Dist = MyFunctions.calcDistance(X, Y, XCount, YCount);

				if ( (Dist <= Parameters.Viewcm) && (Dist > 0) ){//Point is Viewable
					if(getSpotStatus(XCount,YCount)==1){//////Agent at (X,Y) can see (XCount,YCount)
						AverageDist += Dist;
						//AgentsViewable[Seen] = AgentMap[XCount][YCount];
						Seen++;
					}
				}

			}
		}///End Fors


		AverageDist = AverageDist/Seen;
		//System.out.println("X: "+X+", Y: "+Y);
		//System.out.println("AvgDist: " + AverageDist);
		return AverageDist;

	}

	public static void outputAgentMap(){
		for(int Y = AgentMap[0].length-1; Y >= 0;Y--){
			System.out.println();
			for(int X = 0; X <AgentMap[0].length;X++){
				System.out.print(AgentMap[X][Y]+"\t");
			}
		}
		System.out.println();
	}

	public static void outputMap(){
		for(int Y = Map[0].length-1; Y >= 0;Y--){
			System.out.println();
			for(int X = 0; X <Map[0].length;X++){
				System.out.print(Map[X][Y]+"\t");
			}
		}
	}





}
