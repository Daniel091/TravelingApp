package prak.travelerapp.ItemList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import prak.travelerapp.R;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ListItem>> _listDataChild;
    public ItemCheckedListener listener;
    private TextView checkedItems;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<ListItem>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public ListItem getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    static class ViewHolder {
        protected TextView text;
        protected CheckBox checkbox;
    }

    /**
     * Liefert index der Gruppe in der sich ein item befindet
     * @param id id des gesuchten items
     * @return
     */
    public int getGroupPositionForItem(int id){
        String groupName = "";
        Iterator<Map.Entry<String, List<ListItem>>> it = _listDataChild.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ListItem>> pair = it.next();
            List<ListItem> childs = pair.getValue();
            for(ListItem child : childs){
                if(child.getId() == id){
                    groupName=pair.getKey();
                }
            }
            for(int i = 0; i< _listDataHeader.size(); i++){
                if(groupName.equals(_listDataHeader.get(i))){
                    return i;
                }
            }
            }
        return -1;
    }

    /**
     * Liefert child index innerhalb einer Gruppe für ein item
     * @param id id des gesuchten items
     * @return
     */
    public int getChildPositionForItem(int id){
        Iterator<Map.Entry<String, List<ListItem>>> it = _listDataChild.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<ListItem>> pair = it.next();
            List<ListItem> childs = pair.getValue();
            for(int i= 0; i<childs.size();i++){
                if(childs.get(i).getId() == id){
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        View view = null;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.checkbox = (CheckBox) view.findViewById(R.id.checkBox);
            viewHolder.text = (TextView) view.findViewById(R.id.lblListItem);
            viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ListItem item = (ListItem) viewHolder.checkbox.getTag();
                    item.setChecked(buttonView.isChecked());
                    listener.itemClicked(item);
                }
            });

            //verhindere, dass die view den longclick empfängt-> liste empfängt longclick
            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }

                });
            view.setTag(viewHolder);
            viewHolder.checkbox.setTag(getChild(groupPosition, childPosition));
        }else{
            view = convertView;((ViewHolder) view.getTag()).checkbox.setTag(getChild(groupPosition, childPosition));
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.text.setText(getChild(groupPosition, childPosition).getName());
        holder.checkbox.setChecked(getChild(groupPosition, childPosition).isChecked());
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        checkedItems = (TextView) convertView.findViewById(R.id.checkedItems);
        setUpCheckedItems(groupPosition);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        //lblListHeader.setTypeface(null, Typeface.NORMAL);
        lblListHeader.setText(headerTitle.toUpperCase());

        return convertView;
    }

    public void setUpCheckedItems(int groupPosition) {
        String group_name = (String) getGroup(groupPosition);
        List<ListItem> group_items = _listDataChild.get(group_name);
        int unchecked_items = 0;
        for (ListItem item : group_items){
            if (!item.isChecked()){
                unchecked_items++;
            }
        }
        checkedItems.setText(String.valueOf(unchecked_items));
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}