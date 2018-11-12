


import java.util.ArrayList;

public class Evolution {
	
	public static ArrayList<Agent> selectNextGen(ArrayList<Agent> PrevAgents){
		
		ArrayList<Agent> CurrentAgents = new ArrayList<Agent>();
		
		while(CurrentAgents.size()<Parameters.NAgents){////TournamentSlecect
			
			///PickTwoRandomAgents.
			int Agent1;
			int Agent2;
			Agent Winner;
			
			ArrayList<Agent> CrossResults;
			
			
			Agent1 = (int)( (PrevAgents.size())*(Math.random()) );
			Agent2 = (int)( (PrevAgents.size())*(Math.random()) );
			
			//System.out.print("Agent "+Agent1+" Vrs Agent "+Agent2+". Clearly the ");
			if( PrevAgents.get(Agent1).getFitness()>PrevAgents.get(Agent2).getFitness()){
				Winner = PrevAgents.get(Agent1);
				//System.out.print(" First");
			}
			else{
				Winner = PrevAgents.get(Agent2);
				//System.out.print(" Second");
			}
			
			//System.out.println(" Dude won");
			
			if(Math.random()<Parameters.MutProb){
				Winner = mutate(Winner);
			}
			/////If Crossover is choseen instead dont add winner
			if(Math.random()<Parameters.CrossProb){
				//CrossResults = crossover(PrevAgents);
				CurrentAgents.add(crossover(PrevAgents));
				
				
				if(CurrentAgents.size()<Parameters.NAgents){
					//CurrentAgents.add(CrossResults.get(1));
				}
			}else{
				CurrentAgents.add(Winner);
				
				
				/*
				System.out.println("Winner: ");
				System.out.print("Gneome= {");
				for(int i :Winner.getGenome()){
					System.out.print(i+",");
				}
				System.out.println("}. Fitness: "+Winner.getFitness());
				*/
			}
			//System.out.println("Agent "+Agent1+"Fitness:"+PrevAgents.get(Agent1).getFitness()+" Vrs. Agent "+Agent1+"Fitness:"+PrevAgents.get(Agent1).getFitness() + "  Winnerf:"+Winner.getFitness());
			
		}
		
		return CurrentAgents;
		
	}///////end SelectNextGen;
	
	private static Agent mutate(Agent CurrentAgent){
		//////Inverts a bit of genome
		double[] Genome = CurrentAgent.getGenome().clone();
		
		int BitToInvert = (int)( (Genome.length) *Math.random() );
		double NewValue;
		
		/*
		if(Genome[BitToInvert]==0){
			NewValue =1;
		}else if(Genome[BitToInvert]==1){
			NewValue = 0;
		}else{
			NewValue = -1;
			System.out.println("Error in Mutate Genome bit: "+Genome[BitToInvert]);
		}
		*/
		Genome[BitToInvert] = Math.random();
		
		Agent NextAgent = new Agent();
		NextAgent.setGenome(Genome);
		
		return NextAgent;
	}
	
	
	private static Agent crossover(ArrayList<Agent> PrevAgents){
		//////Performs a Tournament select.
		/////Crosesover winners.
		////Cross Point Should be middle. Rounded Down.
		
		
		
		Agent ParentOne;
		Agent ParentTwo;
		
		ParentOne = tournForCrossover(PrevAgents);
		ParentTwo = tournForCrossover(PrevAgents);
		
		double Bit1Speed;
		double Bit2Speed;
		
		int Bit1Dir;
		int Bit2Dir;
		
		double[] GenomeOne = ParentOne.getGenome().clone();
		double[] GenomeTwo = ParentTwo.getGenome().clone();
		
		
		
		////
		
		for(int i=0; i<GenomeOne.length; i++){
			Bit1Speed = GenomeOne[i];
			Bit2Speed = GenomeTwo[i];
			
			if(Math.random()<0.5){/////Not really a necassry part
				GenomeOne[i] = Bit1Speed;
				GenomeTwo[i] = Bit2Speed;
				
			}else{////Could just be this case
				GenomeOne[i] = Bit2Speed;
				GenomeTwo[i] = Bit1Speed;
				
			}
		}
		
		Agent ChildOne = new Agent();//ParentOne;
		ChildOne.setGenome(GenomeOne);
		
		/*
		System.out.println("CrossResults\n[");
		for(double i : GenomeOne){
			System.out.print(i+", ");
		}
		System.out.print("]\n");
		*/
		return ChildOne;
		
	}
	
	private static Agent tournForCrossover(ArrayList<Agent> PrevAgents){
		int Agent1;
		int Agent2;
		Agent Winner;
		
		Agent1 = (int)( (PrevAgents.size())*(Math.random()) );
		Agent2 = (int)( (PrevAgents.size())*(Math.random()) );
		
		if( PrevAgents.get(Agent1).getFitness()>PrevAgents.get(Agent2).getFitness()){
			Winner = PrevAgents.get(Agent1);
		}
		else{
			Winner = PrevAgents.get(Agent2);
		}
		
		return Winner;
	}
	
	

}