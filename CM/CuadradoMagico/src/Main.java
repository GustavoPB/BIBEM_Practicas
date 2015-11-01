import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;
public class Main {
	public static int population_size;
	public static int max_nr_iterations;
	public static void main(String[] args) {
		
		/*
		Population pop = new Population();
		pop.addIndividual(new Square(4));
		pop.addIndividual(new Square(4));
		List<Square> squares = pop.getIndividuals();
		for(Square sq: squares) {
			sq.setPopulation(pop);
		}
		pop.setCrossoverProbabilities();
		System.out.println(pop.getIndividuals().get(0).getRank());
		System.out.println(pop.getIndividuals().get(1).getRank());
		System.out.println(pop.getIndividuals().get(0).getProbabilityCrossover());
		System.out.println(pop.getIndividuals().get(1).getProbabilityCrossover());
		*/
		
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Population size?");
		population_size = sc.nextInt();
		System.out.println("Max number of iterations?");
		max_nr_iterations = sc.nextInt(); 
		
		GA geneticAlgorithm = new GA();
		geneticAlgorithm.intializePopulation(population_size);
		geneticAlgorithm.printPopulation();
		System.out.println("****");
	
		int nr_iterations = 0;
		while(nr_iterations < max_nr_iterations){
			if(geneticAlgorithm.stopCondition()) {
				break;
			}
			geneticAlgorithm.renewPopulation();
//			geneticAlgorithm.printPopulation();
			nr_iterations++;
		}
		geneticAlgorithm.printPopulation();
		List<Square> squaresSatisfy = new ArrayList<Square>();
		System.out.println();
		geneticAlgorithm.printPopulation();
		System.out.println("Solutions:");
		for (Square sq: squaresSatisfy) {
			System.out.println(sq);
		}
		
	}
	

}
