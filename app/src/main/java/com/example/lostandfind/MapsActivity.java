package com.example.lostandfind;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.Manifest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    //GoogleMap 객체
    private GoogleMap mMap;
    private Geocoder geocoder;

    // 출처: https://mainia.tistory.com/1153 [녹두장군 - 상상을 현실로]
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    //GPSTracker class
    private GpsInfo gps;
    Double user_latitude, user_longitude;
    Double click_latitude, click_longitude;

    //onCreateView에 사용
    MapView mapView;
    TextView mapTextView;
    Button mapButton;

    //setCurrentLocation에 사용
    private static final LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);

    private GoogleMap googleMap = null;
    private Marker currentMarker = null;

    //https://duzi077.tistory.com/122
    public void setCurrentLocation(Location location, String markerTitle, String markerSnippet) {
        if (currentMarker != null) currentMarker.remove();

        if (location != null) {
            //현재 위치의 위도 경도 가져오기
            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(currentLocation);
            markerOptions.title(markerTitle);
            markerOptions.snippet(markerSnippet);
            markerOptions.draggable(true);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            currentMarker = this.googleMap.addMarker(markerOptions);

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
            return;
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker = this.googleMap.addMarker(markerOptions);

        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(DEFAULT_LOCATION));
    }

    //Fragment예제 -> 자동검색
    //http://snowdeer.github.io/android/2017/08/21/place-picker-sample/
    static final String TAG = "PlaceAutocomplete";  //Log.i에 사용
    PlaceAutocompleteFragment autocompleteFragment;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    Intent getCreateBoardintent;

    String intentBoardType, intentTitle, intentContent, intentHashTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapTextView = (TextView) findViewById(R.id.mapTextView);
        //인텐트 값 가져오기
        getCreateBoardintent = getIntent();
        intentBoardType = getCreateBoardintent.getExtras().getString("boardType");
        intentTitle = getCreateBoardintent.getExtras().getString("title");
        intentContent = getCreateBoardintent.getExtras().getString("content");
        intentHashTag = getCreateBoardintent.getExtras().getString("hashTag");
        System.out.println("intentBoardType1 : " + intentBoardType);
        System.out.println("intentTitle : " + intentTitle);

        //GPS 사용유무 가져오기
        gps = new GpsInfo(MapsActivity.this);
        if (gps.isGetLocation()) {
            user_latitude = gps.getLatitude();
            user_longitude = gps.getLongitude();

            System.out.println("당신의 위도 : " + user_latitude);
            mapTextView.setText("위도 : " + user_latitude + "  경도 : " + user_longitude);

        } else {
            gps.showSettingsAlert();
        }
        callPermission();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        if (user_latitude != 0.0) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        mapButton = (Button) findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double intentLatitude, intentLongitude;
                if(click_latitude == null){
                    intentLatitude = user_latitude;
                    intentLongitude = user_longitude;
                }else{
                    intentLatitude = click_latitude;
                    intentLongitude = click_longitude;
                }
                System.out.println("intentLongitude : " + intentLongitude);
                System.out.println("intentBoardType2 : " + intentBoardType);
                System.out.println("intentTitle2 : " + intentTitle);
                getCreateBoardintent = getIntent();
                Intent createBoardIntent = new Intent(getApplicationContext(), CreateBoardActivity.class);
                createBoardIntent.putExtra("boardType", intentBoardType);
                createBoardIntent.putExtra("title", intentTitle);
                createBoardIntent.putExtra("content", intentContent);
                createBoardIntent.putExtra("hashTag", intentHashTag);
                createBoardIntent.putExtra("latitude", String.valueOf(intentLatitude));
                createBoardIntent.putExtra("longitude", String.valueOf(intentLongitude));
                createBoardIntent.putExtra("mapLocation", mapTextView.getText().toString());
                startActivity(createBoardIntent);
            }
        });
    }

    //권한요청
    private void callPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        geocoder = new Geocoder(this);
        //출처: https://bitsoul.tistory.com/145 [Happy Programmer~]
        // ↑매개변수로 GoogleMap 객체가 넘어옵니다.

        // camera 좌표 옮기기
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(user_latitude, user_longitude), 17   // 위도, 경도
        ));

        // marker 표시
        // market 의 위치, 타이틀, 짧은설명 추가 가능.
        MarkerOptions marker = new MarkerOptions();

        marker.position(new LatLng(user_latitude, user_longitude))
                .title("현재위치");
        googleMap.addMarker(marker).showInfoWindow(); // 마커추가,화면에출력

        // 마커클릭 이벤트 처리
        // GoogleMap 에 마커클릭 이벤트 설정 가능.
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // 마커 클릭시 호출되는 콜백 메서드
                Toast.makeText(getApplicationContext(),
                        marker.getTitle() + " 클릭했음"
                        , Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // 맵 터치 이벤트 구현 //
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                MarkerOptions mOptions = new MarkerOptions();
                googleMap.clear();
                //마커 타이틀
                mOptions.title("마커좌표");
                click_latitude = point.latitude;   //위도
                click_longitude = point.longitude; //경도
                //마커의 스니펫(간단한 텍스트)설정
                mOptions.snippet(click_latitude.toString() + ", " + click_longitude.toString());
                // LatLng: 위도 경도 쌍을 나타냄
                mOptions.position(new LatLng(click_latitude, click_longitude));
                //마커(핀)추가
                googleMap.addMarker(mOptions);
                mapTextView.setText("위도 : " + click_latitude + "  경도 : " + click_longitude);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = (Place) Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }
}
