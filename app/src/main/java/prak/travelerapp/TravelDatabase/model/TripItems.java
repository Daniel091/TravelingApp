package prak.travelerapp.TravelDatabase.model;

import java.util.ArrayList;
import java.util.List;

public class TripItems {
    private List<Tupel> items_list;

    public TripItems(String items){
        this.items_list = parseItems(items);

    }

    public TripItems(List<Tupel> items_list){
        this.items_list = items_list;
    }

    @Override
    public String toString(){
        String s = null;
        for(int i=0;i<items_list.size();i++){
            if(i < items_list.size()-1){
                s = s.concat(items_list.get(i).toString()+ ",");
            }else{
                s = s.concat(items_list.get(i).toString());
            }
        }
        return s;
    }

    private List<Tupel> parseItems(String items) {
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

}
