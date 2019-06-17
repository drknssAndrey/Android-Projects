package com.andreybalbino.sdkmapasgoogle;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

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

        //mudar exibicao do mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        // Add a marker in Sydney and move the camera
        final LatLng semiaoCananeia = new LatLng(-6.969131, -35.791983);
        //-6.969131, -35.791983

//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(semiaoCananeia);
//        circleOptions.radius(500);//metros
//        circleOptions.fillColor(Color.argb(128, 255, 153, 0));//0 até o 255 para o alfa
//        circleOptions.strokeWidth(10);
//        circleOptions.strokeColor(Color.GREEN);
//        mMap.addCircle(circleOptions);


//        1) -23.586332, -46.658754
//        2) -23.585615, -46.656662
//        3) -23.587158, -46.657037
//        4) -23.587247, -46.658797
//        final PolygonOptions polygonOptions = new PolygonOptions();
//        polygonOptions.add(new LatLng(-6.969305, -35.791926));
//        polygonOptions.add(new LatLng(-6.969300, -35.791883));
//        polygonOptions.add(new LatLng(-6.969521, -35.791859));
//        polygonOptions.add(new LatLng(-6.969497, -35.791795));
//        polygonOptions.fillColor(Color.argb(128, 255, 153, 0));
//        polygonOptions.strokeColor(Color.GREEN);
//        mMap.addPolygon(polygonOptions);

        //adicionar evento de click no mapa
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
//                Toast.makeText(MapsActivity.this, "Lat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_SHORT).show();

                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.add(semiaoCananeia);
                polylineOptions.add(latLng);
                polylineOptions.add(latLng);
                polylineOptions.color(Color.BLUE);
                polylineOptions.width(20);
                mMap.addPolyline(polylineOptions);
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Local")
                        .snippet("Descricao")
                        .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icone_loja)));
            }
        });
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                mMap.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Local")
                        .snippet("Descricao")
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.iconfinder_car_1055099  )));
            }
        });
        mMap.addMarker(new MarkerOptions().position(semiaoCananeia).title("Semião Cananéia")
                /*
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))*/
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icone_carro_roxo_48px)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(semiaoCananeia, 15));
    }
}
