package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import org.json.JSONException;
import org.json.JSONObject;


import org.json.JSONException;
import org.json.JSONObject;

import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonLayer;
import com.google.maps.android.geojson.GeoJsonLineString;
import com.google.maps.android.geojson.GeoJsonLineStringStyle;
import com.google.maps.android.geojson.GeoJsonPointStyle;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMyLocationButtonClickListener,
        android.location.LocationListener {
    private static final String TAG = MapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private static final int REQUEST_FINE_LOCATION_PERMISSION = 102;
    LocationManager locationManager;
    private MarkerOptions markerOptions;
    private GeoJsonLayer layer1,layer2,layer3;
    Marker currentMarker = null;
    private double latitude;
    private double longitude;
    private double lat,lng;
    /*
    private double  lat=22.620314;
    private double  lng=120.213186;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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








    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  try {
            GeoJsonLayer layer1 = new GeoJsonLayer(googleMap, R.raw.style_high,
                    getApplicationContext());
            layer1.addLayerToMap();



            GeoJsonLayer layer2 = new GeoJsonLayer(mMap, R.raw.style_mid,
                    getApplicationContext());


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

           */


        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title("右下可導航至此標記點");
                markerOptions.position(latLng);
                mMap.addMarker(markerOptions);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                Toast.makeText(getApplicationContext(),
                          "標記位置"+
                                "\n經度:" + latLng.longitude +
                                " \n緯度:" + latLng.latitude,
                        Toast.LENGTH_SHORT).show();
            }
        });


        LatLng gaomei = new LatLng(22.626424, 120.265842);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gaomei, 15));

        currentMarker =mMap.addMarker(new MarkerOptions()
                .title("中山大學")
                .position(gaomei));
        currentMarker.setVisible(false);



        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {


               /* markerOptions = new MarkerOptions()
                        .position(new LatLng(poi.latLng.latitude,poi.latLng.longitude))
                        .title(poi.name);
                mMap.addMarker(markerOptions);*/



                if (currentMarker!=null) {
                    /*  currentMarker.remove();    */
                    currentMarker=null;
                }

                if (currentMarker==null) {
                    // currentMarker = mMap.addMarker(new MarkerOptions().position(poi.latLng).title("右下導航"));
                    currentMarker=mMap.addMarker(new MarkerOptions().position(poi.latLng).title("右下可導航至"+poi.name));
                    /* mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng,15),5000,null);*/
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(poi.latLng));


                }

                Toast.makeText(getApplicationContext(), "標記名稱: " +
                                poi.name +
                                "\n經度:" + poi.latLng.longitude +
                                " \n緯度:" + poi.latLng.latitude,
                        Toast.LENGTH_SHORT).show();


            }

        });


        mMap.setOnMyLocationButtonClickListener(this);
        enableMyLocation();


    }

    public void onClick(View view){
        if(view.getId()== R.id.High ){
            try {
                GeoJsonLayer layer1 = new GeoJsonLayer(mMap, R.raw.style_high,
                        getApplicationContext());
                layer1.addLayerToMap();
                layer1.getDefaultPolygonStyle().setStrokeColor(Color.rgb(250, 0, 0));
                layer1.getDefaultPolygonStyle().setStrokeWidth(5);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(view.getId()== R.id.Mid ){
            try {
                GeoJsonLayer layer2 = new GeoJsonLayer(mMap, R.raw.style_mid,
                        getApplicationContext());
                layer2.addLayerToMap();
                layer2.getDefaultPolygonStyle().setStrokeColor(Color.rgb(250, 250, 0));
                layer2.getDefaultPolygonStyle().setStrokeWidth(5);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(view.getId()== R.id.Low ){
            try {
                GeoJsonLayer layer3 = new GeoJsonLayer(mMap, R.raw.style_json,
                        getApplicationContext());
                layer3.addLayerToMap();
                layer3.getDefaultPolygonStyle().setStrokeColor(Color.rgb(0, 250, 0));
                layer3.getDefaultPolygonStyle().setStrokeWidth(5);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(view.getId()== R.id.Clear )
            mMap.clear();
    }



    public boolean onMyLocationButtonClick() {

        if(lat!=0.0&&lng!=0.0) {
            Toast.makeText(getApplicationContext(), "我的位置: " +
                            "\n經度:" + lng +
                            " \n緯度:" + lat,
                    Toast.LENGTH_SHORT).show();
        }
        else{

            Toast.makeText(getApplicationContext(), "我的位置: " +
                            "\n經度:loading" +
                            " \n緯度:loading" ,
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }
    private void enableMyLocation() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.  詢問使用者開啟權限

            // 如果裝置版本是6.0（包含）以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 取得授權狀態，參數是請求授權的名稱
                int hasPermission = checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

                // 如果未授權，向使用者請求
                if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                    // 請求授權
                    //     第一個參數是請求授權的名稱，第二個參數是請求代碼
                    requestPermissions(
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_FINE_LOCATION_PERMISSION);
                }
            }
        } else if (mMap != null) {
            // 在地圖上啟用「我的位置」圖層
            mMap.setMyLocationEnabled(true);
            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,60000, 0, this);


        }
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_FINE_LOCATION_PERMISSION) {
            if (permissions.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                // Permission was denied. Display an error message.
                Toast.makeText(this, "需允許位置資訊授權，\n才能顯示位置圖層", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void onLocationChanged(Location location) {
        if (location != null){
            lat = location.getLatitude();  // 取得經度
            lng = location.getLongitude(); // 取得緯度

            LatLng UserPlace = new LatLng(location.getLatitude(), location.getLongitude());
            CameraPosition cameraPosition =
                    new CameraPosition.Builder()
                            .target(UserPlace)
                            .zoom(mMap.getCameraPosition().zoom)
                            .bearing(location.getBearing())
                            .build();
            // 使用動畫的效果移動地圖
            /* mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
             */

        }
        else{
            Toast.makeText(this, "無法取得定位資訊", Toast.LENGTH_SHORT).show();
        }
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    public void onProviderEnabled(String provider) {
        Toast.makeText(getBaseContext(), "GPS已經開啟", Toast.LENGTH_SHORT).show();
    }
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "GPS已關閉", Toast.LENGTH_SHORT).show();
    }


}