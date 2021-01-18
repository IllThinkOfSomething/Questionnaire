package com.example.and_questionaire.model.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.and_questionaire.views.QuestionnaireActivity;
import com.example.and_questionaire.R;

public class PackAdapter extends BaseAdapter {


    private int numberOfPacks;

    public PackAdapter(int numberOfPacks) {
        this.numberOfPacks = numberOfPacks;
    }

    @Override
    public int getCount() {
        return numberOfPacks;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View v;

        if(convertView == null)
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pack_grid_view_item_layout, parent, false);

        else
            v = convertView;

        //Call Questionnaire
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), QuestionnaireActivity.class);
                intent.putExtra("PACK_NR", position + 1);
                parent.getContext().startActivity(intent);
            }
        });

        ((TextView) v.findViewById(R.id.pack_view_item_text)).setText(String.valueOf(position + 1)); // First element starts from 1

        return v;
    }
}
