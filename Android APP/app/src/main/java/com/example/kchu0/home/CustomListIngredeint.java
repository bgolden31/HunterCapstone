/**
 * Created by quocnguyen on 03/08/2016.
 * Modified input by Kenny Chu to adapt to new custom list format that was needed.
 */

package com.example.kchu0.home;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomListIngredeint extends ArrayAdapter<Ingredient> {

    /**
     * Variables used to initiate the Custom Listview
     */
    ArrayList<Ingredient> ingredients;
    Context context;
    int resource;

    public CustomListIngredeint(Context context, int resource, ArrayList<Ingredient> ingredients) {
        super(context, resource, ingredients);
        this.ingredients = ingredients;
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
            convertView = layoutInflater.inflate(R.layout.custom_list_ingredient, null, true);

        }
        Ingredient ingredients = getItem(position);

        TextView label = (TextView) convertView.findViewById(R.id.label);
        label.setText(ingredients.getText());

        TextView source = (TextView) convertView.findViewById(R.id.source);
        source.setText(Integer.toString(ingredients.getWeight()) + "g");

        return convertView;
    }
}
