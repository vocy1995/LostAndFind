package com.example.lostandfind;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.List;

public class PrintMapMarker extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GoogleMap googleMap = null;
    Geocoder geocoder;

    Double click_latitude, click_longitude;

    TextView mapTextView;
    Button printMapButton;

    String userClick_city;
    String url = "http://192.168.1.3:3000";

    String bulletinsNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_map_marker);

        geocoder = new Geocoder(this);

        mapTextView = (TextView) findViewById(R.id.mapTextView);

        printMapButton = (Button) findViewById(R.id.printMapButton);
        printMapButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(PrintMapMarker.this, TimeLine.class);
                intent.putExtra("city", userClick_city);
                startActivity(intent);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(36.3585694, 128.0385705), 8   // 위도, 경도
        ));

        // marker 표시
        // market 의 위치, 타이틀, 짧은설명 추가 가능.
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(0, 0))
                .title("현재위치");
        googleMap.addMarker(marker).showInfoWindow(); // 마커추가,화면에출력

        // 맵 터치 이벤트 구현 //
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {

                MarkerOptions mOptions = new MarkerOptions();
                googleMap.clear();
                //마커 타이틀
                mOptions.title(userClick_city);
                click_latitude = point.latitude;   //위도
                click_longitude = point.longitude; //경도
                //마커의 스니펫(간단한 텍스트)설정
                mOptions.snippet("게시글 수 : " + bulletinsNumber);
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(click_latitude, click_longitude));
                //마커(핀)추가
                googleMap.addMarker(mOptions);
                mapTextView.setText("위도 : " + click_latitude + "  경도 : " + click_longitude);
                List<Address> list = null;
                try {
                    list = geocoder.getFromLocation(
                            click_latitude, // 위도
                            click_longitude, // 경도
                            10); // 얻어올 값의 개수
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
                }
                if (list != null) {
                    if (list.size() == 0) {
                        System.out.println("해당되는 주소 정보는 없습니다");
                        mapTextView.setText("위도 : " + click_latitude + "  경도 : " + click_longitude);
                    } else {
                        userClick_city = list.get(0).getLocality();
                        System.out.println(list.get(0).getLocality());
                        new JSONTask().execute("http://192.168.1.3:3000/selectBulletinNum");
                        mapTextView.setText(list.get(0).getLocality());
                    }
                }
            }
        });
    }

    //서버로 값전달
    public class JSONTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("city", userClick_city);

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
            System.out.println(result);
            //bulletinsNumber = "0";
            bulletinsNumber = result;
            Toast.makeText(getApplicationContext(), userClick_city + "의 게시글은 " + result + "개 입니다.", Toast.LENGTH_SHORT).show();

        }
    }
}
