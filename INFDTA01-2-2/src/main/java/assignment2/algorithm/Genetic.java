package assignment2.algorithm;

import assignment2.models.ByteIndividual;
import assignment2.models.Individual;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by davey on 3/30/17.
 */
public class Genetic<T> {

    private double crossoverRate;
    private double mutationRate;
    private boolean elitism;
    private int populationSize;
    private int amountOfIterations;
    private Function<T, Double> computeFitness;
    private int BIT_LENGTH;
    private List<Individual<T>> currentPopulation;



    private int j = 0 ;

    private Map<Individual<T>, Double> populationWithFitness = new HashMap<>();

    public Genetic(double crossoverRate, double mutationRate, boolean elitism, int populationSize, int amountOfIterations, Function<T, Double> computeFitness, int BIT_LENGTH) {
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitism = elitism;
        this.populationSize = populationSize;
        this.amountOfIterations = amountOfIterations;
        this.computeFitness = computeFitness;
        this.BIT_LENGTH = BIT_LENGTH;
    }

    public Genetic(double crossoverRate, double mutationRate, boolean elitism, List<Individual<T>> population, int amountOfIterations, Function<T, Double> computeFitness, int BIT_LENGTH){
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.elitism = elitism;
        this.currentPopulation = population;
        this.amountOfIterations = amountOfIterations;
        this.computeFitness = computeFitness;
        this.BIT_LENGTH = BIT_LENGTH;
        this.populationSize = population.size();
    }

    public Object[] run(){
        for ( int generation = 0 ; generation < amountOfIterations ; generation ++ ) {
            List<Double> fitnessesCurrentPopulation = new ArrayList<>();
            for(Individual<T> i : currentPopulation){
                if(i == null){
                    continue;
                }
                T d1 = i.getValue();
                Double d = computeFitness.apply(d1);
                fitnessesCurrentPopulation.add(d);
            }
            for ( int i = 0 ; i < fitnessesCurrentPopulation.size() ; i ++ ) {
                populationWithFitness.put(currentPopulation.get(i), fitnessesCurrentPopulation.get(i));
            }
            List<Individual<T>> nextPopulation = new ArrayList<>();
            if(elitism){
                nextPopulation.add(getBestIndividual(1));
            }
            List<Collection<Individual<T>>> parentsList = new ArrayList<>();
            for ( int i = 0 ; i < populationSize/2 ; i ++ ) {
                Collection<Individual<T>> parents = selectTwoParent(currentPopulation);
                parentsList.add(parents);
            }
            List<Collection<Individual<T>>> children = new ArrayList<>();
            for ( int i = 0 ; i < parentsList.size() ; i ++ ){
                double random = Math.random();
                if(elitism){
                    children.add(getChildren(parentsList.get(i)));
                }else{
                    children.add(getChildren(parentsList.get(i)));
                }
            }
            for ( int i = 0 ; i < children.size() ; i ++ ){
                Iterator<Individual<T>> iter = children.get(i).iterator();
                while(iter.hasNext()){
                    Individual<T> indi1 = iter.next();
                    Individual<T> indi = indi1.mutate(mutationRate, BIT_LENGTH);
                    nextPopulation.add(indi);
                }
            }
            currentPopulation = nextPopulation;
        }
        List<Double> finalFitnesses = currentPopulation.stream().map(individual -> computeFitness.apply(individual.getValue())).collect(Collectors.toList());
        return getBestIndividual(currentPopulation, finalFitnesses);
    }

    private Individual<T> tournamentSelection(List<Individual<T>> currentPopulation, Individual<T> skip){
        double bestFitness = -99999;
        Individual<T> bestIndividual = null;
        for ( int i = 0 ; i < (new Random().nextInt(currentPopulation.size()-3)+2) ; i ++ ){
            int random = new Random().nextInt(populationSize);
            Individual<T> currentIndividual = currentPopulation.get(random);
            if(currentIndividual == null){
                continue;
            }
            double tempFitness = computeFitness.apply(currentPopulation.get(random).getValue());
            if(currentIndividual != skip){
                if(tempFitness > bestFitness){
                    bestIndividual = currentIndividual;
                    bestFitness = tempFitness;
                }
            }
        }
        return bestIndividual;
    }

    private Individual<T> getBestIndividual(int number) {
        Individual<T> bestIndividual = null;
        Double bestFitness = 0.0;

        for (Individual<T> individual : populationWithFitness.keySet()) {
            Double fitness = populationWithFitness.get(individual);

            if (fitness > bestFitness) {
                bestIndividual = individual;
                bestFitness = fitness;
            }
        }

        return bestIndividual;
    }

    private Object[] getBestIndividual(List<Individual<T>> currentPopulation, List<Double> finalFitnesses) {
        Individual<T> bestIndividual = null;
        Double bestFitness = -999999.0;
        double sumFitness = 0.0;

        for (int i = 0; i < finalFitnesses.size(); i++) {
            Double fitness = finalFitnesses.get(i);
            sumFitness += fitness;

            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestIndividual = currentPopulation.get(i);
            }
        }

        return new Object[]{bestIndividual, bestFitness, sumFitness / finalFitnesses.size()};
    }

    private Collection<Individual<T>> getChildren ( Collection<Individual<T>> parents ) {
        Iterator<Individual<T>> iter = parents.iterator();
        List<Individual<T>> parentsList = new ArrayList<>();
        while(iter.hasNext()){
            parentsList.add(iter.next());
        }
        Individual<T> p1 = parentsList.get(0);
        Individual<T> p2 = parentsList.get(1);
        if(p1 == null || p2 == null){
            p1 = tournamentSelection(currentPopulation, null);
            p2 = tournamentSelection(currentPopulation, p1);
        }
        if(Math.random() < crossoverRate) {
            return Arrays.asList(p1.breed(p2), p2.breed(p1));
        }
        return Arrays.asList(p1, p2);
    }

    private Collection<Individual<T>> selectTwoParent(List<Individual<T>> currentPopulation) {
        Individual<T> parent1 = tournamentSelection(currentPopulation, null);
        Individual<T> parent2 = tournamentSelection(currentPopulation, parent1);
        return Arrays.asList(parent1, parent2);
    }

}
