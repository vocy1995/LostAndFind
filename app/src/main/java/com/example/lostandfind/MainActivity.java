package com.example.loatandfind;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Handler;

public class MainActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void create(View view) {
        Intent create = new Intent(MainActivity.this,create_board.class);
        startActivity(create);
    }

    public void test(View view) {
        Intent create = new Intent(MainActivity.this,test_node_js.class);
        startActivity(create);
    }

    public void board(View view) {
        Intent timeline = new Intent(MainActivity.this,TimeLine_test.class);
        startActivity(timeline);
    }
}
