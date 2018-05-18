package com.yuansong.features.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.yuansong.features.BaseActivity;
import com.yuansong.features.R;

public class GDMapShowMapActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private MapView mMapView = null;

    private AMap aMap = null;
    private MyLocationStyle myLocationStyle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gdmap_show_map);

        mToolbar = findViewById(R.id.toolbar);
        mMapView = findViewById(R.id.map);

        mToolbar.setTitle("显示地图");
        setSupportActionBar(mToolbar);

        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();
        aMap.setMyLocationEnabled(true);
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
//        myLocationStyle.anchor(100,100);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
//        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(100,100),17));

//        myLocationStyle.anchor(100,100);
//        aMap.setMyLocationStyle(myLocationStyle);

        showBackOption();
        setLightWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }
}
