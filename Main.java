//Copyright (C) 2018 Ciaran Gallagher <ciaransgallagher@gmai.com>

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;

//import src.Agent;



public class Main {
	
	static String pattern = "#.#######", pattern2 = "#.###";
	static DecimalFormat decFormat = new DecimalFormat(pattern), decFormat2 = new DecimalFormat(pattern2);

	public static void main(String[] args) {						

		/////Initialization


		///Create Map.
		Map Map = new Map();						

		///Create Agent Array;
		ArrayList<Agent> Agents;

		try{
			////If PrintWorld True;
			String FileNameWorld = "Temp";
			String FileNameAverageIter = "Temp";
			String FileNameAverageGen = "Temp";

			FileWriter fileWorld = new FileWriter(FileNameWorld);			
			BufferedWriter writerWorld = new BufferedWriter(fileWorld);			
			PrintWriter WorldOut = new PrintWriter(writerWorld);

			FileWriter fileWriterAverageIter = new FileWriter(FileNameAverageIter);			
			BufferedWriter writerAverageIter = new BufferedWriter(fileWriterAverageIter);			
			PrintWriter ResultsOutAverageIter = new PrintWriter(writerAverageIter);

			FileWriter fileWriterAverageGen = new FileWriter(FileNameAverageGen);			
			BufferedWriter writerAverageGen = new BufferedWriter(fileWriterAverageGen);			
			PrintWriter ResultsOutAverageGen = new PrintWriter(writerAverageGen);

			if(Parameters.PrintWorld){

				if(Parameters.NRuns >1){
					System.out.println("To Many Runs. Only PrintWorld For Single Run");
					return;
				}
				WorldOut.println(Parameters.NAgents+"\t"+Parameters.NIterations+"\t"+Map.getXLength()+"\t"+Map.getYLength());

			}




			//////////////////////////////////////////////////////////////Simulations Start

			for(int ParCount = 0; ParCount<Parameters.SecsPerItArray.length;ParCount++){//////Loop Through parameters
				Parameters.SecsPerIt = Parameters.SecsPerItArray[ParCount];
				Parameters.recalculateParameters();
				Map.mapReset();
				FileNameWorld = "Sim"+Integer.toString(ParCount)+Parameters.DefaultFileName + Double.toString(Parameters.SecsPerIt)+"WorldOut.txt";



				FileNameAverageIter = "Sim"+Integer.toString(ParCount)+Parameters.DefaultFileName + Double.toString(Parameters.SecsPerIt)+"AverageIter.txt";
				FileNameAverageGen = "Sim"+Integer.toString(ParCount)+Parameters.DefaultFileName + Double.toString(Parameters.SecsPerIt)+"AverageGen.txt";

				WorldOut.close();
				ResultsOutAverageIter.close();
				ResultsOutAverageGen.close();

				fileWorld = new FileWriter(FileNameWorld);			
				writerWorld = new BufferedWriter(fileWorld);			
				WorldOut = new PrintWriter(writerWorld);


				fileWriterAverageIter = new FileWriter(FileNameAverageIter);			
				writerAverageIter = new BufferedWriter(fileWriterAverageIter);			
				ResultsOutAverageIter = new PrintWriter(writerAverageIter);

				fileWriterAverageGen = new FileWriter(FileNameAverageGen);			
				writerAverageGen = new BufferedWriter(fileWriterAverageGen);			
				ResultsOutAverageGen = new PrintWriter(writerAverageGen);


				if(Parameters.PrintWorld){

					if(Parameters.NRuns >1){
						System.out.println("Too Many Runs. Only PrintWorld For Single Run");
						return;
					}
					//WorldOut.println(Parameters.NAgents+"\t"+Parameters.NIterations+"\t"+Map.getXLength()+"\t"+Map.getYLength());
					Parameters.worldOutHeader(WorldOut);
				}


				Parameters.outputParametersToFile(ResultsOutAverageGen);
				Parameters.outputParametersToFile(ResultsOutAverageIter);

				System.out.println("\nStarting Sim: "+ParCount+" with below parameters. Output as "+FileNameAverageIter);
				Parameters.verifyParameters();

				Results.NAgentsClusterdArray = new double[Parameters.NGenerations+1][(int)Parameters.NIterations+1];
				Results.AvgNNeighboursArray = new double[Parameters.NGenerations+1][(int)Parameters.NIterations+1];
				Results.AvgNNeighboursClusteredArray = new double[Parameters.NGenerations+1][(int)Parameters.NIterations+1];
				Results.NClustersArray= new double[Parameters.NGenerations+1][(int)Parameters.NIterations+1];
				Results.AvgClusterSizeArray = new double [Parameters.NGenerations+1][(int)Parameters.NIterations+1];
				//Results.Genomes = new double[Parameters.NGenerations][(int) Math.pow(2, Parameters.NGenomes)];
				Results.GroupFitness = new double[Parameters.NGenerations+1];

				Results.ClusterInfo = new double[Parameters.NGenerations+1][(int) (Parameters.NIterations+1)][(int) Parameters.NAgents][6];
				Results.AgentsinCluster = new double[Parameters.NGenerations+1][(int) (Parameters.NIterations+1)][(int) Parameters.NAgents][(int) Parameters.NAgents];

				Results.RunResults = new double[Parameters.NRuns][2];
				Results.RunningIterAverage = new double[Parameters.NGenerations+1][(int) (Parameters.NIterations+1)][4];
				Results.RunningGenAverage = new double[Parameters.NGenerations+1][4];
				for(int RunCount = 0 ;RunCount<Parameters.NRuns;RunCount++){//////Run NRuns times
					Map.mapReset();
					if(RunCount%2 ==0){
						System.out.println("Sim "+ParCount+" Run: "+RunCount);
					}
					//////Agents Need to be Fresh Each Run.
					Agents = new ArrayList<Agent>();
					Agent.resetAgentCount();
					for(int i = 0; i<Parameters.NAgents; i++){
						Agents.add(new Agent());
					}
					/////////////////////Iteration Loop
					if(Parameters.PrintWorld){
						//printWorld(0,Agents,WorldOut);
					}
					////////////////GenStart
					for(int GenCount =0; GenCount<Parameters.NGenerations;GenCount++){
						//////Clear Map

						ArrayList<Agent> Temp = Evolution.selectNextGen(Agents);
						Agents = new ArrayList<Agent>();
						Agent.resetAgentCount();
						Map.mapReset();
						for(int i = 0; i<Parameters.NAgents; i++){
							Agents.add(new Agent());
						}
						if(GenCount >0){////Get New Agents Genome From Tournament
							int i =0;
							for(Agent OldGenome : Temp){
								Agents.get(i).setGenome(OldGenome.getGenome().clone());
								i++;
							}

						}

						//////CreateNew Agent Array.

						if(Parameters.PrintWorld){
							printWorld(GenCount,0,Agents,WorldOut);
						}


						iterResults(GenCount, 0, RunCount,Agents);
						for(int IterCount = 0;IterCount<Parameters.NIterations;IterCount++){///////////Start Iteration

							//System.out.println(ParCount+"\t"+RunCount + "\t"+IterCount+"\n\n");
							for (Agent CurrentAgent : Agents){
								CurrentAgent.takeTurn();;
							}
							////////Iteration Results
							if(Parameters.PrintWorld){
								printWorld(GenCount, IterCount,Agents,WorldOut);
							}

							for(Agent CurrentAgent : Agents){
								CurrentAgent.updateFitness();
							}
							//////////////////////////
							iterResults(GenCount,IterCount+1,RunCount,Agents );
						}////////////End Iteration.

						genResults(GenCount,Agents);

					}///End Gen

					////////////////////

				}////End Run.



				AverageOut(ResultsOutAverageIter,ResultsOutAverageGen);
			}///End Parameters Loop



			WorldOut.close();
			ResultsOutAverageIter.close();
			ResultsOutAverageGen.close();

			System.out.println("Finished!!!");
		} catch(IOException e){
			e.printStackTrace();
		}



	}//////End Main

