package assignment1.algorithm;

import assignment1.models.Client;
import assignment1.models.Cluster;
import assignment1.models.IVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by davey on 3/27/17.
 */
public class KMeans {
    private List<Client> clients;
    private List<Cluster> clusters;
    private static double sse;
    private int number = 0;
    public KMeans(List<Client> clients) {
        this.clients = clients;
        clusters = new ArrayList<>();
        sse = 0.0;
    }

    public void calculate(int amountOfIterations) {
        // Set isChanged to true, to check if the center is changed, if not, stop the iteration
        boolean isChanged = true;
        // Set i to 0, check the number of iterations
        int i = 0;
        while (isChanged && i < amountOfIterations) {
            // --- Call function setCentroids() ---
            setCentroids();
            // clusters.forEach(Cluster::cleacCluster) --> loop through the list and call clearCluster for each in the list
            clusters.forEach(Cluster::clearCluster);
            // Set the change of the center to false
            isChanged = false;
            // Set sse to 0.0
            sse = 0.0;
            // For every client in the list with clients (eg: 100 clients)
            for (Client client : clients) {
                // Set smallestDistance to 99.9
                double smallestDistance = Double.POSITIVE_INFINITY;
                // Initialize closestCluster, but set it value to null
                Cluster closestCluster = null;
                // For every cluster in the clusterslist (for example 3 clusters)
                for(Cluster cluster : clusters) {
                    // Get the euclidean distance between the clusters centroid and the vector of the point
                    Double euclideanDistance = getEuclideanDistance(cluster.getCentroid(), client);
                    number++;
                    // If the smallestDistance (99.9) is bigger than the euclidean distance
                    if (smallestDistance > euclideanDistance){
                        // Set the smallestDistance the same as the euclideanDistance
                        smallestDistance = euclideanDistance;
                        // The closestCluster is the same as this cluster
                        closestCluster = cluster;
                        // Set the ischanged value to true (there is a change)
                        isChanged = true;
                    }
                }

                // If closesCluster is not null, if it contains a value
                if (closestCluster != null) {
                    // Add the vector of this points to the closestCluster point
                    closestCluster.addClient(client);
                    // Sum the current value of sse to the smallestDistance
                    sse += smallestDistance;
                }
            }
            // Add 1 to the value i
            i++;
        }
    }

    public Double getEuclideanDistance(Client v1, Client v2){
        double distance = 0.0;

        for( int i = 0 ; i < v1.getWine().size() ; i ++ ){
            distance += Math.pow((v2.getWine().get(i) - v1.getWine().get(i)), 2);
        }
        return distance;
    }

    private void setCentroids() {
        // For every cluster (3 clusters)
        for ( Cluster cluster : clusters ) {
            // Initiate centroid to an empty vector
            Vector<Double> centroid = new Vector<>();
            // For every wine offer (32)
            for( int i = 0 ; i < clients.get(0).vector().size() ; i ++ ){
                double sum = 0.0;
                // Loop through the client list (the amount of clients for each cluster)
                for( Client vector : cluster.getClients() ) {
                    // Add the value 1 or 0 (bought or not) for each client in the cluster to the sum
                    sum += vector.getWine().get(i);
                }
                // Add to the centroid the amount of each offer bought for each cluster divided by
                // The number of clients
                centroid.add(sum / cluster.getClients().size());
            }
            // If the size of clients is not 0, so more than 1 client in a cluster
            if ( cluster.getClients().size() > 0 ) {
                // Set the list of centroids of each cluster to the centroid lister here created
                cluster.setCentroid(new Client(centroid));
            }
        }
    }

    public void initCentroidsByRandom ( int amountOfClusters ) {
        clusters.clear();
        List<Cluster> clusterCenters = new Vector<>();
        Random randomClass = new Random();
        for ( int i = 0 ; i < amountOfClusters ; i ++ ) {
            int random = randomClass.nextInt(clients.size());
            clusterCenters.add(new Cluster(clients.get(random)));
        }
        clusters = clusterCenters;
    }

    public List<? extends IVector> getClients() {
        return clients;
    }

    public List<Cluster> getClusters() {
        return clusters;
    }

    public double getSse() {
        return sse;
    }
}
