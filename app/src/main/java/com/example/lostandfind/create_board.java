package com.example.lostandfind;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.HashMap;

public class create_board extends AppCompatActivity {
    //데이터를 받아올 URL
    String url = "http://192.168.0.68:3000/androidSendData";
    //php를 읽어올때 사용할 변수
    public GettingData gData;

    //Spinner 객체 및 변수 선언
    Spinner type_question, large_category, middle_category, small_category, color_category;
    String type_question_text, large_category_text, middle_category_text, small_category_text, color_category_text;

    //EditText 객체 및 변수 선언
    EditText brand_name, location, storage_location, content;
    String brand_name_text, location_text, storage_location_text, content_text;

    //콤보박스에 들어갈 객체 선언하기

    //게시글 선언
    String[] type_question_items = {"게시글 선택", "분실물", "습득물"};
    //대분류 선언
    String[] large_category_items = {"대분류 선택", "가전제품", "잡화,화장품", "의류", "기타"};
    //중분류 선언
    String[] domestic_middle_category_items = {"중분류 선택", "PC", "모바일기기", "주변기기"};    //가전제품
    String[] cosmetic_middle_category_items = {"중분류 선택", "가방", "모자", "신발", "악세사리", "화장품"};  //잡화, 화장품
    String[] surround_middle_category_items = {"중분류 선택", "상의", "하의", "기타"}; //의류
    //소분류 선언 - 가전제품
    String[] pc_small_category_items = {"소분류 선택", "노트북", "데스크탑", "기타"}; //가전제품 - PC
    String[] mobile_small_category_items = {"소분류 선택", "태블릿", "핸드폰", "기타"};  //가전제품 - 모바일기기
    String[] other_small_category_items = {"소분류 선택", "USB", "PC용품", "보조배터리", "이어폰", "기타"};  //가전제품 - 주변기기
    //소분류 선언 - 잡화,화장품
    String[] bag_small_category_items = {"소분류 선택","도트백", "백팩", "숄더백", "에코백", "크로스백", "클러치백", "힙색"}; //잡화, 화장품 - 가방
    String[] shose_small_category_items = {"소분류 선택","실내화", "실외화"}; //잡화, 화장품 - 신발
    String[] acce_small_category_items = {"소분류 선택","귀걸이", "목걸이", "반지", "시계", "팔찌"}; //잡화, 화장품 - 악세사리
    String[] wallet_small_category_items = {"소분류 선택","장지갑", "중지갑", "반지갑", "카드지갑"}; //잡화, 화장품 - 지갑
    String[] cosmetic_small_category_items = {"소분류 선택", "색조화장품", "기초화장품"};  //잡화, 화장품 - 화장품
    //소분류 선언 - 의류
    String[] topCloth_small_category_items = {"소분류 선택", "티셔츠", "셔츠", "니트"}; //의류 - 상의
    String[] bottomCloth_small_category_items = {"소분류 선택", "긴바지", "반바지", "긴치마", "짧은치마"};    //의류 - 하의
    String[] color_category_items = {"색깔 선택", "빨강", "초록", "노랑", "파랑", "검정", "분홍", "흰색", "보라", "회색", "기타"};


    String[] middle_category_items = {"중분류 선택"};
    String[] small_category_items = {"소분류 선택"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_board);

        {
            type_question = (Spinner) findViewById(R.id.type_question);
            large_category = (Spinner)findViewById(R.id.large_catergory);
            middle_category = (Spinner)findViewById(R.id.middle_category);
            small_category = (Spinner)findViewById(R.id.small_category);
            color_category = (Spinner)findViewById(R.id.color_category);
        }

        large_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type_question_text = type_question.getSelectedItem().toString();
                large_category_text = large_category.getSelectedItem().toString();
                middle_category_text = middle_category.getSelectedItem().toString();
                small_category_text = small_category.getSelectedItem().toString();
                color_category_text = color_category.getSelectedItem().toString();

