package assignment2;

import assignment2.algorithm.Genetic;
import assignment2.models.ByteIndividual;
import assignment2.models.Individual;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Exercise2{
    private static final int BITS = 7;
    private static final int SMALL_MOUTH = 1;
    private static final int IMPROVED_SENSES = 1 << 1;
    private static final int AGILITY = 1 << 2;
    private static final int LARGER_BRAIN = 1 << 3;
    private static final int HARD_SKIN = 1 << 4;
    private static final int LEG_MUSCLES = 1 << 5;
    private static final int LONG_NECK = 1 << 6;

    public static void main(String[] args) {
        double crossoverRate = 0.85;//Double.parseDouble(args[0]);
        double mutationRate = 0.01;//Double.parseDouble(args[1]);
        boolean elitism = true;//Boolean.parseBoolean(args[2]);
        int populationSize = 10;//Integer.parseInt(args[3]);
        int numIterations = 100;//Integer.parseInt(args[4]);
        if(populationSize < 2) {
            System.err.println("Not enough possible parents");
            return;
        }
        List<Individual<Byte>> population = initPopulation(populationSize);
        Function<Byte, Double> fitnessAlgorithm = in -> {
            double a = 10;
            double b = 100;
            double c = 10;
            double d = 50;
            double e = 2;
            double f = 5;
            int val = in & 0xFF;
            if((val & SMALL_MOUTH) == SMALL_MOUTH) {
                b *= 0.1;
                c *= 5;
            }
            if((val & IMPROVED_SENSES) == IMPROVED_SENSES) {
                a *= 2.5;
                b *= 2.5;
                c *= 0.5;
            }
            if((val & AGILITY) == AGILITY) {
                e *= 2.5;
                f *= 0.1;
            }
            if((val & LARGER_BRAIN) == LARGER_BRAIN) {
                c *= 0.2;
                e *= 1.75;
                f *= 0.35;
            }
            if((val & HARD_SKIN) == HARD_SKIN) {
                a *= 1.75;
                e *= 3;
                f *= 0.5;
            }
            if((val & LEG_MUSCLES) == LEG_MUSCLES) {
                a *= 0.3;
                e *= 3;
            }
            if((val & LONG_NECK) == LONG_NECK) {
                b *= 4;
                d *= 3.5;
            }
            return (b / (a + c + d)) - e - (f / d);
        };
        Genetic algorithm = new Genetic(crossoverRate, mutationRate, elitism, population, numIterations, fitnessAlgorithm, BITS);
        Object[] result = algorithm.run();
        Individual individual = (Individual) result[0];
        Byte valueByte = (byte)individual.getValue();
        String valueString = Byte.toString(valueByte);
        int value = Integer.parseInt(valueString);
        System.out.println(individual.getValue());
        System.out.println("Best fitness: "+ result[1]);
        System.out.println("Average fitness: "+result[2]);
        System.out.println(Integer.toBinaryString(value));
    }

    private static List<Individual<Byte>> initPopulation(int size) {
        List<Individual<Byte>> population = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            population.add(new ByteIndividual(BITS));
        }
        return population;
    }
}






























    /*private static final int BITS = 7;
    private static final int SMALL_MOUTH = 1;
    private static final int IMPROVED_SENSES = 1 << 1;
    private static final int AGILITY = 1 << 2;
    private static final int LARGER_BRAIN = 1 << 3;
    private static final int HARD_SKIN = 1 << 4;
    private static final int LEG_MUSCLES = 1 << 5;
    private static final int LONG_NECK = 1 << 6;

    public static void main(String[] args) {
        double crossoverRate = 1;//Double.parseDouble(args[0]);
        double mutationRate = 1;//Double.parseDouble(args[1]);
        boolean elitism = true;//Boolean.parseBoolean(args[2]);
        int populationSize = 1;//Integer.parseInt(args[3]);
        int numIterations = 1;//Integer.parseInt(args[4]);
        if(populationSize < 2) {
            System.err.println("Not enough possible parents");
            return;
        }
        List<Individual<Byte>> population = initPopulation(populationSize);
        Function<Byte, Double> fitnessAlgorithm = in -> {
            double a = 10;
            double b = 100;
            double c = 10;
            double d = 50;
            double e = 2;
            double f = 5;
            int val = in & 0xFF;
            if((val & SMALL_MOUTH) == SMALL_MOUTH) {
                b *= 0.1;
                c *= 5;
            }
            if((val & IMPROVED_SENSES) == IMPROVED_SENSES) {
                a *= 2.5;
                b *= 2.5;
                c *= 0.5;
            }
            if((val & AGILITY) == AGILITY) {
                e *= 2.5;
                f *= 0.1;
            }
            if((val & LARGER_BRAIN) == LARGER_BRAIN) {
                c *= 0.2;
                e *= 1.75;
                f *= 0.35;
            }
            if((val & HARD_SKIN) == HARD_SKIN) {
                a *= 1.75;
                e *= 3;
                f *= 0.5;
            }
            if((val & LEG_MUSCLES) == LEG_MUSCLES) {
                a *= 0.3;
                e *= 3;
            }
            if((val & LONG_NECK) == LONG_NECK) {
                b *= 4;
                d *= 3.5;
            }
            return b / (a + c + d) - e - f / d;
        };
        Genetic algorithm = new Genetic(crossoverRate, mutationRate, elitism, population, numIterations, fitnessAlgorithm, BITS);
        Object[] result = algorithm.run();
        Individual individual = (Individual) result[0];
        System.out.println(individual.getValue());
        System.out.println("Best fitness: "+ result[1]);
        System.out.println("Average fitness: "+result[2]);
    }

    private static List<Individual<Byte>> initPopulation(int size) {
        List<Individual<Byte>> population = new ArrayList<>(size);
        for(int i = 0; i < size; i++) {
            population.add(new ByteIndividual(BITS));
        }
        return population;
    }
}*/