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
        if (this.x == another.getX()){
            return 0;
        } else if (this.x > another.getX()){
            return 1;
        } else if (this.x < another.getX()){
            return -1;
        }
        return this.compareTo(another);
    }
}
