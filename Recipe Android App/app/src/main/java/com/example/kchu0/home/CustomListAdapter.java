/**
 * Created by quocnguyen on 03/08/2016.
 */

package com.example.kchu0.home;

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

public class CustomListAdapter extends ArrayAdapter<Recipe> {

    /**
     * Variables used to initiate the Custom Listview
    */
    ArrayList<Recipe> recipes;
    Context context;
    int resource;

    public CustomListAdapter(Context context, int resource, ArrayList<Recipe> recipes) {
        super(context, resource, recipes);
        this.recipes = recipes;
        this.context = context;
        this.resource = resource;
    }

    /**
     * Functio used to change the normal Listview, and adds an image, and three
     * rows of text as display.
     * @param  int position that is used to determine which item on listview was clicked.
     * @param  View used to modify the view for the activity that were currently on
     * @param  Viewgroup parent, no specific function. Needed for function call.
     * @return View Returns the new listview based of the specific item value returned.
     **/
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
        source.setText(recipe.getAuthor());

        TextView rate = (TextView) convertView.findViewById(R.id.yield);
        String negative_one = Integer.toString(-1);
        if(negative_one.equals(Integer.toString(recipe.getRating() ) ) ) {
            rate.setText("Rating: No Rating");
        } else {
            rate.setText("Rating: " + Integer.toString(recipe.getRating()));
        }

        return convertView;
    }
}