	public static void iterResults(int gen, int iter,int Run,ArrayList<Agent> Agents){
		///////Calcs and Adds The Results for this iteration to the correct Array.
		double[] ClusterCountingResults = clusterCounting(Agents,gen,iter,Run);
		Results.NClustersArray[gen][iter]+=ClusterCountingResults[0];
		Results.AvgClusterSizeArray[gen][iter]+=ClusterCountingResults[1];

		/////Count NAgentsClustered and AvgNNeighbours and AvgNNeighboursClustered
		int NAgentsClustered=0;
		double AvgNNeighbours=0;
		double AvgNNeighboursClustered =0;

		int NNeighbours;
		for(Agent CurrentAgent : Agents){
			NNeighbours = CurrentAgent.getNNeighbour();
			if(NNeighbours>0){
				NAgentsClustered++;
				AvgNNeighboursClustered+=NNeighbours;
			}
			AvgNNeighbours += NNeighbours;
		}

		AvgNNeighbours = AvgNNeighbours/Parameters.NAgents;
		if(NAgentsClustered>0){
			AvgNNeighboursClustered = AvgNNeighboursClustered/NAgentsClustered;
		}
		else{
			AvgNNeighboursClustered=0;
		}

		Results.NAgentsClusterdArray[gen][iter] += NAgentsClustered;
		Results.AvgNNeighboursArray[gen][iter] += AvgNNeighbours;
		Results.AvgNNeighboursClusteredArray[gen][iter] += AvgNNeighboursClustered;
	}

