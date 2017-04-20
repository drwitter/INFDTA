package assignment1;

import assignment1.models.Client;
import assignment1.models.Cluster;

import java.io.*;
import java.util.*;

/**
 * Created by davey on 3/27/17.
 */
public class WineDataService {
    public static List<Client> loadWineData() throws IOException {
        String file = "src/main/resources/WineData.csv";
        // Reads the file
        BufferedReader in = new BufferedReader(new FileReader(file));

        // Creates empty string called line (for the line of data)
        String line;
        // Creates empty arraylist for clients
        List<Client> clients = new ArrayList<>();
        // Sets firstrow to true, this way you know it is the first line
        boolean firstRow = true;
        // Put the data of each line in the string called 'line'
        while((line = in.readLine()) != null) {
            // Each item in the line will be added to an Array
            String[] item = line.split(",");
            int i = 0;
            // For every item in the line
            for (String offer : item) {
                // Create empty Client
                Client client;
                if(firstRow) {
                    // Assign a new client to the variable client
                    client = new Client();
                }else{
                    // Get the existing client details
                    client = clients.get(i);
                }
                // Get the wine data from the existing client and add the value of offer to it
                client.getWine().add(Double.valueOf(offer));
                if(firstRow) {
                    // Add to i the edited client
                    clients.add(i, client);
                }else{
                    // Change the existing i with the edited client
                    clients.set(i, client);
                }
                // Go to the next item
                i++;
            }
            firstRow = false;
        }
        return clients;
    }

    public static void printClusters(List<Cluster> clusters) {
        System.out.println("");

        for (int i = 0; i < clusters.size(); i++) {
            Cluster cluster = clusters.get(i);

            if (cluster.getClients().size() > 0) {
                TreeMap<Integer, Integer> result = new TreeMap<>();

                for (Client vector : cluster.getClients()) {
                    for (int j = 0; j < vector.getWine().size(); j++) {
                        Double value = vector.getWine().get(j);

                        if (value == 1.0) {
                            if (result.containsKey(j)) {
                                result.put(j, result.get(j) + 1);
                            } else {
                                result.put(j, 1);
                            }
                        }
                    }
                }

                SortedSet<Map.Entry<Integer, Integer>> entries = entriesSortedByValues(result);
                for (Map.Entry<Integer, Integer> entry : entries) {
                    System.out.println("OFFER: " + (entry.getKey() + 1) + " -> bought " + entry.getValue() + " times");
                }

            } else {
                System.out.println("Cluster: " + i + " has no points.");
            }

            System.out.println("");
            System.out.println("-------------------------------");
            System.out.println("");
        }
    }

    private static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
        SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<>(
                (Comparator<Map.Entry<K, V>>) (e2, e1) -> {
                    int res = e1.getValue().compareTo(e2.getValue());
                    return res != 0 ? res : 1;
                }
        );
        sortedEntries.addAll(map.entrySet());
        return sortedEntries;
    }


}
