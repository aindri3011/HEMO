package com.aindri.hemo.nav_package;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.aindri.hemo.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class NavAdapter  extends BaseAdapter {
ArrayList<Nav_Model> arrayList=new ArrayList<>();
Context context;

    public NavAdapter(ArrayList<Nav_Model>al_menus, Context context) {
        this.arrayList = al_menus;
        this.context = context;
    }
    private class ViewHolder{
        TextView text_listactivity;
        ImageView activityicon;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
     ViewHolder holder;
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView==null){
            holder=new ViewHolder();
            convertView=inflater.inflate(R.layout.item_slider,null);
           holder.text_listactivity=convertView.findViewById(R.id.text_listactivity);
           holder.activityicon=convertView.findViewById(R.id.activityicon);
           convertView.setTag(holder);
        }
        else
        {
            holder=(ViewHolder)convertView.getTag();
        }

        holder.text_listactivity.setText(arrayList.get(position).getActivityname());
        holder.activityicon.setImageResource(arrayList.get(position).getIcon());
        return convertView;

    }

}
