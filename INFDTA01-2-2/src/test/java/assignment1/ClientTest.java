package assignment1;

import assignment1.algorithm.KMeans;
import assignment1.WineDataService;
import junit.framework.Assert;
import assignment1.models.Client;
import assignment1.models.Cluster;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class ClientTest {

    private static List<Client> clients;
    private static KMeans kMeans;

    @BeforeClass
    public static void setup() throws IOException {
        clients = WineDataService.loadWineData();
        kMeans = new KMeans(clients);
    }

    @Test
    public void testLoadData() {
        Assert.assertNotNull(clients);
    }

    @Test
    public void testInitCentroidsByRandom() {
        kMeans.initCentroidsByRandom(3);
        List<Cluster> clusterCenter = kMeans.getClusters();
        Assert.assertNotNull(clusterCenter);
    }

    @Test
    public void testKMeansCalculate() {
        int clusters, iterations;

        clusters = 3;
        iterations = 20;
        kMeans.initCentroidsByRandom(clusters);
        kMeans.calculate(iterations);
        System.out.println("With " + clusters + " and " + iterations + " iterations, the sse is: " + kMeans.getSse());

        iterations = 600;
        KMeans kMeans2 = new KMeans(clients);
        kMeans2.initCentroidsByRandom(clusters);
        kMeans2.calculate(iterations);
        System.out.println("With " + clusters + " and " + iterations + " iterations, the sse is: " + kMeans2.getSse());

        clusters = 4;
        iterations = 100;
        KMeans kMeans3 = new KMeans(clients);
        kMeans3.initCentroidsByRandom(clusters);
        kMeans3.calculate(iterations);
        System.out.println("With " + clusters + " and " + iterations + " iterations, the sse is: " + kMeans3.getSse());

        WineDataService.printClusters(kMeans3.getClusters());
    }
}