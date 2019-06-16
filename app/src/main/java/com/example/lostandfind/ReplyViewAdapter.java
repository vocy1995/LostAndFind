package com.example.lostandfind;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ReplyViewAdapter extends BaseAdapter {

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
    } //데이터 크기만큼 listview 생성

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        TextView board_content;
        TextView board_writer;
        TextView time;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = inflater.inflate(R.layout.comment_item, parent, false);
        HashMap<String, String> resultp = new HashMap<String, String>();
        resultp = data.get(position);


        board_writer = (TextView) itemView.findViewById(R.id.reply_writer);
        board_content = (TextView) itemView.findViewById(R.id.comment);
        time = (TextView) itemView.findViewById(R.id.time);


        board_writer.setText(resultp.get(ReplyView.WRITER));
        board_content.setText(resultp.get(ReplyView.CONTENT));
        time.setText(resultp.get(ReplyView.TIME));//해당하는값 출력

        return itemView;
    }
}
