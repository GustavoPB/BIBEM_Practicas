import java.util.ArrayList;
import java.util.List;


public class Population {
	int population_size;
	List<Square> squares = null;
	public Population(){
		population_size = 0;
		squares = new ArrayList<Square>();
	}
	public Population(int pop_size){
		population_size = pop_size;
		squares = new ArrayList<Square>();
		for (int i = 0; i < population_size; i++) {
			squares.add(new Square(4));
		}
	}
	public void addIndividual(Square sq){
		squares.add(sq);
		population_size++;
	}
	public int getSize(){
		return population_size;
	}
	public List<Square> getIndividuals(){
		return squares;
	}
	public List<Square> getBestIndividuals() {
		List<Square> bestIndividuals = new ArrayList<Square>();
		List<Square> population = getIndividuals();
		int bestRank = population.get(0).getRank();//consigo el rango del primero y lo comparo con el rango de cada individuo siempre quedandome con el mejor
		for(int i = 1; i < population_size; i++){
			if(population.get(i).getRank() > bestRank) {
				bestRank = population.get(i).getRank();
			}
		}
		
		for(int i = 0; i < population_size; i++){
			if(population.get(i).getRank() == bestRank) {
				bestIndividuals.add(population.get(i));
			}
		}
		return bestIndividuals;
	}
	public void setCrossoverProbabilities(){
		List<Square> individuals = this.getIndividuals();
		for(Square sq: individuals){
			sq.setProbability();
		}
		float previous_probability = 0;
		for(int i = 0; i < individuals.size(); i++){
			previous_probability += individuals.get(i).getProbability();
			individuals.get(i).setProbabilityCrossover(previous_probability);
		}
		
	}
	public int getRankSum(){
		int sum = 0;
		for(Square sq: this.getIndividuals()){
			sum += sq.getRank();
		}
		return sum;
	}
	public void print(){
		List<Square> squares = this.getIndividuals();
 		for(Square square: squares) {
			System.out.println(square);
			System.out.println(square.getMaxNumberOfIdenticalSums());
			System.out.println(square.getRank());
			System.out.println(square.getProbabilityCrossover());
			System.out.println("--------------");
		}
 		System.out.println("==============");
	}
	
}
