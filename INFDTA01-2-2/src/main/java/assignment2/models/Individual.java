package assignment2.models;

/**
 * Created by davey on 3/30/17.
 */
public abstract class Individual <T> {
    private T value;

    public Individual(int bits) {
        value = randomize(bits);
    }

    protected Individual(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(int bits) { value = randomize(bits); }

    protected abstract T randomize(int bits);

    protected static int getRandomBit(int position) {
        if(Math.random() < 0.5) {
            return 1 << position;
        }
        return 0;
    }

    public abstract Individual<T> breed(Individual<T> p2);

    public abstract Individual<T> mutate(double mutationRate, int bits);
}