                if(large_category_text=="가전제품"){

                    Toast.makeText(getApplicationContext(), "가전제품 선택",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent){

            }
        });

        //취소 버튼 이벤트 -> 뒤로가기
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //저장 버튼 이벤트
        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View view) {
                {
                    type_question_text = type_question.getSelectedItem().toString();
                    large_category_text = large_category.getSelectedItem().toString();
                    middle_category_text = middle_category.getSelectedItem().toString();
                    small_category_text = small_category.getSelectedItem().toString();
                    color_category_text = color_category.getSelectedItem().toString();

                    brand_name = (EditText) findViewById(R.id.brand_name);  //브랜드명
                    brand_name_text = brand_name.getText().toString();

                    location = (EditText)findViewById(R.id.location);
                    location_text = location.getText().toString();

                    storage_location = (EditText)findViewById(R.id.storage_location);
                    storage_location_text = storage_location.getText().toString();

                    content = (EditText)findViewById(R.id.content);
                    content_text = content.getText().toString();
                    int brand_name_text_legth = brand_name_text.length();
                }
                //빈공간확인 -> 확인 후 빈공간이 없으면 게시글 정보 서버로 전달 후 저장
                if ((type_question_text == "게시글 선택")) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((large_category_text == "대분류 선택")) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((middle_category_text == "중분류 선택")) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((middle_category_text == "소분류 선택")) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((middle_category_text == "색깔 선택")) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((brand_name_text.length() == 0)) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((location_text.length() == 0)) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((storage_location_text.length() == 0)) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else if((content_text.length() == 0)) {
                    Toast.makeText(getApplicationContext(), "빈공간이 있습니다.",Toast.LENGTH_SHORT).show();
                }else
                {
                    new JSONTask().execute("http://192.168.0.68:3000/data");
                }
            }
        });

        //콤보박스 값 넣기
        {
            ArrayAdapter<String> adapter_type_question = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, type_question_items);
            adapter_type_question.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            type_question.setAdapter(adapter_type_question);

            ArrayAdapter<String> adapter_large_category = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, large_category_items);
            adapter_large_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            large_category.setAdapter(adapter_large_category);

            ArrayAdapter<String> adapter_middle_category = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, middle_category_items);
            adapter_middle_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            middle_category.setAdapter(adapter_middle_category);

            ArrayAdapter<String> adapter_small_category = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, small_category_items);
            adapter_small_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            small_category.setAdapter(adapter_small_category);

            ArrayAdapter<String> adapter_color_category = new ArrayAdapter<String>(
                    this, android.R.layout.simple_spinner_item, color_category_items);
            adapter_color_category.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            color_category.setAdapter(adapter_color_category);
        }
    }

    //서버에서 데이터 가져오기
    class GettingData extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {
            StringBuilder jsonHtml = new StringBuilder();
            try {
                URL phpUrl = new URL(params[0]);
                HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

                if ( conn != null ) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);

                    if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        while ( true ) {
                            String line = br.readLine();
                            if ( line == null )
                                break;
                            jsonHtml.append(line + "\n");
                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return jsonHtml.toString();
        }

        protected void onPostExecute(String str) {
            try {
                // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
                JSONObject jObject = new JSONObject(str);
                // results라는 key는 JSON배열로 되어있다.
                JSONArray results = jObject.getJSONArray("results");
                String zz = "";
                zz += "Results : \n";
                Log.d("serverGetResult", results.toString());
                for ( int i = 0; i < 3; ++i ) {
                    JSONObject temp = results.getJSONObject(i);
                    zz += "\t1 : " + temp.get("1");
                    zz += "\t2 : " + temp.get("2");
                    zz += "\t3 : " + temp.get("3");
                    zz += "\n\t--------------------------------------------\n";
                }
                content.setText(jObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //서버로 값전달
    public class JSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("type_question", type_question_text);
                jsonObject.accumulate("large_category", large_category_text);
                jsonObject.accumulate("middle_category", middle_category_text);
                jsonObject.accumulate("small_category", small_category_text);
                jsonObject.accumulate("color_category", color_category_text);

                jsonObject.accumulate("brand_name", brand_name_text);
                jsonObject.accumulate("location", location_text);
                jsonObject.accumulate("storage_location", storage_location_text);
                jsonObject.accumulate("content", content_text);

                //accumulate이거 뒤에가 데이터 전송하는거라 여기서 TEXTVIEW 로그인 뷰 긁어오면 데이터 전송은 가능 근데 전송한 데이터를 db로 넣어야할듯
                HttpURLConnection con = null;
                BufferedReader reader = null;
                try {
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
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                    }
                    return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
