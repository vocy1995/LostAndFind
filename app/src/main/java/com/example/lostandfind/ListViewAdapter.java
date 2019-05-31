package com.example.loatandfind;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;

    public ListViewAdapter(Context context,
                           ArrayList<HashMap<String, String>> arraylist) {
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView rank;
        TextView country;
        TextView population;
        ImageView flag;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position from the results
        HashMap<String, String> resultp = new HashMap<String, String>();
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        rank = (TextView) itemView.findViewById(R.id.rank);
        country = (TextView) itemView.findViewById(R.id.country);
        population = (TextView) itemView.findViewById(R.id.population);
        // Locate the ImageView in listview_item.xml
        flag = (ImageView) itemView.findViewById(R.id.flag);

        // Capture position and set results to the TextViews
        rank.setText(resultp.get(TimeLine_test.RANK));
        country.setText(resultp.get(TimeLine_test.COUNTRY));
        population.setText(resultp.get(TimeLine_test.POPULATION));
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class to download and cache
        // images
        imageLoader.DisplayImage(resultp.get(TimeLine_test.FLAG), flag);
        // Capture button clicks on ListView items
       itemView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Get the position from the results
                HashMap<String, String> single_resultp = new HashMap<String, String>();
                single_resultp = data.get(position);
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data rank
                intent.putExtra("rank", single_resultp.get(TimeLine_test.RANK));
                // Pass all data country
                intent.putExtra("country", single_resultp.get(TimeLine_test.COUNTRY));
                // Pass all data population
                intent.putExtra("population", single_resultp.get(TimeLine_test.POPULATION));
                // Pass all data flag
                intent.putExtra("flag", single_resultp.get(TimeLine_test.FLAG));
                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });

        return itemView;
    }
}
