package assignment1.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by davey on 3/27/17.
 */
public class Cluster implements IVector {
    private Client centroid;
    private List<Client> clients;
    private Client position;

    public Cluster(Client centroid) {
        this.centroid = centroid;
        clients = new ArrayList<>();
    }

    public Cluster(Client centroid, List<Client> clients) {
        this.centroid = centroid;
        this.clients = clients;
    }

    public Client getPosition() {
        return position;
    }

    public Client getCentroid() {
        return centroid;
    }

    public int size() { return clients.size(); }

    public void setCentroid(Client centroid) {
        this.centroid = centroid;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClient(List<Client> client) {
        this.clients = client;
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void clearCluster() {
        clients.clear();
    }

    @Override
    public Vector<Double> vector() {
        return centroid.getWine();
    }

}