	public static void genResults(int Gen,ArrayList<Agent> Agents){
		double[] Array;	
		int Fitness=0;

		for(Agent CurrentAgent:Agents){
			Fitness += CurrentAgent.getFitness();
		}

		Results.GroupFitness[Gen]+=Fitness;

		//System.out.println("Gen:"+Gen);
		int Number=0;
		System.out.print("Gen: "+Gen+". ");
		for(Agent CurrentAgent : Agents){
			Number=0;

			Array=CurrentAgent.getGenome();
			if(true){//Gen == Parameters.NGenerations-1){
				System.out.println("\nAgent: "+CurrentAgent.getAgentNumber()+" Fitness: "+CurrentAgent.getFitness());
				System.out.print("(");
				for(int i =0; i<Array.length;i++){
					System.out.println(Array[i]+",");
				}
				System.out.println(")");
			}
			//Results.Genomes[Gen][Number]++;

			//System.out.print("Gneome = {");
			//for(double i :CurrentAgent.getGenome()){
			//System.out.print(i+",");
			//}
			//System.out.println();
		}


	}///End GenResults


	public static double[] clusterCounting(ArrayList<Agent> Agents, int Gen, int Iter, int Run){
		double[] CCResults = new double[2]; //[NClusters, AvgSize]
		int Smooth = (int)Parameters.NAgents+10; ///// How many times it loops through code to set all agents with
		///// Correct Cluster Number.

		int[][] Viewable = new int[(int) Parameters.NAgents][(int) Parameters.NAgents]; ///A List of what each Agent Can see.
		int[] AgentNeighbours = new int[(int) Parameters.NAgents];

		///Reset AgentMap
		Map.resetAgentMap();
		for(Agent CurrentAgent : Agents){
			CurrentAgent.markAgentMap();
		}

		///Assaign Initial Cluster Numbers		
		int Tracker =1;		
		for(Agent CurrentAgent : Agents){
			if(CurrentAgent.getNNeighbour()==0){
				CurrentAgent.setClusterNumber(0);
			}else{
				CurrentAgent.setClusterNumber(Tracker);
				Tracker++;
			}
		}


		//////////////////Find Agents that Each Agent Can see
		int[] Temp = new int[(int) Parameters.NAgents];
		for(Agent CurrentAgent : Agents){
			AgentNeighbours[CurrentAgent.getAgentNumber()] = CurrentAgent.getNNeighbour();

			///////
			Temp = Map.getAgentsViewable(CurrentAgent.getXLocation(), CurrentAgent.getYLocation());
			for(int i =0;i<Temp.length;i++){
				Viewable[CurrentAgent.getAgentNumber()][i] = Temp[i]-1;
			}
		}





		////////Loop Through All Agents Smouth Times.
		Agent Neighbour;
		for(int i =0; i<Smooth;i++){

			for(Agent CurrentAgent : Agents){//////Loop Each Agent

				for(int j =0; j<AgentNeighbours[CurrentAgent.getAgentNumber()];j++){////Loop Through Current Agents Neighbours
					int N = Viewable[CurrentAgent.getAgentNumber()][j];
					//System.out.print("\ni:"+i+" j: "+j+"N:"+N);
					Neighbour = Agents.get(N);

					if(CurrentAgent.getClusterNumber()>Neighbour.getClusterNumber()){
						Neighbour.setClusterNumber(CurrentAgent.getClusterNumber());
					}else if(CurrentAgent.getClusterNumber()<Neighbour.getClusterNumber()){
						CurrentAgent.setClusterNumber(Neighbour.getClusterNumber());
					}
				}
			}


		}/////End Smooth

		////All Agents Should have a cluster Number For their Cluster.

		ArrayList<Cluster> ClustersList= new ArrayList<Cluster>();


		double[] Clusters = new double[Tracker+1];
		int NClusters=0;
		double AvgClusterSize=0;
		for(Agent CurrentAgent : Agents){
			Clusters[CurrentAgent.getClusterNumber()]++;
			///////
			if( (Clusters[CurrentAgent.getClusterNumber()]==1)&&(CurrentAgent.getClusterNumber()!=0) ){////Create new Cluster id none already
				ClustersList.add(new Cluster(CurrentAgent.getClusterNumber()));

			}

			////////Add Agent to Cluster

			if(CurrentAgent.getClusterNumber()!=0){
				boolean ClusterFound=false;
				for(Cluster CurrentCluster : ClustersList){


					if(CurrentCluster.getClusterNumber() == CurrentAgent.getClusterNumber()){
						CurrentCluster.addAgent(CurrentAgent);
						ClusterFound = true;
					}				 
				}

				if(!ClusterFound){
					System.out.println("No Cluster Found for Agent!!!");
				}

			}

		}

		//Each Cluster should have the amount of Agents in it recorded in Clusters.
		int TempClusteredAgents=0;
		for(int i =1; i<Clusters.length;i++){
			if(  (Clusters[i]>0)&&(i>0) ){
				//System.out.println("CNumber: "+i+". NAgents: "+Clusters[i]);
				NClusters++;
				TempClusteredAgents += Clusters[i];
			}
		}



		if (NClusters>0){
			AvgClusterSize = ( (double)TempClusteredAgents ) / ( (double)(NClusters) );
		}
		else{
			AvgClusterSize = 0;
		}



		////Save to Results;

		//Results.ClusterInfo;
		///[Gen][Iter][ClusterNumber][Value] Value(0-CNumber, 1-CZize, 2-CentX, 3-CentY, 4-Dist)

		ArrayList<Agent> AgentsinCluster;
		int k =0;
		int ACount=0;

		double AvAvDist =0;
		for (Cluster CurrentCluster : ClustersList){
			AgentsinCluster = CurrentCluster.getAgents();
			Results.ClusterInfo[Gen][Iter][k][0] = k;
			Results.ClusterInfo[Gen][Iter][k][4] = CurrentCluster.getAvgDist();
			Results.ClusterInfo[Gen][Iter][k][1] = CurrentCluster.getSize();
			Results.ClusterInfo[Gen][Iter][k][2] = (double)CurrentCluster.getCenter()[0];
			Results.ClusterInfo[Gen][Iter][k][3] = (double)CurrentCluster.getCenter()[1];
			Results.ClusterInfo[Gen][Iter][k][5] = CurrentCluster.getAverageDistanceFromNeighbours();
			ACount=0;
			AvAvDist += CurrentCluster.getAvgDist();
			for(Agent CurrentAgent : AgentsinCluster){
				Results.AgentsinCluster[Gen][Iter][k][ACount] = CurrentAgent.getAgentNumber();
				ACount++;
			}
			k++;
		}


		int Biggest =0;
		if( (Gen==Parameters.NGenerations-1)&&(Iter==Parameters.NIterations-1) ){
			for (Cluster CurrentCluster : ClustersList){
				if(CurrentCluster.getSize() >Biggest){
					Biggest = CurrentCluster.getSize();
				}
			}
			Results.RunResults[Run][0] = Biggest;
			Results.RunResults[Run][1] = NClusters;
		}



		Results.RunningIterAverage[Gen][Iter][0] += NClusters;
		Results.RunningIterAverage[Gen][Iter][1] += AvgClusterSize;
		Results.RunningIterAverage[Gen][Iter][2] += AvAvDist/NClusters;
		Results.RunningIterAverage[Gen][Iter][3] += TempClusteredAgents;


		//////////////////////Testing Segmant


		if(Iter ==249&&Gen==49){
			//System.out.println("Runout: "+NClusters+"\t"+AvgClusterSize);
		}


		if((NClusters*AvgClusterSize)>(Parameters.NAgents+0.0001)){
			System.out.println("Total Agents Clustered: "+(NClusters*AvgClusterSize)+". NAgents: "+Parameters.NAgents);
		}


		//////////////////////End Testing

		CCResults[0]= NClusters;
		CCResults[1] = AvgClusterSize;

		return CCResults;
	}



