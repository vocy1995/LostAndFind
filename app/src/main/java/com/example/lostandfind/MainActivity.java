package com.example.loatandfind;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);
    }

    public void create(View view) {
        Intent create = new Intent(MainActivity.this,create_board.class);
        startActivity(create);
    }

    public void test(View view) {
        Intent create = new Intent(MainActivity.this,test_node_js.class);
        startActivity(create);
    }
}
