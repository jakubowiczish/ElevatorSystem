package elevatorsystem.model;

/**
 * Pair class for storing some information
 *
 * @param <F> first parameter
 * @param <S> second parameter
 */
public class Pair<F, S> {
    private F first;
    private S second;


    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }


    public F getFirst() {
        return first;
    }


    public S getSecond() {
        return second;
    }


    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}