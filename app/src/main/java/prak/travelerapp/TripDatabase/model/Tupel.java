package prak.travelerapp.TripDatabase.model;

public class Tupel implements Comparable<Tupel>{
    private final int x;
    private int y;

    public Tupel(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setY(int y){
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + Integer.toString(x) + "," + Integer.toString(y) + ")";
    }

    @Override
    public int compareTo(Tupel another) {
        return Integer.compare(this.x,another.getX());
    }
}
