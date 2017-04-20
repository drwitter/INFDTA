package assignment1;

import assignment1.algorithm.KMeans;
import assignment1.models.Client;
import assignment1.models.Cluster;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

public class Exercise1 {
    public static List<String> stringList = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        int k = 35;
        int numIterations = 15;
        List<Client> clients = parseClients(new File("src/main/resources/a2.txt"));
        KMeans kMeans = new KMeans(clients);
        kMeans.initCentroidsByRandom(k);
        kMeans.calculate(numIterations);
        System.out.println("SSE = "+kMeans.getSse());
        System.out.println(" ----- ");
        for(Cluster c : kMeans.getClusters()) {
            postProces(c);
        }
        writeToFile(stringList);
    }

    public static List<Client> parseClients(File file) throws FileNotFoundException{
        List<Client> clients = new ArrayList<>();
        try(Scanner sc = new Scanner(file)) {
            while(sc.hasNextInt()) {
                Vector<Double> c = new Vector<>();
                c.add(sc.nextDouble());
                c.add(sc.nextDouble());
                clients.add(new Client(c));
            }
        }
        return clients;
    }

    public static void postProces(Cluster c){
        int[] count = new int[c.getCentroid().getWine().size()];
        for(Client v : c.getClients()) {
            for(int i = 0; i < count.length; i++) {
                if(v.getWine().get(i) > 0) {
                    count[i]++;
                }
            }
        }
        System.out.println("Points: " + c.size());
        stringList.add("Points: "+c.size());
        for(int i = 0; i < count.length; i++) {
            stringList.add((i+1)+" was bought "+count[i]+" times");
            System.out.println((i + 1) + " was bought " + count[i] + " times");
        }
        stringList.add(" ----- ");
        System.out.println();
    }

    public static void writeToFile(List<String> stringList){
        try{
            PrintWriter writer = new PrintWriter("src/main/resources/output.txt", "UTF-8");
            for(String string : stringList){
                writer.println(string);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error during writing to file");
        }


        //OutputStreamWriter osw = new OutputStreamWriter(new BufferedOutputStream(output));
    }


}











    /*
        KMeans kMeans = new KMeans(points);
        kMeans.initCentroidsByRandom(numClusters);
        kMeans.calculate(numIterations);
        System.out.println("SSE: "+ kMeans.getSse());
        System.out.println();
        for(Cluster c : kMeans.getClusters()) {
            postProcess(c);
        }
    }

    private static void postProcess(Cluster c) {
        int[] count = new int[c.getCentroid().getWine().size()];
        for(Client v : c.getClients()) {
            for(int i = 0; i < count.length; i++) {
                if(v.getWine().get(i) > 0) {
                    count[i]++;
                }
            }
        }
        System.out.println("Clients: " + c.size());
        for(int i = 0; i < count.length; i++) {
            System.out.println((i + 1) + " was bought " + count[i] + " times");
        }
        System.out.println();
    }*/
