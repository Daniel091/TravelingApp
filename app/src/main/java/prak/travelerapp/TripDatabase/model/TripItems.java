package prak.travelerapp.TripDatabase.model;

import java.util.ArrayList;
import java.util.List;

public class TripItems {
    private ArrayList<Tupel> items_list;

    public TripItems(String items){
        this.items_list = parseItems(items);

    }

    public TripItems(){
        this.items_list= new ArrayList<>();
    }

    public void addItem(int id){

        Tupel item = new Tupel(id,0);
        items_list.add(item);

    }

    public Tupel getItem(int id){

        for(Tupel item : items_list){
            if(id == item.getX()){
                return item;
            }
        }
        return null;
    }

    public ArrayList<Tupel> getItems(){
        return this.items_list;
    }

    public String makeString(){
        String s = "";
        for(int i=0;i<items_list.size();i++){
            if(i < items_list.size()-1){
                s = s + items_list.get(i).toString()+ ";";
            }else{
                s = s + items_list.get(i).toString();
            }
        }
        return s;
    }

    private ArrayList<Tupel> parseItems(String items) {
        items_list = new ArrayList<>();
        String[] array = items.split(";");

        for(String s : array){
            int x,y;
            s = s.substring(1, s.length() - 1);
            String[] values = s.split(",");

            x = Integer.parseInt(values[0]);
            y = Integer.parseInt(values[1]);

            Tupel tupel = new Tupel(x,y);
            items_list.add(tupel);
        }

        return items_list;
    }

    //mark all items as unchecked
    public void cleanItems(){
        for(Tupel item : items_list){
            item.setY(0);
        }

    }

}
