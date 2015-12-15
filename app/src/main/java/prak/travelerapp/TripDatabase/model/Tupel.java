package prak.travelerapp.TripDatabase.model;

public class Tupel{
    private final int x;
    private final int y;

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

    @Override
    public String toString() {
        return "(" + Integer.toString(x) + "," + Integer.toString(y) + ")";
    }
}
