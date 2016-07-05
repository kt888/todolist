package com.example.karpurus.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

 /**
 * Created by karpurus on 6/30/2016.
 */
public class ItemAdapter extends ArrayAdapter<toDoItem> {
    public ItemAdapter(Context context, ArrayList<toDoItem> items) {
            super(context, R.layout.item_task, items);
            }

     @Override
     public View getView(int position, View convertView, ViewGroup parent) {
         // Get the data item for this position
         toDoItem item = getItem(position);
         // Check if an existing view is being reused, otherwise inflate the view
         if (convertView == null) {
             convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_task, parent, false);
         }
         // Lookup view for data population
         TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
         TextView itemPriority = (TextView) convertView.findViewById(R.id.itemPriority);
         TextView itemDate = (TextView) convertView.findViewById(R.id.itemDate);
         // Populate the data into the template view using the data object
         itemName.setText(item.itemName);
         itemPriority.setText(item.priority);
         itemDate.setText(item.dueDate);
         // Return the completed view to render on screen
         return convertView;
     }


 }