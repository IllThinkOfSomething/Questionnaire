package com.example.and_questionaire.model.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.and_questionaire.views.PackActivity;
import com.example.and_questionaire.R;

import java.util.List;
import java.util.Random;

public class SelectionGridViewAdapter extends BaseAdapter
{
    private List<String> sList; // Selection List

    public SelectionGridViewAdapter(List<String> sList) {
        this.sList = sList;
    }

    @Override
    public int getCount() {
        return sList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v;

        if(convertView == null)
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.selection_grid_view_item_layout, parent, false);
        else
            v = convertView;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), PackActivity.class);
                intent.putExtra("Selection", sList.get(position));
                intent.putExtra("SELECTION_ID", position + 1); //+1 to pass position
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) v.findViewById(R.id.selection_name)).setText(sList.get(position));
        Random rnd = new Random();
        int selectionColor = Color.argb(255, rnd.nextInt(180), rnd.nextInt(180), rnd.nextInt(255));
        v.setBackgroundColor(selectionColor);
        return v;

    }
}
