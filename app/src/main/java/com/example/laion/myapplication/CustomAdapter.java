package com.example.laion.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter {

    private ArrayList dataSet;
    Context mContext;

    private static class ViewHolder {
        TextView txtName;
        TextView txtName1;
        CheckBox checkBox;
    }

    public CustomAdapter(ArrayList data, Context context) {
        super(context, R.layout.item_lista, data);
        this.dataSet = data;
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return dataSet.size();
    }

    @Override
    public Estrutura_Lista getItem(int position) {
        return (Estrutura_Lista) dataSet.get(position);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.txtName1 = (TextView) convertView.findViewById(R.id.txtName1);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            result=convertView;
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Estrutura_Lista item = getItem(position);
        viewHolder.txtName.setText(item.nome);
        viewHolder.txtName1.setText(item.data);
        if(item.checked){  //parte em que risca o texto
            viewHolder.txtName.setPaintFlags(viewHolder.txtName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.txtName1.setPaintFlags(viewHolder.txtName1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.txtName.setPaintFlags(0);
            viewHolder.txtName1.setPaintFlags(0);
        }
        viewHolder.checkBox.setChecked(item.checked);

        if(item.isItemSelected){
            result.setBackgroundColor(Color.LTGRAY);
        } else{
            result.setBackgroundColor(Color.TRANSPARENT);
        }

        return result;
    }
}