package com.andreybalbino.localizacaousuario;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String[] permissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Permissoes.validarPermissoes(permissoes, this, 1);
        //objeto responsavel por gerenciar a localizacao do usuario
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Toast.makeText(MapsActivity.this, "latitude" + location.getLatitude(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
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



        /*
        Geocoding -> processo de transformar um endereco ou descricao de um local em latitude/longitude
        reverse geocoding -> processo de transformar latitude/longitude em um endereco
         */
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            /*
             Address[addressLines=[0:"Rua Francisco Machado, 160, Remígio - PB, 58398-000, Brasil"],feature=160,admin=Paraíba,sub-admin=Remígio,locality=null,thoroughfare=Rua Francisco Machado,postalCode=58398-000,countryCode=BR,countryName=Brasil,hasLatitude=true,latitude=-6.9684159,hasLongitude=true,longitude=-35.7950029,phone=null,url=null,extras=null]
             */
//            List<Address> listEnderecos = geocoder.getFromLocation(-6.968400, -35.795002, 1);
            String stringEndereco = "R. Robélia Diniz, 141-37";
            List<Address> listEnderecos = geocoder.getFromLocationName(stringEndereco, 1);
            if (listEnderecos != null && listEnderecos.size() > 0) {
                Address endereco =  listEnderecos.get(0);
                Double lat = endereco.getLatitude();
                Double longi = endereco.getLongitude();
                Log.d("local", "onLocationChanged: " + endereco.getAddressLine(0));

                // Add a marker in Sydney and move the camera
                LatLng sydney = new LatLng(lat, longi);
                mMap.addMarker(new MarkerOptions().position(sydney).title("Here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == getPackageManager().PERMISSION_DENIED) {
                //Alerta
                alertaValidacaoPermissao();
            } else if (permissaoResultado == getPackageManager().PERMISSION_GRANTED) {
                //recuperar localizacao do usuario
                /*
                1) provedor da localizacao
                2) tempo minimo entre atualizacoes de localizacao(milesegundos)
                3) distancia minima entre atualizacoes de localizacao(metros)
                4) location listerner(para recebermos atualizacao)
                 */
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            locationListener
                    );
                }
            }
        }
    }

    private void alertaValidacaoPermissao() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Permissões Negadas");
        alert.setMessage("Para utilizar o app é necessário aceitar as permissões");
        alert.setCancelable(false);
        alert.setPositiveButton("Entendi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.create().show();
    }
}
