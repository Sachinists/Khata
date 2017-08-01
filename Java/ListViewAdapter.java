package com.example.hariom.khata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    ArrayList<String> arrayList;
    public ListViewAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        arrayList=(ArrayList<String>)objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=convertView;
        if(v==null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            v = inflater.inflate(R.layout.transaction_listview, null);
        }
        ViewHolder holder;
        if (convertView == null)
        {
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) v.findViewById(R.id.textView_date);
            holder.txtViewDescription = (TextView) v.findViewById(R.id.textView_transaction);
            holder.reason=(TextView)v.findViewById(R.id.textview_reason_display);
            v.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) v.getTag();
        }
        try {
            String str= arrayList.get(position);
            String paisa=str.substring(0,str.indexOf('$'));
            String date=str.substring(str.indexOf('$')+1,str.indexOf('@'));
            String reason=str.substring(str.indexOf('@')+1);
            holder.txtViewTitle.setText(date);
            holder.txtViewDescription.setText(paisa);
            int p=Integer.valueOf(paisa);
            if(p>0){
                holder.txtViewDescription.setTextColor(Color.parseColor("#318d01"));
            }else{
                holder.txtViewDescription.setTextColor(Color.RED);
            }
            holder.reason.setText(reason);
        }catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return v;
    }
    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView reason;
    }
}
