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
                           ArrayList<HashMap<String, String>> arraylist) { //TimeLine에서 보내주는 값 저장
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);

    }

    @Override
    public int getCount() { //listview 출력되는 값을 위한 설정값
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // Declare Variables
        TextView title;
        TextView writer;
        TextView time;
        TextView hash_tag;
        TextView content;

        ArrayList<Integer> board_no = new ArrayList<>();
        ImageView image;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); //View를 직접사용하기위한 LayoutInflater 사용

        View itemView = inflater.inflate(R.layout.timeline_item, parent, false); //ListView 를위한 View 생성

        HashMap<String, String> resultp = new HashMap<String, String>();
        resultp = data.get(position); //timeline에서 받은 값 저장


        title = (TextView) itemView.findViewById(R.id.title);
        writer = (TextView) itemView.findViewById(R.id.writer);
        time = (TextView) itemView.findViewById(R.id.time);
        hash_tag = (TextView) itemView.findViewById(R.id.hashtag);
        content = (TextView) itemView.findViewById(R.id.content);

        image = (ImageView) itemView.findViewById(R.id.image);


        title.setText(resultp.get(TimeLine.TITLE));
        writer.setText(resultp.get(TimeLine.WRITER));
        time.setText(resultp.get(TimeLine.TIME));
        hash_tag.setText(resultp.get(TimeLine.HASH_TAG));
        content.setText(resultp.get(TimeLine.CONTENT));// 디자인 세팅

        imageLoader.DisplayImage(resultp.get(TimeLine.IMAGE), image); //imageLoader에 있는 displayimage를 사용하여 이미지 사용
        // Capture button clicks on ListView items
        itemView.setOnClickListener(new OnClickListener() {
            HashMap<String, String> single_resultp = new HashMap<String, String>();
            @Override
            public void onClick(View arg0) {//댓글을 사용하기 위한 onclick 뷰 사용
                // Get the position from the results

                //single_resultp = new ArrayList<HashMap<String, String>>();
                single_resultp = data.get(position); //해당하는 글의 댓글을 보기위한 (ex.순서번호)position 사용
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(context, SingleItemView.class);
                // Pass all data rank
                intent.putExtra("no",single_resultp.get(TimeLine.NO));
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
