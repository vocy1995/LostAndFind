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

public class ReplyViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;


    public ReplyViewAdapter(Context context,
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
        TextView no;
        TextView board_content;
        TextView title_no;
        TextView board_writer;

        ArrayList<Integer> board_no = new ArrayList<>();


        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.comment_item, parent, false);
        // Get the position from the results
        HashMap<String, String> resultp = new HashMap<String, String>();
        //System.out.println("inputdata : "+ inputdata);
        //System.out.println("position " + position);
        resultp = data.get(position);
        //System.out.println("resultp : "+resultp.get("title_no"));

        board_writer = (TextView) itemView.findViewById(R.id.reply_writer);
        board_content = (TextView) itemView.findViewById(R.id.comment);


        board_writer.setText(resultp.get(SingleItemView.BOARD_WRITER));
        board_content.setText(resultp.get(SingleItemView.BOARD_CONTENT));
        // Locate the TextViews in listview_item.xml


        // Capture position and set results to the ImageView
        // Passes flag images URL into ImageLoader.class to download and cache
        // images

        // Capture button clicks on ListView items

        return itemView;
    }
}
