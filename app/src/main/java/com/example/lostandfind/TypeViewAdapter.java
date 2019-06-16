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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TypeViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    ArrayList<HashMap<String, String>> data;
    ImageLoader imageLoader;
    String get_name;
    public TypeViewAdapter(Context context,
                               ArrayList<HashMap<String, String>> arraylist,String name) { //TimeLine에서 보내주는 값 저장
        this.context = context;
        data = arraylist;
        imageLoader = new ImageLoader(context);
        get_name = name;
        System.out.println("timeline_id_get : "+ get_name);
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
        TextView location;

        ArrayList<Integer> board_no = new ArrayList<>();
        ImageView image;
        Button comment;

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
        location = (TextView) itemView.findViewById(R.id.location);
        image = (ImageView) itemView.findViewById(R.id.image);
        comment = (Button)itemView.findViewById(R.id.comment);

        title.setText(resultp.get(TypeView.TITLE));
        writer.setText(resultp.get(TypeView.BOARD_WRITER));
        time.setText(resultp.get(TypeView.TIME));
        hash_tag.setText(resultp.get(TypeView.HASH_TAG));
        content.setText(resultp.get(TypeView.CONTENT));// 디자인 세팅
        location.setText(resultp.get(TypeView.LOCATION));


        imageLoader.DisplayImage(resultp.get(TypeView.IMAGE_URL), image); //imageLoader에 있는 displayimage를 사용하여 이미지 사용
        // Capture button clicks on ListView items
        comment.setOnClickListener(new OnClickListener() {
            HashMap<String, String> single_resultp = new HashMap<String, String>();
            @Override
            public void onClick(View arg0) {//댓글을 사용하기 위한 onclick 뷰 사용

                single_resultp = data.get(position);

                Intent intent = new Intent(context, ReplyView.class);

                intent.putExtra("no",single_resultp.get(myWriter.NO));
                intent.putExtra("name",get_name);
                context.startActivity(intent);

            }
        });
        return itemView;
    }


}
