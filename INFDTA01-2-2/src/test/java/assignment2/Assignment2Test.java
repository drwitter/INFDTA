package assignment2;

import assignment2.algorithm.Genetic;
import assignment2.models.ByteIndividual;
import assignment2.models.Individual;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class Assignment2Test {

    public static final Function<Byte, Double> aFakeProblem = b -> {
        int x = b.intValue();
        double y = -Math.pow(x,2)+7*x;
        return y;
    };

    public static Genetic genetic;

    @BeforeClass
    // public static void init() { genetic = new Genetic(crossover, mutation, elitism, population, number of iter, function, bit_length); }
    public static void init() {
        List<Individual<Byte>> currentPopulation = initPopulation(); genetic = new Genetic(0.85, 0.01, true, currentPopulation, 50, aFakeProblem, 5);
    }

    public static List<Individual<Byte>> initPopulation() {
        Random randomClass = new Random();
        List<Individual<Byte>> initialPopulation = new ArrayList<>();
        for( int i = 0 ; i < 10 ; i ++ ) {
            initialPopulation.add(new ByteIndividual(randomClass.nextInt(32)));
        }
        return initialPopulation;
    }

    @Test
    public void test() {
        Individual individual = new ByteIndividual(2);
        Individual individual2 = new ByteIndividual(9);

//        System.out.println(individual.getByteString());
//        System.out.println(individual2.getByteString());
        Assert.assertNotNull(individual);
    }

    @Test
    public void testGenetic() {
        Object[] result = genetic.run();

        Individual individual = (Individual) result[0];
        System.out.println(individual.getValue());

        System.out.println("Best fitness: " + result[1]);

        System.out.println("Average fitness: " + result[2]);
    }
}