package com.example.lostandfind;

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
        TextView title;
        TextView writer;
        TextView time;
        TextView hash_tag;
        TextView content;
        TextView no;
        ArrayList<Integer> board_no = new ArrayList<>();
        ImageView image;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.listview_item, parent, false);
        // Get the position from the results
        HashMap<String, String> resultp = new HashMap<String, String>();
        resultp = data.get(position);

        // Locate the TextViews in listview_item.xml
        title = (TextView) itemView.findViewById(R.id.title);
        writer = (TextView) itemView.findViewById(R.id.writer);
        time = (TextView) itemView.findViewById(R.id.time);
        hash_tag = (TextView) itemView.findViewById(R.id.hashtag);
        content = (TextView) itemView.findViewById(R.id.content);
        // Locate the ImageView in listview_item.xml
        image = (ImageView) itemView.findViewById(R.id.image);
        for(int count=0; count<=position;count++){
            board_no.add(position);
        }




        // Capture position and set results to the TextViews
        title.setText(resultp.get(TimeLine_test.TITLE));
        writer.setText(resultp.get(TimeLine_test.WRITER));
        time.setText(resultp.get(TimeLine_test.TIME));
        hash_tag.setText(resultp.get(TimeLine_test.HASH_TAG));
        content.setText(resultp.get(TimeLine_test.CONTENT));
        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class to download and cache
        // images
        imageLoader.DisplayImage(resultp.get(TimeLine_test.IMAGE), image);
        // Capture button clicks on ListView items
        itemView.setOnClickListener(new OnClickListener() {
            HashMap<String, String> single_resultp = new HashMap<String, String>();
            @Override
            public void onClick(View arg0) {
                // Get the position from the results

                //single_resultp = new ArrayList<HashMap<String, String>>();
                single_resultp = data.get(position);
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data rank
                intent.putExtra("no",single_resultp.get(TimeLine_test.NO));
                // Pass all data country
                //intent.putExtra("writer", single_resultp.get(TimeLine_test.WRITER));
                // Pass all data population

                // Pass all data flag

                // Start SingleItemView Class
                context.startActivity(intent);

            }
        });

        return itemView;
    }
}
