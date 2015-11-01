import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
public class Square {
	//numbers in the square
	List<Integer> elements;
	int dimension;
	Population population = null;

	//the probability is calculated with respect to the current population
	float probability = 0;
	
	//roulette wheel selection
	float probability_order = 0;
	public Square(int dim, List<Integer> numbers){
		dimension = dim;
		elements = numbers;
	}
	public Square(int dim) {
		elements = new ArrayList<Integer>();
		dimension = dim;
		
		Random rand = new Random();
		Integer numberOfElements = Double.valueOf(Math.pow(dimension, 2)).intValue();
		for(int i = 0; i < numberOfElements; i++) {
			elements.add(rand.nextInt(numberOfElements) + 1);
		}
	}
	public List<Integer> getElements(){
		return elements;
	}
	public int getDimension(){
		return dimension;
	}
	public int getElement(int row, int column){
		return elements.get(row * dimension + column);
	}
	public void setPopulation(Population pop){
		population = pop;
	}
	public Population getPopulation(Population pop){
		return population;
	}
	public String toString(){
		String squareString = "";
		for (int i = 0; i < dimension; i++) {
			for (int j = 0; j < dimension; j++) {
				squareString += String.valueOf(String.format("%2d", elements.get(i * dimension + j))) + "  "; 
			}
			squareString = squareString.substring(0, squareString.length() - 1);
			squareString += "\n";
		}
		return squareString;
	}
	//this method will be needed to calculate the rank
	public int getMaxNumberOfIdenticalSums(){
		List<Integer> sums = new ArrayList<Integer>();
		//summing the rows
		for (int i = 0; i < dimension; i++) {
			int sum = 0;
			for(int j = 0; j < dimension; j++) {
				sum += this.getElement(i, j);
			}
			sums.add(sum);
		}
		//summing the columns
		for (int i = 0; i < dimension; i++) {
			int sum = 0;
			for(int j = 0; j < dimension; j++) {
				sum += this.getElement(j, i);
			}
			sums.add(sum);
		}
		//summing the diagonal
		int sum = 0;
		for (int i = 0; i < dimension; i++) {	
			sum += this.getElement(i, i);	
		}
		sums.add(sum);
		//count the number of times each sum occurs
		Map occurencesSums = new HashMap();
		for (Integer summ: sums) {
			if(occurencesSums.get(summ) == null) {
				occurencesSums.put(summ, 1);
			} else {
				int counter = (Integer)occurencesSums.get(summ);
				counter++;
				occurencesSums.put(summ, counter);
			}
		}
		Iterator<Integer> it = occurencesSums.keySet().iterator();
		int max = 0;
		while(it.hasNext()) {
			int currentSum = (Integer)occurencesSums.get(it.next());
			if( currentSum > max) {
				max = currentSum;
			}
		}
		return max;
		
	}
	//the rank is calculated with respect to the population, orders individuals from worse to best
	//rank = pop_size - (1 +number of individuals with more identical sums for rows, colums, the diagonal))
	int getRank(){
		List<Square> individuals = population.getIndividuals();
		int rank = population.getSize();
		for (int i = 0; i < individuals.size(); i++){
			if(individuals.get(i).getMaxNumberOfIdenticalSums() > this.getMaxNumberOfIdenticalSums()){
				rank--;
			}
		}
		return rank;
	}
	//the probability of being taken for crossover, calculated with respect to the rank
	public void setProbability(){
		int pop_size = population.getSize();
		probability = (float)this.getRank() / population.getRankSum();
	}
	public float getProbability(){
		this.setProbability();
		return probability;
	}
	public void setProbabilityCrossover(float prob){
		probability_order = prob;
	}
	public float getProbabilityCrossover(){
		return probability_order;
	}
	public String toBitString(){
		StringBuilder result = new StringBuilder();
		for(Integer number: this.elements){
			for(int i = 4; i >= 0 ; i--) {
		        int mask = 1 << i;
		        result.append((number & mask) != 0 ? "1" : "0");
		    }
		}
		return result.toString();
	} 
	public static Square bitStringToSquare(String bits){
		//presupposes that the max number is 16, that is, counts on 5 bits per number
		int n = 4;
		List<Integer> numbers = new ArrayList<Integer>();
		for(int i = 0; i < n; i++ ){
			for(int j = 0; j < n; j++) {
				int counter = (i * 4  + j) * 5;
				String bitsOneNumber = bits.substring(counter, counter + 5);
				numbers.add(Integer.parseInt(bitsOneNumber, 2));
			}	
		}
		return new Square(n, numbers);
		
	}
	public Square copy(){
		List<Integer> newElements = new ArrayList<Integer>();
		for (Integer i: this.getElements()) {
			newElements.add(i.intValue());
		}
		return new Square(this.getDimension(), newElements);
	}
	
}
