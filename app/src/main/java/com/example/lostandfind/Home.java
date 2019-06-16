package com.example.lostandfind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//액티비티라는것을 상속해주어야 한다.
public class Home extends AppCompatActivity {
    String name;
    TextView name_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // 부모를 받는다.
        setContentView(R.layout.home);      // 어떤 레이아웃을 가리키는가.
        Intent get = getIntent();
        name = get.getStringExtra("name");
        name_view = findViewById(R.id.name_text);
        name_view.setText(name);
        Button type1 = findViewById(R.id.imgBtnCircle_lost);
        type1.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrintMapMarker.class);
                intent.putExtra("name",name);
                intent.putExtra("type","분실물");
                startActivity(intent);
            }
        });
        Button type2 = findViewById(R.id.imgBtnCircle_get);
        type2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PrintMapMarker.class);
                intent.putExtra("name",name);
                intent.putExtra("type","습득물");
                startActivity(intent);
            }
        });
        Button write = findViewById(R.id.imgBtnCircle_write);
        write.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateBoardActivity.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
        Button my = findViewById(R.id.imgBtnCircle_post);
        my.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), myWriter.class);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }
}
