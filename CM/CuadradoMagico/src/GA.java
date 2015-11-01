import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GA {
	public Population population = null;
	public void intializePopulation(int pop_size){
		population = new Population(pop_size);
		for(Square sq: population.getIndividuals()){
			sq.setPopulation(population);
		}
		population.setCrossoverProbabilities();
	}
	//the algorithm stops when it finds one individual that satisfies the condition :D
	//this we'll have to change
	public boolean stopCondition(){
		List<Square> squares = population.getIndividuals();
		for(Square square: squares) {
			if(square.getMaxNumberOfIdenticalSums() == 9) {
				return true;
			}
		}
		return false;
	}
	public void renewPopulation(){
		Population oldPopulation = population;
		int oldPopulationSize = population.getSize();
		//elitism to preserve the best individuals
		List<Square> bestIndividualsOldGen = oldPopulation.getBestIndividuals();
		population = new Population();
		for(Square sq: bestIndividualsOldGen) {
			population.addIndividual(sq.copy());
		}
		
		for(Square sq: population.getIndividuals()) {
			sq.setPopulation(population);
		}
		List<Square> oldPopulationIndividuals = oldPopulation.getIndividuals();
		while(population.getSize() < oldPopulationSize) {
			//roulette wheel
			Square firstParent = null;
			Random random = new Random();
			float random1 = random.nextFloat();
			for(int i = 0; i < oldPopulation.getSize() - 1; i++){
				if (random1 >= oldPopulationIndividuals.get(i).getProbabilityCrossover() && 
							random1 < oldPopulationIndividuals.get(i + 1).getProbabilityCrossover()) {
						firstParent = oldPopulationIndividuals.get(i + 1);
						
				}
				if (firstParent == null) {
					firstParent = oldPopulationIndividuals.get(0);
				}
			}
			Square secondParent = null;
			float random2 = random.nextFloat();
			for(int i = 0; i < oldPopulation.getSize() - 1; i++){
				if (random2 >= oldPopulationIndividuals.get(i).getProbabilityCrossover() && 
							random2 < oldPopulationIndividuals.get(i + 1).getProbabilityCrossover()) {
						secondParent = oldPopulationIndividuals.get(i + 1);
						
				}
				if (secondParent == null) {
					secondParent = oldPopulationIndividuals.get(0);
				}
			}
			//only one child... possible modification?
			Square child = GA.crossover(firstParent, secondParent);
			//uneven numbers problem
			Square mutatedChild = GA.mutate(child);
			mutatedChild.setPopulation(population);
			population.addIndividual(mutatedChild);
			
		}
		population.setCrossoverProbabilities();
		
		
		
	}
	/* 
	public static Square mutate(Square child){
		//we'll have to refine the parameters later
		double mutation_probability = 0.4;
		Random rand = new Random();
		String bitsChild = child.toBitString();
		String bitsMutatedChild = "";
		for(int i = 0; i < bitsChild.length(); i++){
			char current_bit = bitsChild.charAt(i);
			if(rand.nextFloat() < mutation_probability) {
				if(current_bit == '0'){
					bitsMutatedChild += '1';
				} else {
					bitsMutatedChild += '0';
				}
			} else {
				bitsMutatedChild += current_bit;
			}
		}
		return Square.bitStringToSquare(bitsMutatedChild);
	}
	*/
	public static Square mutate(Square child){
		//we'll have to refine the parameters later
		double mutation_probability = 0.2;
		Random rand = new Random();
		List<Integer> elements = child.getElements();
		List<Integer> newElements = new ArrayList<Integer>();
		for(int i = 0; i < elements.size(); i++){
			if(rand.nextFloat() < mutation_probability) {
				Integer newElement = rand.nextInt((int)Math.pow(child.getDimension(), 2)) + 1;
				newElements.add(newElement);
			} else {
				newElements.add(elements.get(i));
			}
		}
		return new Square(child.getDimension(), newElements);
	}
	public static Square crossover(Square firstParent, Square secondParent) {
	//uniform crossover...sort of
	Random rand = new Random();
	StringBuilder mask_builder = new StringBuilder();
	//16 because n is 4 in the population
	for(int i = 0; i < 16; i++){
	    mask_builder.append(rand.nextFloat() >= 0.5 ? "1" : "0");
	}
	String crossover_mask = mask_builder.toString();
	List<Integer> elementsFirstParent = firstParent.getElements();
	List<Integer> elementsSecondParent = secondParent.getElements();
	List<Integer> elementsChild = new ArrayList<Integer>();
	for(int i = 0; i < crossover_mask.length(); i++){
		if(crossover_mask.charAt(i) == '0'){
			elementsChild.add(elementsFirstParent.get(i));
//			bitsSecondChild += bitsSecondParent.charAt(i);
		} else {
			elementsChild.add(elementsSecondParent.get(i));
//			bitsSecondChild += bitsFirstParent.charAt(i);
		}
	}
	
//	Square secondChild = Square.bitStringToSquare(bitsSecondChild);
//	List<Square> children = new ArrayList<Square>();
//	children.add(firstChild);
//	children.add(secondChild);
//	return children;
	return new Square(firstParent.getDimension(), elementsChild);


}
	//have to add squares to the pop later
/*	public static Square crossover(Square firstParent, Square secondParent) {
		//here used uniform crossover but maybe something else would be better?
		
		//generate mask
		Random rand = new Random();
		StringBuilder mask_builder = new StringBuilder();
		//16 because n is 4 in the population
		for(int i = 0; i < 16; i++){
			for(int j = 0; j < 5; j++) {
		        mask_builder.append(rand.nextFloat() >= 0 ? "1" : "0");
		    }
		}
		String crossover_mask = mask_builder.toString();
		String bitsFirstParent = firstParent.toBitString();
		String bitsSecondParent = secondParent.toBitString();
		String bitsFirstChild = "";
//		String bitsSecondChild = "";
		for(int i = 0; i < crossover_mask.length(); i++){
			if(crossover_mask.charAt(i) == '0'){
				bitsFirstChild += bitsFirstParent.charAt(i);
//				bitsSecondChild += bitsSecondParent.charAt(i);
			} else {
				bitsFirstChild += bitsSecondParent.charAt(i);
//				bitsSecondChild += bitsFirstParent.charAt(i);
			}
		}
		Square firstChild = Square.bitStringToSquare(bitsFirstChild);
//		Square secondChild = Square.bitStringToSquare(bitsSecondChild);
//		List<Square> children = new ArrayList<Square>();
//		children.add(firstChild);
//		children.add(secondChild);
//		return children;
		return firstChild;

	
	} */
	public List<Square> satisifiedConditionIndividuals(){
		List<Square> squares = population.getIndividuals();
		List<Square> squaresSatisfy = new ArrayList<Square>();
 		for(Square square: squares) {
			if(square.getMaxNumberOfIdenticalSums() == 9) {
				squaresSatisfy.add(square);
			}
		}
 		return squaresSatisfy;
	}
	public void printPopulation(){
		population.print();
		
	}
}
