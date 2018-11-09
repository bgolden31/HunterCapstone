package com.example.kchu0.test;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by quocnguyen on 03/08/2016.
 */
public class CustomListAdapter extends ArrayAdapter<Recipe> {

    ArrayList<Recipe> recipes;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<Recipe> recipes) {
        super(context, resource, recipes);
        this.recipes = recipes;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom_list_layout, null, true);

        }
        Recipe recipe = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        //Picasso.with(context).load(product.getImage()).into(imageView);
        String img = recipe.getImage();
        Picasso.get().load(img).into(imageView);

        TextView label = (TextView) convertView.findViewById(R.id.label);
        label.setText(recipe.getLabel());

        TextView source = (TextView) convertView.findViewById(R.id.source);
        source.setText(recipe.getSource());

        TextView rate = (TextView) convertView.findViewById(R.id.yield);
        rate.setText(recipe.getCalories());

        return convertView;
    }
}
