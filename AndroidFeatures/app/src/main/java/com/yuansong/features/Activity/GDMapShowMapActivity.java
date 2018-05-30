package com.yuansong.features.Activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolygonOptions;
import com.amap.api.maps.model.Polygon;
import com.amap.api.maps.model.TextOptions;
import com.yuansong.features.BaseActivity;
import com.yuansong.features.Common.CommonFun;
import com.yuansong.features.R;

public class GDMapShowMapActivity extends BaseActivity {

    private Toolbar mToolbar = null;
    private MapView mMapView = null;

    private AMap aMap = null;
    private MyLocationStyle myLocationStyle = null;

    private MarkerOptions mMarkerOptions = null;
    private MarkerOptions mMarkerOptions_in = null;
    private MarkerOptions mMarkerOptions_out = null;

    private LatLng location_center = null;
    private LatLng location_in = null;
    private LatLng location_out = null;
    private Polygon mPolygon = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gd_map_opr,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.opr_location:
                subLocation();
                break;
            case R.id.opr_clear:
                aMap.clear();
                break;
            case R.id.toolbar_add_circle:
                subAddCircle();
                break;
            case R.id.toolbar_add_marker:
                subAddMarker();
                break;
            case R.id.toolbar_calculateLineDistance:
                subCalculateLineDistance();
                break;
            case R.id.toolbar_add_polygon:
                subAddPolygon();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

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

        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Log.i("msg",
                        String.valueOf(latLng.latitude) + " - " + String.valueOf(latLng.longitude));
                if(mPolygon != null){
                    CommonFun.showMsg(GDMapShowMapActivity.this,
                            String.valueOf(mPolygon.contains(latLng)));
                }
            }
        });

        location_center = new LatLng(32.047291,118.797617);
        location_in = new LatLng(32.048482742391606,118.79611283540726);
        location_out = new LatLng(32.05222815601843,118.79691749811174);

        showBackOption();
        setLightWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if(mMarkerOptions != null){
            mMarkerOptions.getIcon().recycle();
        }
        if(mMarkerOptions_in != null){
            mMarkerOptions_in.getIcon().recycle();
        }
        if(mMarkerOptions_out != null){
            mMarkerOptions_out.getIcon().recycle();
        }
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

    private void subLocation(){
        aMap.moveCamera(CameraUpdateFactory.zoomTo(16));
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(location_center));
    }

    private void subAddCircle(){
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(location_center);
        circleOptions.fillColor(getResources().getColor(R.color.mapCover));
        circleOptions.strokeWidth(0);
        circleOptions.radius(400);
        aMap.addCircle(circleOptions);
    }

    private void subAddMarker(){
        mMarkerOptions = new MarkerOptions();
        mMarkerOptions.position(location_center);
        mMarkerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        aMap.addMarker(mMarkerOptions);


        mMarkerOptions_in = new MarkerOptions();
        mMarkerOptions_in.position(location_in);
        mMarkerOptions_in.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        aMap.addMarker(mMarkerOptions_in);


        mMarkerOptions_out = new MarkerOptions();
        mMarkerOptions_out.position(location_out);
        mMarkerOptions_out.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        aMap.addMarker(mMarkerOptions_out);
    }

    private void subCalculateLineDistance(){
        String msg;
        if(mMarkerOptions != null){
            msg = "in - ";
            msg = msg + String.valueOf(AMapUtils.calculateLineDistance(mMarkerOptions.getPosition(),
                    mMarkerOptions_in.getPosition()));
            msg = msg + " | out - ";
            msg = msg + String.valueOf(AMapUtils.calculateLineDistance(mMarkerOptions.getPosition(),
                    mMarkerOptions_out.getPosition()));

            CommonFun.showMsg(GDMapShowMapActivity.this,msg);
        }
        else{
            msg = "未定位点";
            CommonFun.showMsg(GDMapShowMapActivity.this,msg);
        }
        Log.i("msg",msg);
    }

    private void subAddPolygon(){
//        PolygonOptions polygonOptions = new PolygonOptions();
//        polygonOptions.add(location_center);
//        polygonOptions.add(location_in);
//        polygonOptions.add(location_out);

        LatLng p1 = new LatLng(32.05110397968256 , 118.7949112057686);
        LatLng p2 = new LatLng(32.0487748787993 , 118.79739761352539);
        LatLng p3 = new LatLng(32.04942848800746 , 118.80063235759737);
        LatLng p4 = new LatLng(32.04386072479495 , 118.79367873072626);
        LatLng p5 = new LatLng(32.04307179832533 , 118.79820227622986);

        TextOptions textOptions = new TextOptions();
        textOptions.fontSize((int) getResources().getDimension(R.dimen.text_size_default));
        textOptions.fontColor(getResources().getColor(R.color.themeBlueColor));
        textOptions.backgroundColor(getResources().getColor(R.color.transparent));

        textOptions.text("P1");
        textOptions.position(p1);
        aMap.addText(textOptions);

        textOptions.text("P2");
        textOptions.position(p2);
        aMap.addText(textOptions);

        textOptions.text("P3");
        textOptions.position(p3);
        aMap.addText(textOptions);

        textOptions.text("P4");
        textOptions.position(p4);
        aMap.addText(textOptions);

        textOptions.text("P5");
        textOptions.position(p5);
        aMap.addText(textOptions);


        PolygonOptions polygonOptions = new PolygonOptions();
        polygonOptions.add(p1);
        polygonOptions.add(p2);
        polygonOptions.add(p3);
        polygonOptions.add(p5);
        polygonOptions.add(p4);

        polygonOptions.fillColor(getResources().getColor(R.color.mapCover));
        polygonOptions.strokeWidth(0);
        mPolygon = aMap.addPolygon(polygonOptions);

        CommonFun.showMsg(GDMapShowMapActivity.this,
                "多边形已添加，点击地图位置可返回点是否在多边形内");
    }
}
