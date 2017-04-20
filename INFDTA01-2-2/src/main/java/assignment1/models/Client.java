package assignment1.models;

import java.util.Vector;

/**
 * Created by davey on 3/27/17.
 */
public class Client implements IVector {
    Vector<Double> wine = new Vector<>();

    public Client(Vector<Double> wine){ this.wine = wine; }

    public Client() { this.wine = new Vector<>(); }

    public Vector<Double> getWine () { return wine; }

    public void setWine(Vector<Double> wine) { this.wine = wine; }

    @Override
    public Vector<Double> vector() {
        return getWine();
    }
}
