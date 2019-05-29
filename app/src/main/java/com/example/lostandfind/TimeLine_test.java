package com.example.loatandfind;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class TimeLine_test extends AppCompatActivity {

    ListView listView;
    TextView ra,co,po;
    ImageView fl;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_test);
        ra = findViewById(R.id.textView);
        co = findViewById(R.id.textView5);
        po = findViewById(R.id.textView4);
        fl = findViewById(R.id.imageView);

        //listView = (ListView) findViewById(R.id.listView);
        getJSON("http://192.168.60.51:3000/post");
    }


    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        // String[] heroes = new String[jsonArray.length()];
        String[] Rank = new String[jsonArray.length()];
        String[] Coun = new String[jsonArray.length()];
        String[] Pop = new String[jsonArray.length()];
        String[] Flag = new String[jsonArray.length()];

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            Rank[i] = obj.getString("rank");
            Coun[i] = obj.getString("country");
            Pop[i] = obj.getString("population");
            Flag[i] = obj.getString("flag");
            ImageView[] imageViewList = new ImageView[]{fl};

            try {
                Bitmap[] bitmapList = new image_loader().execute(Flag).get();
                for (int ii=0; i<bitmapList.length; i++) {
                    imageViewList[ii].setImageBitmap(bitmapList[ii]);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        ra.setText(Rank[0]);
        co.setText(Coun[0]);
        po.setText(Pop[0]);


    }


}