	public static void AverageOut(PrintWriter out, PrintWriter out2){
		double DivBy = Parameters.NRuns;

		////////For each iter in each gen

		out.println("Gen\tIter\tNClusters\tAvgSize\tAvgDist\tAgentsClustered\n");
		for(int Gen =0; Gen <Parameters.NGenerations;Gen++){
			for(int Iter = 0; Iter<Parameters.NIterations+1;Iter++){
				out.println(Gen+"\t"+Iter+"\t"+(Results.RunningIterAverage[Gen][Iter][0]/DivBy)+"\t"+(Results.RunningIterAverage[Gen][Iter][1]/DivBy)+"\t"+(Results.RunningIterAverage[Gen][Iter][2]/DivBy)+"\t"+(Results.RunningIterAverage[Gen][Iter][3]/DivBy));
			}
		}

		////Only final iter in each gen
		out2.println("Gen\tFitness\tNClusters\tAvgSize\tAvgDist\n");
		for(int Gen =0; Gen<Parameters.NGenerations; Gen++){
			out2.println(Gen+"\t"+Results.GroupFitness[Gen]+"\t"+(Results.RunningIterAverage[Gen][(int) (Parameters.NIterations-1)][0]/DivBy)+"\t"+(Results.RunningIterAverage[Gen][(int) (Parameters.NIterations-1)][1]/DivBy)+"\t"+(Results.RunningIterAverage[Gen][(int) (Parameters.NIterations-1)][2]/DivBy)+(Results.RunningIterAverage[Gen][(int) (Parameters.NIterations-1)][3]/DivBy));
		}
	}



	public static void printWorld(int gen, int iter, ArrayList<Agent> Agents, PrintWriter out){
		//System.out.println("PrintingWorld");
		//out.print(iter +"\n");

		for(Agent CurrentAgent : Agents){
			out.print(gen+"\t"+iter+"\t"+CurrentAgent.getAgentNumber()+"\t"+CurrentAgent.getXLocation()+"\t"+CurrentAgent.getYLocation()+"\t"+"\n");
		}	


	}

}
