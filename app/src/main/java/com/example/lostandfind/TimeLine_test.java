package com.example.loatandfind;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class TimeLine_test extends AppCompatActivity {

    ArrayList<HashMap<String, String>> arraylist;
    ArrayList<String> rankArray, countryArray, populationArray, flageArray;
    RecyclerView recyclerView;
    TextView ra,co,po;
    ImageView fl;
    Bitmap bitmap;
    String rank,coun,pop,flag;
    static String RANK = "rank";
    static String COUNTRY = "country";
    static String POPULATION = "population";
    static String FLAG = "flag";

    ListView listview;
    ListViewAdapter adapter;

    LinearLayoutManager Manager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_main);
        //ra = findViewById(R.id.textView);
        //co = findViewById(R.id.textView5);
        //po = findViewById(R.id.textView4);
        //fl = findViewById(R.id.imageView);

        //listView = (ListView) findViewById(R.id.listView);
        getJSON("http://192.168.35.59:3000/post");
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
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                //화면에 값이 출력 됌
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
        arraylist = new ArrayList<HashMap<String, String>>();
        //JSONArray test_temp = new JSONArray();
        // String[] heroes = new String[jsonArray.length()];
       // String[] Rank = new String[jsonArray.length()];
        //String[] Coun = new String[jsonArray.length()];
        //String[] Pop = new String[jsonArray.length()];
        //String[] Flag = new String[jsonArray.length()];

        System.out.println("JsonArray: " + jsonArray);

        for (int i = 0; i < jsonArray.length(); i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            JSONObject obj = jsonArray.getJSONObject(i);

            //ImageView[] imageViewList = new ImageView[]{fl};
            map.put("rank", obj.getString("rank"));
            map.put("country", obj.getString("country"));
            map.put("population", obj.getString("population"));
            map.put("flag", obj.getString("flag"));
            System.out.println("map : " +map);
            arraylist.add(map);
            System.out.println("\n arraylisttest : " + arraylist);
        }

        /*for(int i=0; i<jsonArray.length(); i++){
            JSONObject jsonInfo = jsonArray.getJSONObject(i);
            rankArray.add(jsonInfo.getString("rank"));
            populationArray.add(jsonInfo.getString("population"));
        }
        System.out.println(rankArray);
        System.out.println(populationArray);*/
        listview = (ListView) findViewById(R.id.listview);
        // Pass the results into ListViewAdapter.java
        adapter = new ListViewAdapter(TimeLine_test.this, arraylist);
        // Binds the Adapter to the ListView
        listview.setAdapter(adapter);
        // Close the progressdialog
    }


}
