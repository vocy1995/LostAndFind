package com.example.lostandfind;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;

public class test_node_js extends AppCompatActivity {
    TextView tvData,JSon2;
    Button btn;
    JSONArray jarray;
    JSONObject jsonobject;
    ListView listview;
    //ListViewAdapter adapter;
    ProgressDialog mProgressDialog;
    ArrayList<HashMap<String, String>> arraylist;
    static String RANK = "rank";
    static String COUNTRY = "country";
    static String POPULATION = "population";
    static String FLAG = "flag";
    private ArrayList<String>ranklist;
    private ArrayList<String>countrylist;
    private ArrayList<String>populationlist;
    private ArrayList<String>flaglist;
    ArrayList<HashMap<String, String>> contactList;
    HashMap<String, String> contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_nodejs_1);

        tvData = findViewById(R.id.textView4);
        JSon2 = findViewById(R.id.textView3);
        btn =findViewById(R.id.httpTest);
        /*ranklist = new ArrayList<>();
        countrylist = new ArrayList<>();
        populationlist = new ArrayList<>();
        flaglist = new ArrayList<>();*/
        contact= new HashMap<>();
        contactList = new ArrayList<>();
        jsonobject = new JSONObject();
        //버튼이 클릭되면 여기 리스너로 옴

        btn.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                //AsyncTask 시작시킴
                new JSONTask().execute("http://192.168.35.59:3000/post");
            }

        });
    }
    public void Image_Load(View view2) {
        Intent intent2 = new Intent(test_node_js.this,GetImageActivity.class);
        startActivity(intent2);
    }

    public void MulterLoader(View view) {
        Intent intent3 = new Intent(test_node_js.this,Multer.class);
        startActivity(intent3);
    }

    /*public void listview_loader(View view) {
        Intent intent4 = new Intent(test_node_js.this,Listview_test.class);
        startActivity(intent4);
    }*/

    public class JSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {

                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try{
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의미
                    con.connect();
                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while((line = reader.readLine()) != null){
                        buffer.append(line);
                    }
                    String json_add = buffer.substring(0,buffer.length());
                    jarray = new JSONArray(json_add);
                    //jsonobject = new JSONObject();
                    //jsonobject.getString(json_add);
                   // jarray = jsonobject.getJSONArray("RowDataPacket"); // 대제목 가져오기

                    for (int i = 0; i < jarray.length(); i++) {
                        //HashMap<String, String> map = new HashMap<String, String>();
                        jsonobject = jarray.getJSONObject(i);

                        /*RANK = jsonobject.getString("rank");
                        COUNTRY = jsonobject.getString("country");
                        POPULATION = jsonobject.getString("population");
                        FLAG = jsonobject.getString("flag");*/




                        /*ranklist.add(RANK);
                        countrylist.add(COUNTRY);
                        populationlist.add(POPULATION);
                        flaglist.add(FLAG);*/
                        contact.put("rank",jsonobject.getString("rank"));
                        contact.put("country",jsonobject.getString("country"));
                        contact.put("population",jsonobject.getString("population"));
                        contact.put("flag",jsonobject.getString("flag"));
                        // Retrive JSON Objects
                        //map.put("rank", jsonobject.getString("rank"));
                        //map.put("country", jsonobject.getString("country"));
                        //map.put("population", jsonobject.getString("population"));
                        //map.put("flag", jsonobject.getString("flag"));
                        // Set the JSON Objects into the array
                        //arraylist.add(map); // 파싱후 arraylist에 저장
                        ;
                        contactList.add(contact);

                    }
                   // ArrayList a = new ArrayList<>();
                    //a.addAll(contactList,contact);
                    //JSon2.setText((CharSequence) contactList);

                    //return json_add;//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
                } catch (MalformedURLException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if(con != null){
                        con.disconnect();
                    }
                    try {
                        if(reader != null){
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
               // JSon2.setText((CharSequence) contact);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //JSon2.setText(contactList.toString());
            //String tttt= String.join("",arraylist);
            //JSon2.setText((CharSequence) contact)
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            /*listview = findViewById(R.id.listview);
            // Pass the results into ListViewAdapter.java
            adapter = new ListViewAdapter(test_node_js.this,contactList);
            // Set the adapter to the ListView
            listview.setAdapter(adapter);
            // Close the progressdialog
            mProgressDialog.dismiss(); //list 뷰 사용 및*/
        }
    }
}
