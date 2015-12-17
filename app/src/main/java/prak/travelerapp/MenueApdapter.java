package prak.travelerapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenueApdapter extends BaseAdapter {
    private Context context;
    String[] menue_links;

    int[] images = {R.mipmap.ic_launcher,R.mipmap.ic_home,R.mipmap.ic_check,R.mipmap.ic_new,
            R.mipmap.ic_clock,R.mipmap.ic_settings,R.mipmap.ic_home,
            R.mipmap.ic_home,R.mipmap.ic_home};

    public MenueApdapter(Context context){
        menue_links=context.getResources().getStringArray(R.array.menue_links);
        this.context = context;
    }

    @Override
    public boolean isEnabled(int position) {
        if (position == 0){
            return false;
        } else {
            return true;
        }
    }

    @Override
    public int getCount() {
        return menue_links.length;
    }

    @Override
    public Object getItem(int position) {
        return menue_links[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (position == 0){
                row = inflater.inflate(R.layout.custom_row_first,parent,false);
            } else {
                row = inflater.inflate(R.layout.custom_row,parent,false);
            }
        } else {
            row = convertView;
        }
        TextView textView = (TextView) row.findViewById(R.id.textView_Row);
        ImageView imageView = (ImageView) row.findViewById(R.id.imageView_Row);

        textView.setText(menue_links[position]);
        imageView.setImageResource(images[position]);

        return row;
    }
}